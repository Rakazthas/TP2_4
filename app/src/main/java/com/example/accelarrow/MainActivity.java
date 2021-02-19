package com.example.accelarrow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager snsManager;
    private Sensor pSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean pSensorDisp = false;

        snsManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pSensor = snsManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        if (pSensor != null){
            pSensorDisp = snsManager.registerListener((SensorEventListener) this, pSensor, SensorManager.SENSOR_DELAY_GAME);
        }else{
            Toast.makeText(getApplicationContext(),"Pas de capteur de proximite detecte, fonctionalite desactivee", Toast.LENGTH_LONG).show();
        }

        if(!pSensorDisp){
            snsManager.unregisterListener((SensorEventListener) this, snsManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
            Toast.makeText(getApplicationContext(),"Capteur de proximite indisponible, fonctionalite desactivee", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        snsManager.registerListener((SensorEventListener) this, pSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        snsManager.unregisterListener((SensorEventListener) this, pSensor);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_PROXIMITY :
                onProxyChange(event);
                break;
            default:
                break;
        }
    }

    private void onProxyChange(SensorEvent event) {
        ImageView pic = (ImageView) findViewById(R.id.wifiPic);
        ViewGroup.LayoutParams params = pic.getLayoutParams();
        if (event.values[0] >= 7){
            params.height = 128;
            params.width = 128;
        }else if (event.values[0] <= 3){
            params.width = 512;
            params.height = 512;
        }else{
            params.height = 256;
            params.width = 256;
        }

        pic.setLayoutParams(params);

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Sensor", sensor.getType()+":"+accuracy);
    }
}