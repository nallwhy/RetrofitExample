package ee.maytr.www.retrofitexample;

import android.content.Context;
import android.widget.Toast;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by json on 2015. 12. 28..
 */
public class WeatherManager {
    private Context context = null;
    private Retrofit retrofit = null;
    private WeatherService weatherService = null;

    public WeatherManager(Context context) {
        this.context = context;

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.httpUrl().newBuilder()
                        .addQueryParameter("APPID", "")
                        .build();
                Request newRequest = chain.request().newBuilder().url(url).build();
                return chain.proceed(newRequest);
            }
        });

        this.retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://api.openweathermap.org/data/2.5/").client(client).build();
        this.weatherService = retrofit.create(WeatherService.class);
    }

    public interface WeatherService {
        @POST("weather")
        Call<Dummy> getWeather(@Query("q") String city);
    }

    Callback<Dummy> weatherCallback = new Callback<Dummy>() {
        @Override
        public void onResponse(Response<Dummy> response, Retrofit retrofit) {
            Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
    };

    public void getWeather() {
        Call<Dummy> call = weatherService.getWeather("London");
        call.enqueue(weatherCallback);
    }

    private class Dummy {
        public Weather main;

        public Dummy(Weather main) {
            this.main = main;
        }

        @Override
        public String toString() {
            return this.main.toString();
        }
    }

    private class Weather {
        public double temp = 0.0;
        public double pressure = 0.0;
        public double humidity = 0.0;
        public double temp_min = 0.0;
        public double temp_max = 0.0;

        public Weather(double temp, double pressure, double humidity, double temp_min, double temp_max) {
            this.temp = temp;
            this.pressure = pressure;
            this.humidity = humidity;
            this.temp_min = temp_min;
            this.temp_max = temp_max;
        }

        @Override
        public String toString() {
            return "temp: " + temp + ", pressure: " + pressure + ", humidity: " + humidity + ", temp_min: " + temp_min + ", temp_max: " + temp_max;
        }
    }
}
