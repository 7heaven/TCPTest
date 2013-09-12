package com.example.tcptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.example.tcptest.util.Accelerometer;
import com.example.tcptest.util.Orientation;
import com.example.tcptest.util.base.BaseSensor.OnDataChangedListener;
import com.example.tcptest.view.BallView;

public class MainActivity extends Activity implements OnDataChangedListener{
	
	private TextView text;
	private BallView ballView;
	
	private Accelerometer accelerometer;
	
	private int xValue;
	private int yValue;
	private int zValue;
	
	private Socket socket = null;
	private boolean establishTCP = false;
	
	private static final String tail = "xxxyyyzzz\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
    	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        text = (TextView) findViewById(R.id.text);
        ballView = (BallView) findViewById(R.id.ballview);
        ballView.setRadius(30);
        
        accelerometer = new Accelerometer(this);
        accelerometer.setOnDataChangedListener(this);
        accelerometer.start(Accelerometer.TYPE_GRAVITY);
        
        establishTCPLink();
    }
    
    @Override
    public void onChange(Sensor sensor, float[] values, int accuracy, long timestamp){
    	
    	ballView.offset(-(int) (values[0] * 10), (int) (values[1] * 10));
		text.setText("X:" + values[0] + "\n" +
		             "Y:" + values[1] + "\n" + 
				     "Z:" + values[2] + "\n");
    	
    	String xOutValue = getProperString((int) (values[0] * 25));
    	String yOutValue = getProperString((int) (values[1] * 25));
    	String zOutValue = getProperString((int) (values[2] * 25));
    	
    	String outValue = xOutValue + yOutValue + zOutValue + tail;
    	if(establishTCP || socket != null){
			try{
				OutputStream out = socket.getOutputStream();
				byte[] outBytes = outValue.getBytes();
				out.write(outBytes);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
    }

	/*
	@Override
	public void onChange(Sensor sensor, float[] values, int accuracy, long timestamp) {
		if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			System.arraycopy(values, 0, lastAccelerometer, 0, values.length);
			lastAccelerometerSet = true;
		}else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			System.arraycopy(values, 0, lastMagnetometer, 0, values.length);
			lastMagnetometerSet = true;
		}
		
		float x = 0;
		float y = 0;
		float z = 0;
		
		if(lastAccelerometerSet && lastMagnetometerSet){
			SensorManager.getRotationMatrix(mR, null, lastAccelerometer, lastMagnetometer);
			SensorManager.getOrientation(mR, mOrientation);
			x = mOrientation[0];
			y = mOrientation[1];
			z = mOrientation[2];
		}
		
		ballView.offset(-(int) (x * 10), (int) (y * 10));
		text.setText("X:" + x + "\n" +
		             "Y:" + y + "\n" + 
				     "Z:" + z + "\n");
		
		xValue = (int) (x * 25);
		yValue = (int) (y * 25);
		zValue = (int) (z * 25);
		
		String xOutValue = getProperString(xValue);
		String yOutValue = getProperString(yValue);
		String zOutValue = getProperString(zValue);
		String outValue = xOutValue + yOutValue + zOutValue + tail;
		if(establishTCP || socket != null){
			try{
				OutputStream out = socket.getOutputStream();
				byte[] outBytes = outValue.getBytes();
				out.write(outBytes);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	 */
	
	private String getProperString(int value){
		String result = null;
		if(value > 0){
			if(value < 10){
				result = "00" + value;
			}else if(value < 100){
				result = "0" + value;
			}else{
				result = value + "";
			}
		}
		
		return result;
	}
    
	private void establishTCPLink(){
		
	    new Thread(){
	    	@Override
	    	public void run(){
				try {
					socket = new Socket("192.168.1.120", 1000);
					String content = null;
					while(true){
		    			try {
							InputStream in = socket.getInputStream();
							byte[] buff = new byte[100];
							
							if(in != null){
								int len = in.read(buff);
								if(len > 0){
									content = new String(buff, 0, len);
								}
							}else{
								Log.e("tcp", "inputfailed");
							}
							if(content != null){
								establishTCP = true;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
		    			
		    		}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		
	    		
	    	}
	    }.start();
	}
	
}
