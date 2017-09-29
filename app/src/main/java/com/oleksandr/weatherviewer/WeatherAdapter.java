package com.oleksandr.weatherviewer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleksandr.weatherviewer.models.WeatherData;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private static final String DATE_FORMAT = "EEE,  MMM d,  yyyy";
    private static final String TAG = WeatherAdapter.class.getSimpleName();
    private Context mContext;

    private String dataLast = null;
    private WeatherData mWeatherData;

    public WeatherAdapter(Context context, WeatherData weatherData) {
        this.mContext = context;
        this.mWeatherData = weatherData;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            convertDateFormat(holder, position);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.with(mContext).load((mContext.getString(R.string.icon_url,
                mWeatherData.getList().get(position).getWeather().get(0).getIcon()))).into(holder.mIconWeather);
        holder.mTextTemp.setText((mContext.getString(R.string.temp,
                mWeatherData.getList().get(position).getMain().getTemp().intValue())));
        holder.mTextHumidity.setText((mContext.getString(R.string.humidity,
                mWeatherData.getList().get(position).getMain().getHumidity())));
        holder.mTextPressure.setText((mContext.getString(R.string.pressure,
                mWeatherData.getList().get(position).getMain().getPressure().intValue())));
        holder.mTextCloudiness.setText(mWeatherData.getList().get(position)
                .getWeather().get(0).getDescription());
    }

    public void setWeatherData(WeatherData weatherData){
        mWeatherData = weatherData;
    }

    private void convertDateFormat(ViewHolder holder,int position) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String dt = mWeatherData.getList().get(position).getDtTxt();

        Date date = format.parse(dt.substring(0,10));

        if (position == 0 || !(DateFormat.format(DATE_FORMAT, date).toString().equals(dataLast))) {
            if (position == 0) {
                holder.mTextDate.setText(mContext.getString(R.string.today, DateFormat.format(DATE_FORMAT, date).toString()));
            }else {
                holder.mTextDate.setText(DateFormat.format(DATE_FORMAT, date).toString());
            }
            holder.mTextDate.setVisibility(View.VISIBLE);
        } else{
            holder.mTextDate.setVisibility(View.GONE);
        }
        holder.mTextTime.setText(dt.substring(11,16));
        dataLast = DateFormat.format(DATE_FORMAT, date).toString();

    }

    @Override
    public int getItemCount() {
        return mWeatherData.getList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_weather)
        ImageView mIconWeather;
        @BindView(R.id.text_time)
        TextView mTextTime;
        @BindView(R.id.text_date)
        TextView mTextDate;
        @BindView(R.id.text_temp)
        TextView mTextTemp;
        @BindView(R.id.text_humidity)
        TextView mTextHumidity;
        @BindView(R.id.text_pressure)
        TextView mTextPressure;
        @BindView(R.id.text_cloudiness)
        TextView mTextCloudiness;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}