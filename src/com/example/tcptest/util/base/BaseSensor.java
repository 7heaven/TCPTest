package com.example.tcptest.util.base;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class BaseSensor implements SensorEventListener {
	
	protected static final String TAG = "BaseSensor";
	
	protected Context mContext;
	protected SensorManager mSensorManager;
	protected int sensorType;
	
	protected OnDataChangedListener mOnDataChangedListener;
	
	public interface OnDataChangedListener{
		public void onChange(Sensor sensor, float[] values, int accuracy, long timestamp);
	}
	
	public BaseSensor(Context context){
		mContext = context;
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public abstract boolean start(int type);
	
	public void stop(){
		if(null != mSensorManager){
			mSensorManager.unregisterListener(this);
		}
	}
	
	public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener){
		mOnDataChangedListener = onDataChangedListener;
	}
	
	protected boolean init(int sensorType, int delay){
		if(null == mSensorManager){
			Log.e(TAG, "SensorManager Error");
			return false;
		}
		
		Sensor sensor = mSensorManager.getDefaultSensor(sensorType);
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
	
	protected void onChange(Sensor sensor, float[] values, int accuracy, long timestamp){
		if(null != mOnDataChangedListener){
			mOnDataChangedListener.onChange(sensor, values, accuracy, timestamp);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		onChange(event.sensor, event.values, event.accuracy, event.timestamp);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}
