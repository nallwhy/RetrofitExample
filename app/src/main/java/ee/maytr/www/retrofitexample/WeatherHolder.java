package ee.maytr.www.retrofitexample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by json on 2015. 12. 30..
 */
public class WeatherHolder extends RecyclerView.ViewHolder {
    public TextView temp_min;
    public TextView temp_max;
    public TextView pressure;
    public TextView humidity;

    public WeatherHolder(View view) {
        super(view);

        temp_min = (TextView) view.findViewById(R.id.temp_min);
        temp_max = (TextView) view.findViewById(R.id.temp_max);
        pressure = (TextView) view.findViewById(R.id.pressure);
        humidity = (TextView) view.findViewById(R.id.humidity);
    }
}