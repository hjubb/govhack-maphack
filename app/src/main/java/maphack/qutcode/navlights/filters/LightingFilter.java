package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class LightingFilter extends Filter {
    private static int lighting;
    public static final int SUNRISE = 0;
    public static final int MIDDAY = 1;
    public static final int SUNSET = 2;
    public static final int NIGHT = 3;

    public LightingFilter() {
        lighting = SUNRISE;
    }

    @Override
    public boolean condition(Accident a) {
        return a.getLighting() == lighting;
    }

    public static void setLighting(int light) {
        if (light >= 0 && light < 4) {
            lighting = light;
            Filters.activateFilter(Filters.LightingFilter, true);
        } else {
            Filters.activateFilter(Filters.LightingFilter, false);
        }
    }
}
