package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class MinFatalityFilter extends Filter {
    private static int minFatality;

    public MinFatalityFilter() {
        super();
        minFatality = 0;
    }

    @Override
    public boolean condition(Accident a) {
        return a.getFatality() >= minFatality;
    }

    public static void setMinFatality(int minf) {
        minFatality = minf;
    }
}
