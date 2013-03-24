package me.tegaralaga.socialvoting.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Session {
	
	SharedPreferences preferences;
	Editor editor;
	
	private Context _context;
	
	int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "SocialVoting";
	
	
	public Session(Context context){
		this._context = context;
		preferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = preferences.edit();
	}
	
	public void createSessionBoolean(String name,boolean status){
    	editor.putBoolean(name, status);
    	editor.commit();
    }
    
    public void createSessionInt(String key,int value){
    	editor.putInt(key, value);
    	editor.commit();
    }
    
    public void createSessionString(String name,String value){
    	editor.putString(name, value);
    	editor.commit();
    }
    
    public int getSessionInt(String arg0,int arg1){
    	return preferences.getInt(arg0, arg1);
    }
    
    public String getSessionString(String name){
    	return preferences.getString(name, null);
    }
    
    public String getSessionString(String name,String defaults){
    	return preferences.getString(name, defaults);
    }
    
    public boolean getSessionBoolean(String name){
    	return preferences.getBoolean(name,false);
    	
    }
    
    public void clear(){
    	editor.clear();
    	editor.commit();
    }
    
    public void removeSession(String name){
    	editor.remove(name);
    	editor.commit();
    }
    
    public void logout(){
    	editor.remove("is_login");
    	editor.remove("username");
    	editor.remove("gcm_registered");
    	editor.remove("registration_id");
    	editor.remove("token");
    	editor.remove("current_album");
    	editor.commit();
    }
    
    public int https_mode(){
    	/*
    	 * 1	Always HTTPS
    	 * 2	On Post Data
    	 * 3	Disable HTTPS
    	 * default 1
    	 */
    	return preferences.getInt("httpsMode",1);
    }
    
    public void https_mode(int value){
    	editor.putInt("httpsMode",value);
    	editor.commit();
    }
    
    public int connect_timeout(){
    	return preferences.getInt("connectTimeout",3000);
    }
    
    public void connect_timeout(int value){
    	editor.putInt("connectTimeout", value);
    	editor.commit();    	
    }
    
    public int read_timeout(){
    	return preferences.getInt("readTimeout",6000);
    }
    
    public void read_timeout(int value){
    	editor.putInt("readTimeout", value);
    	editor.commit();    	
    }
    
    public boolean isLogin(){
    	return preferences.getBoolean("isLogin", false);
    }
    
    public String token(){
    	return preferences.getString("tokenKey",null);
    }
    
    public int selectedNetworkType(){
    	/*
    	 * 1	Mobile Data
    	 * 2	WiFi
    	 * 3	WiFi + Mobile Data
    	 * default 2
    	 */
    	return preferences.getInt("selectedNetworkType",2);
    }
    
    public void selectedNetworkType(int value){
    	editor.putInt("selectedNetworkType", value);
    	editor.commit();
    }
    
    public int selectedMobileNetworkType(){
    	/*
    	 * 1	GPRS
    	 * 2	EDGE
    	 * 3	3G
    	 * 4	3.5G
    	 * 5	3.75G
    	 * 6	4G
    	 * default 6
    	 */
    	return preferences.getInt("selectedMobileNetworkType",6);
    }
    
    public void selectedMobileNetworkType(int value){
    	editor.putInt("selectedMobileNetworkType", value);
    	editor.commit();
    }
}
