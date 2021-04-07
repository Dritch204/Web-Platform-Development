package com.example.ukearthquakes;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

//Drew Ritchie S1710460
public class GeneralFeedsAdapter extends RecyclerView.Adapter<GeneralFeedsAdapter.MyViewHolder> {


    private Context mContext;
    MainActivity mainActivity;


    public GeneralFeedsAdapter(Context mContext, MainActivity _mainActivity) {
        this.mainActivity = _mainActivity;
        this.mContext = mContext;
    }


    //Drew Ritchie S1710460
    @NonNull
    @Override
    public GeneralFeedsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralFeedsAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.general_feeds_item, parent, false));
    }

    //Drew Ritchie S1710460
    //get Location String
    private String getEarthQuakeLocation(String toExtractLocationName){
        String strComma[] = toExtractLocationName.split(",");
        String strSecond[] = strComma[0].split(":");

        return strSecond[2];
    }
    //get Magnitude String
    private String getEarthQuakeMagnitude(String toExtractLocationName){
        String strComma[] = toExtractLocationName.split(",");
        String strSecond[] = strComma[0].split(":");

        return strSecond[1].substring(1);
    }

    //Drew Ritchie S1710460
    //get Color on basis of Magnitude
    private int getColorOFEarthQuakeMagnitude(String toExtractLocationName){
        String strComma[] = toExtractLocationName.split(",");
        String strSecond[] = strComma[0].split(":");

        String strThird[] = strSecond[1].split(" ");
        float checkQuake = Float.parseFloat(strThird[3]);
        if(checkQuake<=0.9){
            return Color.GREEN;
        }else if(checkQuake>=1.0 && checkQuake<=1.9){
            return Color.YELLOW;
        }if(checkQuake>=2.0){
            return Color.RED;
        }

        return Color.BLACK;
    }

    //Drew Ritchie S1710460
    @Override
    public void onBindViewHolder(@NonNull GeneralFeedsAdapter.MyViewHolder holder, int position) {

        holder.location_name.setText(getEarthQuakeLocation(MainActivity.earthQuakeModels.get(position).getTitle()));
        holder.magnitude.setText(getEarthQuakeMagnitude(MainActivity.earthQuakeModels.get(position).getTitle()));
        holder.earthquake_date.setText(MainActivity.earthQuakeModels.get(position).getPubDate());

        holder.color_type.setBackgroundColor(getColorOFEarthQuakeMagnitude(
                MainActivity.earthQuakeModels.get(position).getTitle()));


    }

    //Drew Ritchie S1710460
    @Override
    public int getItemCount() {
        return MainActivity.earthQuakeModels.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView location_name, magnitude, earthquake_date;
        RelativeLayout color_type;


        MyViewHolder(View view) {
            super(view);

            //TextViews
            location_name = view.findViewById(R.id.location_name);
            magnitude = view.findViewById(R.id.magnitude);
            earthquake_date = view.findViewById(R.id.earthquake_date);

            //RelativeLayout
            color_type = view.findViewById(R.id.color_type);


        }
    }

}
