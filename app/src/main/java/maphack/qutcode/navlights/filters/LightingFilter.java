package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public class LightingFilter extends Filter {
    private static int lighting;
    private static final int SUNRISE = 0;
    private static final int MIDDAY = 1;
    private static final int SUNSET = 2;
    private static final int NIGHT = 3;

    public LightingFilter() {
        lighting = SUNRISE;
    }

    @Override
    public boolean condition(Accident a) {
        //return a.getLighting == lighting;s
        return true;
    }

    public static void setLighting(int light) {
        lighting = light;
    }
}
