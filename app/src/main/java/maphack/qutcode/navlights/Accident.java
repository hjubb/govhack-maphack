package maphack.qutcode.navlights;

/**
 * Created by Harrison on 4/07/2015.
 */
public class Accident {
    private int REF;
    private double LAT;
    private double LONG;
    private int FATAL;
    private int HOSPITAL;
    private int MAJOR;
    private int MINOR;
    public Accident(int ref, double lat, double Long, int fatal, int hospital, int major, int minor)
    {
        this.REF = ref;
        this.LAT = lat;
        this.LONG = Long;
        this.FATAL = fatal;
        this.HOSPITAL = hospital;
        this.MAJOR = major;
        this.MINOR = minor;
    }
}
