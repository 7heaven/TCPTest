package com.example.tcptest.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Accelerometer implements SensorEventListener {
	
	private static final String TAG = "Accelerometer";
	
	public static final int TYPE_ACCELEROMETER = 0;
	public static final int TYPE_GRAVITY = 1;
	
	private int type = TYPE_GRAVITY;
	
	private Context mContext;
	private SensorManager mSensorManager;
	private OnDataChangedListener mOnDataChangedListener;
	
	private float[] gravity = new float[3];
	
	public interface OnDataChangedListener{
		public void onChange(float x, float y, float z);
	}
	
	public Accelerometer(Context context){
		mContext = context;
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener){
		mOnDataChangedListener = onDataChangedListener;
	}
	
	public boolean start(int type){
		this.type = type;
		if(null == mSensorManager){
			Log.e(TAG, "SensorManager Error");
			return false;
		}
		
		Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if(null == sensor){
			Log.e(TAG, "Sensor Error:not Found");
			return false;
		}
		
		boolean success = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
		if(!success){
			Log.e(TAG, "register Sensor Listener failed");
			return false;
		}
		
		return true;
	}
	
	public void stop(){
		if(null != mSensorManager){
			mSensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(type){
		case TYPE_ACCELEROMETER:
			final float alpha = 0.8F;
			
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
			
			onChange(event.values[0] - gravity[0],
					 event.values[1] - gravity[1],
					 event.values[2] - gravity[2]);
			break;
		case TYPE_GRAVITY:
			onChange(event.values[0], event.values[1], event.values[2]);
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	private void onChange(float x, float y, float z){
		if(null != mOnDataChangedListener){
			mOnDataChangedListener.onChange(x, y, z);
		}
	}
}
