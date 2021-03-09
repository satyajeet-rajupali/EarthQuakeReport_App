package com.satyajeet.earthquakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class EarthquakeDetailsAdapter extends ArrayAdapter<EarthquakeDetails> {

    public EarthquakeDetailsAdapter(@NonNull Context context, @NonNull List<EarthquakeDetails> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        EarthquakeDetails details = getItem(position);

        TextView magnitude = (TextView)listItemView.findViewById(R.id.magnitude);
        magnitude.setText(String.valueOf(details.getMagintude()));

        GradientDrawable magnitudeDrawable = (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(Double.parseDouble(details.getMagintude()));
        magnitudeDrawable.setColor(magnitudeColor);

        TextView primaryLocation = (TextView)listItemView.findViewById(R.id.primary_location);
        primaryLocation.setText(details.getPrimaryLocation());

        TextView locationOffset = (TextView)listItemView.findViewById(R.id.location_offset);
        locationOffset.setText(details.getLocationOffSet());

        TextView date = (TextView)listItemView.findViewById(R.id.date);
        date.setText(details.getDate());

        TextView time = (TextView)listItemView.findViewById(R.id.time);
        time.setText(details.getTime());

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResourceId ;
        int magnitudeFloor = (int)Math.floor(magnitude);

        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
