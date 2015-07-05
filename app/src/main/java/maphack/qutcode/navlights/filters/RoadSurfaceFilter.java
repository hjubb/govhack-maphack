package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class RoadSurfaceFilter extends Filter{
    private static int roadSurface;
    public static final int DRY = 0;
    public static final int WET = 1;

    public RoadSurfaceFilter() {
        roadSurface = WET;
    }

    @Override
    public boolean condition(Accident a) {
        return a.getRoadCondition() == roadSurface;
    }

    public static void setRoadSurface(int rs) {
        if (rs >= 0 && rs < 2) {
            roadSurface = rs;
            Filters.activateFilter(Filters.RoadSurfaceFilter, true);
        } else {
            Filters.activateFilter(Filters.RoadSurfaceFilter, false);
        }
    }
}
