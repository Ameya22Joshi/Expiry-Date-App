package com.example.expirydate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirydate.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList product_id, productname, numofdays, quantity;
    Animation translate_anim;

    CustomAdapter(Activity activity, Context context, ArrayList product_id, ArrayList productname, ArrayList numofdays, ArrayList quantity){
        this.activity = activity;
        this.context = context;
        this.product_id = product_id;
        this.productname = productname;
        this.numofdays = numofdays;
        this.quantity = quantity;


    }
    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  final MyViewHolder holder, final int position) {

        holder.product_id_txt.setText(String.valueOf(product_id.get(position)));
        holder.product_name.setText(String.valueOf(productname.get(position)));
        holder.days_left.setText(String.valueOf(numofdays.get(position)));
        holder.quantity_txt.setText(String.valueOf(quantity.get(position)));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(product_id.get(position)));
                intent.putExtra("pname",String.valueOf(productname.get(position)));
                intent.putExtra("pdaysleft",String.valueOf(numofdays.get(position)));
                intent.putExtra("pquantity",String.valueOf(quantity.get(position)));

                activity.startActivityForResult(intent,1);
            }
        });

    }

    @Override
    public int getItemCount() {

        return productname.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView product_id_txt,product_name, days_left, quantity_txt;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            product_id_txt = itemView.findViewById(R.id.product_id_txt);
            product_name = itemView.findViewById(R.id.product_name);
            days_left = itemView.findViewById(R.id.days_left);
            quantity_txt = itemView.findViewById(R.id.quantity_txt);


            mainLayout = itemView.findViewById(R.id.mainLayout);
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
