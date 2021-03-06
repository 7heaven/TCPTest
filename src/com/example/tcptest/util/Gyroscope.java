package com.example.tcptest.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.tcptest.util.base.BaseSensor;

public class Gyroscope extends BaseSensor{

	public Gyroscope(Context context) {
		super(context);
	}

	@Override
	public boolean start(int type) {
		sensorType = Sensor.TYPE_GYROSCOPE;
		return init(sensorType, SensorManager.SENSOR_DELAY_GAME);
	}

}
