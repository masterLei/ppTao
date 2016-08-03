package com.example.ltz.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
//import com.google.android.gms.common.api.GoogleApiClient;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RequestQueue mRequestQueue;

    private Context mContext;

    private JsonObjectRequest mJsonObjectRequest;

    private SimpleAdapter simpleAdapter;

    private ListView listView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    private PullToRefreshLayout pullToRefreshLayout;

    private ViewPager viewPager;
    private PagerAdapter mPagerAdapter;
    private List<View> mViews = new ArrayList<View>();// 用来存放Tab01-04

    // 三个Tab，每个Tab包含一个按钮
    private LinearLayout marketTab;
    private LinearLayout selfTab;
    private LinearLayout pictureUploadTab;

    private ImageButton marketImg;
    private ImageButton selfImg;
    private ImageButton pictureUploadImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initViewPage();
        initEvent();
    }

    private void initEvent() {
        marketTab.setOnClickListener(this);
        selfTab.setOnClickListener(this);
        pictureUploadTab.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem) {
                    case 0:
//                        resetImg();
//                        mWeiXinImg.setImageResource(R.drawable.tab_weixin_pressed);
                        break;
                    case 1:
//                        resetImg();
//                        mAddressImg.setImageResource(R.drawable.tab_address_pressed);
                        break;
                    case 2:
//                        resetImg();
//                        mFrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
                        break;
                    case 3:
//                        resetImg();
//                        mSettingImg.setImageResource(R.drawable.tab_settings_pressed);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.id_view_page);
        // 初始化四个LinearLayout
        marketTab = (LinearLayout) findViewById(R.id.id_tab_market);
        pictureUploadTab = (LinearLayout) findViewById(R.id.id_tab_pic_upload);
        selfTab = (LinearLayout) findViewById(R.id.id_tab_self);
        // 初始化四个按钮
        marketImg = (ImageButton) findViewById(R.id.id_img_market);
        pictureUploadImg = (ImageButton) findViewById(R.id.id_img_pic_upload);
        selfImg = (ImageButton) findViewById(R.id.id_img_self);
    }


    /**
     * 初始化ViewPage
     */
    private void initViewPage() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);

        View marketView = mLayoutInflater.inflate(R.layout.market_view, null);
        View picView = mLayoutInflater.inflate(R.layout.picture_upload_view, null);
        View selfView = mLayoutInflater.inflate(R.layout.self_view, null);

        mViews.add(marketView);
        mViews.add(picView);
        mViews.add(selfView);

        // 适配器初始化并设置  
        mPagerAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));

            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return mViews.size();
            }
        };
        viewPager.setAdapter(mPagerAdapter);
    }

        //加载图片
//    private void volley_get_json() {
//        Date times = new Date();
//        String systime = times.getTime() + "";
//        String max_behot_time = systime.substring(0, systime.length() - 3) + "." + Math.random();
//        String url = "http://toutiao.com/api/article/recent/?source=2&count=20&category=__all__&max_behot_time=" + max_behot_time +
//                "&utm_source=toutiao&offset=0&_=" + times.getTime();
//        System.out.println("请求url" + url);
//        mContext = this;
//        mRequestQueue = Volley.newRequestQueue(mContext);
//        mJsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    List<Map<String, Object>> newsList = new ArrayList<>();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        Map map = new HashMap();
//                        map.put("title", object.get("title"));
//                        map.put("author", object.get("source"));
//                        map.put("abstractBody", object.get("abstract"));
//                        map.put("publishTime", "发布于" + object.optString("datetime"));
//                        if (null != object.optString("image_url")) {
//                            map.put("imageUrl", object.optString("image_url"));
//                        }
//                        newsList.add(map);
//                    }
//                    simpleAdapter = new SimpleAdapter(MainActivity.this, newsList, R.layout.new_item,
//                            new String[]{"title", "abstractBody", "author", "imageUrl", "publishTime"},
//                            new int[]{R.id.title, R.id.abstractBody, R.id.author, R.id.photograph, R.id.publishTime}) {
//                        @Override
//                        public void setViewImage(final ImageView v, final String value) {
//                            if (v.getId() == R.id.photograph) {
//                                ImageRequest imageRequest = new ImageRequest(value, new Response.Listener<Bitmap>() {
//                                    @Override
//                                    public void onResponse(Bitmap response) {
//                                        v.setImageBitmap(response);
//                                    }
//                                }, 0, 0, null, null);
//                                mRequestQueue.add(imageRequest);
//                                pullToRefreshLayout.setRefreshComplete();
//                            }
//                        }
//                    };
////                    listView = (ListView) findViewById(R.id.listView);
//                    listView.setAdapter(simpleAdapter);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("请求失败" + error);
//            }
//        });
//        mRequestQueue.add(mJsonObjectRequest);
//    }

    //下拉刷新
//    private void pull_to_refresh(){
//
////        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
//
//        ActionBarPullToRefresh
//                .from(this)
//                .options(
//                        Options.create()
//                                .scrollDistance(.75f)
//                                .build()
//                )
//                .allChildrenArePullable()
//                .listener(new OnRefreshListener() {
//                    @Override
//                    public void onRefreshStarted(View view) {
//                        volley_get_json();
//
//                    }
//                }).setup(pullToRefreshLayout);
//
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tab_market:
                viewPager.setCurrentItem(0);
//                resetImg();
//                marketImg.setImageResource(R.drawable.tab_weixin_pressed);
                break;
            case R.id.id_tab_pic_upload:
                viewPager.setCurrentItem(1);
//                resetImg();
//                mAddressImg.setImageResource(R.drawable.tab_address_pressed);
                break;
            case R.id.id_tab_self:
                viewPager.setCurrentItem(2);
//                resetImg();
//                mFrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
                break;
            default:
                break;
        }
    }
}
