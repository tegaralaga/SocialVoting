package me.tegaralaga.socialvoting.utils;

import me.tegaralaga.socialvoting.utils.Utils.ReturnData;

import org.apache.http.client.HttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.github.kevinsawicki.http.HttpRequest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class Connection {
	
	public final static int HTTP_CONNECTION_TIMEOUT = 3000;
	public final static int HTTP_SOCKET_TIMEOUT = 6000;
	public final static String SERVER = "https://miscellaneous-tegaralaga.rhcloud.com/project/socialvoting/api/v1/";
	public final static String EXT = ".json";
	public final static int ALWAYS_HTTPS = 1;
	public final static int HTTPS_ONLY_POST_DATA = 2;
	public final static int DISABLE_HTTPS = 3;
	public final static int MOBILE_DATA_ONLY = 1;
	public final static int WIFI_ONLY = 2;
	public final static int WIFI_AND_MOBILE_DATA = 3;
	public final static int GPRS = 1;
	public final static int EDGE = 2;
	public final static int THREEG = 3;
	public final static int THREEPOINTFIVEG = 4;
	public final static int THREEPOINTSEVENTYFIVEG = 5;
	public final static int FOURG = 6;
	
	private Session __session;
	private ConnectivityManager __cm;
	private NetworkInfo __ni;
	private Context __context;
	private TelephonyManager __tm;
	
	public final static String url(String url){
		String server = SERVER.replace("https","http");
		return server+url+EXT;
	}
	
	public final static String androidCustomUserAgent(){
		String user_agent="";
		try {
			user_agent+="ANDROID";
			user_agent+=" | OS-VERSION : "+System.getProperty("os.version");
			user_agent+=" | SDK-VERSION : "+String.valueOf(android.os.Build.VERSION.SDK_INT);
			user_agent+=" | DEVICE : "+android.os.Build.DEVICE;
			user_agent+=" | MODEL : "+android.os.Build.MODEL;
			user_agent+=" | PRODUCT : "+android.os.Build.PRODUCT;
			user_agent+=" | ID : "+android.os.Build.ID;
			user_agent+=" | MANUFACTURER : "+android.os.Build.MANUFACTURER;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user_agent;
	}
	
	public String ua(){
		String user_agent="";
		try {
			user_agent+="ANDROID";
			user_agent+=" | SDK-VERSION : "+String.valueOf(android.os.Build.VERSION.SDK_INT);
			user_agent+=" | SDK-ID : "+android.os.Build.ID;
			user_agent+=" | MODEL : "+android.os.Build.MODEL;
			user_agent+=" | MANUFACTURER : "+android.os.Build.MANUFACTURER;
			
			String device_id = null;
			if(__tm.getDeviceId()==null){
				device_id = Secure.getString(__context.getContentResolver(),Secure.ANDROID_ID);
			} else {
				device_id = __tm.getDeviceId();
			}
			if(device_id == null){
				device_id = "Unknown";
			}
			user_agent+=" | ID : "+device_id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user_agent;
	}
	
	public final static void set_user_agent(HttpClient hc){
		String user_agent="ANDROID";
		try {
			user_agent+=" | OS-VERSION : "+System.getProperty("os.version");
			user_agent+=" | SDK-VERSION : "+String.valueOf(android.os.Build.VERSION.SDK_INT);
			user_agent+=" | DEVICE : "+android.os.Build.DEVICE;
			user_agent+=" | MODEL : "+android.os.Build.MODEL;
			user_agent+=" | PRODUCT : "+android.os.Build.PRODUCT;
			user_agent+=" | ID : "+android.os.Build.ID;
			user_agent+=" | MANUFACTURER : "+android.os.Build.MANUFACTURER;
		} catch (Exception e) {
			e.printStackTrace();
		}
		hc.getParams().setParameter(CoreProtocolPNames.USER_AGENT,user_agent);
	}
	
	public Connection(Context context){
		__context=context;
		__session = new Session(__context);
		__tm = (TelephonyManager)__context.getSystemService(Context.TELEPHONY_SERVICE);
		__cm = (ConnectivityManager) __context.getSystemService((Context.CONNECTIVITY_SERVICE));
	}
	
	public String server_url(String url){
		if(__session.https_mode() == 1 || __session.https_mode() == 2){
			return SERVER+url+EXT;
		} else {
			String server = SERVER.replace("https","http");
			return server+url+EXT;
		}
	}
	
	public HttpRequest get(String url){
		HttpRequest request = HttpRequest.get(server_url(url));
		request.userAgent(androidCustomUserAgent());
		request.acceptJson();
		request.acceptGzipEncoding().uncompress(true);
		if(__session.https_mode() == 1){
			request.trustAllCerts();
			request.trustAllHosts();
		}
		return request;
	}
	
	public HttpRequest post(String url){
		HttpRequest request = HttpRequest.post(server_url(url));
		request.userAgent(androidCustomUserAgent());
		request.acceptJson();
		request.acceptGzipEncoding().uncompress(true);
		if(__session.https_mode() == 1 || __session.https_mode() == 2){
			request.trustAllCerts();
			request.trustAllHosts();
		}
		return request;
	}
	
	public boolean isOnline(){
		__ni = __cm.getActiveNetworkInfo();
		if(__ni!=null && __ni.isConnectedOrConnecting()){
			return true;
		}
		return false;
	}
	
	public ReturnData checkConnectionStatus(){
		ReturnData data = new ReturnData(false,null);
		__ni = __cm.getActiveNetworkInfo();
		if(__cm.getBackgroundDataSetting()){
			try {
				if(__ni != null){
					int type = __ni.getType();
					int selected_network_type = __session.selectedNetworkType();
					int selected_mobile_network_type = __session.selectedMobileNetworkType();
					if(selected_network_type == MOBILE_DATA_ONLY){
						if(type == ConnectivityManager.TYPE_MOBILE){
							if(__ni.isConnected()){
								int current_mobile_network_type = __ni.getSubtype();
								if(mobileNetworkCategory(current_mobile_network_type) >= selected_mobile_network_type){
									data.setStatus(true);
								} else {
									data.setResult("You have been selected "+mobileNetworkCategoryName(selected_mobile_network_type)+", but your current network is "+mobileNetworkCategoryNameDetail(current_mobile_network_type));
								}
							} else {
								data.setResult("Mobile connection cannot be established.");
							}
						} else {
							data.setResult("Mobile connection unavailable");
						}
					} else if (selected_network_type == WIFI_ONLY) {
						if(type == ConnectivityManager.TYPE_WIFI){
							if(__ni.isConnected()){
								data.setStatus(true);
							} else {
								data.setResult("WiFi connection cannot be established.");
							}
						} else {
							data.setResult("WiFi connection unavailable");
						}
					} else {
						if(type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE){
							boolean wifi = false;
							boolean mobile = false;
							if(type == ConnectivityManager.TYPE_WIFI){
								if(__ni.isConnected()){
									wifi = true;
								}
							}
							if(type == ConnectivityManager.TYPE_MOBILE){
								if(__ni.isConnected()){
									mobile = true;
								}
							}
							if(wifi || mobile){
								if(wifi){
									if(__ni.isConnected()){
										data.setStatus(true);
									} else {
										data.setResult("WiFi connection cannot be established.");
									}
								} else {
									if(__ni.isConnected()){
										int current_mobile_network_type = __ni.getSubtype();
										if(mobileNetworkCategory(current_mobile_network_type) >= selected_mobile_network_type){
											data.setStatus(true);
										} else {
											data.setResult("You have been selected "+mobileNetworkCategoryName(selected_mobile_network_type)+", but your current network is "+mobileNetworkCategoryNameDetail(current_mobile_network_type));
										}
									} else {
										data.setResult("Mobile connection cannot be established.");
									}
								}
							} else {
								data.setResult("WiFi and mobile connection cannot be established.");
							}
						} else {
							data.setResult("WiFi and mobile connection unavailable");
						}
					}
				} else {
					data.setResult("Internet connection unavailable");
				}
			} catch (Exception e) {
				data.setResult("Internet connection unavailable");
			}
		} else {
			data.setResult("Background data disabled, please enable it first.");
		}
		return data;
	}
	
	public int mobileNetworkCategory(int selected){
		int category = 1;
		switch (selected) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
			category = 1;
			break;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			category = 2;
			break;
		case TelephonyManager.NETWORK_TYPE_UMTS:
			category = 3;
			break;
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			category = 4;
			break;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			category = 4;
			break;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			category = 4;
			break;
		case 15:
			category = 5;
			break;
		case 13:
			category = 6;
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			category = 3;
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			category = 4;
			break;
		case 12:
			category = 5;
			break;
		}
		return category;
	}
	
	public String mobileNetworkCategoryName(int selected){
		String string = "Unknown";
		switch (selected) {
		case 1:
			string = "GPRS";
			break;
		case 2:
			string = "EDGE";
			break;
		case 3:
			string = "3G";
			break;
		case 4:
			string = "3.5G";
			break;
		case 5:
			string = "3.75G";
			break;
		case 6:
			string = "4G";
			break;
		}
		return string;
	}
	
	public String mobileNetworkCategoryNameDetail(int selected){
		String string = "Unknown";
		switch (selected) {
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
		return string;
	}
}
