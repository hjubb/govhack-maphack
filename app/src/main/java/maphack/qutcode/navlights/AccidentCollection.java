package maphack.qutcode.navlights;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.CharArrayBuffer;
import android.database.Cursor;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
    private DatabaseHelp DataBase;
    private final int RENDER_LIMIT = 350;
    private String CSV_PATH = "csv/weightedlocations.csv";
    private static final LatLng UQ = new LatLng(-27.4975628,153.0133963);
    private LatLngBounds AUSTRALIA = new LatLngBounds(
            new LatLng(-44, 113), new LatLng(-10, 154));

    public AccidentCollection(GoogleMap map, Context context) {
        mMap = map;
        setupHeatMap();
        setupData(context);
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

    private boolean underRenderLimit(int renderCount) {
        return renderCount < 350;
    }

    public void setupData(Context context) {
        accidents = new ArrayList<WeightedLatLng>();
        if (DataBase == null) {
            DataBase = new DatabaseHelp(context);
        }
        Cursor c = DataBase.getAccidents(mMap.getProjection().getVisibleRegion().latLngBounds,RENDER_LIMIT);
        int renderCount = 0;
        int count = c.getCount();

        for (int i = 0; i < count; i++) {
            LatLng location = new LatLng(c.getDouble(1), c.getDouble(2));
            Accident a = new Accident(location, c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6));
            if (Filters.toDisplay(a) && underRenderLimit(renderCount++)) {
                accidents.add(a);
            }
            c.moveToNext();
        }
        if (renderCount != 0) {
            mProvider.setWeightedData(accidents);
            mOverlay.clearTileCache();
        }
        //c.close();
        //DataBase.close();
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
