package com.oleksandr.weatherviewer;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr on 30.03.2017.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private static final String TAG = WeatherAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Weather> mList;
    private String mDateChange = new String();
    //private OnItemClickListener mListener;



    public WeatherAdapter(Context context, ArrayList<Weather> list) {
        this.mContext = context;
        Log.i("SIZE", list.size() + "");
        this.mList = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        //View view = inflater.inflate(R.layout.item_weather, parent, false);
        View view = inflater.inflate(R.layout.item_weather_tmp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Weather item = mList.get(position);
        if (position == 0 || !(item.getDate().equals(mList.get(position - 1).getDate()))) {
            holder.mTextDate.setText(item.getDate());
        } else{
            holder.mTextDate.setVisibility(View.GONE);
        }
        Picasso.with(mContext).load(item.getIcon()).into(holder.mIconWeather);
        holder.mTextTime.setText(item.getTime());
        holder.mTextTemp.setText(item.getTemp());
        holder.mTextHumidity.setText(item.getHumidity());
        holder.mTextPressure.setText(item.getPressure());
        holder.mTextCloudiness.setText(item.getCloudiness());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onItemClick(item);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    public interface OnItemClickListener {
//        void onItemClick(Weather item);
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIconWeather;
        private TextView mTextTime;
        private TextView mTextDate;
        private TextView mTextTemp;
        private TextView mTextHumidity;
        private TextView mTextPressure;
        private TextView mTextCloudiness;


        //TODO Bind views
        public ViewHolder(View itemView) {
            super(itemView);

            mTextDate = (TextView) itemView.findViewById(R.id.text_date);
            mTextTime = (TextView) itemView.findViewById(R.id.text_time);
            mTextPressure = (TextView) itemView.findViewById(R.id.text_pressure);
            mTextHumidity = (TextView) itemView.findViewById(R.id.text_humidity);
            mTextTemp = (TextView) itemView.findViewById(R.id.text_temp);
            mIconWeather = (ImageView) itemView.findViewById(R.id.icon_weather);
            mTextCloudiness = (TextView) itemView.findViewById(R.id.text_cloudiness);
            //ButterKnife.bind(this, itemView);
        }
    }
}