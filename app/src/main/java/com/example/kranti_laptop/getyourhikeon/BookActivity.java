package com.example.kranti_laptop.getyourhikeon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Map;

public class BookActivity extends AppCompatActivity {
    private TextView date,time,location,address,trip;
    private Button book;
    private ImageView imageView,imageView1;
    private Intent intent;
    private Bundle bundle;
    private BigDecimal cost;
    private String destination;
    private int year,month,day,hour,minute;
    private DatabaseReference mDatabaseReference;

   // private PayPalConfiguration m_config;
   // private String m_paypalClientId="AXofvJVlRbFZmwZdlYNvPaT29lPJCdbWfvbojPN4TbI8Ii73qcydF-nfFXuOfx9uu9u7y06Y8mvHOYKP";
    private Intent paypalService;
    private String uid,id,state,email;
    private int payPalRequestCode=7326;
    private JSONArray result;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        intent=getIntent();
        firebaseAuth=FirebaseAuth.getInstance();

        bundle=intent.getExtras();
        if(firebaseAuth.getCurrentUser()!=null) {
            email = firebaseAuth.getCurrentUser().getEmail();
            uid = firebaseAuth.getCurrentUser().getUid();
        }
        year= bundle.getInt("year");
        month= bundle.getInt("month");
        day= bundle.getInt("day");
        hour= bundle.getInt("hour");
        minute= bundle.getInt("minute");
        destination=bundle.getString("destination");

//        if(destination=="Kungsleden")
//        {
            cost=new BigDecimal(400.00);
//        }

       /* m_config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) //Production for real
                .clientId(m_paypalClientId);
        paypalService = new Intent(this, PayPalService.class);
        paypalService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_config);
        startService(paypalService);*/

        imageView=(ImageView)findViewById(R.id.close);
        imageView1=(ImageView)findViewById(R.id.topImage);
        date=(TextView)findViewById(R.id.date);
        time=(TextView)findViewById(R.id.time);
        location=(TextView)findViewById(R.id.destination);
        address=(TextView)findViewById(R.id.location);
        trip=(TextView)findViewById(R.id.trip);
        book=(Button)findViewById(R.id.book);

        String loc = location.getText().toString();
        String dt = date.getText().toString();
        String ti = time.getText().toString();


//        imageView.setImageResource(R.drawable.slide2);
//        Picasso.with(getApplicationContext()).load(R.drawable.cover).fit().centerCrop().into(imageView1);

        location.setText(destination);
        date.setText(day+":"+month+":"+year);
        time.setText(hour+":"+minute);
        String date2 = day+":"+month+":"+year;
        String time2 = hour+":"+minute;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("trip");
        final String tripId = mDatabaseReference.push().getKey();
        final BookData data = new BookData(destination,date2,time2);
        Log.d("Book activity", "onCreate: "+data.getDate());
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child(tripId).setValue(data);
                Log.d("Book Activity", "onClick:"+data);
                Intent intent = new Intent(BookActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(BookActivity.this, "Your Trip has been Booked", Toast.LENGTH_LONG).show();
            }
        });
    }
    /*@Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    void pay(View view)
    {
        PayPalPayment payment=new PayPalPayment(cost,"USD",destination,
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent, payPalRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==payPalRequestCode)
            if(resultCode== Activity.RESULT_OK)
            {
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null)
//                {
                    try {
                        //Getting the payment details
                        final String paymentDetails = confirmation.toJSONObject().toString(4);
                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);

                            //Displaying payment details
                            JSONObject jsonObject=jsonDetails.getJSONObject("response");
                            id=jsonObject.getString("id");
                            Toast.makeText(this, id, Toast.LENGTH_LONG).show();
                            state=jsonObject.getString("state");

                        } catch (JSONException e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.i("paymentExample", paymentDetails);
                        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://getyourhike.esy.es/bookingnew.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        //Showing toast message of the response
                                        Toast.makeText(BookActivity.this, s , Toast.LENGTH_LONG).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        //Showing toast
                                        Toast.makeText(BookActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                //Converting Bitmap to String

                                //Getting Image Name

                                //Creating parameters
                                Map<String,String> params = new Hashtable<String, String>();

                                //Adding parameters

                                    params.put("id",uid);

                                params.put("email",email );
                                params.put("destination", destination);
                                params.put("date",day+":"+month+":"+year);
                                params.put("time",hour+":"+minute);
                                params.put("paymentStatus",state);
                                params.put("paymentId",id);

                                //returning parameters
                                return params;
                            }
                        };

                        //Creating a Request Queue
                        RequestQueue requestQueue = Volley.newRequestQueue(this);

                        //Adding request to the queue
                        requestQueue.add(stringRequest);


                        //Starting a new activity for the payment details and also putting the payment details with intent
                        Toast.makeText(getApplicationContext(),paymentDetails,Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                        if(confirmation!=null) {
                            String state = confirmation.getProofOfPayment().getState();
                        }
                if(state.equals("approved")) {
                    Toast.makeText(getApplicationContext(), "Payment Approved", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Error in Payment",Toast.LENGTH_LONG).show();
            }

    }*/
    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest("http://getyourhike.esy.es/trek_list_insert.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;

                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("result");
                            getHikeData(result);
                            //Calling method getStudents to get the students from the JSON Array
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getHikeData(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                    if(json.getString("name").equalsIgnoreCase(destination)) {
                        //Adding the name of the student to array list
                        address.setText(json.getString("address"));
                        Picasso.with(this).load(json.getString("photo")).fit().centerCrop().into(imageView1);
                        trip.setText("Your trip to " + destination+" ("+json.getString("location")+")");
                    }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
