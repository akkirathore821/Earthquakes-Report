package com.example.earthquake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

//    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> earthquakes) {
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_view2, parent, false);
        }


        // Find the earthquake at the given position in the list of earthquakes
        Earthquake currentEarthquake = getItem(position);

        // Find the TextView with view ID
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        TextView location = (TextView) listItemView.findViewById(R.id.earthquake_location);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        TextView depth = (TextView) listItemView.findViewById(R.id.intensity);
        TextView coordinates = (TextView) listItemView.findViewById(R.id.eartquake_coordinates);

        //Initialize the date
        String formattedMagnitude = Formatter(currentEarthquake.getMagnitude());
        String formattedDepth = Formatter(currentEarthquake.getDepth()) + " KM";
        String formattedLocation = currentEarthquake.getPlace();
        String formattedDate = formatDate( new Date(currentEarthquake.getTime()));
        String formattedTime = formatTime( new Date(currentEarthquake.getTime()));
        String formattedCoordinates = (Formatter(currentEarthquake.getLatitude()) +
                " South Latitude - " + Formatter(currentEarthquake.getLongitude()) +
                " East Longitude");

//        System.out.println(formattedCoordinates);

        // Display the data
        magnitudeView.setText(formattedMagnitude);
        dateView.setText(formattedDate);
        timeView.setText(formattedTime);
        location.setText(formattedLocation);
        depth.setText(formattedDepth);
        coordinates.setText(formattedCoordinates);


        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);

    }

    private String Formatter(double value) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(value);
    }
}
