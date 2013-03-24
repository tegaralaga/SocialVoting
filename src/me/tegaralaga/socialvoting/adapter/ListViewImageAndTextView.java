package me.tegaralaga.socialvoting.adapter;

import android.graphics.Bitmap;

public class ListViewImageAndTextView {

	public final static int RESOURCES = 1;
	public final static int BITMAP = 2;
	private int __type=1;
	private int __res;
	private Bitmap __bitmap=null;
	private String __text;
	
	public ListViewImageAndTextView(int res,String text){
		__res = res;
		__text = text;
	}
	
	public ListViewImageAndTextView(Bitmap bitmap,String text){
		__bitmap = bitmap;
		__text = text;
	}
	
	public int getResource(){
		return __res;
	}
	
	public Bitmap getBitmap(){
		return __bitmap;
	}
	
	public String getText(){
		return __text;
	}	
	
	public void setType(int type){
		__type = type;
	}
	
	public int getType(){
		if(__bitmap == null){
			return __type;			
		} else {
			return 2;
		}
	}
	
}