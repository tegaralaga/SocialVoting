package me.tegaralaga.socialvoting.adapter;

public class Timeline {

	private int __id;
	private int __type;
	private boolean __includeImages;
	private boolean __matureContent;
	private Created __created;
	private User __user;
	private Category __category;
	
	public Timeline(int id,int type,boolean includeImages,boolean matureContent){
		__id = id;
		__type = type;
		__includeImages = includeImages;
		__matureContent = matureContent;
	}
	
	public int id(){
		return __id;
	}
	
	public int type(){
		return __type;
	}
	
	public boolean includeImages(){
		return __includeImages;
	}
	
	public boolean matureContent(){
		return __matureContent;
	}
	
	public void setCreated(Created created){
		__created = created;
	}
	
	public void setCreated(long timestamp,String timespan){
		__created = new Created(timestamp, timespan);
	}
	
	public Created created(){
		return __created;
	}
	
	public void setCategory(Category category){
		__category = category;
	}
	
	public void setCategory(int id,String name){
		__category = new Category(id, name);
	}
	
	public Category category(){
		return __category;
	}
	
	public void setUser(User user){
		__user = user;
	}
	
	public User user(){
		return __user;
	}
	
	public class Created{
		
		private long __timestamp;
		private String __timespan;
		
		public Created(long timestamp,String timespan){
			__timestamp = timestamp;
			__timespan = timespan;
		}
		
		public long timestamp(){
			return __timestamp;
		}
		
		public String timespan(){
			return __timespan;
		}
		
	}
	
	public class Category{
		
		private int __id;
		private String __name;
		
		public Category(int id,String name){
			__id = id;
			__name = name;
		}
		
		public int id(){
			return __id;
		}
		
		public String name(){
			return __name;
		}
		
	}
	
}
