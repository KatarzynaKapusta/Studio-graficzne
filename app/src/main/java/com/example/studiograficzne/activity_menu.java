package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.util.Log;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class activity_menu extends AppCompatActivity {
    private TextView occupationTxtView, nameTxtView, workTxtView;
    private TextView emailTxtView, phoneTxtView, videoTxtView, facebookTxtView, twitterTxtView;
    private ImageView userImageView, emailImageView, phoneImageView, videoImageView;
    private ImageView facebookImageView, twitterImageView;
    private final String TAG = this.getClass().getName().toUpperCase();
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private Map<String, String> userMap;
    private String email;
    private String userid;
    private static final String USERS = "Users";

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //receive data from login screen
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        DatabaseReference userRef = rootRef.child(USERS);
        Log.v("USERID", userRef.getKey());

        occupationTxtView = findViewById(R.id.occupation_textview);
        nameTxtView = findViewById(R.id.name_textview);
        workTxtView = findViewById(R.id.workplace_textview);
        emailTxtView = findViewById(R.id.email_textview);
        phoneTxtView = findViewById(R.id.phone_textview);
        videoTxtView = findViewById(R.id.video_textview);
        facebookTxtView = findViewById(R.id.facebook_textview);

        userImageView = findViewById(R.id.user_imageview);
        emailImageView = findViewById(R.id.email_imageview);
        phoneImageView = findViewById(R.id.phone_imageview);
        videoImageView = findViewById(R.id.phone_imageview);
        facebookImageView = findViewById(R.id.facebook_imageview);

//        FirebaseUser user1 = mAuth.getCurrentUser();
//        String UID = user1.getUid();


        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            String nickname, mail, moneyString;
            Double money;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    {
                        money = keyId.child("UserGameInfo").child("money").getValue(Double.class);
                        moneyString = String.valueOf(money);
                        nickname = keyId.child("nickname").getValue(String.class);
                        mail = keyId.child("email").getValue(String.class);

                        break;
                    }
                }
                nameTxtView.setText(nickname);
                emailTxtView.setText(mail);
                phoneTxtView.setText(moneyString);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    } //OnCreate end
}