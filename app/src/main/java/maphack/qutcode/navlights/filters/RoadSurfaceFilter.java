package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class RoadSurfaceFilter extends Filter{
    private static int roadSurface;
    private static final int DRY = 0;
    private static final int WET = 1;

    public RoadSurfaceFilter() {
        roadSurface = WET;
    }

    @Override
    public boolean condition(Accident a) {
        //return a.getRoadSurface == roadSurface;
        return true;
    }

    public static void setRoadSurface(int rs) {
        roadSurface = rs;
    }
}
