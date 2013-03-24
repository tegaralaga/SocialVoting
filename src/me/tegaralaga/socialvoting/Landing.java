package me.tegaralaga.socialvoting;

import java.util.HashMap;

import me.tegaralaga.socialvoting.callback.ProgressBarIndeterminateCallback;
import me.tegaralaga.socialvoting.fragments.FragmentSignIn;
import me.tegaralaga.socialvoting.fragments.FragmentSignUp;
import me.tegaralaga.socialvoting.settings.BasicNetworkConnection;
import me.tegaralaga.socialvoting.utils.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class Landing extends SherlockFragmentActivity implements ProgressBarIndeterminateCallback {
	
	private ActionBar __ab;
	private TabHost __th;
	private TabManager __tm;
	private boolean __isWorking=false;
	public static Utils __utils;
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		__ab = getSupportActionBar();
		__ab.setDisplayShowTitleEnabled(false);
		__ab.setBackgroundDrawable( new ColorDrawable(Color.parseColor("#5EBDCF")));
		setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.tab_bottom_layout);
		__th = (TabHost)findViewById(android.R.id.tabhost);
		__th.setup();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    __th.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
			__th.getTabWidget().setDividerPadding(0);
		} else {
			__th.getTabWidget().setDividerDrawable(getResources().getDrawable(R.drawable.tab_divider));
		}
		__tm = new TabManager(this,__th,android.R.id.tabcontent,this);
		__tm.addTab(createTab("signin","Sign In"),FragmentSignIn.class,null);
		__tm.addTab(createTab("signup","Sign Up"),FragmentSignUp.class,null);
		if (savedInstanceState != null) {
            __th.setCurrentTabByTag(savedInstanceState.getString("current_tab"));
        }
		__utils = new Utils(getApplicationContext());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.landing, menu);
		return (super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onPrepareOptionsMenu (final Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_resend_code:
		break;
		case R.id.menu_reset_password:
		break;
		case R.id.menu_about:
			startActivity(new Intent(getApplicationContext(), About.class));
		break;
		case R.id.menu_network_connection:
			startActivity(new Intent(getApplicationContext(), BasicNetworkConnection.class));
		break;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current_tab", __th.getCurrentTabTag());
    }
	
	private TabSpec createTab(final String tag,final String title){
        final View tab = LayoutInflater.from(__th.getContext()).inflate(R.layout.landing_tab, null);
        ((TextView)tab.findViewById(R.id.text)).setText(title);
        return __th.newTabSpec(tag).setIndicator(tab);
    }
	
	public static class TabManager implements TabHost.OnTabChangeListener {
		
		private final Landing __landing;
		private final FragmentActivity __fa;
        private final TabHost __th;
        private final int __containerID;
        private final HashMap<String, TabInfo> __ti = new HashMap<String, TabInfo>();
        TabInfo __lastTab;

        static final class TabInfo {
        	
            private final String __tag;
            private final Class<?> __class;
            private final Bundle __args;
            private Fragment __fragment;

            TabInfo(String tag, Class<?> clss, Bundle args) {
                __tag = tag;
                __class = clss;
                __args = args;
            }
            
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;
            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId, Landing landing) {
        	__landing = landing;
            __fa = activity;
            __th = tabHost;
            __containerID = containerId;
            __th.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(__fa));
            String tag = tabSpec.getTag();
            TabInfo info = new TabInfo(tag, clss, args);
            info.__fragment = __fa.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.__fragment != null && !info.__fragment.isDetached()) {
                FragmentTransaction ft = __fa.getSupportFragmentManager().beginTransaction();
                ft.detach(info.__fragment);
                ft.commit();
            }
            __ti.put(tag, info);
            __th.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
        	if(__landing.__isWorking == false){
        		FragmentSignUp.__isAvatarProvided = false;
	            TabInfo newTab = __ti.get(tabId);
	            if (__lastTab != newTab) {
	                FragmentTransaction ft = __fa.getSupportFragmentManager().beginTransaction();
	                if (__lastTab != null) {
	                    if (__lastTab.__fragment != null) {
	                        ft.detach(__lastTab.__fragment);
	                    }
	                }
	                if (newTab != null) {
	                    if (newTab.__fragment == null) {
	                        newTab.__fragment = Fragment.instantiate(__fa,newTab.__class.getName(), newTab.__args);
	                        ft.add(__containerID, newTab.__fragment, newTab.__tag);
	                    } else {
	                        ft.attach(newTab.__fragment);
	                    }
	                }
	                __lastTab = newTab;
	                ft.commit();
	                __fa.getSupportFragmentManager().executePendingTransactions();
	            }
        	}
        }
    }

	@Override
	public void startProgressBarIndeterminate() {
		__isWorking = true;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				__th.getTabWidget().setEnabled(false);
				setSupportProgressBarIndeterminateVisibility(true);
			}
		});		
	}

	@Override
	public void stopProgressBarIndeterminate() {
		__isWorking = false;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				__th.getTabWidget().setEnabled(true);	
				setSupportProgressBarIndeterminateVisibility(false);
			}
		});
	}
	
}
