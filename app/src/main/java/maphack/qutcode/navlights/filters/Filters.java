package maphack.qutcode.navlights.filters;

import java.util.ArrayList;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class Filters {
    private static ArrayList<Filter> filters = new ArrayList<>();
    public static final int maxFatalityFilter = 0;
    public static final int minFatalityFilter = 1;
    public static final int RoadSurfaceFilter = 2;
    public static final int LightingFilter = 3;

    public static void prepare() {
        filters.add(new MaxSeverityFilter());
        filters.add(new MinSeverityFilter());
        filters.add(new RoadSurfaceFilter());
        filters.add(new LightingFilter());

        //testing
        //activateFilter(maxFatalityFilter, true);
        //MaxSeverityFilter.setMaxSeverity(3);
        //activateFilter(minFatalityFilter, true);
        //MinSeverityFilter.setMinSeverity(2);
        //activateFilter(RoadSurfaceFilter, true);
        //activateFilter(LightingFilter, true);
    }

    public static void activateFilter(int f, boolean active) {
        filters.get(f).setActive(active);
    }

    public static boolean toDisplay(Accident a) {
        for (Filter f : filters) {
            if (!f.check(a)) {
                return false;
            }
        }
        return true;
    }
}
