package com.zws.wifiboarddetect;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WifiStandSettings extends Activity {

	private Button back ;
	private Button save ;
	private long firstTime = 0;
	private EditText mMacAddressRangeBegin;
	private EditText mMacAddressRangeEnd;
	private EditText mStandPercent;
	private EditText mSsid_1_Name_EditText;
	private EditText mSsid_1_Level__EditText;
	private EditText mSsid_2_Name_EditText;
	private EditText mSsid_2_Level__EditText;
	private EditText mSsid_3_Name_EditText;
	private EditText mSsid_3_Level__EditText;
	private EditText mSsid_4_Name_EditText;
	private EditText mSsid_4_Level__EditText;
	private EditText mSsid_5_Name_EditText;
	private EditText mSsid_5_Level__EditText;
	
	private String MAC_NULL = "XX:XX:XX:XX:XX:XX";
	private SharedPreferences mSettingsData = null;
	private SharedPreferences.Editor mSettingsEd = null;
	
	private String mGetMacAddressRangeBegin = null;
	private String mGetMacAddressRangeEnd = null;
	private int mGetStandPercent = 0;
	private String mSsid_1_Name = null;
	private int mSsid_1_Level = 0 ;
	private String mSsid_2_Name = null;
	private int mSsid_2_Level = 0 ;
	private String mSsid_3_Name = null;
	private int mSsid_3_Level = 0 ;
	private String mSsid_4_Name = null;
	private int mSsid_4_Level = 0 ;
	private String mSsid_5_Name = null;
	private int mSsid_5_Level = 0 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		mSettingsData = getApplicationContext().getSharedPreferences("wifiDetectData", MODE_PRIVATE);
		mSettingsEd = mSettingsData.edit();
		
		mGetMacAddressRangeBegin = mSettingsData.getString("mac_range_begin", MAC_NULL);
		mGetMacAddressRangeEnd = mSettingsData.getString("mac_range_end", MAC_NULL);
		mGetStandPercent = mSettingsData.getInt("standPercent", 100);
		mSsid_1_Name = mSettingsData.getString("ssid_1_name", null);
		mSsid_1_Level = mSettingsData.getInt("ssid_1_level", -120);
		mSsid_2_Name = mSettingsData.getString("ssid_2_name", null);
		mSsid_2_Level = mSettingsData.getInt("ssid_2_level", -120);
		mSsid_3_Name = mSettingsData.getString("ssid_3_name", null);
		mSsid_3_Level = mSettingsData.getInt("ssid_3_level", -120);
		mSsid_4_Name = mSettingsData.getString("ssid_4_name", null);
		mSsid_4_Level = mSettingsData.getInt("ssid_4_level", -120);
		mSsid_5_Name = mSettingsData.getString("ssid_5_name", null);
		mSsid_5_Level = mSettingsData.getInt("ssid_5_level", -120);
		
		
		mMacAddressRangeBegin = (EditText)findViewById(R.id.macAddressRangeBegin);
		mMacAddressRangeEnd = (EditText)findViewById(R.id.macAddressRangeEnd);
		mStandPercent = (EditText)findViewById(R.id.standPercent);
		mSsid_1_Name_EditText = (EditText)findViewById(R.id.ssid_1_name);
		mSsid_1_Level__EditText = (EditText)findViewById(R.id.ssid_1_level);
		mSsid_2_Name_EditText = (EditText)findViewById(R.id.ssid_2_name);
		mSsid_2_Level__EditText = (EditText)findViewById(R.id.ssid_2_level);
		mSsid_3_Name_EditText = (EditText)findViewById(R.id.ssid_3_name);
		mSsid_3_Level__EditText = (EditText)findViewById(R.id.ssid_3_level);
		mSsid_4_Name_EditText = (EditText)findViewById(R.id.ssid_4_name);
		mSsid_4_Level__EditText = (EditText)findViewById(R.id.ssid_4_level);
		mSsid_5_Name_EditText = (EditText)findViewById(R.id.ssid_5_name);
		mSsid_5_Level__EditText = (EditText)findViewById(R.id.ssid_5_level);
		
		
		mMacAddressRangeBegin.setText(mGetMacAddressRangeBegin);
		mMacAddressRangeEnd.setText(mGetMacAddressRangeEnd);
		mStandPercent.setText(mGetStandPercent + "");
		mSsid_1_Name_EditText.setText(mSsid_1_Name);
		mSsid_1_Level__EditText.setText(mSsid_1_Level+"");
		mSsid_2_Name_EditText.setText(mSsid_2_Name);
		mSsid_2_Level__EditText.setText(mSsid_2_Level+"");
		mSsid_3_Name_EditText.setText(mSsid_3_Name);
		mSsid_3_Level__EditText.setText(mSsid_3_Level+"");
		mSsid_4_Name_EditText.setText(mSsid_4_Name);
		mSsid_4_Level__EditText.setText(mSsid_4_Level+"");
		mSsid_5_Name_EditText.setText(mSsid_5_Name);
		mSsid_5_Level__EditText.setText(mSsid_5_Level+"");
		
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
				mSettingsEd.putString("mac_range_begin", mMacAddressRangeBegin.getText().toString());
				mSettingsEd.putString("mac_range_end", mMacAddressRangeEnd.getText().toString());
				mSettingsEd.putInt("standPercent", Integer.parseInt(mStandPercent.getText().toString()));
				mSettingsEd.putString("ssid_1_name", mSsid_1_Name_EditText.getText().toString());
				mSettingsEd.putInt("ssid_1_level", Integer.parseInt(mSsid_1_Level__EditText.getText().toString()));
				mSettingsEd.putString("ssid_2_name", mSsid_2_Name_EditText.getText().toString());
				mSettingsEd.putInt("ssid_2_level", Integer.parseInt(mSsid_2_Level__EditText.getText().toString()));
				mSettingsEd.putString("ssid_3_name", mSsid_3_Name_EditText.getText().toString());
				mSettingsEd.putInt("ssid_3_level", Integer.parseInt(mSsid_3_Level__EditText.getText().toString()));
				mSettingsEd.putString("ssid_4_name", mSsid_4_Name_EditText.getText().toString());
				mSettingsEd.putInt("ssid_4_level", Integer.parseInt(mSsid_4_Level__EditText.getText().toString()));
				mSettingsEd.putString("ssid_5_name", mSsid_5_Name_EditText.getText().toString());
				mSettingsEd.putInt("ssid_5_level", Integer.parseInt(mSsid_5_Level__EditText.getText().toString()));
				
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
