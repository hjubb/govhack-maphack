package maphack.qutcode.navlights.filters;

import java.util.ArrayList;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class Filters {
    private static ArrayList<Filter> filters = new ArrayList<>();
    public static final int maxSeverityFilter = 0;
    public static final int minSeverityFilter = 1;
    public static final int RoadSurfaceFilter = 2;
    public static final int LightingFilter = 3;

    public static void prepare() {
        filters.add(new MaxSeverityFilter());
        filters.add(new MinSeverityFilter());
        filters.add(new RoadSurfaceFilter());
        filters.add(new LightingFilter());

        //testing
        //MaxSeverityFilter.setMaxSeverity(3);
        //MinSeverityFilter.setMinSeverity(2);
    }

    public static void activateFilter(int f, boolean active) {
        filters.get(f).setActive(active);
    }

    public static boolean toDisplay(Accident a) {
        for (Filter f : filters) {
            if (!f.condition(a)) {
                return false;
            }
        }
        return true;
    }
}
