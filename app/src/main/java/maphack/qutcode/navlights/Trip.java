package maphack.qutcode.navlights;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by kane on 5/07/2015.
 */
public class Trip {
    private Polyline trip;
    private Date startTime;
    private Date endTime;

    public Trip(GoogleMap mMap) {
        //saveshit
        startTime = Calendar.getInstance().getTime();

        trip = mMap.addPolyline(new PolylineOptions()
                .width(2)
                .color(Color.BLACK)
                .geodesic(true)
                .visible(true)
                .zIndex(0)
                .add(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())));
    }

    public void end() {
        //save shit
        endTime = Calendar.getInstance().getTime();
    }

    public void update(Location location) {
        List<LatLng> points = trip.getPoints();
        points.add(new LatLng(location.getLatitude(), location.getLongitude()));
        trip.setPoints(points);
    }

    public void plot(GoogleMap mMap) {
        Polyline temp = mMap.addPolyline(new PolylineOptions()
                .width(2)
                .color(Color.BLACK)
                .geodesic(true)
                .visible(true)
                .zIndex(0));
        temp.setPoints(trip.getPoints());

        Random rnd = new Random();
        temp.setColor(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
    }
}
