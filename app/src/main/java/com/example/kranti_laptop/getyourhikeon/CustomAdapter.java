package com.example.kranti_laptop.getyourhikeon;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;
    private ArrayList<String> trek;
    private Context context;
    Fragment fragment;
    private MyOnListener onListener;

    public interface MyOnListener{
        public void OnposClicked(int pos);
    }


    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        TextView textViewName1;
        ImageView imageViewIcon;
        LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.trek_name);
            this.textViewName1 = (TextView) itemView.findViewById(R.id.trek_location);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.display_trek_image);
            this.linearLayout =(LinearLayout) itemView.findViewById(R.id.linearLayout);
            imageViewIcon.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d("Size","Item selected");
            onListener.OnposClicked(getAdapterPosition());
        }
    }

    public CustomAdapter(Context context,ArrayList<DataModel> data,ArrayList<String> trek,MyOnListener listener) {
        this.dataSet = data;
        this.context=context;
        this.trek=trek;
        this.onListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_treks, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewName1 = holder.textViewName1;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewName1.setText(dataSet.get(listPosition).getLocation());
        Picasso.with(context)
                .load(dataSet.get(listPosition).getImage()).resize(400,400).centerInside()
                .into(imageView);
//
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                fragment = new HomeFragment();
////                Bundle bundle=new Bundle();
////                bundle.putString("KUNGSLEDEN","KUNGSLEDEN");
////                fragment.setArguments(bundle);
//                Toast.makeText(context,"id"+listPosition,Toast.LENGTH_LONG).show();
//
//            }
//        });
//        textViewName4.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//
////Enabling scrolling on TextView.
//
//        textViewName4.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public int getItemCount() {
        Log.d("Size",String.valueOf(dataSet.size()));
        return dataSet.size();
    }
}