package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class MaxFatalityFilter extends Filter{
    private static int maxFatality;

    public MaxFatalityFilter() {
        super();
        maxFatality = 0;
    }

    @Override
    public boolean condition(Accident a) {
        return a.getFatality() <= maxFatality;
    }

    public static void setMaxFatality(int maxf) {
        maxFatality = maxf;
    }
}
