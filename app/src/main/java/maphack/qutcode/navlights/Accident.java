package maphack.qutcode.navlights;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.heatmaps.WeightedLatLng;

import maphack.qutcode.navlights.filters.LightingFilter;
import maphack.qutcode.navlights.filters.RoadSurfaceFilter;

/**
 * Created by Harrison on 4/07/2015.
 */
public class Accident extends WeightedLatLng{
    private LatLng loc;
    private int severity, light, roadCondition;

    public Accident(LatLng location, int fatal, int hospital, int major, int minor, String surface, int hour)
    {
        super(location, (3*fatal + 2*hospital + 2*major + minor));
        loc = location;
        severity = 3*fatal + 2*hospital + 2*major + minor;

        if (surface.contains("Wet")) {
            roadCondition = RoadSurfaceFilter.WET;
        } else {
            roadCondition = RoadSurfaceFilter.DRY;
        }

        if (hour <= 6) {
            light = LightingFilter.NIGHT;
        } else if (hour <= 9) {
            light = LightingFilter.SUNRISE;
        } else if (hour <= 15) {
            light = LightingFilter.MIDDAY;
        } else if (hour <= 20){
            light = LightingFilter.SUNSET;
        } else {
            light = LightingFilter.NIGHT;
        }
    }

    public LatLng getLocation() {
        return loc;
    }

    public int getSeverity() {
        return severity;
    }

    public int getLighting() {
        return light;
    }

    public int getRoadCondition() {
        return roadCondition;
    }
}
