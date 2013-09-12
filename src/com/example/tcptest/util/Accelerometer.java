package com.example.tcptest.util;

import com.example.tcptest.util.base.BaseSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class Accelerometer extends BaseSensor {
	
	private static final String TAG = "Accelerometer";
	
	public static final int TYPE_ACCELEROMETER = 0;
	public static final int TYPE_GRAVITY = 1;
	
	private int type = TYPE_GRAVITY;
	
	private float[] gravity = new float[3];
	
	public Accelerometer(Context context){
		super(context);
	}
	
	public boolean start(int type){
		sensorType = Sensor.TYPE_ACCELEROMETER;
		return init(sensorType, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(type){
		case TYPE_ACCELEROMETER:
			final float alpha = 0.8F;
			
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
			
			onChange(event.sensor, new float[]{event.values[0] - gravity[0],
					                           event.values[1] - gravity[1],
					                           event.values[2] - gravity[2]}, event.accuracy, event.timestamp);
			break;
		case TYPE_GRAVITY:
			super.onSensorChanged(event);
			break;
		}
	}
}
