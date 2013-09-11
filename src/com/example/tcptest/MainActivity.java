package com.example.tcptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.example.tcptest.util.Accelerometer;
import com.example.tcptest.util.base.BaseSensor.OnDataChangedListener;
import com.example.tcptest.view.BallView;

public class MainActivity extends Activity implements OnDataChangedListener{
	
	private TextView text;
	private BallView ballView;
	
	private Accelerometer accelerometer;
	
	private int yValue;
	
	private Socket socket = null;
	private boolean establishTCP = false;

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
	public void onChange(float[] values, int accuracy, long timestamp) {
		float x = values[0];
		float y = values[1];
		float z = values[2];
		
		ballView.offset(-(int) (x * 10), (int) (y * 10));
		text.setText("X:" + x + "\n" +
		             "Y:" + y + "\n" + 
				     "Z:" + z + "\n");
		
		yValue = (int) (y * 25);
		
		String outValue = null;
		if(yValue < 10){
			outValue = "00" + yValue + "ctlytenexactlyt\n";
		}else if (yValue < 100){
			outValue = "0" + yValue + "ctlytenexactlyt\n";
		}else{
			outValue = yValue + "ctlytenexactlyt\n";
		}
		Log.d("y:", yValue + "");
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
