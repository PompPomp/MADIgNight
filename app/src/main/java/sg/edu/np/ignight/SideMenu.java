package sg.edu.np.ignight;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import sg.edu.np.ignight.Map.PermissionsActivity;

public class SideMenu extends Activity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private boolean initialVerificationSent;

    private String verificationId;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String Uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        // Side menu layout
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),height);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.START;
        params.x = 0;
        params.y = 0;
        getWindow().setAttributes(params);

        // Toast Message
        LayoutInflater inflater = getLayoutInflater();
        View customtoast = inflater.inflate(R.layout.toast_message_bg, findViewById(R.id.toast_message));
        TextView txtMessage = customtoast.findViewById(R.id.toast_message);
        txtMessage.setText("Thanks for smashing this button! unfortunately we do not have this feature yet, we will try to get it out by Stage 2!!!");
        txtMessage.setTextColor(Color.RED);
        Toast mToast = new Toast(getApplicationContext());
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(customtoast);

        // Display profile
        TextView editprofile = findViewById(R.id.editprofile_sidemenu);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfile = new Intent(SideMenu.this, ProfileCreationActivity.class);
                editProfile.putExtra("ProfileCreated", true);
                startActivity(editProfile);
            }
        });

        // Display profile
        TextView profileCust = findViewById(R.id.ProfileCustTextView);
        profileCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileCust = new Intent(SideMenu.this, ViewProfileCustomisation.class);
                profileCust.putExtra("ProfileCreated", true);
                startActivity(profileCust);
            }
        });

        // About us page
        TextView aboutus = findViewById(R.id.aboutus_sidemenu);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main_to_aboutus = new Intent(SideMenu.this,AboutUsActivity.class);
                startActivity(main_to_aboutus);
            }
        });

        TextView map = findViewById(R.id.map_sidemenu);

        // Asks for location permission (MAP)
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent mapPage = new Intent(SideMenu.this, MapActivity.class);
                    startActivity(mapPage);
                }else {
                    Intent mapPage = new Intent(SideMenu.this, PermissionsActivity.class);
                    startActivity(mapPage);
                }

            }
        });

        // Terms and services
        TextView TandC = findViewById(R.id.TandC_sidemenu);
        TandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main_to_tnc = new Intent(SideMenu.this, TNC.class);
                startActivity(main_to_tnc);
            }
        });

        // Button for logout
        TextView logout = findViewById(R.id.logout_sidemenu);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance("https://madignight-default-rtdb.asia-southeast1.firebasedatabase.app/").goOffline();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // side menu button for activity report activity. pls edit if its wrong
        TextView activityReport = findViewById(R.id.activityReport_sidemenu);
        activityReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityReport_Activity.class);
                startActivity(intent);
            }
        });

        // Create Blog tab
        TextView createBlogBtn = findViewById(R.id.menuCreateBlogBtn);
        createBlogBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Intent createBlog = new Intent(SideMenu.this, BlogActivity.class);
                createBlog.putExtra("canEdit", true);
                startActivity(createBlog);
            }
        });

        // go to notification settings
        TextView notificationSettingsButton = findViewById(R.id.chatNotificationSettings_sidemenu);
        notificationSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        /*
        // go to profile view customisation
        TextView profileViewCustomisation = findViewById(R.id.customiseProfleView_sideMenu);
        profileViewCustomisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewProfileCustomisation.class));
            }
        });
        */


        // Delete user tab
        Button delete_acc = findViewById(R.id.delete_acc_btn);
        delete_acc.setOnClickListener(new View.OnClickListener() { // onclick on delete account
            @Override
            public void onClick(View view1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SideMenu.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(SideMenu.this).inflate(R.layout.activity_delete_alert_dialog, (ConstraintLayout)findViewById(R.id.layoutDialogContainer));
                builder.setView(view);

                final AlertDialog alertDialog = builder.create(); //Display alert dialog
                callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    // verification complete -> sign in with credentials
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        ReAuthPhoneAuthCredential(phoneAuthCredential, alertDialog);
                    }

                    // OTP sent -> store verification id and resending token, start timer for OTP resend
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        initialVerificationSent = true;
                        resendingToken = forceResendingToken;
                        allowResendOTP(view);
                    }

                    // verification failed -> call setDefaultFields()
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        e.printStackTrace();
                    }
                };
                EditText delete_word = view.findViewById(R.id.delete_input);
                delete_word.addTextChangedListener(new TextWatcher() { //check the DELETE word to confirm delete
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { // check everytime the input changes
                        Button delete_otp_btn = view.findViewById(R.id.delete_send_otp);
                        EditText get_delete_otp = view.findViewById(R.id.delete_otp);
                        if (delete_word.getText().toString().equals("DELETE")) { // Matched the DELETE word
                            delete_otp_btn.setEnabled(true);
                            delete_otp_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view2) {
                                    delete_word.setEnabled(false);
                                    if (initialVerificationSent) {
                                        resendVerificationCode();
                                    }
                                    else {
                                        startPhoneNumberVerification();
                                    }
                                    delete_otp_btn.setEnabled(false);
                                    get_delete_otp.setEnabled(true);
                                    get_delete_otp.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                                        @Override
                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                            Log.d("uid",Uid);
                                            if (get_delete_otp.length() == 6) {
                                                view.findViewById(R.id.button_yes_delete).setEnabled(true);
                                                view.findViewById(R.id.button_yes_delete).setBackgroundResource(R.drawable.btn_delete_delete);
                                                view.findViewById(R.id.button_yes_delete).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view3) {
                                                        verifyPhoneNumberWithCode(view, alertDialog);
                                                        if (countDownTimer != null) {
                                                            countDownTimer.cancel();
                                                            delete_otp_btn.setText("send OTP");
                                                            get_delete_otp.setText("");
                                                        }
                                                    }
                                                });
                                            }
                                            else { // If does not match length, set to disable the buttons to prevent miss click
                                                Log.d("OTP",get_delete_otp.getText().toString());
                                                view.findViewById(R.id.button_yes_delete).setEnabled(false);
                                                view.findViewById(R.id.button_yes_delete).setBackgroundResource(R.drawable.btn_delete_delete_locked);
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable editable) {}
                                    });
                                }
                            });
                        }
                        else { // If does not match delete, set to disable the buttons to prevent miss click
                            view.findViewById(R.id.button_yes_delete).setEnabled(false);
                            view.findViewById(R.id.delete_otp).setEnabled(false);
                            view.findViewById(R.id.delete_send_otp).setEnabled(false);
                            delete_otp_btn.setText("send OTP");
                            get_delete_otp.setText("");
                            view.findViewById(R.id.button_yes_delete).setBackgroundResource(R.drawable.btn_delete_delete_locked);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {}
                });

                view.findViewById(R.id.button_cancel_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                if (alertDialog.getWindow() != null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        ImageView profilePicSideMenu = findViewById(R.id.profilepic_sidemenu);

        DatabaseReference myRef = FirebaseDatabase.getInstance("https://madignight-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("user");

        myRef.child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String existProfilePic = snapshot.child("profileUrl").getValue(String.class);
                Glide.with(getApplicationContext()).load(existProfilePic).placeholder(R.drawable.ic_baseline_image_24).into(profilePicSideMenu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private CountDownTimer countDownTimer;

    // use countdown timer to change sendOTPButton text and enable the button to allow resending the OTP after one minute
    private void allowResendOTP(View view) {
        Button send_otp = view.findViewById(R.id.delete_send_otp);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                send_otp.setText(Long.toString(Math.round(l / 1000.0)) + "(s)");
            }

            @Override
            public void onFinish() {
                send_otp.setText("send OTP");
                send_otp.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    // start to verify phone number (send otp to the retrieved phone number)
    private void startPhoneNumberVerification() {
        DatabaseReference phoneNum = FirebaseDatabase.getInstance("https://madignight-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("user").child(Uid).child("phone");
        phoneNum.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                        .setPhoneNumber(snapshot.getValue().toString())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(SideMenu.this)
                        .setCallbacks(callbacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled", error.toException());
            }
        });
    }

    // resend otp
    private void resendVerificationCode() {
        DatabaseReference phoneNum = FirebaseDatabase.getInstance("https://madignight-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("user").child(Uid).child("phone");
        phoneNum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("phone2",snapshot.getValue().toString());
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                        .setPhoneNumber(snapshot.getValue().toString())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(SideMenu.this)
                        .setCallbacks(callbacks)
                        .setForceResendingToken(resendingToken)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled", error.toException());
            }
        });
    }

    // create a PhoneAuthCredential object with the verification id obtained from onCodeSent()
    private void verifyPhoneNumberWithCode(View view, AlertDialog alertDialog) {
        EditText delete_otp_input = view.findViewById(R.id.delete_otp);
        Log.d("Verify",verificationId);
        Log.d("OTP",delete_otp_input.getText().toString());
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, delete_otp_input.getText().toString());
        ReAuthPhoneAuthCredential(credential, alertDialog);
    }

    // reauth the user with the credentials and delete
    private void ReAuthPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential, AlertDialog alertDialog) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user");
        List<String> Chats = new ArrayList<>();
        List<String> ChatsId = new ArrayList<>();
        List<String> ChatsRequest = new ArrayList<>();
        // Get chats
        myRef.child(Uid).child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot chatIDSnapshot : snapshot.getChildren()) {
                        Log.d("chat data",chatIDSnapshot.getKey());
                        Chats.add(chatIDSnapshot.getKey());
                        ChatsId.add(chatIDSnapshot.getValue().toString());
                    }
                    //Delete chat
                    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chat");
                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot chatIDSnapshot : snapshot.getChildren()) {
                                    if (Chats.contains(chatIDSnapshot.getKey())) {
                                        Log.d("chat data in chat","deleted");
                                        chatRef.child(chatIDSnapshot.toString()).removeValue();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("DeletingChatsError",error.toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DeletingChatsError",error.toString());
            }
        });
        // Get chat request
        for (String i : ChatsId) {
            myRef.child(i).child("chatRequests").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot reqSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot chatIDSnapshot : reqSnapshot.getChildren()) {
                                if (chatIDSnapshot.getValue() == Uid) {
                                    Log.d("newreqchat",chatIDSnapshot.getKey());
                                    ChatsRequest.add(chatIDSnapshot.getKey());
                                }
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("GettingChatReqError",error.toString());
                }
            });
        }
        myRef.child(Uid).child("chatRequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get Chat request
                    for (DataSnapshot chatReqStatusSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot chatReq : chatReqStatusSnapshot.getChildren()) {
                            Log.d("chatrequestdata",chatReqStatusSnapshot.getKey());
                            ChatsRequest.add(chatReq.getKey());
                        }
                    }
                    // Delete Chat request
                    DatabaseReference chatRequest = FirebaseDatabase.getInstance().getReference("chatRequest");
                    chatRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot chatReqSnapshot : snapshot.getChildren()) {
                                    if (ChatsRequest.contains(chatReqSnapshot.getKey())) {
                                        Log.d("chatrequest","deleted");
                                        chatRequest.child(chatReqSnapshot.getKey()).removeValue();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("DeletingChatsError",error.toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DeletingChatsError",error.toString());
            }
        });
        // Delete all user with data of chats
        user.reauthenticate(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userIDSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot chatIDSnapshot : userIDSnapshot.child("chats").getChildren()) {
                                        if (Chats.contains(chatIDSnapshot.getKey())) {
                                            Log.d("people with chat","deleted");
                                            myRef.child(userIDSnapshot.getKey()).child("chats").child(chatIDSnapshot.getKey()).removeValue();
                                            myRef.child(userIDSnapshot.getKey()).child("chatRequests").child("sent").removeValue();
                                        }
                                    }
                                    // Delete chatreq
                                    for (DataSnapshot chatReqStatusSnapshot : userIDSnapshot.child("chatRequests").getChildren()) {
                                        for (DataSnapshot chatReq : chatReqStatusSnapshot.getChildren()) {
                                            if (ChatsRequest.contains(chatReq.getKey())) {
                                                Log.d("people with chatREQ",chatReq.getKey());
                                                myRef.child(userIDSnapshot.getKey()).child("chatRequests").child(chatReqStatusSnapshot.getKey()).child(chatReq.getKey()).removeValue();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("DeletingChatsError",error.toString());
                        }
                    });
                    myRef.child(Uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() { // Delete the user
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        alertDialog.dismiss();
                                        Log.d("Delete Status", user.getUid());
                                        Intent main_to_start = new Intent(getApplicationContext(), LoginActivity.class);
                                        main_to_start.putExtra("deleteUser", user.getUid());
                                        main_to_start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(main_to_start); // Go back to login
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else {
                    Log.e("AuthError",task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}