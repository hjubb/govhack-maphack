package maphack.qutcode.navlights;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.heatmaps.WeightedLatLng;

/**
 * Created by Harrison on 4/07/2015.
 */
public class Accident extends WeightedLatLng{
    private LatLng loc;
    private int severity;

    public Accident(LatLng location, int fatal, int hospital, int major, int minor)
    {
        super(location, (3*fatal + 2*hospital + 2*major + minor));
        loc = location;
        severity = 3*fatal + 2*hospital + 2*major + minor;
    }

    public LatLng getLocation() {
        return loc;
    }

    public int getSeverity() {
        return severity;
    }
}
