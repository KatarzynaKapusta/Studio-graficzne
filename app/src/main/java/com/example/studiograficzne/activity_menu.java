package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
        import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class activity_menu extends AppCompatActivity {

    private TextView nicknameTxtView, userIdTxtView, emailTxtView, studioNameTxtView, levelTxtView, moneyTxtView, resourcesTxtView;
    private ImageView userProfImgImageView;
    private ImageButton editStudioNameBtn;
    private final String TAG = this.getClass().getName().toUpperCase();
    private String email;
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

        nicknameTxtView = findViewById(R.id.usrProfNicknameTextView);
        userIdTxtView = findViewById(R.id.usrProfUserIdTextView);
        emailTxtView = findViewById(R.id.usrProfEmailTextView);
        studioNameTxtView = findViewById(R.id.usrProfStudioNameTextView);
        levelTxtView = findViewById(R.id.usrProfLevelTextView);
        moneyTxtView = findViewById(R.id.usrProfMoneyTextView);
        resourcesTxtView = findViewById(R.id.usrProfResourcesTextView);
        editStudioNameBtn = findViewById(R.id.editStudioNameButton);

        userProfImgImageView = findViewById(R.id.usrProfUserImage);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            String uid = user.getUid();
            userIdTxtView.setText(uid);
        } else {
            userIdTxtView.setText(R.string.strNotLogged);
        }

        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            String nickname, mail, moneyString, levelString, resourcesString, uidString, studioName;
            Double money, level, resources;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("UserInfo").child("email").getValue().equals(email))
                    {
                        nickname = keyId.child("UserInfo").child("nickname").getValue(String.class);
                        mail = keyId.child("UserInfo").child("email").getValue(String.class);
                        money = keyId.child("UserGameInfo").child("money").getValue(Double.class);
                        moneyString = String.valueOf(money);
                        level = keyId.child("UserGameInfo").child("level").getValue(Double.class);
                        levelString = String.valueOf(level);
                        resources = keyId.child("UserGameInfo").child("resources").getValue(Double.class);
                        resourcesString = String.valueOf(resources);
                        studioName = keyId.child("UserStudioInfo").child("studioName").getValue(String.class);

                        break;
                    }
                }

                nicknameTxtView.setText(nickname);
                emailTxtView.setText(mail);
                moneyTxtView.setText(moneyString);
                levelTxtView.setText(levelString);
                resourcesTxtView.setText(resourcesString);
                studioNameTxtView.setText(studioName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        editStudioNameBtn.setOnClickListener(view -> openActivityactivity_nazwij_studio());


    } //OnCreate end

    private void openActivityactivity_nazwij_studio() {
        Intent intent = new Intent(this, activity_nazwij_studio.class);
        startActivity(intent);
    }
}