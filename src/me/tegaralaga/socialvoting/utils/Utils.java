package me.tegaralaga.socialvoting.utils;

import java.util.Calendar;

import me.tegaralaga.socialvoting.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {
	
	public final static int VOTE = 1;
	public final static int POLL = 2;
	public final static String PACIFICO = "pacifico.ttf";
	
	private Context __context;	
	private DisplayMetrics __dm;
	private Toast __toast;
	private LayoutInflater __inflater;
	private View __toast_layout;
	private TextView __toast_text;
	
	public Utils(Context context){
		__context = context;
		__dm = __context.getResources().getDisplayMetrics();
		__toast = new Toast(__context);
		__inflater = (LayoutInflater)__context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toast_layout = __inflater.inflate(R.layout.toast,null);
		__toast_layout = toast_layout.findViewById(R.id.toast);
		__toast_text = (TextView)__toast_layout.findViewById(R.id.text);
		__toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
		__toast.setDuration(Toast.LENGTH_SHORT);
		__toast.setView(toast_layout);
	}
	
	public static boolean isValidEmail(String email){
		if(email == null){
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
	}
	
	public int dp_to_px(int dp){
		return (int)((dp * __dm.density) + 0.5);
	}
	
	public int px_to_dp(int px){
		return (int) ((px/__dm.density)+0.5);
	}
	
	public void screen_size(){
		int screen_layout = __context.getResources().getConfiguration().screenLayout;
		if((screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) ==  Configuration.SCREENLAYOUT_SIZE_SMALL){
			toast("SMALL");
		} else if ((screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) ==  Configuration.SCREENLAYOUT_SIZE_NORMAL){
			toast("NORMAL");
		} else if ((screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK) ==  Configuration.SCREENLAYOUT_SIZE_LARGE){
			toast("LARGE");
		} else {
			toast("XLARGE");
		}
	}
	
	public void changeTypeFace(String font, TextView textView){
		Typeface typeface = Typeface.createFromAsset(__context.getAssets(),"fonts/"+font);
		textView.setTypeface(typeface);
	}
	
	public void toast(String text){
		Toast.makeText(__context, text, Toast.LENGTH_SHORT).show();
	}
	
	public void pop(String text){
		__toast_text.setText(text);
		__toast.show();
	}
	
	public void toast(String text, boolean long_duration){
		if(long_duration){
			Toast.makeText(__context, text, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(__context, text, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public static class ReturnData{
		
		private boolean __status;
		private String __result;
		
		public ReturnData(boolean status,String result){
			__status = status;
			__result = result;
		}
		
		public void setStatus(boolean status){
			__status = status;
		}
		
		public void setResult(String result){
			__result = result;
		}
		
		public boolean status(){
			return __status;
		}
		
		public String result(){
			return __result;
		}
	}
	
	public static void datePickerDialog(final Context context,LayoutInflater inflater,final EditText birthday){
		final Dialog dialog = new Dialog(context,R.style.DialogNoTitle);
		final View view = inflater.inflate(R.layout.dialog_date_picker,null);
		final DatePicker dp = (DatePicker) view.findViewById(R.id.date_picker);
		final Button set = (Button) view.findViewById(R.id.set_date);
		Calendar calendar = Calendar.getInstance();
		final int currentYear = calendar.get(Calendar.YEAR);
		final int currentMonth = calendar.get(Calendar.MONTH);
		final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		if(birthday.getText().toString().trim().length()==0){
			dp.init(currentYear,currentMonth,currentDay,new OnDateChangedListener() {
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
					if(year > currentYear){
						view.updateDate(currentYear, monthOfYear, dayOfMonth);
					}
					if(monthOfYear > currentMonth && year >= currentYear){
						view.updateDate(currentYear, currentMonth, dayOfMonth);
					}
					if(dayOfMonth > currentDay && monthOfYear >= currentMonth && year >= currentYear){
						view.updateDate(currentYear, currentMonth, currentDay);
					}
				}
			});
		} else {
			String[] string = birthday.getText().toString().trim().split("/");
			int selectedDay = Integer.parseInt(string[0]);
			int selectedMonth = Integer.parseInt(string[1])-1;
			int selectedYear = Integer.parseInt(string[2]);
			dp.init(selectedYear,selectedMonth,selectedDay,new OnDateChangedListener() {
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
					if(year > currentYear){
						view.updateDate(currentYear, monthOfYear, dayOfMonth);
					}
					if(monthOfYear > currentMonth && year >= currentYear){
						view.updateDate(currentYear, currentMonth, dayOfMonth);
					}
					if(dayOfMonth > currentDay && monthOfYear >= currentMonth && year >= currentYear){
						view.updateDate(currentYear, currentMonth, currentDay);
					}
				}
			});			
		}
		set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar now = Calendar.getInstance();
				Calendar then = Calendar.getInstance();
				then.clear();
				then.set(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
				int diff = now.get(Calendar.YEAR) - then.get(Calendar.YEAR);
				if(then.get(Calendar.MONTH) > now.get(Calendar.MONTH) || (now.get(Calendar.MONTH) == then.get(Calendar.MONTH) && then.get(Calendar.DATE) > now.get(Calendar.DATE))){
					diff--;
				}
				if(diff>=13){
					String day = Integer.toString(dp.getDayOfMonth());
					String month = Integer.toString(dp.getMonth()+1);
					String year = Integer.toString(dp.getYear());
					if(day.length()==1){
						day = "0"+day;
					}
					if(month.length()==1){
						month = "0"+month;
					}
					birthday.setText(day+"/"+month+"/"+year);
					dialog.dismiss();
				} else {
					Toast.makeText(context, "You must be at least 13 years old to sign up.",Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(new OnCancelListener() {						
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog = null;
			}
		});
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.show();
	}
}