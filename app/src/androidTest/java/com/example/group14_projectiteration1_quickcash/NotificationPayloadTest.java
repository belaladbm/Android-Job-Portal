package com.example.group14_projectiteration1_quickcash;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;

public class NotificationPayloadTest {

    @Test
    public void testPayloadConstruction() throws Exception {
        String jobId = "job123";
        String jobLocation = "Halifax";

        JSONObject notification = new JSONObject();
        notification.put("title", "New Job Created");
        notification.put("body", "A new job has been posted in " + jobLocation);

        JSONObject data = new JSONObject();
        data.put("job_id", jobId);
        data.put("jobLocation", jobLocation);

        JSONObject message = new JSONObject();
        message.put("topic", "jobs");
        message.put("notification", notification);
        message.put("data", data);

        JSONObject body = new JSONObject();
        body.put("message", message);

        assertEquals("New Job Created", body.getJSONObject("message").getJSONObject("notification").getString("title"));
        assertEquals("Halifax", body.getJSONObject("message").getJSONObject("data").getString("jobLocation"));
    }
}
