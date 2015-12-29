package ee.maytr.www.retrofitexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by json on 2015. 12. 30..
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder> {
    private List<WeatherManager.Weather> weathers;
    private MainActivity mainActivity = null;

    public WeatherAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.mainActivity.bus.register(this);
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        if (position >= weathers.size())
            return;

        holder.temp_min.setText(String.format("Temp Min: %f", weathers.get(position).temp_min));
        holder.temp_max.setText(String.format("Temp Max: %f", weathers.get(position).temp_max));
        holder.pressure.setText(String.format("Pressure: %f", weathers.get(position).pressure));
        holder.humidity.setText(String.format("Humidity: %d", weathers.get(position).humidity));
    }

    @Override
    public int getItemCount() {
        return weathers == null ? 0 : weathers.size();
    }

    @Subscribe
    public void setWeathers(ArrayList<WeatherManager.Weather> weathers) {
        this.weathers = weathers;
        notifyDataSetChanged();
    }
}