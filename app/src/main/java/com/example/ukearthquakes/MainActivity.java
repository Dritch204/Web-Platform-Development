package com.example.ukearthquakes;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

// Drew Ritchie S1710460
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AsyncHttpClient client = new AsyncHttpClient();
    public static ArrayList<EarthQuakeModels> earthQuakeModels = new ArrayList<>();
    private static final String BASE_URL = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    EarthQuakeModels earthQuakeObj;
    private String text;
    RecyclerView general_feeds_view_frg_list;
    ProgressBar general_feeds_view_frg_progressbar;
    GeneralFeedsAdapter generalFeedsAdapter;

    private GoogleMap mMap;

    // Drew Ritchie S1710460
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //clearing the ArrayList
        if (earthQuakeModels.size() > 0)
            earthQuakeModels.clear();


        //Initializing the Views
        general_feeds_view_frg_progressbar = findViewById(R.id.general_feeds_view_frg_progressbar);
        general_feeds_view_frg_list = findViewById(R.id.general_feeds_view_frg_list);

        general_feeds_view_frg_list = findViewById(R.id.general_feeds_view_frg_list);
        general_feeds_view_frg_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        general_feeds_view_frg_list.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(general_feeds_view_frg_list.getContext(),
                linearLayoutManager.getOrientation());
        general_feeds_view_frg_list.addItemDecoration(dividerItemDecoration);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map2);
        mapFragment.getMapAsync(this);

        //Creating HTTP client
        client.get(BASE_URL, new AsyncHttpResponseHandler() {
            //It will be called on the Successful HTTPs Response

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String xmlRes = new String(responseBody);
                Log.d("MainActXMLResponse", xmlRes);
                XmlPullParserFactory factory = null;
                try {
                    //Initializing the XmlPullParser Factory
                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new StringReader(xmlRes));
                    int eventType = xpp.getEventType();

                    //Checking the Tag and reading the elements
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagname = xpp.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (tagname.equalsIgnoreCase("item")) {
                                    // create a new instance of employee
                                    //employee = new Employee();
                                    earthQuakeObj = new EarthQuakeModels();
                                }
                                break;

                            case XmlPullParser.TEXT:
                                text = xpp.getText();
                                break;

                            case XmlPullParser.END_TAG:
                                if (earthQuakeObj != null) {
                                    if (tagname.equalsIgnoreCase("item")) {
                                        // add employee object to list
                                        earthQuakeModels.add(earthQuakeObj);
                                    } else if (tagname.equalsIgnoreCase("title")) {
                                        earthQuakeObj.setTitle(text);
                                    } else if (tagname.equalsIgnoreCase("description")) {
                                        earthQuakeObj.setDescription(text);
                                    } else if (tagname.equalsIgnoreCase("link")) {
                                        earthQuakeObj.setLink(text);
                                    }else if (tagname.equalsIgnoreCase("pubDate")) {
                                        earthQuakeObj.setPubDate(text);
                                    } else if (tagname.equalsIgnoreCase("lat")) {
                                        earthQuakeObj.setEarthQuakeLat(text);
                                    } else if (tagname.equalsIgnoreCase("long")) {
                                        earthQuakeObj.setEarthQuakeLong(text);
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                        eventType = xpp.next();
                    }
                    System.out.println("End document");
                    general_feeds_view_frg_list.setVisibility(View.VISIBLE);
                    generalFeedsAdapter = new GeneralFeedsAdapter(MainActivity.this,MainActivity.this);
                    general_feeds_view_frg_list.setAdapter(generalFeedsAdapter);
                    setUpMap();
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                    general_feeds_view_frg_progressbar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            //It will be called on the Failed HTTPs
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("MainActXMLResponse", error.getMessage());
                general_feeds_view_frg_progressbar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Error: " + error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    //Drew Ritchie S1710460
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //Drew Ritchie S1710460
    //get Magnitude String
    private int getEarthQuakeMagnitude(String toExtractLocationName){
        String strComma[] = toExtractLocationName.split(",");
        String strSecond[] = strComma[0].split(":");

        String strThird[] = strSecond[1].split(" ");
        float checkQuake = Float.parseFloat(strThird[3]);
        if(checkQuake<=0.9){
            return R.drawable.location_green;
        }else if(checkQuake>=1.0 && checkQuake<=1.9){
            return R.drawable.location_yellow;
        }if(checkQuake>=2.0){
            return R.drawable.location_red;
        }

        return Color.BLACK;
    }

    //Drew Ritchie S1710460
    private void setUpMap(){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(true);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < earthQuakeModels.size(); i++) {

            try {

                Marker marker = mMap.addMarker(new MarkerOptions().position(
                        new LatLng(
                                Double.parseDouble(earthQuakeModels.get(i).getEarthQuakeLat()),
                                Double.parseDouble(earthQuakeModels.get(i).getEarthQuakeLong())
                        ))
                        .icon(bitmapDescriptorFromVector(this,getEarthQuakeMagnitude(earthQuakeModels.get(i).getTitle()))));

                builder.include(marker.getPosition());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        LatLngBounds bounds = builder.build();


        int widthNew = getResources().getDisplayMetrics().widthPixels;
        int heightNew = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (widthNew * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);//55.3781° N, 3.4360


        mMap.setMaxZoomPreference(17.0f);
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f));
        mMap.animateCamera(cu);


        //Disable the Navigation of Google Maps
        mMap.getUiSettings().setMapToolbarEnabled(false);
        general_feeds_view_frg_progressbar.setVisibility(View.GONE);

    }

    //Drew Ritchie S1710460
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}