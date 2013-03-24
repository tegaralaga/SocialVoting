package me.tegaralaga.socialvoting.fragments;

import me.tegaralaga.socialvoting.R;
import me.tegaralaga.socialvoting.utils.DeviceInformation;
import me.tegaralaga.socialvoting.utils.Utils;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentDeviceInfo extends SherlockFragment{
	
	private Utils __utils;
	private Context __context;
	private View __view;
	private LayoutInflater __inflater;
	private TextView __title;
	private DeviceInformation __deviceInformation;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		__context = getSherlockActivity();
		__utils = new Utils(__context);
		__deviceInformation = new DeviceInformation(__context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		__inflater=inflater;
		__view = __inflater.inflate(R.layout.fragment_device_info,container,false);
		__title = (TextView)__view.findViewById(R.id.title);
		if(android.os.Build.VERSION.SDK_INT>=14){
//			__title.setTextAppearance(__context,android.R.style.TextAppearance_DeviceDefault_Large);
		}
		__utils.changeTypeFace(Utils.PACIFICO,__title);
		return __view;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.i("1 SCREEN_TYPE",__deviceInformation.screenType());
		Log.i("2 SCREEN_DENSITY",__deviceInformation.screenDensity());
		Log.i("3 SCREEN_WIDTH",__deviceInformation.screenWidth());
		Log.i("4 SCREEN_HEIGHT",__deviceInformation.screenHeight());
		Log.i("5 SDK_VERSION",__deviceInformation.SDKVersion());
		Log.i("6 SDK_VERSION_NAME",__deviceInformation.SDKVersionName());
		Log.i("7 DEVICE_MODEL",__deviceInformation.deviceModel());
		Log.i("8 DEVICE_MANUFACTURER",__deviceInformation.deviceManufacturer());
		Log.i("9 DEVICE_CARRIER",__deviceInformation.deviceCarrier());
		Log.i("10 SIM_STATE",__deviceInformation.simState());
		Log.i("11 DEVICE_ID",__deviceInformation.deviceID());
		Log.i("12 PHONE_TYPE",__deviceInformation.phoneType());
		Log.i("13 NETWORK_TYPE",__deviceInformation.networkType());
		Log.i("13 CURRENT_NETWORK_TYPE_NAME",__deviceInformation.currentNetworkTypeName());
		Log.i("13 CURRENT_MOBILE_NETWORK_TYPE_NAME",__deviceInformation.currentMobileNetworkTypeName());
	}

}
