package com.android.pc.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.android.pc.map.utils.LocationUtil;

public class LocationActivity extends AppCompatActivity implements LocationSource {

    private MapView mapView;

    /**
     AMap 类是地图的控制器类，用来操作地图。它所承载的工作包括：地图图层切换（如卫星图、黑夜地图）、改变地图状态（地图旋转角度、俯仰角、中心点坐标和缩放级别）、添加点标记（Marker）、绘制几何图形(Polyline、Polygon、Circle)、各类事件监听(点击、手势等)等，AMap 是地图 SDK 最重要的核心类，诸多操作都依赖它完成。
     在 MapView 对象初始化完毕之后，构造 AMap 对象
     **/
    private AMap aMap;
    //位置更改监听
    private OnLocationChangedListener mLocationChangeListener;

    private AMapLocationClient mlocationClient;

    //配置定位参数，比如精准度，定位时间间隔
    private AMapLocationClientOption mLocationOption;

    private LocationUtil locationUtil;

    //自定义定位小蓝点的Marker
    Marker locationMarker;

    boolean useMoveToLocationWithMapMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
    }


    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setUpMap();
    }



    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        setLocationCallBack();

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }


    private void setLocationCallBack(){
        locationUtil = new LocationUtil();
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void callBack(String str,double lat,double lgt,AMapLocation aMapLocation) {

                //根据获取的经纬度，将地图移动到定位位置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat,lgt)));
                mLocationChangeListener.onLocationChanged(aMapLocation);
                //添加定位图标
                aMap.addMarker(locationUtil.getMarkerOption(str,lat,lgt));
            }
        });
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    /**
     *  locationSource  listener  激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mLocationChangeListener = listener;
        locationUtil.startLocate(getApplicationContext());

    }

    /**
     *  locationSource  listener  停止定位
     */
    @Override
    public void deactivate() {
        mLocationChangeListener = null;

    }

}
