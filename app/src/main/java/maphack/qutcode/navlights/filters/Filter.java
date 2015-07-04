package maphack.qutcode.navlights.filters;

import maphack.qutcode.navlights.Accident;

/**
 * Created by kane on 4/07/2015.
 */
public abstract class Filter {
    private boolean active;

    public Filter() {
        active = false;
    }

    public boolean check(Accident a) {
        if (active) {
            return condition(a);
        } else {
            return true;
        }
    }

    //return true if it passes
    protected abstract boolean condition(Accident a);

    public void setActive(boolean act) {
        active = act;
    }
}
