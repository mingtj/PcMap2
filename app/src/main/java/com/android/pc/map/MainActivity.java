package com.android.pc.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.android.pc.map.bean.Gps;
import com.android.pc.map.utils.ConvertUtil;
import com.android.pc.map.utils.PositionUtil;


/**
 * http://2y3334n648.iok.la/
 * 1.GPS 接收原始经纬数据解析处理
 * 2.原始经纬数据转换成百度或者高德地图坐标
 * 3.百度或者高德上描点处理
 * 4.
 */
public class MainActivity extends AppCompatActivity {


    //精度
    private EditText mLng;
    //纬度
    private EditText mLat;

    //转换结果
    private TextView mWsgToGcjResult,mWsgToBdResult;

    //转换结果的准确性
    private TextView mWsgToGcjCheck,mWsgToBdCheck;

    private Button mBtnTransfrom,mBtnToMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        testData();
    }

    private void initView(){
        mLng = findViewById(R.id.lng);
        mLat = findViewById(R.id.lat);
        mWsgToGcjResult = findViewById(R.id.gcj02_result);
        mWsgToBdResult = findViewById(R.id.bd09_result);
        mWsgToGcjCheck = findViewById(R.id.gcj02_result_true);
        mWsgToBdCheck = findViewById(R.id.bd09_result_true);
        mBtnTransfrom = findViewById(R.id.btn_transform);
        mBtnToMap = findViewById(R.id.btn_to_map);
    }

    public void transformEvent(View view){
        testData();
    }

    void testData(){

        double lng = ConvertUtil.convertToDouble(mLng.getText().toString(),0.0);
        double lat = ConvertUtil.convertToDouble(mLat.getText().toString(),0.0);
        System.out.println("----lng:"+lng);
        System.out.println("----lat:"+lat);


        Gps gps = new Gps(lat,lng);
        Gps gcj = PositionUtil.gps84_To_Gcj02(gps.getWgLat(), gps.getWgLon());
        System.out.println("gcj  :" + gcj);

        if(gcj==null){
            System.out.println("----gcj---null--");
        }else{
            System.out.println("--gcj--lng:"+gcj.getWgLon());
            System.out.println("--gcj--lat:"+gcj.getWgLat());
            System.out.println("==============================================================");
            mWsgToGcjResult.setText("精度："+gcj.getWgLon()+"_纬度："+gcj.getWgLat());

            System.out.println("==============================================================");
            LatLng ll = new LatLng(lat,lng);
            OtherCoorConverterGd(ll);
            System.out.println("==============================================================");

            Gps bd = PositionUtil.gcj02_To_Bd09(gcj.getWgLat(), gcj.getWgLon());
            if(bd==null){
                System.out.println("----bd---null--");
            }else{
                System.out.println("--bd--lng:"+bd.getWgLon());
                System.out.println("--bd--lat:"+bd.getWgLat());
                mWsgToBdResult.setText("精度："+bd.getWgLon()+"_纬度："+bd.getWgLat());
            }
        }


//        // 北斗芯片获取的经纬度为WGS84地理坐标 28.0948150010,112.9986987001
//        Gps gps = new Gps(28.0948150010,112.9986987001);
//        System.out.println("gps :" + gps);
//        Gps gcj = gps84_To_Gcj02(gps.getWgLat(), gps.getWgLon());
//        System.out.println("gcj :" + gcj);
//        Gps star = gcj_To_Gps84(gcj.getWgLat(), gcj.getWgLon());
//        System.out.println("star:" + star);
//        Gps bd = gcj02_To_Bd09(gcj.getWgLat(), gcj.getWgLon());
//        System.out.println("bd  :" + bd);
//        Gps gcj2 = bd09_To_Gcj02(bd.getWgLat(), bd.getWgLon());
//        System.out.println("gcj :" + gcj2);
    }



   public void toMap(View view){
        Intent i = new Intent(this,TestMapActivity.class);
        startActivity(i);
    }


        /**
         * 其他坐标系转到高德坐标系
         * @param sourceLatLng:待转换坐标点 LatLng类型
         */
        private void OtherCoorConverterGd(LatLng sourceLatLng){
            CoordinateConverter converter  = new CoordinateConverter(this);
            // CoordType.GPS 待转换坐标类型
            converter.from(CoordinateConverter.CoordType.GPS);
            // sourceLatLng待转换坐标点 LatLng类型
            converter.coord(sourceLatLng);
            // 执行转换操作
            LatLng desLatLng = converter.convert();
            if(desLatLng==null){
                System.out.println("---gaode---coorConverter---nul-----");
            }else{
                System.out.println("---gaode---coorConverter---lng:"+desLatLng.longitude);
                System.out.println("---gaode---coorConverter---lat:"+desLatLng.latitude);
            }
    }

}

