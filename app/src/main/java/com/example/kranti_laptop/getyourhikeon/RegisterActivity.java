
package com.example.kranti_laptop.getyourhikeon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Hashtable;
import java.util.Map;

public class RegisterActivity extends Activity implements View.OnClickListener {
    private static final String TAG ="Register" ;
    //defining view objects
    private EditText editTextEmail,editTextContact,editTextName;
    private EditText editTextPassword;
    private Button buttonSignup;
    String name,email,password,contact,uid;
    private TextView textViewSignin;
    Context context;
    FirebaseUser user;

    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        context=this;
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        email = editTextEmail.getText().toString().trim();
        password  = editTextPassword.getText().toString().trim();
        name  = editTextName.getText().toString().trim();
        contact  = editTextContact.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter name", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter contact", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length()<=6)
        {
            Toast.makeText(this,"Please enter password longer than 6 letters", Toast.LENGTH_LONG).show();
            return;
        }



        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();



        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            user = firebaseAuth.getCurrentUser();
                            if(user!=null) {

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });
                            }
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://getyourhike.esy.es/UserInfo.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            //Disimissing the progress dialog
                                            //Showing toast message of the response
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            //Dismissing the progress dialog

                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    //Creating parameters
                                    Map<String, String> params = new Hashtable<String, String>();

                                    //Adding parameters
                                    params.put("uid",firebaseAuth.getCurrentUser().getUid());
                                    params.put("name", name);
                                    params.put("contact", contact);
                                    params.put("email", email);
                                    //returning parameters
                                    return params;
                                }
                            };
                            //Creating a Request Queue
                            RequestQueue requestQueue = Volley.newRequestQueue(context);

                            //Adding request to the queue
                            requestQueue.add(stringRequest);

                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else
//                            {
//                            //display some message here
//                            Toast.makeText(RegisterActivity.this,"Registration Error", Toast.LENGTH_LONG).show();
//                            Log.e("Error", "onComplete: ",task.getException() );
//                        }
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            // show error toast ot user ,user already exist
                            Toast.makeText(RegisterActivity.this,"User already exist", Toast.LENGTH_LONG).show();
                        } catch (FirebaseNetworkException e) {
                            //show error tost network exception
                            Toast.makeText(RegisterActivity.this,"Check your internet connection", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                        }

                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}



