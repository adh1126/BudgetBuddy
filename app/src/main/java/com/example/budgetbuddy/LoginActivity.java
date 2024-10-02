package com.example.budgetbuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    ImageView imageView;
    TextView textView;
    EditText editText1, editText2;
    Button button;
    ProgressDialog pdLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.tvNewUser);
        editText1 = findViewById(R.id.etUsername);
        editText2 = findViewById(R.id.etPassword);
        button = findViewById(R.id.btnLogin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().isEmpty()){
                    editText1.setError("Please enter username");
                } else if(editText2.getText().toString().isEmpty()){
                    editText2.setError("Please enter password");
                }else{
                    pdLogin = new ProgressDialog(LoginActivity.this);
                    pdLogin.setTitle("Please wait...");
                    pdLogin.setMessage("Login in process");
                    pdLogin.setCanceledOnTouchOutside(true);
                    pdLogin.show();

                    userLoginDetails();

                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void userLoginDetails(){
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();

//        params.put("username",editText1.getText().toString());
//        params.put("password",editText2.getText().toString());
//
//        client.post("http://192.168.204.247:80/expense_trackerAPI/userLogin.php",params,new JsonHttpResponseHandler()
//                {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//                        try {
//                            String status = response.getString("success");
//                            if(status.equals("1")){
//                                pdLogin.dismiss();
//                                Toast.makeText(LoginActivity.this, "Logged in successfully!!", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//                                startActivity(i);
//                                finish();
//                            }else {
//                                pdLogin.dismiss();
//                                Toast.makeText(LoginActivity.this, "Invalid username or password!!", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        pdLogin.dismiss();
//                        Toast.makeText(LoginActivity.this, "Server error!!", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//        );
        String email = editText1.getText().toString();
        String password = editText2.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            // Fetch user data from Firestore
                            db.collection("users").document(userId).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String username = documentSnapshot.getString("username");
                                                String email = documentSnapshot.getString("email");
                                                // You can now use the fetched data, e.g., show it in the UI
                                                Toast.makeText(LoginActivity.this, "Login successful!!", Toast.LENGTH_SHORT).show();

                                                // Proceed to next activity, e.g., HomeActivity
                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pdLogin.dismiss();
                                            Toast.makeText(LoginActivity.this, "Failed to retrieve data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            pdLogin.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect email or password!!" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
