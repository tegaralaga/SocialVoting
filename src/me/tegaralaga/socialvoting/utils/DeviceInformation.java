package me.tegaralaga.socialvoting.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceInformation {
	
	private Context __context;
	private ScreenSize __screenSize;
	private TelephonyManager __tm;
	private ConnectivityManager __cm;
	private NetworkInfo __ni;
	private String __screenWidth;
	private String __screenHeight;
	private int __simState;
	private int __phoneType;
	private int __networkType;
	
	public DeviceInformation(Context context){
		__context = context;
		__screenSize = new ScreenSize(__context);
		__cm = (ConnectivityManager) __context.getSystemService((Context.CONNECTIVITY_SERVICE));
		__tm = (TelephonyManager)__context.getSystemService(Context.TELEPHONY_SERVICE);
		__ni = __cm.getActiveNetworkInfo();
		__simState = __tm.getSimState();
		__phoneType = __tm.getPhoneType();
		__networkType = __tm.getNetworkType();
		__screenWidth = __screenSize.getWidth();
		__screenHeight = __screenSize.getHeight();
	}
	
	public String screenType(){
		int screen_layout = __context.getResources().getConfiguration().screenLayout;
		String screen_size = "Unknown";
		if((screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) ==  Configuration.SCREENLAYOUT_SIZE_SMALL){
			screen_size = "Small";
		} else if ((screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) ==  Configuration.SCREENLAYOUT_SIZE_NORMAL){
			screen_size = "Normal";
		} else if ((screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) ==  Configuration.SCREENLAYOUT_SIZE_LARGE){
			screen_size = "Large";
		} else if ((screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) ==  Configuration.SCREENLAYOUT_SIZE_XLARGE){
			screen_size = "XLarge";
		} else if ((screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) ==  Configuration.SCREENLAYOUT_SIZE_UNDEFINED){
			screen_size = "Undefined";
		}
		return screen_size;
	}
	
	public String screenDensity(){
		String screen_density = "Unknown";
		switch (__context.getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			screen_density = "Low";
		    break;
		case DisplayMetrics.DENSITY_MEDIUM:
			screen_density = "Medium";
		    break;
		case DisplayMetrics.DENSITY_HIGH:
			screen_density = "High";
		    break;
		case DisplayMetrics.DENSITY_XHIGH:
			screen_density = "XHigh";
		    break;
		case DisplayMetrics.DENSITY_TV:
			screen_density = "TV";
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			screen_density = "XXHigh";
			break;
		}
		return screen_density;
	}
	
	public String deviceID(){
		String device_id;
		if(__tm.getDeviceId()==null){
			device_id = Secure.getString(__context.getContentResolver(),Secure.ANDROID_ID);
		} else {
			device_id = __tm.getDeviceId();
		}
		if(device_id == null){
			device_id = "Unknown";
		}
		return device_id;
	}
	
	public String SDKVersion(){
		return String.valueOf(android.os.Build.VERSION.SDK_INT);
	}
	
	public String SDKVersionName(){
		return String.valueOf(android.os.Build.ID);
	}
	
	public String deviceModel(){
		return android.os.Build.MODEL;
	}
	
	public String deviceManufacturer(){
		return android.os.Build.MANUFACTURER;
	}
	
	public String deviceCarrier(){
		if(__simState == TelephonyManager.SIM_STATE_ABSENT){
			return "No Carrier";
		} else {
			return __tm.getNetworkOperatorName();
		}
	}
	
	public String simState(){
		String string = "Unknown";
		switch (__simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
			string = "Unavailable";
			break;
		case TelephonyManager.SIM_STATE_READY:
			string = "Available";
			break;
		}
		return string;
	}
	
	public String phoneType(){
		String string =  "Unknown";
		switch (__phoneType) {
		case TelephonyManager.PHONE_TYPE_GSM:
			string = "GSM";
		break;
		case TelephonyManager.PHONE_TYPE_CDMA:
			string = "CDMA";
		break;
		case TelephonyManager.PHONE_TYPE_SIP:
			string = "SIP";
		break;
		}
		return string;
	}
	
	public String networkType(){
		String string = "Unknown";
		if(simState().equalsIgnoreCase("Unknown") || simState().equalsIgnoreCase("Unavailable")){
			string = "Mobile data unavailable";
		} else {
			switch (__networkType) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
				string = "GPRS";
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				string = "EDGE";
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				string = "3G (UMTS)";
				break;
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				string = "3.5G (HSDPA)";
				break;
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				string = "3.5G (HSUPA)";
				break;
			case TelephonyManager.NETWORK_TYPE_HSPA:
				string = "3.5G (HSPA)";
				break;
			case 15:
				string = "3.75G (HSPA+)";
				break;
			case 13:
				string = "4G (LTE)";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				string = "3G (EVDO)";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				string = "3.5G (EVDO Rev.A)";
				break;
			case 12:
				string = "3.75G (EVDO Rev.B)";
				break;
			}
		}
		return string;
	}
	
	public String currentNetworkTypeName(){
		String string = "Network unavailable";
		try {
			int network_type = __ni.getType();
			switch (network_type) {
			case ConnectivityManager.TYPE_MOBILE:
				string = "Mobile";
				break;
			case ConnectivityManager.TYPE_WIFI:
				string = "WiFi";
				break;
			case ConnectivityManager.TYPE_WIMAX:
				string = "WiMax";
				break;
			}
		} catch (Exception e) {
		}
		return string;
	}
	
	public String currentMobileNetworkTypeName(){
		String string = "Mobile data unavailable";
		if(simState().equalsIgnoreCase("Available")){
			if(currentNetworkTypeName().equalsIgnoreCase("mobile")){
				string = __ni.getSubtypeName();
			}
		}
		return string;
	}
	
	public String screenWidth(){
		return __screenWidth;
	}
	
	public String screenHeight(){
		return __screenHeight;
	}
	
	
	
	public class ScreenSize{
		
		private String __width;
		private String __height;
		
		@SuppressWarnings("deprecation")
		public ScreenSize(Context context){
	    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    	Display display = wm.getDefaultDisplay();
			if(android.os.Build.VERSION.SDK_INT >= 13){
				Point size = new Point();
				display.getSize(size);
				__width = String.valueOf(size.x);
				__height = String.valueOf(size.y);
			} else {
				__width = String.valueOf(display.getWidth());
				__height = String.valueOf(display.getHeight());
			}
		}
		
		public String getWidth(){
			return __width;
		}
		
		public String getHeight(){
			return __height;
		}
		
	}

}
