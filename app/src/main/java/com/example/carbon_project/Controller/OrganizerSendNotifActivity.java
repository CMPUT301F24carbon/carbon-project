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
* Bundle bundle = new Bundle();
* bundle.putString("eventId", {put id here});
* intent.putExtras(bundle);
*
* startActivity(intent);
*
*
* */
public class OrganizerSendNotifActivity extends AppCompatActivity {
    private EditText title;
    private EditText body;
    private Spinner userType;
    private Button send;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_send_notif);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            eventId = bundle.getString("eventId", "");
        }

        title = findViewById(R.id.notif_title);
        body = findViewById(R.id.notif_body);
        userType = findViewById(R.id.spinnerNotif);
        send = findViewById(R.id.btnSendNotif);

        String[] spinnerItems = {"Selected List", "Rejected List", "Waiting List"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = userType.getSelectedItem().toString();
                String titleText = title.getText().toString();
                String bodyText = body.getText().toString();
                switch (type){
                    case "Selected List":
                        Notification.sendToSelected(eventId,titleText,bodyText);
                        break;
                    case "Rejected List":
                        Notification.sendToRejected(eventId,titleText,bodyText);
                        break;
                    case "Waiting List":
                        Notification.sendToWating(eventId,titleText,bodyText);
                        break;
                    default:
                        break;
                }
                Toast.makeText(OrganizerSendNotifActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
