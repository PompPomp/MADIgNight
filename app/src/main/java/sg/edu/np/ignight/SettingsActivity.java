package sg.edu.np.ignight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_CHAT_NOTIFICATION_ENABLED = "messageNotificationsEnabled";
    public static final String KEY_CHAT_NOTIFICATION_RINGTONE = "messageNotificationRingtone";
    public static final String KEY_CHAT_NOTIFICATION_VIBRATION = "messageNotificationVibration";
    public static final String KEY_CHAT_NOTIFICATION_PRIORITY = "messageNotificationPriority";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ImageButton backButton = findViewById(R.id.chatNotificationSettingsBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settingsFrame, new NotificationSettingsFragment())
                    .commit();
        }
    }
}