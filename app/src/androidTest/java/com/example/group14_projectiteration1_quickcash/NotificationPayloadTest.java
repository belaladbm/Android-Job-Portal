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
    @Test(expected = org.json.JSONException.class)
    public void testMissingJobIdThrowsException() throws Exception {
        JSONObject data = new JSONObject();
        data.put("jobLocation", "Halifax");

        // Should throw JSONException because job_id is missing
        data.getString("job_id");
    }
    @Test
    public void testJobIdInPayload() throws Exception {
        String jobId = "job456";
        String jobLocation = "Dartmouth";

        JSONObject data = new JSONObject();
        data.put("job_id", jobId);
        data.put("jobLocation", jobLocation);

        assertEquals("job456", data.getString("job_id"));
    }
    @Test
    public void testEmptyJobLocationBodyMessage() throws Exception {
        String jobLocation = "";

        JSONObject notification = new JSONObject();
        notification.put("title", "New Job Created");
        notification.put("body", "A new job has been posted in " + jobLocation);  // Should result in: "A new job has been posted in "

        assertEquals("A new job has been posted in ", notification.getString("body"));
    }
    @Test
    public void testPayloadHasRequiredKeys() throws Exception {
        JSONObject notification = new JSONObject();
        notification.put("title", "Test Title");
        notification.put("body", "Test Body");

        JSONObject data = new JSONObject();
        data.put("job_id", "abc123");
        data.put("jobLocation", "Halifax");

        JSONObject message = new JSONObject();
        message.put("topic", "jobs");
        message.put("notification", notification);
        message.put("data", data);

        assert(message.has("topic"));
        assert(message.has("notification"));
        assert(message.has("data"));
    }



}
