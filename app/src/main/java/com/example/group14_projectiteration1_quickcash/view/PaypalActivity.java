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
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Application;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;


/**
 * This class handles the payment gateway through paypal integration
 * NOTE: The following classe's code is referred from Tutorial-8-Paypal-Integration
 * */
public class PaypalActivity extends AppCompatActivity {

    private static final String TAG = PaypalActivity.class.getName();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    private Application application;

    // UI Elements
    private TextView amountTV;
    private Button payNowBtn;
    private TextView paymentStatusTV;
    private TextView jobDetailsTV;

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

        // Get application from intent
        application = (Application) getIntent().getSerializableExtra("application");

        // Initialize UI and PayPal
        initUI();
        configPayPal();
        initActivityLauncher();
        setListeners();
    }

    private void initUI() {
        // Initialize UI elements
        amountTV = findViewById(R.id.amountTV);
        payNowBtn = findViewById(R.id.payNowBtn);
        paymentStatusTV = findViewById(R.id.paymentStatusTV);
        jobDetailsTV = findViewById(R.id.jobDetailsTV);

        // Set payment amount from application
        amountTV.setText(String.format("$%.2f", application.getJobPay()));

        // Set job details from application
        String details = String.format("Payment for: %s\nEmployee: %s",
                application.getJobTitle(), application.getEmployeeEmail());
        jobDetailsTV.setText(details);
    }

    private void configPayPal() {
        // Configure PayPal with sandbox environment and client ID
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(BuildConfig.PAYPAL_CLIENT_ID);

        // Start PayPal service
        Intent service = new Intent(this, PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        startService(service);
    }

    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            PaymentConfirmation confirmation =
                                    data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                            if (confirmation != null) {
                                try {
                                    // Get payment details
                                    String paymentDetails = confirmation.toJSONObject().toString(4);

                                    // Extract payment info
                                    JSONObject payObj = new JSONObject(paymentDetails);
                                    String payID = payObj.getJSONObject("response").getString("id");
                                    String state = payObj.getJSONObject("response").getString("state");

                                    paymentStatusTV.setText(
                                            String.format("Payment %s%nPayment ID: %s", state, payID)
                                    );

                                } catch (JSONException e) {
                                    Log.e(TAG, "Payment confirmation error: ", e);
                                    paymentStatusTV.setText("Payment confirmation error");
                                }
                            }
                        }
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        paymentStatusTV.setText("Invalid payment");
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        paymentStatusTV.setText("Payment canceled");
                    }
                });
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
}
