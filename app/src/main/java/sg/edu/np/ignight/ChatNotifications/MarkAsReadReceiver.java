package sg.edu.np.ignight.ChatNotifications;

import static android.content.ContentValues.TAG;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// broadcast receiver to set message to seen
public class MarkAsReadReceiver extends BroadcastReceiver {

    private int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String chatID = bundle.getString("chatID");
        notificationID = bundle.getInt("notificationID");

        DatabaseReference chatDB = FirebaseDatabase.getInstance("https://madignight-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("chat").child(chatID);

        String currentUserUID = FirebaseAuth.getInstance().getUid();

        // set all received messages as read
        Query query = chatDB.child("messages").orderByChild("isSeen");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.child("isSeen").getValue().toString().equals("false")) {
                            if (!childSnapshot.child("creator").getValue().toString().equals(currentUserUID)) {
                                childSnapshot.getRef().child("isSeen").setValue(true);
                            }
                        }
                    }

                    // clear unread message count for current user
                    chatDB.child("unread").child(currentUserUID).setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });

        dismissNotification(context);
    }

    // cancel notification
    private void dismissNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
        for (StatusBarNotification notification : activeNotifications) {
            if (notification.getId() == notificationID) {
                notificationManager.cancel(notification.getTag(), notificationID);
            }
        }
    }
}
