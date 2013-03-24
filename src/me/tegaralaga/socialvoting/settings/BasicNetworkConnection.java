package me.tegaralaga.socialvoting.settings;

import java.util.ArrayList;
import me.tegaralaga.socialvoting.R;
import me.tegaralaga.socialvoting.adapter.ExpandablePreferenceList;
import me.tegaralaga.socialvoting.adapter.ExpandablePreferencesListAdapter;
import me.tegaralaga.socialvoting.adapter.ExpandablePreferenceListChild;
import me.tegaralaga.socialvoting.utils.Session;
import me.tegaralaga.socialvoting.utils.Utils;
import me.tegaralaga.socialvoting.view.CustomExpandableListView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class BasicNetworkConnection extends SherlockActivity {
	
	private ActionBar _ab;
	private ArrayList<ExpandablePreferenceList> _preferencesList = new ArrayList<ExpandablePreferenceList>();
	private ExpandablePreferencesListAdapter _adapter;
	private CustomExpandableListView _preferences;
	private Utils _utils;
	private Session _session;
	private int _lastGroupExpanded=69;
	private ExpandablePreferenceList _preferences_https_mode;
	private ExpandablePreferenceList _preferences_connect_timeout;
	private ExpandablePreferenceList _preferences_read_timeout;
	private ExpandablePreferenceList _preferences_network_mode;
	private ExpandablePreferenceList _preferences_mobile_network;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_network_connection);
		_ab = getSupportActionBar();
		_ab.setBackgroundDrawable( new ColorDrawable(Color.parseColor("#5EBDCF")));
		_ab.setHomeButtonEnabled(true);
		_ab.setDisplayHomeAsUpEnabled(true);
		_ab.setDisplayShowTitleEnabled(false);
		_preferencesList.clear();
		_preferencesList.trimToSize();
		_utils = new Utils(getApplicationContext());
		_session = new Session(getApplicationContext());
		_adapter = new ExpandablePreferencesListAdapter(getApplicationContext());
		_preferences = (CustomExpandableListView)findViewById(R.id.preferences);
		_preferences.setExpanded(true);
		_preferences.setGroupIndicator(null);
		_adapter.setGroup(_preferencesList);
		_preferences.setAdapter(_adapter);
		_preferences.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,int groupPosition, long id) {
				if(groupPosition != _lastGroupExpanded){
		            parent.collapseGroup(_lastGroupExpanded);
		        }
				parent.expandGroup(groupPosition);
		        parent.smoothScrollToPosition(groupPosition);
		        if(_lastGroupExpanded == groupPosition){
		        	parent.collapseGroup(groupPosition);
		        	groupPosition = 69;
		        }
		        _lastGroupExpanded = groupPosition;
			    return true;
			}
		});
		_preferences_https_mode = new ExpandablePreferenceList("HTTPS Mode",_session.https_mode());
		_preferences_connect_timeout = new ExpandablePreferenceList("Connect Timeout (Seconds)",_session.connect_timeout(),15);
		_preferences_read_timeout = new ExpandablePreferenceList("Read Timeout (Seconds)",_session.read_timeout(),30);
		_preferences_network_mode = new ExpandablePreferenceList("Network Mode",_session.selectedNetworkType());
		_preferences_mobile_network = new ExpandablePreferenceList("Mobile Network",_session.selectedMobileNetworkType());
		ArrayList<ExpandablePreferenceListChild> preference_https_mode_child = new ArrayList<ExpandablePreferenceListChild>(3);
		ArrayList<ExpandablePreferenceListChild> preference_connect_timeout_child = new ArrayList<ExpandablePreferenceListChild>(1);
		ArrayList<ExpandablePreferenceListChild> preference_read_timeout_child = new ArrayList<ExpandablePreferenceListChild>(1);
		ArrayList<ExpandablePreferenceListChild> preference_network_mode_child = new ArrayList<ExpandablePreferenceListChild>(3);
		ArrayList<ExpandablePreferenceListChild> preference_mobile_network_child = new ArrayList<ExpandablePreferenceListChild>(6);
		preference_https_mode_child.add(new ExpandablePreferenceListChild("Always HTTPS"));
		preference_https_mode_child.add(new ExpandablePreferenceListChild("On Post Data"));
		preference_https_mode_child.add(new ExpandablePreferenceListChild("Disable HTTPS"));
		preference_read_timeout_child.add(new ExpandablePreferenceListChild("Read Timeout"));
		preference_connect_timeout_child.add(new ExpandablePreferenceListChild("Connect Timeout"));
		preference_network_mode_child.add(new ExpandablePreferenceListChild("Mobile Data Only"));
		preference_network_mode_child.add(new ExpandablePreferenceListChild("WiFi Only"));
		preference_network_mode_child.add(new ExpandablePreferenceListChild("WiFi + Mobile Data"));
		preference_mobile_network_child.add(new ExpandablePreferenceListChild("GPRS"));
		preference_mobile_network_child.add(new ExpandablePreferenceListChild("EDGE"));
		preference_mobile_network_child.add(new ExpandablePreferenceListChild("3G"));
		preference_mobile_network_child.add(new ExpandablePreferenceListChild("3.5G"));
		preference_mobile_network_child.add(new ExpandablePreferenceListChild("3.75G"));
		preference_mobile_network_child.add(new ExpandablePreferenceListChild("4G"));
		_preferences_https_mode.setChild(preference_https_mode_child);
		_preferences_https_mode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioButton radio = (RadioButton) v;
				int value = Integer.valueOf((String)radio.getContentDescription());
				_preferences_https_mode.setValue(value);
				_adapter.notifyDataSetChanged();
				_session.https_mode(value);
			}
		});
		_preferences_connect_timeout.setChild(preference_connect_timeout_child);
		_preferences_connect_timeout.setChildType(ExpandablePreferenceList.SEEKBAR);
		_preferences_connect_timeout.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar){}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar){}
			@Override
			public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser) {
				if(progress==0){
					progress = 1;
				}
				_session.connect_timeout(progress*1000);
				_preferences_connect_timeout.setValue(_session.connect_timeout());
				_adapter.notifyDataSetChanged();
			}
		});
		_preferences_read_timeout.setChild(preference_read_timeout_child);
		_preferences_read_timeout.setChildType(ExpandablePreferenceList.SEEKBAR);
		_preferences_read_timeout.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar){}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar){}
			@Override
			public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser) {
				if(progress==0){
					progress = 1;
				}
				_session.read_timeout(progress*1000);
				_preferences_read_timeout.setValue(_session.read_timeout());
				_adapter.notifyDataSetChanged();
			}
		});
		_preferences_network_mode.setChild(preference_network_mode_child);
		_preferences_network_mode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioButton radio = (RadioButton) v;
				int value = Integer.valueOf((String)radio.getContentDescription());
				_preferences_network_mode.setValue(value);
				_adapter.notifyDataSetChanged();
				_session.selectedNetworkType(value);
			}
		});
		_preferences_mobile_network.setChild(preference_mobile_network_child);
		_preferences_mobile_network.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioButton radio = (RadioButton) v;
				int value = Integer.valueOf((String)radio.getContentDescription());
				_preferences_mobile_network.setValue(value);
				_adapter.notifyDataSetChanged();
				_session.selectedMobileNetworkType(value);
			}
		});
		_preferencesList.add(_preferences_https_mode);
		_preferencesList.add(_preferences_connect_timeout);
		_preferencesList.add(_preferences_read_timeout);
		_preferencesList.add(_preferences_network_mode);
		_preferencesList.add(_preferences_mobile_network);
		_adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.basic_network_connection, menu);
		return (super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
		break;
		case R.id.menu_reset:
			_session.clear();
			_preferences_https_mode.setValue(_session.https_mode());
			_preferences_connect_timeout.setValue(_session.connect_timeout());
			_preferences_read_timeout.setValue(_session.read_timeout());
			_preferences_network_mode.setValue(_session.selectedNetworkType());
			_preferences_mobile_network.setValue(_session.selectedMobileNetworkType());
			_adapter.notifyDataSetChanged();
			_utils.toast("Reset to Default");
		break;
		}
		return (super.onOptionsItemSelected(item));
	}
		
}