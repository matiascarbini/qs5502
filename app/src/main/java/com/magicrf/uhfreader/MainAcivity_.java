package com.magicrf.uhfreader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.zyapi.CommonApi;
import android.zyapi.Conversion;

import com.android.hdhe125klf.reader.R;

public class MainAcivity_ extends Activity implements OnItemSelectedListener, OnClickListener {
	CommonApi mCommonApi;
	private Button mOnoffSerialBtn;
	private TextView mRecvTex;
	private Button mCleanBtn;
	private int mComFd =-1;
	private boolean isOpen = false;
	private final int MAX_RECV_BUF_SIZE = 512;
	private byte [] recv;
	private final static int  SHOW_RECV_DATA = 18;
	private String strRead;
	private CheckBox mHexshow;
	private MediaPlayer player;

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
				case SHOW_RECV_DATA:
					String s =(String) msg.obj;
					if(s!=null){
						mRecvTex.append(s+"");
						Log.e("", "1111:"+s);
						player.start();
					}
					break;
				case 101:
					send(Conversion.HexString2Bytes("1234567"));
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jy_layout);
		findviews();
		setlistner();

		mCommonApi=new CommonApi();

		mCommonApi.setGpioDir(27,0);
		mCommonApi.getGpioIn(27);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mCommonApi.setGpioMode(27,0);
				mCommonApi.setGpioDir(27,1);
				int ret =mCommonApi.setGpioOut(27,1);

				if(ret == 0){
					Toast.makeText(MainAcivity_.this, "Set Success" , Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MainAcivity_.this, "Set Fail" , Toast.LENGTH_SHORT).show();
				}
			}
		}, 1000);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!isOpen){
//				Log.d(Tag, "mCommPort = " +mCommPort+" mBaudrate =" +mBaudrate);

//				mComFd = mCommonApi.openCom("/dev/ttyMT2", 1200, 8, 'N', 1);

					mComFd = mCommonApi.openCom("/dev/ttyS1", 9600, 8, 'N', 1);

					if(mComFd > 0){
						Toast.makeText(getApplicationContext(), "Open the serial port successfully", Toast.LENGTH_SHORT).show();
						isOpen = true;
						mOnoffSerialBtn.setText("close"+"");

						send(new byte[]{(byte) 0xaa});

						readData();

					}else{
						Toast.makeText(getApplicationContext(), "Open the serial port fail", Toast.LENGTH_SHORT).show();
						isOpen = false;
					}
				}else{
					mCommonApi.closeCom(mComFd);
					mOnoffSerialBtn.setText("open"+"");
					isOpen = false;
				}
			}
		}, 2000);

		player = MediaPlayer.create(getApplicationContext(), R.raw.msg);

	}

	private void findviews(){

		mRecvTex = (TextView) findViewById(R.id.recvtex);
		mCleanBtn = (Button) findViewById(R.id.cleanvalue);
		mHexshow = (CheckBox) findViewById(R.id.showhex);

		mOnoffSerialBtn = (Button) findViewById(R.id.onoffserial);
		mOnoffSerialBtn.setOnClickListener(this);

		mCommonApi= new CommonApi();
	}

	private void setlistner(){
		mCleanBtn.setOnClickListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
							   long id) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

			case R.id.onoffserial:
				if(!isOpen){
//				Log.d(Tag, "mCommPort = " +mCommPort+" mBaudrate =" +mBaudrate);

//				mComFd = mCommonApi.openCom("/dev/ttyMT2", 1200, 8, 'N', 1);

					mComFd = mCommonApi.openCom("/dev/ttyS1", 9600, 8, 'N', 1);

					if(mComFd > 0){
						Toast.makeText(this, "Open the serial port successfully", Toast.LENGTH_SHORT).show();
						isOpen = true;
						mOnoffSerialBtn.setText("close"+"");

					    send(new byte[]{(byte) 0xaa});

						readData();

					}else{
						Toast.makeText(this, "Open the serial port fail", Toast.LENGTH_SHORT).show();
						isOpen = false;
					}
				}else{
					mCommonApi.closeCom(mComFd);
					mOnoffSerialBtn.setText("open"+"");
					isOpen = false;
				}
				break;
			case R.id.cleanvalue:
				mRecvTex.setText("");
				break;
			default:
				break;
		}
	}
	/**
	 * 读数据线程
	 */
	private void readData(){
		new Thread(){
			public void run(){
				while(isOpen){
					int ret = 0;
					byte[] buf = new byte[MAX_RECV_BUF_SIZE+1];
					ret = mCommonApi.readComEx(mComFd, buf, MAX_RECV_BUF_SIZE, 0, 0);
					if (ret <= 0) {
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;
					}
					recv = new byte[ret];
					System.arraycopy(buf, 0, recv, 0, ret);
//					if(mHexshow.isChecked()){
					strRead = Conversion.Bytes2HexString(recv);
//					}else {
//						try {
//							strRead = new String(recv,"gb2312");
//							Log.e("", "strRead:"+strRead);
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
					if(strRead!=null){
						Message msg = handler.obtainMessage(SHOW_RECV_DATA);
						msg.obj=strRead;
						msg.sendToTarget();
					}
				}
			}
		}.start();
	}

	/**
	 * 发送数据
	 */
	private void send(byte[]data){
		if(data==null)return;
		if(mComFd>0){
			mCommonApi.writeCom(mComFd, data, data.length);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//close readThread
		handler.removeMessages(0);
		mCommonApi.setGpioDir(27,0);
		mCommonApi.setGpioOut(27,0);
		mCommonApi.closeCom(mComFd);
		isOpen =false;
	}

}
