package com.zws.wifiboarddetect;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WifiStandSettings extends Activity {

	private Button back ;
	private Button save ;
	private long firstTime = 0;
	private EditText mMacAddressRange;
	private EditText mSsid_1_Name_EditText;
	private EditText mSsid_1_Level__EditText;
	private EditText mSsid_2_Name_EditText;
	private EditText mSsid_2_Level__EditText;
	
	private String MAC_NULL = "XX:XX:XX:XX:XX:XX";
	private SharedPreferences mSettingsData = null;
	private SharedPreferences.Editor mSettingsEd = null;
	
	private String mGetMacAddressRange = null;
	private String mSsid_1_Name = null;
	private int mSsid_1_Level = 0 ;
	private String mSsid_2_Name = null;
	private int mSsid_2_Level = 0 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		mSettingsData = getApplicationContext().getSharedPreferences("wifiDetectData", MODE_PRIVATE);
		mSettingsEd = mSettingsData.edit();
		
		mGetMacAddressRange = mSettingsData.getString("mac_range", MAC_NULL);
		mSsid_1_Name = mSettingsData.getString("ssid_1_name", null);
		mSsid_1_Level = mSettingsData.getInt("ssid_1_level", 0);
		mSsid_2_Name = mSettingsData.getString("ssid_2_name", null);
		mSsid_2_Level = mSettingsData.getInt("ssid_2_level", 0);
		
		
		mMacAddressRange = (EditText)findViewById(R.id.macAddressRange);
		mSsid_1_Name_EditText = (EditText)findViewById(R.id.ssid_1_name);
		mSsid_1_Level__EditText = (EditText)findViewById(R.id.ssid_1_level);
		mSsid_2_Name_EditText = (EditText)findViewById(R.id.ssid_2_name);
		mSsid_2_Level__EditText = (EditText)findViewById(R.id.ssid_2_level);
		
		
		mMacAddressRange.setText(mGetMacAddressRange);
		mSsid_1_Name_EditText.setText(mSsid_1_Name);
		mSsid_1_Level__EditText.setText(mSsid_1_Level+"");
		mSsid_2_Name_EditText.setText(mSsid_2_Name);
		mSsid_2_Level__EditText.setText(mSsid_2_Level+"");
		
		back = (Button)findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), WifiBoardDetect.class);
				startActivity(intent);
				finish();
			}
		});
		save = (Button)findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSettingsEd.putString("mac_range", mMacAddressRange.getText().toString());
				mSettingsEd.putString("ssid_1_name", mSsid_1_Name_EditText.getText().toString());
				mSettingsEd.putInt("ssid_1_level", Integer.parseInt(mSsid_1_Level__EditText.getText().toString()));
				mSettingsEd.putString("ssid_2_name", mSsid_2_Name_EditText.getText().toString());
				mSettingsEd.putInt("ssid_2_level", Integer.parseInt(mSsid_2_Level__EditText.getText().toString()));
				
				mSettingsEd.commit();
				Toast.makeText(WifiStandSettings.this, "保存成功", 
	                        Toast.LENGTH_SHORT).show(); 
			}
		});

	}
	/**
	 *程序优化，点击两次退出程序
	 * */
	public boolean onKeyUp(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
            long secondTime = System.currentTimeMillis(); 
            if (secondTime - firstTime > 800) {//如果两次按键时间间隔大于800毫秒，则不退出 
                Toast.makeText(this, "再按一次退出程序...", 
                        Toast.LENGTH_SHORT).show(); 
                firstTime = secondTime;//更新firstTime 
                return true; 
            } else { 
                System.exit(0);//否则退出程序 
            } 
        } 
        return super.onKeyUp(keyCode, event); 
    }   
}
