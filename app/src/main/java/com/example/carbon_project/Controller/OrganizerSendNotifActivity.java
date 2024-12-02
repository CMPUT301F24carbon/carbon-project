package com.example.carbon_project.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Notification;
import com.example.carbon_project.R;


/*
* Usage:
* Intent intent = new Intent({put activity here}, OrganizerSendNotifActivity.class);
* intent.putExtra("eventId", eventId);
*
* startActivity(intent);
*
*Open this with an eventId to open a menu that send notifications to users of that event
*You can send notifications to selected, rejected, and waiting users.
* */
public class OrganizerSendNotifActivity extends AppCompatActivity {
    private EditText body;
    private Spinner userType;
    private Button send;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_send_notif);

        eventId = getIntent().getStringExtra("eventId");

        body = findViewById(R.id.notif_body);
        userType = findViewById(R.id.spinnerNotif);
        send = findViewById(R.id.btnSendNotif);

        String[] spinnerItems = {"Enrolled List", "Selected List", "Waiting List"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = userType.getSelectedItem().toString();
                String bodyText = body.getText().toString();
                switch (type){
                    case "Enrolled List":
                        Notification.sendToEnrolled(eventId,bodyText);
                        break;
                    case "Selected List":
                        Notification.sendToSelected(eventId,bodyText);
                        break;
                    case "Waiting List":
                        Notification.sendToWating(eventId,bodyText);
                        break;
                    default:
                        break;
                }
                Toast.makeText(OrganizerSendNotifActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
