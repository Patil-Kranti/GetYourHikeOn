package com.example.kranti_laptop.getyourhikeon;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends android.support.v4.app.Fragment {

    View view;
    private Context context;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private  RecyclerView recyclerView;
    private  ArrayList<DataModel> data;
    private Button bookNow,bookLater;
    private Calendar c;
    private Intent intent;
    private Bundle bundle;
    private String destination="";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner spinner;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    //An ArrayList for Spinner Items
    private ArrayList<String> treks;
    private ProgressBar progressBar;

    //JSON Array
    private JSONArray result;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home, container, false);
        context=view.getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        bookNow=(Button) view.findViewById(R.id.bookNow);
        bookLater=(Button) view.findViewById(R.id.bookLater);
        spinner=(Spinner) view.findViewById(R.id.spinner);
        progressBar=(ProgressBar) view.findViewById(R.id.progress);
        treks=new ArrayList<String>();
        Bundle args = getArguments();
        if (args  != null && args.containsKey("KUNGSLEDEN")){
            String userId = args.getString("KUNGSLEDEN");

        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    destination=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                if(destination=="Enter Your Destination")
                {
                    Toast.makeText(context,"Please enter destination",Toast.LENGTH_LONG).show();
                    return;
                }

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

//                                txtTime.setText(hourOfDay + ":" + minute);
                                intent=new Intent(context,BookActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putInt("year",mYear);
                                bundle.putInt("month",mMonth);
                                bundle.putInt("day",mDay);
                                bundle.putInt("hour",hourOfDay);
                                bundle.putInt("minute",minute);
                                bundle.putString("destination",destination);
                                intent.putExtras(bundle);
                                startActivity(intent);

                                }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        bookLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                if(destination=="Enter Your Destination")
                {
                    Toast.makeText(context,"Please enter destination",Toast.LENGTH_LONG).show();
                    return;
                }


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, final int year,
                                                  final int monthOfYear, final int dayOfMonth) {
//                                Toast.makeText(context,dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,Toast.LENGTH_LONG).show();
//                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                Intent intent=new Intent(context,BookActivity.class);
//                                Bundle bundle=new Bundle();
//                                bundle.putInt("year",year);
//                                bundle.putInt("month",monthOfYear);
//                                bundle.putInt("day",dayOfMonth);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {

//                                txtTime.setText(hourOfDay + ":" + minute);
                                                Intent intent=new Intent(context,BookActivity.class);
                                              Bundle bundle=new Bundle();
                                              bundle.putInt("year",year);
                                              bundle.putInt("month",monthOfYear);
                                              bundle.putInt("day",dayOfMonth);
                                                bundle.putInt("hour",hourOfDay);
                                                bundle.putInt("minute",minute);
                                                bundle.putString("destination",destination);
                                                intent.putExtras(bundle);
                                                startActivity(intent);

                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.trek_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();

//        data = new ArrayList<DataModel>();
//        for (int i = 0; i < MyData.nameArray.length; i++) {
//            data.add(new DataModel(
//                    MyData.nameArray[i],
//                    MyData.locationArray[i],
//                    MyData.id_[i],
//                    MyData.drawableArray[i]
//            ));
//        }
        getData();

        adapter = new CustomAdapter(context, data, treks, new CustomAdapter.MyOnListener() {
            @Override
            public void OnposClicked(int pos) {
                spinner.setSelection(pos+1,true);
            }
        });
        recyclerView.setAdapter(adapter);

        imageView=(ImageView) view.findViewById(R.id.logout);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMessage();
            }
        });



        return view;
    }
    public void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        { public void onClick(DialogInterface dialog, int which)
        { switch (which)
        { case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
            firebaseAuth.signOut();
            //closing activity
            getActivity().finish();
            //starting login activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            break;
            case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                break;
        } } };
        AlertDialog.Builder builder = new AlertDialog.Builder(context); builder.setMessage("Logout?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show(); }
    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest("http://getyourhike.esy.es/trek_list_insert.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("result");

                                                        //Calling method getStudents to get the students from the JSON Array
                            getHikes(result);
                            getHikeData(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(context,"Please Check Your Internet Connection And Restart",Toast.LENGTH_LONG).show();
                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getHikes(JSONArray j){
        //Traversing through all the items in the json array
        treks.add("Enter Your Destination");
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                //Adding the name of the student to array list
                treks.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, treks));
        spinner.setSelection(0);

    }
    private void getHikeData(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                //Adding the name of the student to array list
                data.add(new DataModel(json.getString("name"),json.getString("location"),i,json.getString("photo")));



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        adapter.notifyDataSetChanged();
    }

}
