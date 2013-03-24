package me.tegaralaga.socialvoting.utils;

import java.io.File;

import android.os.Environment;

public class FileHelper {
	
	public final static String SOCIAL_VOTING_DIRECTORY = ".SOCIALVOTING";
	public final static String SOCIAL_VOTING_DIRECTORY_TMP = ".SOCIALVOTING/.tmp";
	
	public final static boolean isSDCardPresent(){
		try {
			return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			return false;
		}
	}
	
	public final static boolean isAppsDirectoryExists(){
		try {
			File social_voting_directory = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY);
			if(social_voting_directory.isDirectory() && social_voting_directory.exists() && social_voting_directory.canWrite()){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public final static boolean createAppsDirectory(){
		try {
			File social_voting_directory = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY);
			if(social_voting_directory.exists()){
				return true;
			} else {
				if(social_voting_directory.mkdirs()){
					File social_voting_directory_tmp = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY_TMP);
					if(social_voting_directory_tmp.exists()){
						return true;
					} else {
						if(social_voting_directory_tmp.mkdirs()){
							File social_voting_nomedia = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY+"/.nomedia");
							if(social_voting_nomedia.exists()){
								return true;
							} else {
								return social_voting_nomedia.createNewFile();
							}
						} else {
							return false;
						}
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public final static boolean createUserDirectory(String username){
		if(createAppsDirectory()){
			File user_directory = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY+"/"+username);
			if(user_directory.exists()){
				return true;
			} else {
				return user_directory.mkdirs();
			}
		} else {
			return false;
		}
	}
	
	public final static boolean createUserAvatarDirectory(String username){
		if(createUserDirectory(username)){
			File user_avatar_directory = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY+"/"+username+"/avatar");
			if(user_avatar_directory.exists()){
				return true;
			} else {
				return user_avatar_directory.mkdirs();
			}
		} else {
			return false;
		}
	}
	
	public final static File userDirectory(String username){
		File user_directory = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY+"/"+username);
		if(user_directory.exists()){
			return user_directory;
		} else {
			return null;
		}
	}
	
	public final static String userDirectoryString(String username){
		File user_directory = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY+"/"+username);
		if(user_directory.exists()){
			return user_directory.toString();
		} else {
			return null;
		}
	}
	
	public final static File userAvatarDirectory(String username){
		File user_avatar_directory = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY+"/"+username+"/avatar");
		if(user_avatar_directory.exists()){
			return user_avatar_directory;
		} else {
			return null;
		}
	}
	
	public final static String userAvatarDirectoryString(String username){
		File user_avatar_directory = new File(Environment.getExternalStorageDirectory()+"/"+SOCIAL_VOTING_DIRECTORY+"/"+username+"/avatar");
		if(user_avatar_directory.exists()){
			return user_avatar_directory.toString();
		} else {
			return null;
		}
	}

}
