package maphack.qutcode.navlights;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;

public class MainHeatmap extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private static Context mContext;
    private DatabaseHelp dbHelp;
    private Cursor accidents;
    private ArrayList<Accident> accidentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_heatmap);
        setUpMapIfNeeded();
        mContext = getApplicationContext();
        dbHelp = new DatabaseHelp(this);
        Cursor c = dbHelp.getAccidents();
        accidentList = new ArrayList<Accident>();
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            if (c.getDouble(1) > 0) {
                Accident addAcc = new Accident(c.getInt(0), c.getDouble(1), c.getDouble(2),
                        c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6));
                accidentList.add(addAcc);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dbHelp.close();
        accidents.close();
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
        addHeatMap();
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void addHeatMap() {
        List<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
        list.add(new WeightedLatLng(new LatLng(1, -1), 1));
        list.add(new WeightedLatLng(new LatLng(2, -1), 2));
        list.add(new WeightedLatLng(new LatLng(3, -2), 1));
        list.add(new WeightedLatLng(new LatLng(4, -3), 3));
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(list)
                .build();
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }
}
