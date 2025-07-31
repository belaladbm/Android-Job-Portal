package com.example.group14_projectiteration1_quickcash.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group14_projectiteration1_quickcash.BuildConfig;
import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Application;
import com.google.firebase.database.DatabaseReference;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * This class handles the payment gateway through PayPal integration.
 * NOTE: The following class's code is referred from Tutorial-8-Paypal-Integration
 */
public class PaypalActivity extends AppCompatActivity {

    private static final String TAG = PaypalActivity.class.getName();

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    private Application application;
    private firebaseHelper firebaseHelper;
    private DatabaseReference applicationRef;

    private TextView amountTV;
    private Button payNowBtn;
    private TextView paymentStatusTV;
    private TextView jobDetailsTV;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paypal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userEmail = getIntent().getStringExtra("userEmail");
        application = (Application) getIntent().getSerializableExtra("application");

        firebaseHelper = new firebaseHelper();
        firebaseHelper.connectFirebase();
        applicationRef = firebaseHelper.applydbref;

        initUI();
        configPayPal();
        initActivityLauncher();
        setListeners();
        checkPaymentStatus();
        setupBackToDashboardButton();
    }

    private void initUI() {
        amountTV = findViewById(R.id.amountTV);
        payNowBtn = findViewById(R.id.payNowBtn);
        paymentStatusTV = findViewById(R.id.paymentStatusTV);
        jobDetailsTV = findViewById(R.id.jobDetailsTV);

        amountTV.setText(String.format("$%.2f", application.getJobPay()));
        String details = String.format("Payment for: %s\nEmployee: %s",
                application.getJobTitle(), application.getEmployeeEmail());
        jobDetailsTV.setText(details);
    }

    private void configPayPal() {
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(BuildConfig.PAYPAL_CLIENT_ID);

        Intent service = new Intent(this, PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        startService(service);
    }

    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        handlePaymentResult(result.getData());
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        paymentStatusTV.setText("Invalid payment");
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        paymentStatusTV.setText("Payment canceled");
                    }
                });
    }

    private void handlePaymentResult(Intent data) {
        if (data == null) return;

        PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
        if (confirmation == null) return;

        try {
            JSONObject response = confirmation.toJSONObject().getJSONObject("response");
            String payID = response.getString("id");
            String state = response.getString("state");

            paymentStatusTV.setText(String.format("Payment %s%nPayment ID: %s", state, payID));

            if ("approved".equalsIgnoreCase(state)) {
                updatePaymentStatus();
                updateUIForPaidStatus();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Payment confirmation error: ", e);
            paymentStatusTV.setText("Payment confirmation error");
        }
    }

    private void setListeners() {
        payNowBtn.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        PayPalPayment payment = new PayPalPayment(
                new BigDecimal(application.getJobPay()),
                "CAD",
                "Job Payment: " + application.getJobTitle(),
                PayPalPayment.PAYMENT_INTENT_SALE
        );

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        activityResultLauncher.launch(intent);
    }

    private void checkPaymentStatus() {
        if ("paid".equalsIgnoreCase(application.getStatus())) {
            updateUIForPaidStatus();
        }
    }

    private void updateUIForPaidStatus() {
        payNowBtn.setEnabled(false);
        payNowBtn.setText("Already Paid");
        paymentStatusTV.setText("Payment already completed");
    }

    private void updatePaymentStatus() {
        application.setStatus("paid");
        if (application.getId() != null) {
            applicationRef.child(application.getId()).child("status").setValue("paid")
                    .addOnSuccessListener(aVoid ->
                            Log.d(TAG, "Payment status updated successfully"))
                    .addOnFailureListener(e ->
                            Log.e(TAG, "Failed to update payment status", e));
        }
    }

    private void setupBackToDashboardButton() {
        Button backToDashboardButton = findViewById(R.id.backToDashboardButton);
        backToDashboardButton.setOnClickListener(v -> backToDashboard());
    }

    private void backToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}