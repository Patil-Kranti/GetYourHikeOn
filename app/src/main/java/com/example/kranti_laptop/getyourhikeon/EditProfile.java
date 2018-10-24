package com.example.kranti_laptop.getyourhikeon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;


public class EditProfile extends AppCompatActivity implements View.OnClickListener{
    private Button buttonChoose;
    private Button buttonUpload,submit;
    private JSONArray result;
    private ImageView imageView;

    private EditText editTextName,editTextContact;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Bitmap bitmap;
    String email,contact;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://getyourhike.esy.es/DisplayPicture.php";

    private String KEY_IMAGE = "image";
    private String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        submit = (Button) findViewById(R.id.submit);
        editTextName=(EditText)findViewById(R.id.editText);
        editTextContact=(EditText)findViewById(R.id.editText1);
        email=user.getEmail();


        imageView  = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(EditProfile.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(EditProfile.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_EMAIL, email);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if(v == buttonChoose){
            showFileChooser();
        }

        if(v == buttonUpload) {
            uploadImage();
        }
        if (v== submit){
            if(user!=null) {
                if (!(editTextName.getText().toString().trim().equalsIgnoreCase(""))) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(editTextName.getText().toString().trim())
                            .build();
                    Toast.makeText(getApplicationContext(), "Successfully Changed", Toast.LENGTH_LONG).show();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("App", "User profile updated.");
                                    }
                                }
                            });
                }
                if (!(editTextContact.getText().toString().trim().equalsIgnoreCase(""))) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://getyourhike.esy.es/update_contact.php",
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
                            params.put("contact", editTextContact.getText().toString());
                            params.put("email", email);
                            //returning parameters
                            return params;
                        }
                    };
                    //Creating a Request Queue
                    RequestQueue requestQueue = Volley.newRequestQueue(this);

                    //Adding request to the queue
                    requestQueue.add(stringRequest);
                    Toast.makeText(getApplicationContext(), "Successfully Changed", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}
