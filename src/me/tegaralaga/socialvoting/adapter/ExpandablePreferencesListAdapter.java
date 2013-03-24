package me.tegaralaga.socialvoting.adapter;

import java.util.ArrayList;

import me.tegaralaga.socialvoting.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class ExpandablePreferencesListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private LayoutInflater _inflater;
	private ArrayList<ExpandablePreferenceList> _groupList = new ArrayList<ExpandablePreferenceList>();
	
	public ExpandablePreferencesListAdapter(Context context){
		_context = context;
		_inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_groupList.clear();
		_groupList.trimToSize();
	}
	
	public void setGroup(ArrayList<ExpandablePreferenceList> groupList){
		_groupList = groupList;
		_groupList.trimToSize();
	}
	
	public void addGroup(ExpandablePreferenceList group){
		_groupList.add(group);
		_groupList.trimToSize();
	}
	
	@Override
	public ExpandablePreferenceListChild getChild(int groupPosition, int childPosition) {
		return _groupList.get(groupPosition).getChild(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition,int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder = null;
		ExpandablePreferenceList group = getGroup(groupPosition);
		ExpandablePreferenceListChild child = getChild(groupPosition, childPosition);
		int layout_type = getChildType(groupPosition, childPosition);
		if(convertView == null){
			switch (layout_type) {
			case ExpandablePreferenceList.TOGGLE_INT:
				convertView = _inflater.inflate(R.layout.simple_expandable_list_view_child,null);
				break;
			case ExpandablePreferenceList.TEXTVIEW_INT:
				convertView = _inflater.inflate(R.layout.simple_expandable_list_view_child,null);
				break;
			case ExpandablePreferenceList.SEEKBAR_INT:
				convertView = _inflater.inflate(R.layout.seekbar_expandable_list_view_child,null);
				break;
			case ExpandablePreferenceList.EDITTEXT_INT:
				convertView = _inflater.inflate(R.layout.simple_expandable_list_view_child,null);
				break;
			default:
				convertView = _inflater.inflate(R.layout.checked_expandable_list_view_child,null);				
				break;
			}
			childViewHolder = new ChildViewHolder(convertView);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		switch (layout_type) {
		case ExpandablePreferenceList.TOGGLE_INT:
			childViewHolder.label().setText(child.getLabel());
			break;
		case ExpandablePreferenceList.TEXTVIEW_INT:
			childViewHolder.label().setText(child.getLabel());
			break;
		case ExpandablePreferenceList.SEEKBAR_INT:
			SeekBar seekbar = childViewHolder.seekbar();
			seekbar.setMax(group.getMaxValue());
			seekbar.setProgress(group.getValue()/1000);
			seekbar.setOnSeekBarChangeListener(group.onSeekBarChangeListener());
			childViewHolder.label().setText(String.valueOf(group.getValue()/1000));
			break;
		case ExpandablePreferenceList.EDITTEXT_INT:
			childViewHolder.label().setText(child.getLabel());
			break;
		default:
			if(childPosition == (group.getValue()-1)){
				childViewHolder.radio().setChecked(true);
			} else {
				childViewHolder.radio().setChecked(false);
			}
			childViewHolder.radio().setText(child.getLabel());
			childViewHolder.radio().setContentDescription(String.valueOf(childPosition+1));
			childViewHolder.radio().setOnClickListener(group.onClickListener());
		}
		convertView.setBackgroundResource(R.drawable.list_item_shape_middle_child);
		int child_size = getChildrenCount(groupPosition);
		if(child_size==1){
			if(groupPosition == (getGroupCount()-1)){
				convertView.setBackgroundResource(R.drawable.list_item_shape_bottom_child);
			}
		} else {
			if(isLastChild){
				if(groupPosition == (getGroupCount()-1)){
					convertView.setBackgroundResource(R.drawable.list_item_shape_bottom_child);
				}
			}
		}
		switch (layout_type) {
		case ExpandablePreferenceList.SEEKBAR_INT:
			convertView.setPadding(10,20,5,20);
			break;
		default:
			convertView.setPadding(10,5,5,5);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return _groupList.get(groupPosition).getChilds().size();
	}

	@Override
	public ExpandablePreferenceList getGroup(int groupPosition) {
		return _groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		_groupList.trimToSize();
		return _groupList.size();
	}
	
	@Override
	public int getChildTypeCount(){
		return 5;
	}
	
	@Override
    public int getChildType(int groupPosition, int childPosition){
		ExpandablePreferenceList group = getGroup(groupPosition);
		return group.getChildTypeInt();
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition,final boolean isExpanded,View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder = null;
		if(convertView == null){
			convertView = _inflater.inflate(R.layout.simple_expandable_list_view_group,null,false);
			groupViewHolder = new GroupViewHolder(convertView);
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		ExpandablePreferenceList group = getGroup(groupPosition);
		groupViewHolder.label().setText(group.getLabel());
		ImageView expand = groupViewHolder.expand();
		Drawable drawable = null;
		if(isExpanded){
			groupViewHolder.label().setTextColor(Color.parseColor("#333333"));
			drawable = _context.getResources().getDrawable(R.drawable.light_navigation_expand);
		} else {
			groupViewHolder.label().setTextColor(Color.parseColor("#666666"));
			drawable = _context.getResources().getDrawable(R.drawable.light_navigation_next_item);
		}
		expand.setImageDrawable(drawable);
		int size = getGroupCount();
		int last = size-1;
		if(size == 1){
			if(isExpanded){
				convertView.setBackgroundResource(R.drawable.list_item_shape_top_selected);
			} else {
				convertView.setBackgroundResource(R.drawable.list_item_selector_all);
			}
		} else {
			if(groupPosition==0){
				if(isExpanded){
					convertView.setBackgroundResource(R.drawable.list_item_shape_top_selected);
				} else {
					convertView.setBackgroundResource(R.drawable.list_item_selector_top);
				}
			} else if (groupPosition==last) {
				if(isExpanded){
					convertView.setBackgroundResource(R.drawable.list_item_shape_middle_selected);
				} else {
					convertView.setBackgroundResource(R.drawable.list_item_selector_bottom);
				}
			} else {
				if(isExpanded){
					convertView.setBackgroundResource(R.drawable.list_item_shape_middle_selected);
				} else {
					convertView.setBackgroundResource(R.drawable.list_item_selector_middle);
				}
			}
		}
		convertView.setPadding(5,5,5,5);
		return convertView;
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return _groupList.get(groupPosition).isEnabled();
	}
	
	@Override
	public boolean areAllItemsEnabled() {
	    return true;
	}
	
	public class GroupViewHolder{
		
		private View _groupViewHolder;
		private TextView _label;
		private ImageView _expand;
		
		public GroupViewHolder(View viewHolder){
			_groupViewHolder = viewHolder;
		}
		
		public TextView label(){
			_label = (TextView)_groupViewHolder.findViewById(R.id.label);
			return _label;
		}
		
		public ImageView expand(){
			_expand = (ImageView)_groupViewHolder.findViewById(R.id.expand);
			return _expand;
		}
	}
	
	public class ChildViewHolder{
		private View _childViewHolder;
		private TextView _label;
		private RadioButton _radio;
		private SeekBar _seekBar;
		
		public ChildViewHolder(View viewHolder){
			_childViewHolder = viewHolder;
		}
		
		public TextView label(){
			_label = (TextView)_childViewHolder.findViewById(R.id.label);
			return _label;
		}
		
		public RadioButton radio(){
			_radio = (RadioButton)_childViewHolder.findViewById(R.id.radio);
			return _radio;
		}
		
		public SeekBar seekbar(){
			_seekBar = (SeekBar)_childViewHolder.findViewById(R.id.seekbar);
			return _seekBar;
		}
	}

}
