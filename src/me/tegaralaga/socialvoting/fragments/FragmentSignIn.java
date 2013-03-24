package me.tegaralaga.socialvoting.fragments;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import me.tegaralaga.socialvoting.Landing;
import me.tegaralaga.socialvoting.R;
import me.tegaralaga.socialvoting.callback.ConnectionQueueCallback;
import me.tegaralaga.socialvoting.connection.ConnectionEntity;
import me.tegaralaga.socialvoting.connection.ConnectionQueue;
import me.tegaralaga.socialvoting.utils.Connection;
import me.tegaralaga.socialvoting.utils.Utils.ReturnData;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentSignIn extends SherlockFragment {
	
	private Landing __landing;
	private Context __context;
	private View __view;
	private LayoutInflater __inflater;
	private RelativeLayout __layoutAlert;
	private TextView __alert;
	private EditText __username,__password;
	private Button __signinButton;
	InputMethodManager keyboard;
	private Connection __connection;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		__inflater=inflater;
		__view = __inflater.inflate(R.layout.signin,container,false);
		__layoutAlert = (RelativeLayout)__view.findViewById(R.id.layout_alert);
		__alert = (TextView)__view.findViewById(R.id.alert);
		__username = (EditText)__view.findViewById(R.id.username);
		__password = (EditText)__view.findViewById(R.id.password);
		__signinButton = (Button)__view.findViewById(R.id.signin_button);
		__layoutAlert.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				__layoutAlert.setVisibility(View.GONE);
				__alert.setText("");
			}
		});
		__signinButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				keyboard.hideSoftInputFromWindow(__signinButton.getWindowToken(),0);
				signin(__username.getText().toString().trim(),__password.getText().toString().trim());
			}
		});
		return __view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		__landing = ((Landing)getSherlockActivity());
		__context = getSherlockActivity();
		__connection = new Connection(__context);
		keyboard =  (InputMethodManager)__context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		__alert.setText("Welcome to SocialVoting");
	}
	
	private void __showAlert(final String text){
		__landing.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if(__layoutAlert.isShown()){
						__layoutAlert.setVisibility(View.GONE);
						__alert.setText("");
					}
					__layoutAlert.setVisibility(View.VISIBLE);
					__alert.setText(text);
				} catch (Exception e) {
					Log.e("ERROR",e.toString());
				}
			}
		});
	}
	
	private void signin(String username,String password){
		ReturnData connectionStatus = __connection.checkConnectionStatus();
		if(connectionStatus.status()){
			__setEnabled(false);
			ConnectionEntity ce = new ConnectionEntity(__context);
			Map<String, String> data = new HashMap<String, String>(2);
			data.put("username",username);
			data.put("password",password);
	//		ce.setFormData(data);
			ce.setURL("agent");
			ce.get();
			ce.fakeLogin();
			ce.acceptJSON(false);
			ce.acceptGZIPEncoding(false);
			ce.setConnectionQueueCallback(new ConnectionQueueCallback() {
				@Override
				public void response(int returnCode, final String response) {
					__setEnabled(true);
					if(returnCode == -1){
						if(response.length()>0){
							__showAlert(response);
						} else {
							__showAlert("Unknown error, please try again.");
						}
						return;
					}
					JSONObject json;
					try {
						if(response.length()>0){
							json = new JSONObject(response);
							boolean success = json.getBoolean("success");
							if(success){
								__showAlert(response);
								__landing.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										__username.setText("");
										__password.setText("");
									}
								});
							} else {
								String message = json.getString("message");
								__showAlert(message);
							}
						} else {
							__showAlert("Please try again.");
						}
					} catch (Exception e) {
						__showAlert(e.toString());
					}
				}
				@Override
				public void response(int returnCode, byte[] response) {
				}
			});
			ConnectionQueue cq = ConnectionQueue.getInstance();
			cq.setProgressBarIndeterminateCallback(__landing);
			try {
				cq.addConnection(ce);
			} catch (IOException e) {
				__setEnabled(true);
				__showAlert(e.toString());
			} catch (InterruptedException e) {
				__setEnabled(true);
				__showAlert(e.toString());
			}
		} else {
			__showAlert(connectionStatus.result());
		}
	}
	
	private void __setEnabled(final boolean enabled){
		__landing.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				__username.setEnabled(enabled);
				__password.setEnabled(enabled);
				__signinButton.setEnabled(enabled);
			}
		});
		
	}
}
