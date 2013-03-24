package me.tegaralaga.socialvoting.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.tegaralaga.socialvoting.callback.ProgressBarIndeterminateCallback;

public class ConnectionQueue {

	private static final int MAX_CONNECTION = 5;
	private ArrayList<ConnectionEntity> __queue;
	private ArrayList<ConnectionEntity> __active;
	private ProgressBarIndeterminateCallback __progressBarIndeterminateCallback;
	private static ConnectionQueue __instance;
	
	private ConnectionQueue(){
		__queue=new ArrayList<ConnectionEntity>();
		__active=new ArrayList<ConnectionEntity>();
		__queue.trimToSize();
		__active.trimToSize();
	}
	
	public static ConnectionQueue getInstance(){
		if	(__instance == null) {
			__instance = new ConnectionQueue();
		}
		return __instance;
	}
	
	public void setProgressBarIndeterminateCallback(ProgressBarIndeterminateCallback progressBarIndeterminateCallback){
		__progressBarIndeterminateCallback = progressBarIndeterminateCallback;
	}
	
	public ProgressBarIndeterminateCallback getProgressBarIndeterminateCallback(){
		return __progressBarIndeterminateCallback;
	}
	
	public void addConnection(ConnectionEntity ce) throws IOException, InterruptedException{
		synchronized (__instance) {
			__queue.add(ce);
			__queue.trimToSize();
			startNext();
		}
	}
	
	private synchronized void startNext() throws IOException, InterruptedException{
		if( __active.size() < MAX_CONNECTION && __queue.size() > 0 ){
			ConnectionEntity ce = __queue.get(0);
			__queue.remove(0);
			__active.add(ce);
			createConnection(ce);
			if(__progressBarIndeterminateCallback != null) {
				__progressBarIndeterminateCallback.startProgressBarIndeterminate();
			}
		}
	}
	
	private synchronized void createConnection(ConnectionEntity ce) throws IOException{
		AsyncConnection ac = new AsyncConnection(this);
		ac.setConnectionEntity(ce);
		ac.start();
	}
	
	public void remove(ConnectionEntity ce) throws IOException, InterruptedException{
		synchronized (__instance) {
			__active.remove(ce);
			startNext();
		}		
		final ScheduledExecutorService exec=Executors.newScheduledThreadPool(1);
		exec.schedule(new Runnable() {
			@Override
			public void run() {
				synchronized (__instance) {
					if( __active.size() == 0 && __progressBarIndeterminateCallback != null ){
						__progressBarIndeterminateCallback.stopProgressBarIndeterminate();
					}
				}
			}
		},1,TimeUnit.SECONDS);
	}
	
	public void retry(final ConnectionEntity ce) throws IOException, InterruptedException {
		synchronized (__instance) {
			__active.remove(ce);
		}
		final ScheduledExecutorService exec=Executors.newScheduledThreadPool(1);
		exec.schedule(new Runnable()  {
			@Override
			public void run() {
				synchronized (__instance) {
						__queue.add(ce);
					try {
						startNext();
					} catch (IOException e) {
						__queue.remove(ce);
					} catch (InterruptedException e) {
						__queue.remove(ce);
					}
				}
			}
		},30L,TimeUnit.SECONDS);
	}
	
}
