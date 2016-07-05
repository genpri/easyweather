package com.daniloprado.weather.view.cityforecast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daniloprado.weather.R;
import com.daniloprado.weather.data.dto.ForecastDto;
import com.daniloprado.weather.model.City;
import com.daniloprado.weather.model.weather.Data;
import com.daniloprado.weather.util.DateUtils;
import com.daniloprado.weather.util.WeatherUtils;
import com.daniloprado.weather.view.base.ContractFragment;
import com.daniloprado.weather.widget.DailyWeatherView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityForecastFragment extends ContractFragment<CityForecastFragment.Contract> implements CityForecastContract.View {

    public interface Contract {
        void onBackPressed();
    }

    @BindView(R.id.textview_temperature)
    TextView textViewTemperature;

    @BindView(R.id.textview_city_name)
    TextView textViewCityName;

    @BindView(R.id.textview_current_city_weather)
    TextView textViewCurrentWeather;

    @BindView(R.id.imageview_current_day_weather)
    ImageView imageViewCurrentWeather;

    @BindView(R.id.first_day)
    DailyWeatherView dailyWeatherViewDayOne;

    @BindView(R.id.second_day)
    DailyWeatherView dailyWeatherViewDayTwo;

    @BindView(R.id.third_day)
    DailyWeatherView dailyWeatherViewDayThree;

    @BindView(R.id.fourth_day)
    DailyWeatherView dailyWeatherViewDayFour;

    @BindView(R.id.fifth_day)
    DailyWeatherView dailyWeatherViewDayFive;

    @Inject CityForecastContract.Presenter presenter;
    private City city;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDiComponent().inject(this);
        loadArgs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_forecast, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.setView(this);
        presenter.loadData(city);
    }

    public void loadArgs() {
        Bundle bundle = getArguments();
        city = (City) bundle.getSerializable("city");
    }

    @Override
    public void showLoadingLayout() {

    }

    @Override
    public void showContentLayout() {

    }

    @Override
    public void showErrorLayout() {

    }

    @Override
    public void setupUi(ForecastDto dto) {
        textViewTemperature.setText(String.valueOf(WeatherUtils.getFormattedTemperature(dto.currently.temperature)));
        textViewCityName.setText(city.name);
        textViewCurrentWeather.setText(dto.currently.summary);
        imageViewCurrentWeather.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                WeatherUtils.getWeatherIconResourceFromString(dto.currently.icon),
                null));

        setupDayForecast(dailyWeatherViewDayOne, dto.daily.data.get(1));
        setupDayForecast(dailyWeatherViewDayTwo, dto.daily.data.get(2));
        setupDayForecast(dailyWeatherViewDayThree, dto.daily.data.get(3));
        setupDayForecast(dailyWeatherViewDayFour, dto.daily.data.get(4));
        setupDayForecast(dailyWeatherViewDayFive, dto.daily.data.get(5));
    }

    private void setupDayForecast(DailyWeatherView dailyWeatherView, Data data) {
        dailyWeatherView.setDayOfTheWeek(DateUtils.getDayOfTheWeekFromUnixTime(data.time));
        dailyWeatherView.setDayWeatherImage(WeatherUtils.getWeatherIconResourceFromString(data.icon));
        dailyWeatherView.setMaxDayTemp(WeatherUtils.getFormattedTemperature(data.temperatureMax));
        dailyWeatherView.setMinDayTemp(WeatherUtils.getFormattedTemperature(data.temperatureMin));
    }

}