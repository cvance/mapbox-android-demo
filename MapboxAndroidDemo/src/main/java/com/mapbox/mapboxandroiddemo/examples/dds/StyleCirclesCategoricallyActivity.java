package com.mapbox.mapboxandroiddemo.examples.dds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;

/**
 * Use data-driven styling to set circles' colors based on imported vector data.
 */
public class StyleCirclesCategoricallyActivity extends AppCompatActivity {

  private MapView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_dds_style_circles_categorically);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                Style.Builder builder = new Style.Builder()
                        .fromUrl("https://cdn.airmap.com/static/map-styles/stage/0.10.0-beta1/standard.json");
                mapboxMap.setStyle(builder, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        Map<String, String> sourceMap = new HashMap<>();
                        sourceMap.put("usa_ama", "ama_field");
                        sourceMap.put("usa_fish_wildlife_refuge", "park");
                        sourceMap.put("usa_national_marine_sanctuary", "park");
                        sourceMap.put("usa_national_park", "park");
                        sourceMap.put("usa_sec_91", "emergency,fire,special_use_airspace,tfr,wildfire");
                        sourceMap.put("usa_sec_336", "airport,controlled_airspace");
                        sourceMap.put("usa_wilderness_area", "park");
                        sourceMap.put("usa_airmap_rules", "airport,hospital,power_plant,prison,school");

                        for (String source : sourceMap.keySet()) {
                            String layers = sourceMap.get(source);

                            String urlTemplates = "https://stage.api.airmap.com/tiledata/v1/" + source + "/" + layers + "/{z}/{x}/{y}";
                            TileSet tileSet = new TileSet("2.2.0", urlTemplates);
                            tileSet.setMaxZoom(12f);
                            tileSet.setMinZoom(8f);
                            VectorSource tileSource = new VectorSource(source, tileSet);
                            style.addSource(tileSource);

                            String[] layersArray = layers.split(",");
                            for (String layer : layersArray) {
                                FillLayer fillLayer = new FillLayer("airmap|" + source + "|" +  layer, source)
                                        .withProperties(
                                                fillOpacity(0.2f),
                                                fillColor(rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)))
                                        );
                                fillLayer.setSourceLayer(source + "_" + layer);
                                style.addLayer(fillLayer);
                            }
                        }
          }
        });
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}
