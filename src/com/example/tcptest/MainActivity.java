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
import com.example.tcptest.util.Accelerometer.OnDataChangedListener;
import com.example.tcptest.view.BallView;

public class MainActivity extends Activity implements OnDataChangedListener{
	
	private TextView text;
	private BallView ballView;
	
	private Accelerometer accelerometer;
	
	private int yValue;

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
	public void onChange(float x, float y, float z) {
		ballView.offset(-(int) (x * 10), (int) (y * 10));
		text.setText("X:" + x + "\n" +
		             "Y:" + y + "\n" + 
				     "Z:" + z + "\n");
		
		yValue = (int) y * 10;
	}
    
	private void establishTCPLink(){
		
	    new Thread(){
	    	@Override
	    	public void run(){
	    		Socket socket = null;
				try {
					socket = new Socket("192.168.1.120", 1000);
					String content = null;
					while(true){
		    			try {
							InputStream in = socket.getInputStream();
							byte[] buff = new byte[52];
							
							if(in != null){
								int len = in.read(buff);
								content = new String(buff, 0, len);
							}else{
								Log.e("tcp", "inputfailed");
							}
							if(content != null){
								Log.d("tcp", content);
								OutputStream out = socket.getOutputStream();
								Log.d("tcp", "write");
								byte[] outBytes = "exactlytenexactlyten".getBytes();
								if(outBytes.length == 20){
									Log.d("tcp", "this should work with 20 bytes");
									out.write(outBytes);
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
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
