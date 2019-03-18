package com.android.pc.map;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements LocationSource,AMapLocationListener {

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
            setUpMap();
        }
    }



    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        useMoveToLocationWithMapMode = true;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();

        useMoveToLocationWithMapMode = false;
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
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是指定位间隔
            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     *  locationSource  listener  停止定位
     */
    @Override
    public void deactivate() {
        mLocationChangeListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mLocationChangeListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                System.out.println("-------------定位成功----------------");
//                LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
//                //展示自定义定位小蓝点
//                if(locationMarker == null) {
//                    //首次定位
//                    locationMarker = aMap.addMarker(new MarkerOptions().position(latLng)
//                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
//                            .anchor(0.5f, 0.5f));
//
//                    //首次定位,选择移动到地图中心点并修改级别到15级
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                } else {
//
//                    if(useMoveToLocationWithMapMode) {
//                        //二次以后定位，使用sdk中没有的模式，让地图和小蓝点一起移动到中心点（类似导航锁车时的效果）
//                        startMoveLocationAndMap(latLng);
//                    } else {
//                        startChangeLocation(latLng);
//                    }
//
//                }


                //=======================================
                LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                if(locationMarker == null) {
                    //首次定位
                    locationMarker = aMap.addMarker(new MarkerOptions().position(latLng)
//                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                            .anchor(0.5f, 0.5f));
                }

                //首次定位,选择移动到地图中心点并修改级别到15级
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                //=======================================

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }


//    /**
//     * 同时修改自定义定位小蓝点和地图的位置
//     * @param latLng
//     */
//    private void startMoveLocationAndMap(LatLng latLng) {
//
//        //将小蓝点提取到屏幕上
//        if(projection == null) {
//            projection = aMap.getProjection();
//        }
//        if(locationMarker != null && projection != null) {
//            LatLng markerLocation = locationMarker.getPosition();
//            Point screenPosition = aMap.getProjection().toScreenLocation(markerLocation);
//            locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
//
//        }
//
//        //移动地图，移动结束后，将小蓝点放到放到地图上
//        myCancelCallback.setTargetLatlng(latLng);
//        //动画移动的时间，最好不要比定位间隔长，如果定位间隔2000ms 动画移动时间最好小于2000ms，可以使用1000ms
//        //如果超过了，需要在myCancelCallback中进行处理被打断的情况
//        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng),1000,myCancelCallback);
//
//    }
}
