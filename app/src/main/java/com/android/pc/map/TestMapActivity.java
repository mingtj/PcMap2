package com.android.pc.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.NavigateArrowOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;

public class TestMapActivity extends AppCompatActivity {




    private static final int  CHANGE_CENTER = 1000;
    private static final int  NvaLine1 = 1001;
    private static final int  NvaLine2 = 1002;
    private LatLng LastDot;


     Handler mHandler = new Handler(){
         public void handleMessage(Message msg) {
             switch (msg.what) {
                 case CHANGE_CENTER:
                     changePositionCenter(ll);
                     break;
                 case NvaLine1:
                     NvaLine();
                     DrawLine(Constants.DONGHU,Constants.LANHUADAO);
                     break;
                 case NvaLine2:
                     NvaLine2();
                     list.add(Constants.NONGJIACAI);
                     DrawLine(LastDot,Constants.NONGJIACAI);
                     break;
             }
             super.handleMessage(msg);
         }
     };

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

        //markerDot(ll);

         list = new ArrayList<>();
        list.add(Constants.DONGHU);
        list.add(Constants.LANHUADAO);

        mHandler.sendEmptyMessageDelayed(NvaLine1,2000);
    }


    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
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



    private void NvaLine(){
            // 加一个箭头对象（NavigateArrow）对象在地图上
            aMap.addNavigateArrow(new NavigateArrowOptions().add(Constants.DONGHU,
                    Constants.WUDANG).width(20));
            aMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(new CameraPosition(Constants.WUDANG, 16f, 38.5f, 300)));

            markerDot(Constants.WUDANG,"武昌会馆","湖北省武汉市武昌区鲁磨路235号");

            mHandler.sendEmptyMessageDelayed(NvaLine2,5000);

    }


    List<LatLng> list;
    private void DrawLine(LatLng startDot,LatLng endDot){
        //划线
        aMap.addPolyline(new PolylineOptions()
                .addAll(list)
                .width(20f));

        //转换中心点到endDot
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(endDot,16f,38.5f,300)));//CameraPosition(LatLng target, float zoom, float tilt, float bearing)  tilt:目标可视区域的倾斜度，以角度为单位;bearing:可视区域指向的方向，以角度为单位，从正北向逆时针方向计算，从0 度到360 度。


        //add marker
        for (int i =1;i<list.size();i++){
            markerDot(list.get(i),"位置"+i,"内容"+i);
        }


        LastDot = endDot;

        /**
         *

         LatLng A = new LatLng(Lat_A, Lon_A);
         LatLng B = new LatLng(Lat_B, Lon_B);
         LatLng C = new LatLng(Lat_C, Lon_C);
         LatLng D = new LatLng(Lat_D, Lon_D);
         aMap.addPolyline((new PolylineOptions())
         .add(A, B, C, D)
         .width(10)
         .color(Color.argb(255, 1, 255, 255)));

         */
    }





    private void NvaLine2(){
        aMap.clear();

        // 加一个箭头对象（NavigateArrow）对象在地图上
        aMap.addNavigateArrow(new NavigateArrowOptions().add(Constants.DONGHU,Constants.WUDANG,
                Constants.GONGMAOXUEYUAN).width(20));
        aMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition(Constants.GONGMAOXUEYUAN, 16f, 38.5f, 300)));

        markerDot(Constants.WUDANG,"武昌会馆","湖北省武汉市武昌区鲁磨路235号");

        markerDot(Constants.GONGMAOXUEYUAN,"武汉工贸学院","湖北省武汉市武昌区大学城316号");

    }

    //标记某个点位，并转换中心点到这个位置
    private void markerDot(LatLng latLng,String title,String centent){
        //湖北省武汉市武昌区沿湖大道16号
//        LatLng latLng = new LatLng(39.906901,116.397972);
        aMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(centent));
    }

//美好长江  美好名流汇  首地云梦台  东原乐见城  地铁盛观上城
    private void changePositionCenter(LatLng latLng){
        //转换中心点，并设置放大倍数
//        changeCamera(
//                CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                        Constants.DONGHU, 18, 30, 0)));


//       //指定latlng并设置中心点
//        CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
//        changeCamera(cu);

        //设置地图中心点以及缩放级别。   latLng - 地图中心点。  zoom - 缩放级别，[3-20]，数字越大，显示越细致
        CameraUpdate cu2 = CameraUpdateFactory.newLatLngZoom(latLng,13);
        changeCamera(cu2);


        //添加marker点
        aMap.clear();
        aMap.addMarker(new MarkerOptions().position(Constants.DONGHU)
                        .title("武汉东湖风景区")//title
                        .snippet("湖北省武汉市武昌区沿湖大道16号")  //content
                // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        );
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {

        aMap.moveCamera(update);

    }


}
