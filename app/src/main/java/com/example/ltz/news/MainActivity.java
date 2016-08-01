package com.example.ltz.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;

    private StringRequest mStringRequest;

    private Context mContext;

    private JsonObjectRequest mJsonObjectRequest;

    private SimpleAdapter simpleAdapter;

    private ListView listView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private PullToRefreshLayout pullToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        volley_get_json();

        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);

        ActionBarPullToRefresh
                .from(this)
                .options(
                        Options.create()
                                .scrollDistance(.75f)
                                .build()
                )
                .allChildrenArePullable()
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        volley_get_json();

                    }
                }).setup(pullToRefreshLayout);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void volley_get_json() {
//        http://toutiao.com/api/article/recent/?source=2&count=20&category=__all__&max_behot_time=1463660528.07&utm_source=toutiao&offset=0&_=1463660529025
//        long systime;
        Date times = new Date();
        String systime = times.getTime() + "";
        String max_behot_time = systime.substring(0, systime.length() - 3) + "." + Math.random();
        String url = "http://toutiao.com/api/article/recent/?source=2&count=20&category=__all__&max_behot_time=" + max_behot_time +
                "&utm_source=toutiao&offset=0&_=" + times.getTime();
        System.out.println("请求url" + url);
        mContext = this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mJsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    List<Map<String, Object>> newsList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Map map = new HashMap();
                        map.put("title", object.get("title"));
                        map.put("author", object.get("source"));
                        map.put("abstractBody", object.get("abstract"));
                        map.put("publishTime", "发布于" + object.optString("datetime"));
                        if (null != object.optString("image_url")) {
                            map.put("imageUrl", object.optString("image_url"));
                        }
                        newsList.add(map);
                    }
                    simpleAdapter = new SimpleAdapter(MainActivity.this, newsList, R.layout.new_item,
                            new String[]{"title", "abstractBody", "author", "imageUrl", "publishTime"},
                            new int[]{R.id.title, R.id.abstractBody, R.id.author, R.id.photograph, R.id.publishTime}) {
                        @Override
                        public void setViewImage(final ImageView v, final String value) {
                            if (v.getId() == R.id.photograph) {
                                ImageRequest imageRequest = new ImageRequest(value, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        v.setImageBitmap(response);
                                    }
                                }, 0, 0, null, null);
                                mRequestQueue.add(imageRequest);
                                pullToRefreshLayout.setRefreshComplete();
                            }
                        }
                    };
                    listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(simpleAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求失败" + error);
            }
        });
        mRequestQueue.add(mJsonObjectRequest);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.ltz.news/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.ltz.news/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
