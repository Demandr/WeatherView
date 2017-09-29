package com.oleksandr.weatherviewer.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oleksandr.weatherviewer.R;
import com.oleksandr.weatherviewer.WeatherAdapter;
import com.oleksandr.weatherviewer.models.WeatherData;
import com.oleksandr.weatherviewer.presentation.presenter.WeatherPresenter;
import com.oleksandr.weatherviewer.presentation.view.WeatherView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends MvpAppCompatFragment implements WeatherView {
    public static final String TAG = "WeatherFragment";
    @InjectPresenter
    WeatherPresenter mWeatherPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.text_city)
    TextView mTextCity;
    private WeatherAdapter mWeatherAdapter;

    @ProvidePresenter
    WeatherPresenter provideWeatherPresenter(){
        return new WeatherPresenter(getActivity().getApplicationContext());
    }

    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
        mWeatherPresenter.loadDataWeather("Kiev");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, v);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener
                (new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "QueryTextSubmit: " + s);
                        mWeatherPresenter.loadDataWeather(s);
                        searchView.clearFocus();
                        mTextCity.setText(s);
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d(TAG, "QueryTextChange: " + s);
                        return false;
                    }
                });
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void updateWeatherView(WeatherData weather) {
        if (mWeatherAdapter == null){
            mWeatherAdapter = new WeatherAdapter(getActivity(), weather);
            mRecyclerView.setAdapter(mWeatherAdapter);
        }else{
            mWeatherAdapter.setWeatherData(weather);
            mWeatherAdapter.notifyDataSetChanged();
        }
    }
}
