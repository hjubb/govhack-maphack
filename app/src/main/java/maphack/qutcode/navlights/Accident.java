package maphack.qutcode.navlights;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.heatmaps.WeightedLatLng;

/**
 * Created by Harrison on 4/07/2015.
 */
public class Accident extends WeightedLatLng{

    public Accident(double lat, double lng, int fatal, int hospital, int major, int minor)
    {
        super(new LatLng(lat, lng), (3*fatal + 2*hospital + 2*major + minor));
    }
}
