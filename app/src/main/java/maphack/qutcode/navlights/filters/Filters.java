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

    public static void prepare() {
        filters.add(new MaxFatalityFilter());
        filters.add(new MinFatalityFilter());

        //testing
        //activateFilter(maxFatalityFilter, true);
        //MaxFatalityFilter.setMaxFatality(4);
        //activateFilter(minFatalityFilter, true);
        //MinFatalityFilter.setMinFatality(4);
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
