package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class MaxSeverityFilter extends Filter{
    private static int maxSeverity;

    public MaxSeverityFilter() {
        maxSeverity = 0;
    }

    @Override
    public boolean condition(Accident a) {
        return a.getSeverity() <= maxSeverity;
    }

    public static void setMaxSeverity(int maxs) {
        maxSeverity = maxs;
    }
}