package sg.edu.np.ignight;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.ignight.Timestamp.TimestampObject;

public class MainMenuActivity extends AppCompatActivity {

    // Edit profile, Logout, about page, stage 2: map, paywalls, terms & conditions??
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        updateConnection();

        Button home = findViewById(R.id.home_menu);// go back to home menu
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout_menu, new Homepage_fragment());
                ft.commit();
            }
        });
        Button chat = findViewById(R.id.chat_menu);// list of chats with other people (Use fragment view)
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout_menu , new ChatListFragment());
                ft.commit();
            }
        });
        ImageView profile = findViewById(R.id.ownerprofile_menu); //display slide menu
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ArrayList<UserObject> data = new ArrayList<>();

        RecyclerView rv = findViewById(R.id.recyclerView2);
        MainMenuAdapter adapter = new MainMenuAdapter(MainMenuActivity.this, data);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);

        rv.setAdapter(adapter);
        rv.setLayoutManager(layout);
    }

    private void updateConnection() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://madignight-default-rtdb.asia-southeast1.firebasedatabase.app/");

        db.goOnline();

        DatabaseReference userPresenceRef = db.getReference("presence/" + FirebaseAuth.getInstance().getUid());

        DatabaseReference connectionRef = userPresenceRef.child("connection");
        DatabaseReference lastOnlineRef = userPresenceRef.child("lastOnline");

        DatabaseReference connectedRef = db.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);

                if (connected) {
                    connectionRef.setValue(true);
                    lastOnlineRef.removeValue();

                    connectionRef.onDisconnect().setValue(false);


                    try {
                        lastOnlineRef.onDisconnect().setValue(new TimestampObject(new Date().toString()).toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }
}