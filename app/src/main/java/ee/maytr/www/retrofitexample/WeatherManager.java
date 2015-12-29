package ee.maytr.www.retrofitexample;

import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by json on 2015. 12. 28..
 */
public class WeatherManager {
    private Context context = null;
    private Retrofit retrofit = null;
    private WeatherService weatherService = null;
    private Bus bus = null;

    public WeatherManager(Context context) {
        this.context = context;
        this.bus = ((MainActivity) this.context).bus;
        bus.register(this);

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.httpUrl().newBuilder()
                        .addQueryParameter("APPID", "1e3c093b9eedf8d705bd0cf6d5012e2f")
                        .build();
                Request newRequest = chain.request().newBuilder().url(url).build();
                return chain.proceed(newRequest);
            }
        });

        this.retrofit = new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create()).baseUrl("http://api.openweathermap.org/data/2.5/").client(client).build();
        this.weatherService = retrofit.create(WeatherService.class);
    }

    public void getWeather() {
        Call<Map<String, Object>> call = weatherService.getWeather("London");
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Response<Map<String, Object>> response, Retrofit retrofit) {
//                Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show();
                Map<String, Object> body = response.body();

                ArrayList<Weather> weathers = new ArrayList<>();
                List<Map<String, Object>> list = (List<Map<String, Object>>)body.get("list");
                for (Map<String, Object> weatherJson : list) {
                    Map<String, Object> temp = (Map<String, Object>)weatherJson.get("temp");
                    weathers.add(new Weather((double)temp.get("min"), (double)temp.get("max"), (double)weatherJson.get("pressure"), (int)weatherJson.get("humidity")));
                }
                bus.post(weathers);
                Toast.makeText(context, String.valueOf(body.get("cod")), Toast.LENGTH_LONG).show();
//                List<Map<String, Object>> list = (List<Map<String, Object>>)body.get("list");
//                bus.post(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface WeatherService {
        @POST("forecast/daily")
        Call<Map<String, Object>> getWeather(@Query("q") String city);
    }

    public class Dummy {
        public List<Weather> list;

        public Dummy(List<Weather> list) {
            this.list = list;
        }
    }

    public class Weather {
        public double temp_min = 0.0;
        public double temp_max = 0.0;
        public double pressure = 0.0;
        public int humidity = 0;

        public Weather(double temp_min, double temp_max, double pressure, int humidity) {
            this.temp_min = temp_min;
            this.temp_max = temp_max;
            this.pressure = pressure;
            this.humidity = humidity;
        }
    }
}
