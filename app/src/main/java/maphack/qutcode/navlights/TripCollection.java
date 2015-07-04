package maphack.qutcode.navlights;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import maphack.qutcode.navlights.TripResults;

/**
 * Created by kane on 5/07/2015.
 */
public class TripCollection {
    ArrayList<TripResults> trips;
    Trip currentTrip;
    GoogleMap mMap;

    public TripCollection(GoogleMap map) {
        trips = new ArrayList<>();
        mMap = map;
    }

    public void startOrEndTrip() {
        if (currentTrip == null) {
            currentTrip = new Trip(mMap);
        } else {
            trips.add(currentTrip.end());
            currentTrip = null;
            mMap.clear();
        }
    }

    public void updateCurrentTrip(Location location) {
        if (currentTrip != null) {
            currentTrip.update(location);
        }
    }
}
