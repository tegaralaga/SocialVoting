package me.tegaralaga.socialvoting.adapter;

public class User {
	
	private int __id;
	private int __age;
	private int __gender;
	private int __level;
	private String __username;
	private String __email;
	private String __fullname;
	private String __birthday;
	private String __token;
	private LastSeen __lastseen;
	private Avatar __avatar;
	
	// For the purpose of active user
	public User(int id,String username,String email,String fullname,String birthday,int age,int gender,int level,String token){
		__id = id;
		__username = username;
		__email = email;
		__fullname = fullname;
		__birthday = birthday;
		__age = age;
		__gender = gender;
		__level = level;
		__token = token;
	}
	
	// For the purpose of timeline user
	public User(int id,String username,String fullname,int gender,int level){
		__id = id;
		__username = username;
		__fullname = fullname;
		__gender = gender;
		__level = level;
	}
	
	public int id(){
		return __id;
	}
	
	public int age(){
		return __age;
	}
	
	public int gender(){
		return __gender;
	}
	
	public int level(){
		return __level;
	}
	
	public String username(){
		return __username;
	}
	
	public String email(){
		return __email;
	}
	
	public String fullname(){
		return __fullname;
	}
	
	public String birthday(){
		return __birthday;
	}
	
	public String token(){
		return __token;
	}
	
	public LastSeen lastseen(){
		return __lastseen;
	}
	
	public Avatar avatar(){
		return __avatar;
	}
	
	public void setLastSeen(long timestamp,String timespan){
		__lastseen = new LastSeen(timestamp, timespan);
	}
	
	public void setAvatar(String avatar){
		__avatar = new Avatar(avatar);
	}
	
	// Avatar holder
	public class Avatar {
		
		private String __avatar;
		
		public Avatar(String avatar){
			__avatar = avatar;
		}
		
		public String normal(){
			return __avatar;
		}
		
		public String small(){
			return __avatar;
		}
		
		public String medium(){
			return __avatar;
		}
		
		public String large(){
			return __avatar;
		}
		
	}
	
	public class LastSeen {

		private long __timestamp;
		private String __timespan;
		
		public LastSeen(long timestamp,String timespan){
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
	
}
