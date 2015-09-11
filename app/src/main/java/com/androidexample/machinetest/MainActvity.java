package com.androidexample.machinetest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.swipe.R;
import com.androidexample.machinetest.SimpleGestureFilter.SimpleGestureListener;
import com.androidexample.machinetest.adapter.ListAdapter;
import com.androidexample.machinetest.adapter.ListGetSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActvity extends Activity implements SimpleGestureListener,Animation.AnimationListener{

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private ProgressDialog pgLogin;
    private SimpleGestureFilter detector;
    private ProgressDialog pDialog;
    String URL="http://erp.technomobssolutions.com/mena_app/mobilemanager/getRows";
    String reply;

    LinearLayout layout_left;
    RelativeLayout layout_top_parent;
    LinearLayout layout_right;
    LinearLayout layout_down_parent;

    ArrayList<ListGetSet> detailList;
    ListAdapter adapter;
    ListView listview;
    Animation slideRight;
    Animation slideDown;

    ImageView imgHome,imgPage,imgPeople,arrowHome,arrowPage,arrowPeople;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        arrowHome = (ImageView) findViewById(R.id.arrowHome);
        arrowPage = (ImageView) findViewById(R.id.arrowPage);
        arrowPeople = (ImageView) findViewById(R.id.arrowPeople);

        layout_left = (LinearLayout) findViewById(R.id.layout_left);
        layout_top_parent = (RelativeLayout) findViewById(R.id.layout_top_parent);
        layout_right = (LinearLayout) findViewById(R.id.layout_right);
        layout_down_parent = (LinearLayout) findViewById(R.id.layout_down_parent);

        slideRight = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_right);
        slideRight.setAnimationListener(MainActvity.this);

        slideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        slideDown.setAnimationListener(MainActvity.this);

        detailList = new ArrayList<ListGetSet>();
        detector = new SimpleGestureFilter(this,this);

        LinearLayout.LayoutParams paramsLayout = (LinearLayout.LayoutParams) layout_right.getLayoutParams();
        paramsLayout.weight = 1.0f;
        layout_right.setLayoutParams(paramsLayout);

        new MyAsyncTask().execute();  // call async task
        listview = (ListView) findViewById(R.id.list);
        adapter = new ListAdapter(getApplicationContext(), R.layout.list_row, detailList);

        //--------------------------------------------
        //Progress bar
        //--------------------------------------------

        pgLogin = new ProgressDialog(MainActvity.this);
        pgLogin.setMessage("Just a moment please...");
        pgLogin.setIndeterminate(true);
        pgLogin.setCancelable(false);
        pgLogin.setCanceledOnTouchOutside(false);

        pgLogin.show();

    }

    public void imageHome(View v)
    {
        arrowHome.setVisibility(View.VISIBLE);
        arrowPeople.setVisibility(View.GONE);
        arrowPage.setVisibility(View.GONE);
    }

    public void imagePage(View v)
    {
        arrowHome.setVisibility(View.GONE);
        arrowPeople.setVisibility(View.GONE);
        arrowPage.setVisibility(View.VISIBLE);
    }

    public void imagePeople(View v)
    {
        arrowHome.setVisibility(View.GONE);
        arrowPeople.setVisibility(View.VISIBLE);
        arrowPage.setVisibility(View.GONE);
    }
//----------------------------------------------------------------------
//Start Swipe Listener
//----------------------------------------------------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }


    @Override
    public void onSwipe(int direction) {

        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT :

                if(layout_top_parent.getVisibility() == View.VISIBLE)
                {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_down_parent.getLayoutParams();
                    layout_down_parent.setLayoutParams(params);

                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) layout_right.getLayoutParams();
                    params1.weight = 0.8f;
                    layout_right.setLayoutParams(params1);
                }
                else
                {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_down_parent.getLayoutParams();
                    params.weight = 1.0f;
                    layout_down_parent.setLayoutParams(params);
                }
                if(layout_left.getVisibility() == View.GONE) {
                    layout_left.setVisibility(View.VISIBLE);

                    layout_left.startAnimation(slideRight);
                }


                break;

            case SimpleGestureFilter.SWIPE_LEFT :
                layout_left.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_right.getLayoutParams();
                params.weight = 1.0f;
                layout_right.setLayoutParams(params);
                break;

            case SimpleGestureFilter.SWIPE_DOWN :
                if(layout_top_parent.getVisibility() == View.GONE) {
                    layout_top_parent.setVisibility(View.VISIBLE);
                    layout_top_parent.startAnimation(slideDown);
                }
                break;

            case SimpleGestureFilter.SWIPE_UP :

                if(layout_left.getVisibility() == View.VISIBLE)
                {
                    layout_left.setVisibility(View.VISIBLE);
                }
                else
                {
                    layout_left.setVisibility(View.GONE);
                }
                layout_top_parent.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDoubleTap() {

    }
//----------------------------------------------------------------------
// End Swipe Listener
//----------------------------------------------------------------------

 //----------------------------------------------------------------------
// Start Web parsing
//----------------------------------------------------------------------
    private class MyAsyncTask extends  AsyncTask<String, Void, Void>{


        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            //String request = String.format(params[0], "12");
            reply = getResponseByParsing(URL);
            //getResponseByParsing(URL);
            if (reply != null) {
                try {
                    JSONObject jsonObj = new JSONObject(reply);
                    JSONObject response = jsonObj.getJSONObject("response");
                    JSONArray data = response.getJSONArray("data");
                    Log.e("tag ", "length "+ data.length());

                    for (int i = 0; i < data.length(); i++)
                    {
                        JSONObject c = data.getJSONObject(i);
                        String id = c.getString("id");
                        Log.e("Tag ", "id "+ id);
                        String title = c.getString("property");
                        Log.e("Tag ", "property "+ title);
                        String property_title = c.getString("property_title");
                        Log.e("Tag ", "property_title "+ property_title);

                        ListGetSet listValue = new ListGetSet();
                        listValue.setId(id);
                        listValue.setTitlel(title);

                        detailList.add(listValue);
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            if (pgLogin.isShowing()) {
                pgLogin.cancel();
                pgLogin.dismiss();
            }
            Log.e("tag ","set adapter");
            listview.setAdapter(adapter);

        }
    }

    public  String getResponseByParsing(String URL)
    {
        String response_string = null;
        // Build the JSON object to pass parameters
        try
        {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("param", "detailedhometest");
            jsonObj.put("community", "Dubailand");
            jsonObj.put("propertyid", "forsale");
            jsonObj.put("start", "1");
            jsonObj.put("noofrows", "5");


            // Create the POST object and add the parameters
            HttpPost httpPost = new HttpPost(URL);
            StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (response != null)
            {

                response_string = EntityUtils.toString(resEntity);
                Log.e("tag ", "response " + response_string);
            }
        }
        catch(Exception e)
        {
            Log.e("tag", "Exception " + e);
        }

        return response_string;
    }

//----------------------------------------------------------------------
// End Web parsing
//----------------------------------------------------------------------

}

