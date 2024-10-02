package com.example.budgetbuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText RegName;
    EditText RegEmail;
    EditText RegMobileNo ;
    EditText RegUsername;
    EditText RegPassword;
    Button registorbtn;
    TextView tvoldAccount;
    ProgressDialog pdRegistration;
    ImageView showHidePass;
    boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
         RegName =findViewById(R.id.etRegistrationName);
         RegEmail =findViewById(R.id.etRegistrationEmail);
         RegMobileNo =findViewById(R.id.etRegistrationMobile);
         RegUsername =findViewById(R.id.etRegistrationUsername);
         RegPassword =findViewById(R.id.etRegistraionPassword);
         registorbtn= findViewById(R.id.btnRegistrationBtn);
         tvoldAccount = findViewById(R.id.tvAlreadyHaveAccount);

        registorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RegName.getText().toString().isEmpty()){
                    RegName.setError("Please enter name!!");
                }else if(RegEmail.getText().toString().isEmpty()){
                    RegEmail.setError("Please enter email id!!");
                }else if(!RegEmail.getText().toString().contains("@gmail.com")){
                    RegEmail.setError("Please enter a proper email id");
                }else if(RegMobileNo.getText().toString().isEmpty()) {
                    RegMobileNo.setError("Please enter mobile number!!");
                }else if(RegMobileNo.getText().toString().length() > 10) {
                    RegMobileNo.setError("Your mobile number has more than 10 numbers");
                }else if(RegMobileNo.getText().toString().length() < 10) {
                    RegMobileNo.setError("Your mobile number has less than 10 numbers");
                }else if(RegUsername.getText().toString().isEmpty()){
                    RegUsername.setError("Please Enter username!!");
                }else if(!RegUsername.getText().toString().matches("^[a-zA-Z0-9._@]+$")) {
                    RegUsername.setError("You can use underscore and dot in username");
                }else if(RegPassword.getText().toString().isEmpty()){
                    RegPassword.setError("Please enter password!!");
                }else if (!RegPassword.getText().toString().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$")) {
                    RegPassword.setError("Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character");
                }else{
                    pdRegistration = new ProgressDialog(RegistrationActivity.this);
                    pdRegistration.setTitle("Please wait..");
                    pdRegistration.setMessage("Registration in process");
                    pdRegistration.setCanceledOnTouchOutside(true);
                    pdRegistration.show();
                    userRegistrationDetails();

                }
            }
        });
        tvoldAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void userRegistrationDetails() {
//        AsyncHttpClient client = new AsyncHttpClient();   //client server communication
//        RequestParams params = new RequestParams();  //data put
//
//        params.put("name",RegName.getText().toString());
//        params.put("email",RegEmail.getText().toString());
//        params.put("mobile_no",RegMobileNo.getText().toString());
//        params.put("username",RegUsername.getText().toString());
//        params.put("password",RegPassword.getText().toString());
//
//        client.post("http://192.168.204.247:80/expense_trackerAPI/userRegistration.php/",params,
//                new JsonHttpResponseHandler()
//                {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//                        String status;
//                        try {
//                            status = response.getString("success");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        if (status.equals("1")){
//                            pdRegistration.dismiss();
//                            Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
//                            startActivity(i);
//                            finish();
//
//                        }else{
//                            pdRegistration.dismiss();
//                            Toast.makeText(RegistrationActivity.this, "Data is already present", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        pdRegistration.dismiss();
//                        Toast.makeText(RegistrationActivity.this, "Server error", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//        );
        String email = RegEmail.getText().toString();
        String password = RegPassword.getText().toString();
        String username = RegUsername.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();  // Get user ID

                            // Prepare user data to store in Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("password", password);
                            userData.put("email", email);
                            userData.put("username", username);

                            // Store user data in Firestore under "users" collection with document ID as userId
                            db.collection("users").document(userId).set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegistrationActivity.this, "Registration successful and data stored!", Toast.LENGTH_SHORT).show();
                                            // Proceed to next activity, e.g., HomeActivity
                                            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pdRegistration.dismiss();
                                            Toast.makeText(RegistrationActivity.this, "Data storing failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            pdRegistration.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // Method to save user info to Firestore
    private void saveUserInfoToFirestore(String userId, String username, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("createdAt", FieldValue.serverTimestamp());

        // Add user info to Firestore under "users" collection
        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegistrationActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    // Redirect to login or home screen
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegistrationActivity.this, "Failed to store user info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}