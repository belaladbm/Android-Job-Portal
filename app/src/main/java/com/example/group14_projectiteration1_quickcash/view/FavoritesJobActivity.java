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

/**
 * This activity displays all the jobs the current user has marked as favorite.
 * Users can also remove all their favorite jobs at once using a button.
 */

public class FavoritesJobActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobAdapter adapter;
    private final List<Job> favoriteJobs = new ArrayList<>();
    private final Set<String> favoriteJobIds = new HashSet<>();
    private String userId;
    private Button removeAllFavoritesButton;
    private DatabaseReference favRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_job);

        // Set up the RecyclerView for showing favorite jobs
        recyclerView = findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the "Remove All" button from the layout
        removeAllFavoritesButton = findViewById(R.id.removeAllFavoritesButton); // AT-16

        // Get the current user's ID
        userId = CurrentUser.getInstance().getCurrentUser().getUserId();

        // Load favorite jobs from the database
        favRef = FirebaseDatabase.getInstance().getReference("Favorites").child(userId);

        // Set up the adapter for showing favorite jobs
        adapter = new JobAdapter(this, favoriteJobs, null, null, userId, favoriteJobIds);
        recyclerView.setAdapter(adapter);

        loadFavoritesFromFirebase();

       // Deletes everything saved as this user's favorite jobs in the database
        removeAllFavoritesButton.setOnClickListener(v ->
                // Remove all data under the user's Favorites node
                favRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favoriteJobs.clear();
                        favoriteJobIds.clear();
                        adapter.updateJobs(favoriteJobs);
                        Toast.makeText(this, "All favorite jobs removed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to remove favorites", Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    /**
     * This method loads all the favorite jobs for the current user from Firebase.
     * It updates the local list and refreshes the screen when data changes.
     */
    private void loadFavoritesFromFirebase() {

        // Listen for changes in the database
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteJobs.clear();
                favoriteJobIds.clear();
                // Go through each favorite job and add it to the list
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null) {
                        favoriteJobs.add(job);
                        favoriteJobIds.add(job.getJobId());
                    }
                }
                adapter.updateJobs(favoriteJobs);
            }

            /**
             * This method runs if there's an error while loading data from Firebase.
             * It shows a message to the user explaining the problem.
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavoritesJobActivity.this,
                        "Failed to load favorites: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}