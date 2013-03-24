package me.tegaralaga.socialvoting.adapter;

import java.util.ArrayList;
import java.util.List;

import me.tegaralaga.socialvoting.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewImageAndTextViewAdapter extends BaseAdapter {
	
	private Context context;
	private List<ListViewImageAndTextView> data = new ArrayList<ListViewImageAndTextView>();
	
	public ListViewImageAndTextViewAdapter(Context context){
		this.context = context;
	}
	
	public void addItem(ListViewImageAndTextView add) {
		data.add(add);
	}
	
	public int getCount() {
		return data.size();
	}
	
	public Object getItem(int position) {
		return data.get(position);
	}
	
	public void setListItems(List<ListViewImageAndTextView> list) {
		data = list;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int pos, View cv, ViewGroup vg){
		ViewHolder holder = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(cv == null){
			cv = inflater.inflate(R.layout.listview_image_and_textview,null,false);
			holder = new ViewHolder(cv);
			cv.setTag(holder);
		} else {
			holder = (ViewHolder) cv.getTag();
		}
		ListViewImageAndTextView content = data.get(pos);
		if(content.getType()==ListViewImageAndTextView.RESOURCES){
			holder.image().setImageResource(content.getResource());
		} else {
			holder.image().setImageBitmap(content.getBitmap());
		}
		holder.text().setText(content.getText());
		return cv;
	}
	
	public class ViewHolder{
		
		private View __row;
		private TextView __text;
		private ImageView __image;
		public ViewHolder(View row){
			__row=row;
		}
		
		public TextView text(){
			__text=(TextView) __row.findViewById(R.id.text);
			return __text;
		}
		
		public ImageView image(){
			__image=(ImageView) __row.findViewById(R.id.image);
			return __image;
		}	
		
	}
	
}
