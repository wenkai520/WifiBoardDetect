package com.zws.wifiboarddetect;

import android.util.Log;

public class Calculate {
	
	private static final String TAG = "zws";
	private static final String MAC_NULL = "XX:XX:XX:XX:XX:XX";
	private static int begin = -1;
	private static int end = -1;
	
	public Calculate(){
		
	}
	
	public String CalculateCommonMac(String pMacBegin , String pMacEnd){
		String macCommon = MAC_NULL;
		String macCommonBegin = null;
		String macCommonEnd = null;
		StringBuffer begin_sbu = new StringBuffer();
		StringBuffer end_sbu = new StringBuffer();
		StringBuffer temp_sbu = new StringBuffer();
		
		for(int i = 0 ;i < pMacBegin.length(); i++){
			if(pMacBegin.charAt(i) == pMacEnd.charAt(i)){
				begin_sbu.append(pMacBegin.charAt(i));
			}else{ 
				begin = i;
				break;
			}
		}
		macCommonBegin =  begin_sbu.toString();
		Log.d(TAG," CommonBegin = " + macCommonBegin);
		Log.d(TAG," i = " + begin);
		
		for(int j = pMacBegin.length() - 1; j > 0 ; j--){
			if(pMacBegin.charAt(j) == pMacEnd.charAt(j)){
				end_sbu.append(pMacBegin.charAt(j));
			}else{ 
				end = j;
				break;
			}
		}
		Log.d(TAG," CommonEnd = " + end_sbu.toString());
		Log.d(TAG," j = " + end);
		
		for(int k = 0 ;k < begin ;k++){
			temp_sbu.append(pMacBegin.charAt(k));
		}
		for(int k = begin ;k < end + 1 ;k++){
			if(k ==2 || k == 5 || k == 8 || k ==11)
				temp_sbu.append(':');
			else
				temp_sbu.append('x');
		}
		for(int k = end + 1;k < pMacBegin.length() ;k++){
			temp_sbu.append(pMacBegin.charAt(k));
		}
		Log.d(TAG," CommonMac = " + temp_sbu.toString());
		macCommon = temp_sbu.toString();
		
		return macCommon;
	}
	
		public String CalculateWifiBoardMacCommon(String getWifiMac) {
			String getWifiMacCommon = null ;
			StringBuffer get_sbu = new StringBuffer();

			for(int k = 0 ;k < begin ;k++){
				get_sbu.append(getWifiMac.charAt(k));
			}
			for(int k = begin ;k < end + 1 ;k++){
				if(k ==2 || k == 5 || k == 8 || k ==11)
				get_sbu.append(':');
			else
				get_sbu.append('x');
			}
			for(int k = end + 1;k < getWifiMac.length() ;k++){
				get_sbu.append(getWifiMac.charAt(k));
			}
			getWifiMacCommon = get_sbu.toString();
			Log.d(TAG," getWifiMacCommon = " + getWifiMacCommon);
			
			return getWifiMacCommon;
		}
		
		public boolean isInMacRange(String pMacBegin, String pMacEnd, String pGetWifiMac){
			
			boolean _flag = false;
			String _macBeginInterception = null;
			String _macEndInterception = null;
			String _macGetWifiInterception = null;
			StringBuffer mac_begin_sbu = new StringBuffer();
			StringBuffer mac_end_sbu = new StringBuffer();
			StringBuffer mac_wifi_sbu = new StringBuffer();
			
			for(int i = begin;i < end + 1 ;i++){
				if(!(pMacBegin.charAt(i) == ':'))
					mac_begin_sbu.append(pMacBegin.charAt(i));
				if(!(pMacEnd.charAt(i) == ':'))
					mac_end_sbu.append(pMacEnd.charAt(i));
				if(!(pGetWifiMac.charAt(i) == ':'))
					mac_wifi_sbu.append(pGetWifiMac.charAt(i));
			}
			_macBeginInterception = mac_begin_sbu.toString();
			_macEndInterception = mac_end_sbu.toString();
			_macGetWifiInterception = mac_wifi_sbu.toString();
			
			Log.d(TAG, "_macBegin = " + pMacBegin);
			Log.d(TAG, "_macGetWifi = " + pGetWifiMac);
			Log.d(TAG, "_macEnd = " + pMacEnd);
		
			Log.d(TAG, "_macBeginInterception = " + _macBeginInterception);
			Log.d(TAG, "_macGetWifiInterception = " + _macGetWifiInterception);
			Log.d(TAG, "_macEndInterception = " + _macEndInterception);
			
			Log.d(TAG, "_macBeginInterceptionto16 = " + Integer.parseInt(_macBeginInterception, 16));
			Log.d(TAG, "_macGetWifiInterceptionto16 = " + Integer.parseInt(_macGetWifiInterception, 16));
			Log.d(TAG, "_macEndInterceptionto16 = " + Integer.parseInt(_macEndInterception, 16));
			
			if(Integer.parseInt(_macGetWifiInterception, 16) >= Integer.parseInt(_macBeginInterception, 16) &&
					Integer.parseInt(_macGetWifiInterception, 16) <= Integer.parseInt(_macEndInterception, 16))
			{
				_flag = true;
				Log.d(TAG,"  in macRange   ");
			}else{
				_flag = false;
				Log.d(TAG,"  out macRange  ");
			}
			
			return _flag;
		}
}
