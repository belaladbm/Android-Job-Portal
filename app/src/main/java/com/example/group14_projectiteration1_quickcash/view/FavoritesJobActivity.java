package com.example.group14_projectiteration1_quickcash.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.adapter.JobAdapter;
import com.example.group14_projectiteration1_quickcash.model.CurrentUser;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesJobActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public JobAdapter adapter;
    public final List<Job> favoriteJobs = new ArrayList<>();
    public String userId;
    public final Set<String> favoriteJobIds = new HashSet<>();
    private Button removeAllFavoritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_job);

        recyclerView = findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        removeAllFavoritesButton = findViewById(R.id.removeAllFavoritesButton); // AT-16

        userId = CurrentUser.getInstance().getCurrentUser().getUserId();

        adapter = new JobAdapter(this, favoriteJobs, null, null, userId, favoriteJobIds);
        recyclerView.setAdapter(adapter);

        loadFavoritesFromFirebase();

        // Remove all favorites when button clicked
        removeAllFavoritesButton.setOnClickListener(v -> {
            DatabaseReference favRef = FirebaseDatabase.getInstance()
                    .getReference("Favorites")
                    .child(userId);

            favRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    favoriteJobs.clear();
                    favoriteJobIds.clear();
                    adapter.updateJobs(favoriteJobs);
                    Toast.makeText(this, "All favorite jobs removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to remove favorites", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadFavoritesFromFirebase() {
        DatabaseReference favRef = FirebaseDatabase.getInstance()
                .getReference("Favorites")
                .child(userId);

        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteJobs.clear();
                favoriteJobIds.clear();
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null) {
                        favoriteJobs.add(job);
                        favoriteJobIds.add(job.getJobId());
                    }
                }
                adapter.updateJobs(favoriteJobs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavoritesJobActivity.this, "Failed to load favorites: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}