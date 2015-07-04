package maphack.qutcode.navlights;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.*;
import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;

public class MainHeatmap extends FragmentActivity implements LocationListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private ArrayList<Accident> accidents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_heatmap);
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        setupHeatMap();
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                MainHeatmap.this.updateHeatMap();
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,this);

    }
    @Override
    public void onLocationChanged(Location location) {
        //mMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mMap.animateCamera(cameraUpdate);
        //locationManager.removeUpdates(this);
    }

    private void setupHeatMap() {
        accidents = new ArrayList<>();
        DatabaseHelp DataBase = new DatabaseHelp(this);
        Cursor c = DataBase.getAccidents();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            LatLng location = new LatLng(c.getDouble(1), c.getDouble(2));
            accidents.add(new Accident(location, c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6)));
            c.moveToNext();
        }

        ArrayList<WeightedLatLng> l = new ArrayList<>();
        l.add(new WeightedLatLng(new LatLng(1, 1), 1));

        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(l)
                .radius(20)
                .opacity(1)
                .build();
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }

    private void updateHeatMap() {
        List<WeightedLatLng> list = new ArrayList<>();
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        System.out.println("bounds: " + bounds.toString());

        int c = 0;
        int i = 0;
        while (i < accidents.size() && c < 200) {
            if (bounds.contains(accidents.get(i).getLocation())) {
                list.add(accidents.get(i));
                c++;
            }
            i++;
        }

        if (list.size() == 0) {
            list.add(new WeightedLatLng(new LatLng(1, 1), 1));
        }
        mProvider.setWeightedData(list);
        mOverlay.clearTileCache();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
    }

