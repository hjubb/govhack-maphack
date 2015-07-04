package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class MinSeverityFilter extends Filter {
    private static int minSeverity;

    public MinSeverityFilter() {
        minSeverity = 0;
    }

    @Override
    public boolean condition(Accident a) {
        return a.getSeverity() >= minSeverity;
    }

    public static void setMinSeverity(int minf) {
        minSeverity = minf;
    }
}