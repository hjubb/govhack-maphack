package maphack.qutcode.navlights;

import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;

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
        while(!c.isAfterLast() && underRenderLimit(renderCount++)) {
            LatLng location = new LatLng(c.getDouble(1), c.getDouble(2));
            accidents.add(new Accident(location, c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6)));
            c.moveToNext();
        }
        if (renderCount == 0)
        {
            return;
        }
        mProvider.setWeightedData(accidents);
        mOverlay.clearTileCache();
        //c.close();
        //DataBase.close();
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
    }
}
