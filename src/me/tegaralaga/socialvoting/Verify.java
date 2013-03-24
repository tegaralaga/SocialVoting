package me.tegaralaga.socialvoting;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import me.tegaralaga.socialvoting.utils.Connection;
import me.tegaralaga.socialvoting.utils.Session;
import me.tegaralaga.socialvoting.utils.Utils;
import me.tegaralaga.socialvoting.utils.Utils.ReturnData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class Verify extends SherlockActivity{
	
	private ActionBar __ab;
	private Intent __intent;
	private Bundle __bundle;
	private EditText __code;
	private TextView __alert;
	private Button __verify;
	private String __after,__email;
	private Connection __conn;
	private Session __session;
	private static VerifyCode __verifyCode;
	private Utils __utils;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verify);
		__ab = getSupportActionBar();
		__ab.setBackgroundDrawable( new ColorDrawable(Color.parseColor("#5EBDCF")));
		setSupportProgressBarIndeterminateVisibility(false);
		__conn = new Connection(getApplicationContext());
		__session = new Session(getApplicationContext());
		__utils = new Utils(getApplicationContext());
		__code = (EditText)findViewById(R.id.code);
		__alert = (TextView)findViewById(R.id.alert);
		__verify = (Button)findViewById(R.id.verify);
		__intent = this.getIntent();
		__bundle = __intent.getExtras();
		__verifyCode = new VerifyCode();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(__bundle == null){
			onBackPressed();
		} else {
			__after = __bundle.getString("after");
			__email = __bundle.getString("email");
			__alert.setText("Please check your "+__email+" Inbox/Spam/Junk Folder for verification code");
			__verify.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String code = __code.getText().toString().trim();
					if(code.length()==6){
						if(__verifyCode.getStatus() != AsyncTask.Status.RUNNING){
							__verifyCode.execute(code,__email,__after);
						} else {
							__utils.toast("Task already running, please wait.");
						}
					} else {
						__utils.toast("Code must be 6 characters");
					}
				}
			});
		}
	}
	
	public class VerifyCode extends AsyncTask<String, Void, Object>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			__code.setEnabled(false);
			__verify.setEnabled(false);
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Object doInBackground(String... params) {
			ReturnData result = new ReturnData(false,null);
			String code = params[0];
			String email = params[1];
			String type = params[2];
			if(__conn.isOnline()){
				HttpRequest request=HttpRequest.post(__conn.server_url("session/verify"));
				request.userAgent(Connection.androidCustomUserAgent());
				request.acceptJson();
				request.acceptGzipEncoding().uncompress(true);
				if(__session.https_mode() == 1 || __session.https_mode() == 2){
					request.trustAllCerts();
					request.trustAllHosts();
				}
				Map<String, String> data = new HashMap<String,String>(3);
				data.put("type",type);
				data.put("email",email);
				data.put("code",code);
				request.form(data);
				try {
					if(request.code()==200){
						String body = request.body();
						result.setStatus(true);
						result.setResult(body);
					} else {
						result.setResult("Server currently on trouble, please try again in a few seconds (or minutes (or hours)).");
					}
				} catch (HttpRequestException e) {
					result.setResult(e.getMessage());
				} catch (Exception e) {
					result.setResult(e.getMessage());
				}
			} else {
				result.setResult("Internet connection is unavailable");
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Object object){
			super.onPostExecute(object);
			__verifyCode = new VerifyCode();
			__code.setEnabled(true);
			__verify.setEnabled(true);
			setSupportProgressBarIndeterminateVisibility(false);
			ReturnData result = (ReturnData) object;
			if(result.status()){
				try {
					JSONObject json = new JSONObject(result.result());
					boolean success = json.getBoolean("success");
					if(success){
						__utils.toast("Verification code success, please sign in.");
						Intent intent = new Intent(getApplicationContext(),Landing.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					} else {
						__utils.toast(json.getString("message"));
					}
				} catch (Exception e) {
					__utils.toast(e.getMessage());
				}
			} else {
				__utils.toast(result.result());
			}
		}
		
	}
	
}
