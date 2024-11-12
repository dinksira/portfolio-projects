package com.example.ha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to HomeActivity when the back button is clicked
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Optional: finish MainActivity if you don't want to keep it in the back stack
            }
        });

        // Initialize RecyclerView
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageList.add(new Message("Bot: Hi there! \uD83D\uDE0A How are you feeling today? I'm here to assist you. If you're experiencing any discomfort or symptoms, feel free to share with me. Your health and well-being are important, and I'm here to help. Take your time and let me know how you're feeling.", MessageType.BOT));
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setAdapter(chatAdapter);

        // Initialize EditText and Button
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Set click listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String userInput = messageEditText.getText().toString().trim();
        if (!userInput.isEmpty()) {
            // Display user message in RecyclerView
            messageList.add(new Message("You: " + userInput, MessageType.USER));
            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);

            try {
                // Read data from health_records.json file in the assets folder
                InputStream inputStream = getAssets().open("health_records.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                String json = new String(buffer, StandardCharsets.UTF_8);

                // Parse JSON string
                JSONObject jsonObject = new JSONObject(json);

                // Query the JSON object for responses
                if (jsonObject.has("responses")) {
                    JSONObject responsesObject = jsonObject.getJSONObject("responses");
                    if (responsesObject.has(userInput)) {
                        String response = responsesObject.getString(userInput);
                        messageList.add(new Message("Bot: " + response, MessageType.BOT));
                    } else {
                        messageList.add(new Message("Bot: I'm sorry, I couldn't find information about \"" + userInput + "\".", MessageType.BOT));
                    }
                    chatAdapter.notifyDataSetChanged();
                    chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            // Clear input field
            messageEditText.setText("");
        } else {
            Toast.makeText(this, "Please enter a symptom", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle user's response to follow-up question
    private void handleFollowUpResponse(String symptom, String response) {
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
            try {
                // Read data from health_records.json file in the assets folder
                InputStream inputStream = getAssets().open("health_records.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                String json = new String(buffer, StandardCharsets.UTF_8);

                // Parse JSON string
                JSONObject jsonObject = new JSONObject(json);

                // Query the JSON object for responses
                if (jsonObject.has("responses")) {
                    JSONObject responsesObject = jsonObject.getJSONObject("responses");
                    if (responsesObject.has(symptom)) {
                        JSONObject symptomObject = responsesObject.getJSONObject(symptom);
                        JSONObject followUpObject = symptomObject.optJSONObject("follow_up");
                        if (followUpObject != null) {
                            // Display follow-up response based on user's answer
                            String followUpResponse = followUpObject.getString(response.toLowerCase());
                            messageList.add(new Message("Bot: " + followUpResponse, MessageType.BOT));
                            chatAdapter.notifyDataSetChanged();
                            chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                            return;
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        messageList.add(new Message("Bot: I'm sorry, I couldn't find information about the symptom \"" + symptom + "\".", MessageType.BOT));
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
    }
}
