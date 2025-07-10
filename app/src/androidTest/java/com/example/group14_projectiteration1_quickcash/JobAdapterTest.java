package com.example.group14_projectiteration1_quickcash;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group14_projectiteration1_quickcash.adapter.JobAdapter;
import com.example.group14_projectiteration1_quickcash.model.Job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class JobAdapterTest {
    private JobAdapter jobAdapter;
    private List<Job> testJobList;

    @Before
    public void setUp() {
        testJobList = new ArrayList<>();

        // Add test jobs
        testJobList.add(new Job("Job 1", "Description 1", "Category 1", "Halifax", "test1@email.com"));
        testJobList.add(new Job("Job 2", "Description 2", "Category 2", "Halifax", "test2@email.com"));
        testJobList.add(new Job("Job 3", "Description 3", "Category 3", "Halifax", "test3@email.com"));

        jobAdapter = new JobAdapter(testJobList);
    }

    @Test
    public void testGetJobCount(){
        assertEquals(3, jobAdapter.getItemCount());
    }
}
