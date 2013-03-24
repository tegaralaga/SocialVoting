package me.tegaralaga.socialvoting.connection;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

import me.tegaralaga.socialvoting.callback.ConnectionQueueCallback;
import me.tegaralaga.socialvoting.connection.ConnectionEntity.MultiPartFile;
import me.tegaralaga.socialvoting.utils.Connection;
import me.tegaralaga.socialvoting.utils.Session;

public class AsyncConnection extends Thread {

	private ConnectionEntity __ce;
	private ConnectionQueue __cq;
	private Context __context;
	private Connection __connection;
	private Session __session;
	private boolean use_https = false;
	
	public AsyncConnection(ConnectionQueue cq){
		__cq = cq;
	}
	
	public void setConnectionEntity(ConnectionEntity ce){
		__ce = ce;
		__context = __ce.getContext();
		__connection = new Connection(__context);
		__session = new Session(__context);
	}
	
	@Override
	public void run(){
		ConnectionQueueCallback connectionQueueCallback = __ce.getConnectionQueueCallback();
		HttpRequest request;
		if(__ce.getMethod().equals(ConnectionEntity.GET)){
			String url = Connection.SERVER;
			if(__ce.isHTTPSModeSet()){
				if(__ce.getHTTPSMode() == Connection.DISABLE_HTTPS || __ce.getHTTPSMode() == Connection.HTTPS_ONLY_POST_DATA){
					url = url.replace("https","http");
				} else {
					use_https = true;
				}
			} else {
				if(__session.https_mode() == Connection.DISABLE_HTTPS || __session.https_mode() == Connection.HTTPS_ONLY_POST_DATA){
					url = url.replace("https","http");
				} else {
					use_https = true;
				}
			}
			if(__ce.acceptByte()){
				url = url + __ce.getURL(); 
			} else {
				url = url + __ce.getURL() + Connection.EXT;
			}
			if(__ce.isQueryParameterSet()){
				request = HttpRequest.get(url,__ce.getQueryParameter(),true);
			} else {
				request = HttpRequest.get(url);
			}
		} else {
			String url = Connection.SERVER;
			if(__ce.isHTTPSModeSet()){
				if(__ce.getHTTPSMode() == Connection.DISABLE_HTTPS){
					url = url.replace("https","http");
				} else {
					use_https = true;
				}
			} else {
				if(__session.https_mode() == Connection.DISABLE_HTTPS){
					url = url.replace("https","http");
				} else {
					use_https = true;
				}
			}
			url = url + __ce.getURL() + Connection.EXT;
			if(__ce.isQueryParameterSet()){
				request = HttpRequest.post(url,__ce.getQueryParameter(),true);
			} else {
				request = HttpRequest.post(url);
			}
		}
		request.userAgent(__connection.ua());
		if(use_https){
			request.trustAllCerts();
			request.trustAllHosts();
		}
		if(__ce.acceptByte() == false){
			if(__ce.acceptJSON()){
				request.acceptJson();
			}
			if(__ce.acceptGZIPEncoding()){
				request.acceptGzipEncoding().uncompress(true);
			}
		}
		if(__ce.isConnectTimeoutSet()){
			request.connectTimeout(__ce.getConnectTimeout());
		} else {
			request.connectTimeout(__session.connect_timeout());
		}
		if(__ce.isReadTimeoutSet()){
			request.readTimeout(__ce.getReadTimeout());
		} else {
			request.readTimeout(__session.read_timeout());
		}
		if(__ce.isFakeLogin()){
			request.header("X-API-KEY","3c6ed9b3824f86767b7d331b0af7a4ca");
		} else {
			if(__session.isLogin()){
				request.header("X-API-KEY",__session.token());
			}
		}
		try {
			if(__ce.getMethod().equals(ConnectionEntity.MULTIPART)){
				Map<String, String> multipart = __ce.getFormData();
				for(Entry<String,String> row : multipart.entrySet()){
					request.part(row.getKey(),row.getValue());
				}
				if(__ce.getMultiPartFile() != null){
					MultiPartFile multiPartFile = __ce.getMultiPartFile();
					if(multiPartFile.isMultiFile()){
						
					} else {
						request.part(multiPartFile.getName(),multiPartFile.getFile().getName(),multiPartFile.getFile());
					}
				}
			} else if (__ce.getMethod().equals(ConnectionEntity.POST)) {
				request.form(__ce.getFormData());
			}
			int returnCode = request.code();
			if(request.serverError()){
				if(__ce.acceptByte()){
					connectionQueueCallback.response(-1,"Server Error".getBytes());
				} else {
					connectionQueueCallback.response(-1,"Server Error");
				}
			} else {
				if(request.badRequest()){
					if(__ce.acceptByte()){
						connectionQueueCallback.response(-1,"Bad Request".getBytes());
					} else {
						connectionQueueCallback.response(-1, "Bad Request");
					}
				} else {
					if(request.notFound()){
						if(__ce.acceptByte()){
							connectionQueueCallback.response(-1,"Not Found".getBytes());
						} else {
							connectionQueueCallback.response(-1,"Not Found");
						}
					} else {
						if(__ce.acceptByte()){
							if(returnCode == 415){
								connectionQueueCallback.response(415,request.body().getBytes());
							} else {
								connectionQueueCallback.response(returnCode,request.bytes());
							}
						} else {
							connectionQueueCallback.response(returnCode,request.body());
						}
					}
				}
			}
		} catch (HttpRequestException e) {
			if(__ce.acceptByte()){
				connectionQueueCallback.response(-1,e.getMessage().getBytes());
			} else {
				connectionQueueCallback.response(-1,e.getMessage());
			}
		} catch (Exception e) {
			if(__ce.acceptByte()){
				connectionQueueCallback.response(-1,e.toString().getBytes());
			} else {
				connectionQueueCallback.response(-1,e.toString());
			}
		} finally {
			try {
				request.disconnect();
				__cq.remove(__ce);
			} catch (IOException e) {
				if(__ce.acceptByte()){
					connectionQueueCallback.response(-1,e.toString().getBytes());
				} else {
					connectionQueueCallback.response(-1,e.toString());
				}
			} catch (InterruptedException e) {
				if(__ce.acceptByte()){
					connectionQueueCallback.response(-1,e.toString().getBytes());
				} else {
					connectionQueueCallback.response(-1,e.toString());
				}
			}
		}
	}
	
}