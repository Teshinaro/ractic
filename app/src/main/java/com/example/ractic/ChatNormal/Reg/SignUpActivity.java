package com.example.ractic.ChatNormal.Reg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ractic.ChatNormal.ChatActivity;
import com.example.ractic.ChatNormal.User;
import com.example.ractic.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

public class SignUpActivity extends AppCompatActivity {
    EditText name;
    EditText loginUp;
    EditText passwordUp;
    EditText passwordAgain;
    Button CreateAccount;
    TextView goTosignIn;
    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = new Auth(getApplicationContext());
        defineViews();
        assignListeners();
        Boolean isEverythingOK = true;
    }
    private void assignListeners() {
        goTosignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEverythingOK = true;

                String nickname = name.getText().toString();
                String login = loginUp.getText().toString();
                String password = passwordUp.getText().toString();
                String repeatPassword = passwordAgain.getText().toString();

                if (login.isEmpty()) {
                    isEverythingOK = false;
                    loginUp.setError("Это поле осталось пустым");
                } else if (nickname.isEmpty()) {
                    isEverythingOK = false;
                    name.setError("Это поле осталось пустым");
                } else if (password.isEmpty()) {
                    isEverythingOK = false;
                    passwordUp.setError("Это поле осталось пустым");
                } else if (repeatPassword.isEmpty()) {
                    isEverythingOK = false;
                    passwordAgain.setError("Это поле осталось пустым");
                } else if (password.length() < 8) {
                    isEverythingOK = false;
                    passwordUp.setError("Пароль должен состоять не менее, чем из 8 символов");
                } else if (!password.matches("\\w+")) {
                    isEverythingOK = false;
                    passwordUp.setError("Пароль должен состоять только из латинских букв, символов нижнего подчёркивания или цифр");
                } else if (!repeatPassword.equals(password)) {
                    isEverythingOK = false;
                    passwordAgain.setError("Пароли не совпадают");
                }

                if (isEverythingOK) {
                    Query query = FirebaseDatabase.getInstance().getReference().child("accounts").orderByChild("login").equalTo(login);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {
                                loginUp.setError("Аккаунт с таким логином уже существует");
                            } else {
                                DatabaseReference accounts = FirebaseDatabase.getInstance().getReference().child("accounts");
                                String key = accounts.push().getKey();
                                User user = new User(nickname, login, password, key);
                                accounts.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        auth.setUsername(nickname);
                                        auth.setUser(user);
                                        auth.setKey(key);
                                        Intent intent = new Intent(SignUpActivity.this, ChatActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void defineViews() {
        name = findViewById(R.id.NameUp);
        loginUp = findViewById(R.id.loginUp);
        passwordUp = findViewById(R.id.PassUp);
        passwordAgain = findViewById(R.id.PassUpRpt);
        CreateAccount = findViewById(R.id.CreateAcc);
        goTosignIn = findViewById(R.id.goTosignIn);
    }
}