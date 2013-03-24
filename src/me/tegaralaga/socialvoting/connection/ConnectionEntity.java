package me.tegaralaga.socialvoting.connection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.tegaralaga.socialvoting.callback.ConnectionQueueCallback;
import me.tegaralaga.socialvoting.utils.Connection;

import android.content.Context;

public class ConnectionEntity {
	
	private Context __context;
	private String __method;
	private String __url;
	private MultiPartFile __multiPartFile = null;
	private Map<String,String> __formData = new HashMap<String, String>();
	private ConnectionQueueCallback __connectionQueueCallback;
	private Map<?,?> __queryParameter;
	private boolean __isQueryParameterSet = false;
	private boolean __isHTTPSModeSet = false;
	private boolean __isConnectTimeoutSet = false;
	private boolean __isReadTimeoutSet = false;
	private int __connectTimeout = Connection.HTTP_CONNECTION_TIMEOUT;
	private int __readTimeout = Connection.HTTP_SOCKET_TIMEOUT;
	private int __httpsMode = Connection.ALWAYS_HTTPS;
	private boolean __acceptJSON = true;
	private boolean __acceptGZIPEncoding = true;
	private boolean __acceptByte = false;
	private boolean __fakeLogin = false;
	public final static String POST = "POST";
	public final static String GET = "GET";
	public final static String MULTIPART = "MULTIPART";
	
	public ConnectionEntity(Context context){
		__context = context;
	}
	
	public Context getContext(){
		return __context;
	}
	
	public void setConnectionQueueCallback(ConnectionQueueCallback connectionQueueCallback){
		__connectionQueueCallback = connectionQueueCallback;
	}
	
	public ConnectionQueueCallback getConnectionQueueCallback(){
		return __connectionQueueCallback;
	}
	
	public void setHTTPSMode(int httpsMode){
		__isHTTPSModeSet = true;
		__httpsMode = httpsMode;
	}
	
	public int getHTTPSMode(){
		return __httpsMode;
	}
	
	public void setQueryParameter(Map<?,?> params){
		__isQueryParameterSet = true;
		__queryParameter = params;
	}
	
	public Map<?,?> getQueryParameter(){
		return __queryParameter;
	}
	
	public boolean isQueryParameterSet(){
		return __isQueryParameterSet;
	}
	
	public void setConnectTimeout(int timeout){
		__isConnectTimeoutSet = true;
		__connectTimeout = timeout;
	}
	
	public int getConnectTimeout(){
		return __connectTimeout;
	}
	
	public void setReadTimeout(int timeout){
		__isReadTimeoutSet = true;
		__readTimeout = timeout;
	}
	
	public int getReadTimeout(){
		return __readTimeout;
	}
	
	public boolean isHTTPSModeSet(){
		return __isHTTPSModeSet;
	}
	
	public boolean isConnectTimeoutSet(){
		return __isConnectTimeoutSet;
	}
	
	public boolean isReadTimeoutSet(){
		return __isReadTimeoutSet;
	}
	
	public void fakeLogin(){
		__fakeLogin = true;
	}
	
	public boolean isFakeLogin(){
		return __fakeLogin;
	}
	
	public void get(){
		__method = GET;
	}
	
	public void post(){
		__method = POST;
	}
	
	public void multipart(){
		__method = MULTIPART;
	}
	
	public void acceptJSON(boolean accept){
		__acceptJSON = accept;
	}
	
	public void acceptGZIPEncoding(boolean accept){
		__acceptGZIPEncoding = accept;
	}
	
	public void acceptByte(boolean accept){
		__acceptByte = accept;
	}
	
	public boolean acceptJSON(){
		return __acceptJSON;
	}
	
	public boolean acceptGZIPEncoding(){
		return __acceptGZIPEncoding;
	}
	
	public boolean acceptByte(){
		return __acceptByte;
	}
	
	public void setURL(String url){
		__url = url;
	}
	
	public String getURL(){
		
		return __url;
	}
	
	public void setMethod(String method){
		__method = method;
	}
	
	public String getMethod(){
		return __method;
	}
	
	public void setFormData(Map<String, String> data){
		__formData = data;
	}
	
	public void addFormData(String key,String value){
		__formData.put(key, value);
	}
	
	public Map<String, String> getFormData(){
		return __formData;
	}
	
	public void setMultiPartFile(MultiPartFile multiPartFile){
		__multiPartFile = multiPartFile;
	}
	
	public MultiPartFile getMultiPartFile(){
		return __multiPartFile;
	}
	
	public class MultiPartFile{
		
		private String __name;		
		private File __file;
		private ArrayList<File> __multiFile = new ArrayList<File>();
		private boolean __isMultiFile;
		
		public MultiPartFile(String name,File file){
			__name = name;
			__file = file;
		}
		
		public MultiPartFile(String name){
			__name = name;
		}
		
		public File getFile(){
			return __file;
		}
		
		public String getName(){
			return __name;
		}
		
		public void setMultiFile(boolean multiFile){
			__isMultiFile = multiFile;
		}
		
		public boolean isMultiFile(){
			return __isMultiFile;
		}
		
		public void setMultiFile(ArrayList<File> multiFile){
			__multiFile = multiFile;
		}
		
		public ArrayList<File> getMultiFile(){
			__multiFile.trimToSize();
			return __multiFile;
		}
		
		public void addMultiFile(File file){
			__multiFile.add(file);
			__multiFile.trimToSize();
		}
		
	}
	
}
