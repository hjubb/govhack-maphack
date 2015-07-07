package maphack.qutcode.navlights;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import maphack.qutcode.navlights.filters.Filters;

/**
 * Created by kane on 4/07/2015.
 */
public class AccidentCollection {
    private ArrayList<WeightedLatLng> accidents;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private GoogleMap mMap;
    private Polyline boundsLine;
    private DatabaseHelp DataBase;
    private int renderLimit = 10;
    private String CSV_PATH = "csv/weightedlocations.csv";
    private static final LatLng UQ = new LatLng(-27.45865128326186,153.04429460316896);
    private LatLngBounds AUSTRALIA = new LatLngBounds(
            //new LatLng(-44, 113), new LatLng(-10, 154));
            new LatLng(-27.5364603754385,152.98249684274197), new LatLng(-27.492583,153.018866));
    private LatLngBounds bounds = AUSTRALIA;

    public AccidentCollection(GoogleMap map, Context context) {
        mMap = map;
        setupHeatMap();
        DataBase = new DatabaseHelp(context);
        setupData();
    }

//    public void updateHeatMap() {
//        List<WeightedLatLng> list = new ArrayList<>();
//        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
//        int renderCount = 0;
//        if (list.size() != 0) {
//            mProvider.setWeightedData(list);
//            mOverlay.clearTileCache();
//        }
//    }

    public void setupData() {
        accidents = new ArrayList<WeightedLatLng>();
        Cursor c = DataBase.getAccidents(bounds,renderLimit);

        int count = c.getCount();
        for (int i = 0; i < count; i++) {
            LatLng location = new LatLng(c.getDouble(1), c.getDouble(2));
            Accident a = new Accident(location, c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6), c.getString(7), c.getInt(8));
            accidents.add(a);
            c.moveToNext();
        }
        if (accidents.size() > 0) {
            updateData();
        }
        //c.close();
        //DataBase.close();
    }

    public void updateData() {
        ArrayList<WeightedLatLng> ac = new ArrayList<>();
        for (WeightedLatLng a : accidents) {
            if (Filters.toDisplay((Accident) a)) {
                ac.add(a);
            }
        }
        if (ac.size() > 0) {
            mProvider.setWeightedData(ac);
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    public void updateBounds() {
        bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        increaseBounds();
        redrawBoundLines();
        setupData();
        System.gc();
    }

    public void updateRenderLimit(int lim) {
        if (lim == -1) {
            renderLimit = 10;
        } else {
            renderLimit = lim;
        }
        updateBounds();
    }

    public void updateIfNeeded() {
        LatLngBounds cameraBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        //double d = Math.abs(Math.abs(cameraBounds.northeast.longitude) - Math.abs(cameraBounds.southwest.longitude));
        //!bounds.contains(cameraBounds.northeast) || !bounds.contains(cameraBounds.southwest)
        if (!bounds.contains(cameraBounds.getCenter())) {
//            double latdiff = (cameraBounds.northeast.latitude - cameraBounds.southwest.latitude);
//            double lngdiff = (cameraBounds.northeast.longitude - cameraBounds.southwest.longitude);
//            LatLng newNE = new LatLng(cameraBounds.getCenter().latitude + latdiff, cameraBounds.getCenter().longitude + lngdiff);
//            LatLng newSW = new LatLng(cameraBounds.getCenter().latitude - latdiff, cameraBounds.getCenter().longitude - lngdiff);
//            bounds = new LatLngBounds(newSW, newNE);

            updateBounds();

            setupData();
        }
    }

    private void redrawBoundLines() {
        mMap.clear();
        //TODO re-attatch trip poly
        boundsLine = mMap.addPolyline(new PolylineOptions()
                .width(2)
                .color(Color.BLACK)
                .geodesic(true)
                .visible(true)
                .zIndex(0));

        ArrayList<LatLng> boundPoints = new ArrayList<>();
        boundPoints.add(bounds.northeast);
        boundPoints.add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude));
        boundPoints.add(bounds.southwest);
        boundPoints.add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude));
        boundPoints.add(bounds.northeast);

        boundsLine.setPoints(boundPoints);
    }

    private void increaseBounds() {
        double increasePercentage = 1.5;
        double latadj = ((bounds.northeast.latitude - bounds.southwest.latitude) * (increasePercentage - 1));
        //double lngadj = ((bounds.northeast.longitude - bounds.southwest.longitude) * (increasePercentage - 1));
        LatLng newNE = new LatLng(bounds.northeast.latitude + latadj, bounds.northeast.longitude + latadj);
        LatLng newSW = new LatLng(bounds.southwest.latitude - latadj, bounds.southwest.longitude - latadj);
        bounds = new LatLngBounds(newSW, newNE);
    }

//    private void setupData(Context context) throws Exception {
//        accidents = new ArrayList<>();
//        List<String[]> rows = readCsv(context);
//
//        for (String[] r : rows) {
//            LatLng loc = new LatLng(parseDouble(r[0]), parseDouble(r[1]));
//            accidents.add(new Accident(loc, parseDouble(r[2])));
//        }
//    }

    public final List<String[]> readCsv(Context context) {
        List<String[]> accidentsList = new ArrayList<String[]>();
        AssetManager assetManager = context.getAssets();

        try {
            InputStream csvStream = assetManager.open(CSV_PATH);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);
            String[] line;

            // throw away the header
            // I didn't include a header
//            csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                accidentsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accidentsList;
    }

    private void setupHeatMap() {
        ArrayList<WeightedLatLng> temp = new ArrayList<>();
        temp.add(new WeightedLatLng(new LatLng(0, 0), 0));
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(temp)
                .radius(20)
                .opacity(0.8)
                .build();
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(UQ)
                .zoom(13)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
