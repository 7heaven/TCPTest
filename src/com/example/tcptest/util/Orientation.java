package com.example.tcptest.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.tcptest.util.base.BaseSensor;

public class Orientation extends BaseSensor {

	public Orientation(Context context) {
		super(context);
	}

	@Override
	public boolean start(int type) {
		return init(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME) && init(Sensor.TYPE_MAGNETIC_FIELD, SensorManager.SENSOR_DELAY_GAME);
	}

}
