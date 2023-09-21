package com.example.nftstore;

import static android.widget.Toast.makeText;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.DB.MyCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class Authorization extends AppCompatActivity {

    ImageButton enter_button, sign_up_button;
    FirebaseAuth mAuth;

    private String nicknameRegStr, emailRegStr, password1RegStr, password2RegStr;

    ConstraintLayout root;

    Dialog progress_dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            DBQuery.loadData(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    startActivity(new Intent(Authorization.this, MainActivity.class));
                    Authorization.this.finish();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(Authorization.this, "SMTH went wrong, try again!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        setContentView(R.layout.authorization);

        enter_button = findViewById(R.id.enter_button);
        sign_up_button = findViewById(R.id.sign_up_button);

        root = findViewById(R.id.root_element);

        // Вход
        enter_button.setOnClickListener(new View.OnClickListener() {

            final EditText email = findViewById(R.id.emailField);
            final EditText password = findViewById(R.id.passwordField);

            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString().trim())) {
                    Toast.makeText(Authorization.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().trim().length() < 1) {
                    Toast.makeText(Authorization.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                        .addOnCompleteListener(Authorization.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Authorization.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    DBQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            Intent intent = new Intent(Authorization.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(Authorization.this, "SMTH went wrong on login",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(Authorization.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        sign_up_button.setOnClickListener(view -> openRegistrationWindow());
    }


    // Окно регистрации
    private void openRegistrationWindow() {
        AlertDialog dialog;
        dialog = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = LayoutInflater.from(this);
        View registration_window = inflater.inflate(R.layout.registration_window, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(registration_window);

        EditText nickname = registration_window.findViewById(R.id.nicknameField);
        EditText email = registration_window.findViewById(R.id.emailField);
        EditText password1 = registration_window.findViewById(R.id.passwordField);
        EditText password2 = registration_window.findViewById(R.id.passwordField2);
        ImageButton create_account_btn = registration_window.findViewById(R.id.create_account_button);


        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nicknameRegStr = nickname.getText().toString().trim();
                emailRegStr = email.getText().toString().trim();
                password1RegStr = password1.getText().toString().trim();
                password2RegStr = password2.getText().toString().trim();

                if (nicknameRegStr.length() < 5) {
                    Toast.makeText(Authorization.this, "Enter a nickname more than 5 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < DBQuery.g_usersList.size(); i++)
                    if (nicknameRegStr.toUpperCase().equals(DBQuery.g_usersList.get(i).getNickname().trim().toUpperCase())) {
                        Toast.makeText(Authorization.this, "This nickname is not available", Toast.LENGTH_SHORT).show();
                    }

                    if (emailRegStr.isEmpty()) {
                        Toast.makeText(Authorization.this, "Enter your email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password1RegStr.length() < 5) {
                        Toast.makeText(Authorization.this, "Enter a password of more than 5 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password2RegStr.length() < 5) {
                        Toast.makeText(Authorization.this, "Confirm password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password1RegStr.compareTo(password2RegStr) != 0) {
                        Toast.makeText(Authorization.this, "Passwords must match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Регистрация пользователя
                    mAuth.createUserWithEmailAndPassword(emailRegStr, password1RegStr)
                            .addOnCompleteListener(Authorization.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        makeText(Authorization.this, "Sign Up Success", Toast.LENGTH_SHORT).show();

                                        DBQuery.createUserData(emailRegStr, nicknameRegStr, new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                DBQuery.loadData(new MyCompleteListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(Authorization.this, MainActivity.class);
                                                        startActivity(intent);
                                                        Authorization.this.finish();
                                                    }

                                                    @Override
                                                    public void onFailure() {
                                                        makeText(Authorization.this, "SMTH went wrong, try again!",
                                                                Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure() {
                                                makeText(Authorization.this, "SMTH went wrong, try again",
                                                        Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });
                                    } else {
                                        dialog.dismiss();
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Authorization.this, "Registration error",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            }
        });
        dialog.show();
    }
}



//auth.createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString())
//        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//@Override
//public void onSuccess(AuthResult authResult) {
//        MyProfile user = new MyProfile();
//        user.setNickname(nickname.getText().toString());
//        user.setEmail(email.getText().toString());
//        user.setPassword1(password1.getText().toString());
//        user.setPassword2(password2.getText().toString());
//
//        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//        .setValue(user)
//        .addOnSuccessListener(new OnSuccessListener<Void>() {
//@Override
//public void onSuccess(Void unused) {
//        Snackbar.make(root, "MyProfile added", Snackbar.LENGTH_SHORT).show();
//        }
//        });
//        }
//        }).addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        Snackbar.make(root, "Registration error" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
//        }
//        });





//    private void openRegistrationWindow() {
//        AlertDialog dialog;
//        dialog = new AlertDialog.Builder(this).create();
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View registration_window = inflater.inflate(R.layout.registration_window, null, false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setView(registration_window);
//
//        nickname = registration_window.findViewById(R.id.nicknameField);
//        email = registration_window.findViewById(R.id.emailField);
//        password1 = registration_window.findViewById(R.id.passwordField);
//        password2 = registration_window.findViewById(R.id.passwordField2);
//        create_account_btn = registration_window.findViewById(R.id.create_account_button);
//
//        create_account_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                nicknameStr = nickname.getText().toString().trim();
//                emailStr = email.getText().toString().trim();
//                password1Str = password1.getText().toString().trim();
//                password2Str = password2.getText().toString().trim();
//
//                if (nickname.getText().toString().length() < 5) {
//                    Snackbar.make(root, "Enter a nickname of more than 5 characters", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(email.getText().toString())) {
//                    Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (password1.getText().toString().length() < 5) {
//                    Snackbar.make(root, "Enter a password of more than 5 characters", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (password2.getText().toString().length() < 5) {
//                    Snackbar.make(root, "Repeat a password", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Регистрация пользователя
//                auth.createUserWithEmailAndPassword(emailStr, password1Str)
//                        .addOnCompleteListener(Authorization.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(Authorization.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
//
//                                    DBQuery.createUserData(emailStr, nicknameStr, new MyCompleteListener() {
//                                        @Override
//                                        public void onSuccess() {
//                                            DBQuery.loadData(new MyCompleteListener() {
//                                                @Override
//                                                public void onSuccess() {
//                                                    Intent intent = new Intent(Authorization.this, MainActivity.class);
//                                                    startActivity(intent);
//                                                    Authorization.this.finish();
//                                                }
//
//                                                @Override
//                                                public void onFailure() {
//                                                    Toast.makeText(Authorization.this, "SMTH went wrong, try again!",
//                                                            Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                        }
//
//                                        @Override
//                                        public void onFailure() {
//                                            Toast.makeText(Authorization.this, "SMTH went wrong, try again",
//                                                    Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                } else {
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                    Toast.makeText(Authorization.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//
//        });
//        dialog.show();
//    }
