package maphack.qutcode.navlights;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by kane on 5/07/2015.
 */
public class TripResults {
    private Date startTime, endTime;
    private LatLng start, end;
    private float distance;
    private float hours;

    public TripResults(Date st, Date et, List<LatLng> points) {
        startTime = st;
        endTime = et;
        start = points.get(0);
        end = points.get(points.size() - 1);

        int total = 0;
        for (int i = 1; i < points.size(); i++) {
            total += distFrom(points.get(i-1), points.get(i));
        }
        distance = total/1000;
        long diff = endTime.getTime() - startTime.getTime();
        hours = diff / (60 * 60 * 1000);
    }

    //TODO plz
    public String toStr() {
        String str = "Start Time: " + startTime.toString() + "/n";
        str += "End Time: " + endTime.toString() + "/n";
        str += "Hours: " + hours;
        str += "Distance(Km): " + distance;
        //position
        return str;
    }

    //http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
    public static float distFrom(LatLng p1, LatLng p2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(p2.latitude-p1.latitude);
        double dLng = Math.toRadians(p2.longitude-p1.longitude);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(p1.latitude)) * Math.cos(Math.toRadians(p2.latitude)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}
