package com.android.pc.map;

import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.Polyline;

public class TestMapActivity extends AppCompatActivity implements LocationListener {


    private MapView mMapView = null;

    /**
    AMap 类是地图的控制器类，用来操作地图。它所承载的工作包括：地图图层切换（如卫星图、黑夜地图）、改变地图状态（地图旋转角度、俯仰角、中心点坐标和缩放级别）、添加点标记（Marker）、绘制几何图形(Polyline、Polygon、Circle)、各类事件监听(点击、手势等)等，AMap 是地图 SDK 最重要的核心类，诸多操作都依赖它完成。
    在 MapView 对象初始化完毕之后，构造 AMap 对象
     **/
    private AMap aMap;

    private MyLocationStyle myLocationStyle;

    private LatLng ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);

        //114.40921251376498,30.556236448195556
        ll = new LatLng(30.556236448195556,114.40921251376498);
//            aMap.setTrafficEnabled(true);// 显示实时交通状况
//            //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
//            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式


        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);//此方法必须重写


        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }


        markerDot(ll);
    }


    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();

//        if(null != mlocationClient){
//            mlocationClient.onDestroy();
//        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }



    //实现定位蓝点
    private void dingWeiDot(){

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。


//        aMap.setLocationSource(this);
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

    }

    @Override
    public void onLocationChanged(Location location) {
//        if (mListener != null&&amapLocation != null) {
//            if (amapLocation != null
//                    &&amapLocation.getErrorCode() == 0) {
//                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//            } else {
//                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
//                Log.e("AmapErr",errText);
//            }
//        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void markerDot(LatLng latLng){
        //湖北省武汉市武昌区沿湖大道16号
//        LatLng latLng = new LatLng(39.906901,116.397972);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("湖北省武汉市武昌区沿湖大道16号").snippet("DefaultMarker"));
        marker.isVisible();
    }


}
