package me.tegaralaga.socialvoting.adapter;

import java.util.ArrayList;

import android.view.View.OnClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ExpandablePreferenceList {
	
	public final static String RADIOBUTTON="radiobutton";
	public final static String TOGGLE="toggle";
	public final static String SEEKBAR="seekbar";
	public final static String EDITTEXT="edittext";
	public final static String TEXTVIEW="textview";
	public final static int RADIOBUTTON_INT=1;
	public final static int TOGGLE_INT=2;
	public final static int SEEKBAR_INT=4;
	public final static int TEXTVIEW_INT=3;
	public final static int EDITTEXT_INT=5;
	private String _groupLabel;
	private String _groupPreferenceName;
	private String _childType=RADIOBUTTON;
	private int _childTypeInt=RADIOBUTTON_INT;
	private boolean _isEnabled = true;
	private int _value = 0;
	private int _maxValue = 10;
	private ArrayList<ExpandablePreferenceListChild> _childList = new ArrayList<ExpandablePreferenceListChild>();
	private OnSeekBarChangeListener _onSeekBarChangeListener=null;
	private OnClickListener _onClickListener=null;
	
	public ExpandablePreferenceList(String groupLabel,String groupPreferenceName,String childType,int value,boolean isEnabled){
		_groupLabel = groupLabel;
		_groupPreferenceName = groupPreferenceName;
		_childType = childType;
		_isEnabled = isEnabled;
		_value = value;
		_childList.clear();
		_childList.trimToSize();
	}
	
	public ExpandablePreferenceList(String groupLabel,String groupPreferenceName,String childType,int value){
		_groupLabel = groupLabel;
		_groupPreferenceName = groupPreferenceName;
		_childType = childType;
		_value = value;
		_childList.clear();
		_childList.trimToSize();
	}
	
	public ExpandablePreferenceList(String groupLabel,String groupPreferenceName,int value){
		_groupLabel = groupLabel;
		_groupPreferenceName = groupPreferenceName;
		_value = value;
		_childList.clear();
		_childList.trimToSize();
	}
	
	public ExpandablePreferenceList(String groupLabel,String groupPreferenceName){
		_groupLabel = groupLabel;
		_groupPreferenceName = groupPreferenceName;
		_childList.clear();
		_childList.trimToSize();
	}
	
	public ExpandablePreferenceList(String groupLabel,int value){
		_groupLabel = groupLabel;
		_value = value;
		_childList.clear();
		_childList.trimToSize();
	}
	
	public ExpandablePreferenceList(String groupLabel,int value,int maxValue){
		_groupLabel = groupLabel;
		_value = value;
		_maxValue = maxValue;
		_childList.clear();
		_childList.trimToSize();
	}
	
	public void setEnabled(boolean enabled){
		_isEnabled = enabled;
	}
	
	public void setValue(int value){
		_value = value;
	}
	
	public void setChildType(String childType){
		_childType = childType;
	}
	
	public boolean isEnabled(){
		return _isEnabled;
	}
	
	public String getLabel(){
		return _groupLabel;
	}
	
	public String getChildType(){
		return _childType;
	}
	
	public int getValue(){
		return _value;
	}
	
	public int getMaxValue(){
		return _maxValue;
	}
	
	public int getChildTypeInt(){
		int childTypeInt=3;
		if(_childType.equals(TOGGLE)){
			childTypeInt = 2;
		} else if (_childType.equals(RADIOBUTTON)){
			childTypeInt = 1;
		} else if (_childType.equals(SEEKBAR)){
			childTypeInt = 4;
		} else if (_childType.equals(EDITTEXT)){
			childTypeInt = 5;
		} else {
			childTypeInt = 3;
		}
		_childTypeInt = childTypeInt;
		return _childTypeInt;
	}
	
	public String getPreferenceName(){
		return _groupPreferenceName;
	}
	
	public void addChild(ExpandablePreferenceListChild child){
		_childList.add(child);
		_childList.trimToSize();
	}
	
	public void setChild(ArrayList<ExpandablePreferenceListChild> childList){
		_childList.clear();
		_childList = childList;
		_childList.trimToSize();
	}
	
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener){
		_onSeekBarChangeListener = listener;
	}
	
	public void setOnClickListener(OnClickListener listener){
		_onClickListener = listener;
	}
	
	public OnSeekBarChangeListener onSeekBarChangeListener(){
		return _onSeekBarChangeListener;
	}
	
	public OnClickListener onClickListener(){
		return _onClickListener;
	}
	
	public ArrayList<ExpandablePreferenceListChild> getChilds(){
		return _childList;
	}
	
	public ExpandablePreferenceListChild getChild(int childPosition){
		return _childList.get(childPosition);
	}
	
}
