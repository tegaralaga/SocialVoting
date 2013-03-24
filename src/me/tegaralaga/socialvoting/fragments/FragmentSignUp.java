package me.tegaralaga.socialvoting.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import me.tegaralaga.socialvoting.Landing;
import me.tegaralaga.socialvoting.R;
import me.tegaralaga.socialvoting.Verify;
import me.tegaralaga.socialvoting.adapter.ListViewImageAndTextView;
import me.tegaralaga.socialvoting.adapter.ListViewImageAndTextViewAdapter;
import me.tegaralaga.socialvoting.utils.Connection;
import me.tegaralaga.socialvoting.utils.FileHelper;
import me.tegaralaga.socialvoting.utils.ImageHelper;
import me.tegaralaga.socialvoting.utils.Utils;
import me.tegaralaga.socialvoting.utils.Utils.ReturnData;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class FragmentSignUp extends SherlockFragment {
	
	private Landing __landing;
	private int SELECT_AVATAR_FROM_GALLERY = 69;
	private int CROP_AVATAR = 70;
	private Context context;
	private View __view;
	private ImageView __take_avatar,__take_avatar_button,__birthday_button;
	private EditText __usernameBox,__emailBox,__passwordBox,__fullnameBox,__birthdayBox,__retypePasswordBox;
	private RadioGroup __gender;
	private Bitmap __avatar;
	private Dialog __dialog = null;
	private Utils __utils;
	private Button __signup_button;
	private LayoutInflater __inflater;
	public static boolean __isAvatarProvided = false;
	private Uri __selectedImageUri = null;
	private Connection __conn;
	public static SignUpTask __signup;
	private static String __avatarFilePath = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		__inflater=inflater;
		__view = __inflater.inflate(R.layout.signup,container,false);
		__take_avatar = (ImageView)__view.findViewById(R.id.take_avatar);
		__take_avatar_button = (ImageView)__view.findViewById(R.id.take_avatar_button);
		__usernameBox = (EditText)__view.findViewById(R.id.username);
		__emailBox = (EditText)__view.findViewById(R.id.email);
		__fullnameBox = (EditText)__view.findViewById(R.id.fullname);
		__birthdayBox = (EditText)__view.findViewById(R.id.birthday);
		__gender = (RadioGroup)__view.findViewById(R.id.gender);
		__birthday_button = (ImageView)__view.findViewById(R.id.birthday_button);
		__passwordBox = (EditText)__view.findViewById(R.id.password);
		__retypePasswordBox = (EditText)__view.findViewById(R.id.retype_password);
		__avatar = BitmapFactory.decodeResource(context.getResources(),R.drawable.default_avatar);
		__signup_button = (Button)__view.findViewById(R.id.signup_button);
		__take_avatar.setImageBitmap(ImageHelper.getRoundedCornerBitmap(__avatar, 4));
		__take_avatar.setAdjustViewBounds(true);
		__take_avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(__dialog == null){
					__dialog = new Dialog(getSherlockActivity(),R.style.DialogNoTitle);
					View view = __inflater.inflate(R.layout.dialog_take_image,null);
					ListView lv = (ListView)view.findViewById(R.id.dialog_take_image);
					final ListViewImageAndTextViewAdapter adapter = new ListViewImageAndTextViewAdapter(context);
					if(__isAvatarProvided){
						adapter.addItem(new ListViewImageAndTextView(R.drawable.light_content_remove,"Remove Avatar"));
						adapter.addItem(new ListViewImageAndTextView(R.drawable.light_images_crop,"Crop Avatar"));
						lv.setAdapter(adapter);
						lv.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
								__dialog.dismiss();
								__dialog = null;
								Intent intent;
								switch (arg2) {
								case 0:
									Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.default_avatar);
									__take_avatar.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 4));
									__take_avatar.setAdjustViewBounds(true);
									__take_avatar.setScaleType(ScaleType.FIT_XY);
									__take_avatar_button.setVisibility(View.VISIBLE);
									__isAvatarProvided = false;
									__selectedImageUri = null;
									__avatar = bitmap;
								break;
								case 1:
									intent = new Intent("com.android.camera.action.CROP");
								    intent.setType("image/*");
								    List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,0);
								    int size = list.size();
								    if(size>0 && __selectedImageUri != null){
								    	intent.setData(__selectedImageUri);
									    intent.putExtra("outputX",150);
									    intent.putExtra("outputY",150);
									    intent.putExtra("crop", "true");
								        intent.putExtra("aspectX", 1);
								        intent.putExtra("aspectY", 1);
								        intent.putExtra("return-data",true);
								        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
								        ResolveInfo info = list.get(0);
								        intent.setComponent(new ComponentName(info.activityInfo.packageName,info.activityInfo.name));
								        startActivityForResult(intent,CROP_AVATAR);
								    } else {
								    	Toast.makeText(getSherlockActivity(), "Your device doesn't support image cropping, consider buy a new devices ;)",Toast.LENGTH_SHORT).show();
								    }
								break;
								}
							}
						});
					} else {
						adapter.addItem(new ListViewImageAndTextView(R.drawable.light_content_picture,"Import from Gallery"));
						adapter.addItem(new ListViewImageAndTextView(R.drawable.light_device_access_camera,"Take from Camera"));
						lv.setAdapter(adapter);
						lv.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
								__dialog.dismiss();
								__dialog = null;
								switch (arg2) {
								case 0:
									Intent intent = new Intent();
									intent.setType("image/*");
									intent.setAction(Intent.ACTION_GET_CONTENT);
									startActivityForResult(Intent.createChooser(intent,"Select Avatar"),SELECT_AVATAR_FROM_GALLERY);
								break;
								case 1:
								break;
								}
							}
						});
					}
					__dialog.setContentView(view);
					__dialog.setCancelable(true);
					__dialog.setCanceledOnTouchOutside(true);
					__dialog.setOnCancelListener(new OnCancelListener() {						
						@Override
						public void onCancel(DialogInterface dialog) {
							__dialog = null;
						}
					});
					__dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					__dialog.show();
				}
			}
		});
		__signup_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if(__signup.getStatus() != AsyncTask.Status.RUNNING){
//					__signup.execute(null,null,null);
//				}
//				String username = __usernameBox.getText().toString().trim();
//				String email = __emailBox.getText().toString().trim();
//				String fullname = __fullnameBox.getText().toString().trim();
//				String birthday = __birthdayBox.getText().toString().trim();
//				String password = __passwordBox.getText().toString().trim();
//				String retype_password = __retypePasswordBox.getText().toString().trim();
				final String username = "tegaralaga";
				final String email = "tegaralaga@live.com";
				final String fullname = "Bias Tegaralaga";
				final String birthday = "17/01/1990";
				final String password = "alastair";
				final String retype_password = "alastair";
				int radioButtonID = __gender.getCheckedRadioButtonId();
				String gndr = "male";
				if(radioButtonID == R.id.male){
					gndr = "male";
				} else {
					gndr = "female";
				}
				final String gender = gndr;
				if(username.length()>0){
					if(username.length()>=4 && username.length()<=16){
						if(Character.isDigit(username.charAt(0))){
							__utils.toast("First character must not be numeric characters, only alphabetical characters allowed.");
							__usernameBox.requestFocus();
						} else {
							if(username.startsWith("-") || username.startsWith("_") || username.endsWith("-") || username.endsWith("_")){
								__utils.toast("First character and last character must not be - or _ character.");
								__usernameBox.requestFocus();
							} else {
								if(email.length()>0){
									if(Utils.isValidEmail(email)){
										if(fullname.length()>0){
											if(fullname.length()>=2){
												if(birthday.length()==10){
													if(password.length()>0){
														if(password.length()>=8){
															if(retype_password.equals(password)){
																__usernameBox.setText(username);
																__emailBox.setText(email);
																__fullnameBox.setText(fullname);
																__birthdayBox.setText(birthday);
																__passwordBox.setText(password);
																__retypePasswordBox.setText(retype_password);
																if(__isAvatarProvided){
																	if(__signup.getStatus() != AsyncTask.Status.RUNNING){
																		__signup.execute(username,email,fullname,birthday,password,gender);
																	} else {
																		__utils.toast("Task already running, please wait.");
																	}
																} else {
																	if(__dialog == null){
																		__dialog = new Dialog(getSherlockActivity(),R.style.DialogNoTitle);
																		View view = __inflater.inflate(R.layout.dialog_yes_no_with_title,null);
																		TextView title = (TextView) view.findViewById(R.id.title);
																		TextView message = (TextView) view.findViewById(R.id.message);
																		Button yes = (Button) view.findViewById(R.id.yes);
																		Button no = (Button) view.findViewById(R.id.no);
																		title.setText("Image Avatar");
																		message.setText("You don't provide image avatar, yet. Are you sure want to continue?\n\nIf you want to provide your image avatar please click \"Cancel\", and provide your image avatar.");
																		yes.setText("Continue");
																		no.setText("Cancel");
																		yes.setOnClickListener(new OnClickListener() {
																			@Override
																			public void onClick(View v) {
																				__dialog.dismiss();
																				__dialog = null;
																				if(__signup.getStatus() != AsyncTask.Status.RUNNING){
																					__signup.execute(username,email,fullname,birthday,password,gender);
																				} else {
																					__utils.toast("Task already running, please wait.");
																				}
																			}
																		});
																		no.setOnClickListener(new OnClickListener() {
																			@Override
																			public void onClick(View v) {
																				__dialog.dismiss();
																				__dialog = null;
																			}
																		});
																		__dialog.setContentView(view);
																		__dialog.setCancelable(true);
																		__dialog.setCanceledOnTouchOutside(true);
																		__dialog.setOnCancelListener(new OnCancelListener() {						
																			@Override
																			public void onCancel(DialogInterface dialog) {
																				__dialog = null;
																			}
																		});
																		__dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
																		__dialog.show();
																	}
																}
															} else {
																__utils.toast("Retype Password must be equals with Password.");
																__retypePasswordBox.requestFocus();
															}
														} else {
															__utils.toast("Password must be at least 8 characters.");
															__passwordBox.requestFocus();
														}
													} else {
														__utils.toast("Password must not be empty.");
														__passwordBox.requestFocus();
													}
												} else {
													__utils.toast("Birthday must not be empty.");
													__birthdayBox.requestFocus();
												}
											} else {
												__utils.toast("Full Name must be at least 2 characters");
												__fullnameBox.requestFocus();
											}
										} else {
											__utils.toast("Full Name must not be empty.");
											__fullnameBox.requestFocus();
										}
									} else {
										__utils.toast("Invalid E-Mail format.");
										__emailBox.requestFocus();
									}
								} else {
									__utils.toast("E-Mail must not be empty.");
									__emailBox.requestFocus();
								}
							}
						}
					} else {
						__utils.toast("Username must be at least 4 characters and 16 characters max.");
						__usernameBox.requestFocus();
					}
				} else {
					__utils.toast("Username must not be empty.");
					__usernameBox.requestFocus();
				}
			}
		});
		__usernameBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String username = __usernameBox.getText().toString().trim();
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(username.length()>0){
						if(username.length()>=4 && username.length()<=16){
							if(Character.isDigit(username.charAt(0))){
								__utils.toast("First character must not be numeric characters, only alphabetical characters allowed.");
								return true;
							} else {
								if(username.startsWith("-") || username.startsWith("_") || username.endsWith("-") || username.endsWith("_")){
									__utils.toast("First character and last character must not be - or _ character.");
									return true;
								} else {
									__usernameBox.setText(username);
									return false;
								}
							}
						} else {
							__utils.toast("Username must be at least 4 characters and 16 characters max.");
							return true;							
						}
					} else {
						__utils.toast("Username must not be empty.");
						return true;
					}
				} else {
					return false;
				}
			}
		});
		__emailBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String email = __emailBox.getText().toString().trim();
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(email.length()>0){
						if(Utils.isValidEmail(email)){
							__emailBox.setText(email);
							return false;
						} else {
							__utils.toast("Invalid E-Mail format.");
							return true;							
						}
					} else {
						__utils.toast("E-Mail must not be empty.");
						return true;
					}
				} else {
					return false;
				}
			}
		});
		__fullnameBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String fullname = __fullnameBox.getText().toString().trim();
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(fullname.length()>0){
						if(fullname.length()>=2){
							__fullnameBox.setText(fullname);
							return false;
						} else {
							__utils.toast("Full Name must be at least 2 characters");
							return true;							
						}
					} else {
						__utils.toast("Full Name must not be empty.");
						return true;
					}
				} else {
					return false;
				}
			}
		});
		__birthdayBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					Utils.datePickerDialog(getSherlockActivity(), __inflater, __birthdayBox);
				}
			}
		});
		__birthdayBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String birthday = __birthdayBox.getText().toString().trim();
				if(keyCode == KeyEvent.KEYCODE_DEL){
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_ENTER){
					if(birthday.length()==10){
						return false;
					} else {
						__utils.toast("Birthday must not be empty.");
						return true;
					}
				} else {
					return false;
				}
			}
		});
		__birthday_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(__birthdayBox.hasFocus()){
					Utils.datePickerDialog(getSherlockActivity(), __inflater, __birthdayBox);
				} else {
					__birthdayBox.requestFocus();
				}
				
			}
		});
		__passwordBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String password = __passwordBox.getText().toString().trim();
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(password.length()>0){
						if(password.length()>=8){
							__passwordBox.setText(password);
							return false;						
						} else {
							__utils.toast("Password must be at least 8 characters.");
							return true;						
						}
					} else {
						__utils.toast("Password must not be empty.");
						return true;
					}
				} else {
					return false;
				}
			}
		});
		__retypePasswordBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String password = __passwordBox.getText().toString().trim();
				String retype_password = __retypePasswordBox.getText().toString().trim();
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(password.length()>0){
						if(password.length()>=8){
							if(retype_password.equals(password)){
								__retypePasswordBox.setText(retype_password);
								return false;
							} else {
								__utils.toast("Retype Password must be equals with Password.");
								return true;
							}
						} else {
							__passwordBox.requestFocus();
							return false;							
						}
					} else {
						__passwordBox.requestFocus();
						return false;
					}
				} else {
					return false;
				}
			}
		});
		return __view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == SherlockActivity.RESULT_OK){
			if(requestCode == SELECT_AVATAR_FROM_GALLERY){
				try {
    				__take_avatar_button.setVisibility(View.GONE);
    				__selectedImageUri = data.getData();
					String selectedImagePath = getPath(__selectedImageUri);
			    	FileInputStream fis = new FileInputStream(selectedImagePath);
					byte[] file = IOUtils.toByteArray(fis);
					Options opts=new Options();
	    			opts.inJustDecodeBounds=true;
	    			BitmapFactory.decodeByteArray(file,0,file.length,opts);
	    			fis.close();
	    			int width=opts.outWidth;
	    			int height=opts.outHeight;
	    			Bitmap bitmap=null;
	    			if(width>150 || height>150){
	    				bitmap = decodeFile(selectedImagePath);
	    				__take_avatar.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 4));
	    				__take_avatar.setAdjustViewBounds(true);
	    				__take_avatar.setScaleType(ScaleType.FIT_XY);
	    			} else {
	    				bitmap = BitmapFactory.decodeFile(selectedImagePath);
	    				__take_avatar.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 4));
	    				__take_avatar.setAdjustViewBounds(true);
	    				__take_avatar.setScaleType(ScaleType.FIT_XY);
	    			}
	    			__avatar = bitmap;
					Intent intent = new Intent("com.android.camera.action.CROP");
				    intent.setType("image/*");
				    List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,0);
				    int size = list.size();
				    if(size>0){
					    intent.setData(__selectedImageUri);
					    intent.putExtra("outputX",150);
					    intent.putExtra("outputY",150);
					    intent.putExtra("crop", "true");
				        intent.putExtra("aspectX", 1);
				        intent.putExtra("aspectY", 1);
				        intent.putExtra("return-data",true);
				        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
				        ResolveInfo info = list.get(0);
				        intent.setComponent(new ComponentName(info.activityInfo.packageName,info.activityInfo.name));
				        startActivityForResult(intent,CROP_AVATAR);
				    } else {
				    	__selectedImageUri = null;
				    }
				    __isAvatarProvided = true;
				} catch (Exception e) {
					Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
				}
			} else if (requestCode == CROP_AVATAR) {
				Bundle extras = data.getExtras();
				if(extras != null){
					Bitmap bitmap = extras.getParcelable("data");
					__take_avatar.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 4));
    				__take_avatar.setAdjustViewBounds(true);
    				__take_avatar.setScaleType(ScaleType.FIT_XY);
    				__avatar=bitmap;
				}
			}
		} else if (resultCode == SherlockActivity.RESULT_CANCELED) {
//			
		}
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		__landing = ((Landing)getSherlockActivity());
		context = getSherlockActivity();
		__utils = new Utils(context);
		__conn = new Connection(context);
		__signup = new SignUpTask();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	private String getPath(Uri uri){
		String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor =context.getContentResolver().query(uri, projection, null, null, null);
		try {
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        String string = cursor.getString(column_index);
	        return string;
		} finally {
			cursor.close();
		}
    }
	
	private Bitmap decodeFile(String filename){
		File f = new File(filename);
		int IMAGE_MAX_SIZE=150;
	    Bitmap b = null;
	    try {
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        fis.close();
	        int inSample=1;
	        int width=o.outWidth;
	        int height=o.outHeight;
			if (width>IMAGE_MAX_SIZE||height>IMAGE_MAX_SIZE){
				int scaledWidth;
				int scaledHeight;
				
				if (width>height){
					scaledHeight=(IMAGE_MAX_SIZE*height)/width+8;
					inSample=height/scaledHeight;
				} else {
					scaledWidth=(IMAGE_MAX_SIZE*width)/height-8;
					inSample=width/scaledWidth;
				}
			}
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = inSample;
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        fis.close();
	    } catch (IOException e) {
	    }
	    return b;
	}
	
	public class SignUpTask extends AsyncTask<String, Void, Object>{

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
//			__landing.setWorking(true);
			__landing.startProgressBarIndeterminate();
			__signup_button.setEnabled(false);
			__usernameBox.setEnabled(false);
			__emailBox.setEnabled(false);
			__fullnameBox.setEnabled(false);
			__birthdayBox.setEnabled(false);
			__birthday_button.setEnabled(false);
			__passwordBox.setEnabled(false);
			__retypePasswordBox.setEnabled(false);
			__take_avatar.setEnabled(false);
			__take_avatar_button.setEnabled(false);
			__gender.setEnabled(false);
		}
		
		@Override
		protected Object doInBackground(String... params) {
			String username = params[0];
			String email = params[1];
			String password = params[4];
			String fullname = params[2];
			String birthday = params[3];
			String gender = params[5];
			ReturnData result=new ReturnData(false,null);
			if(__conn.isOnline()){
				String filepath = null;
				if(__isAvatarProvided){
					filepath = moveAvatarImageToTemporary(__avatar);
					File fp = new File(filepath);
					if(fp.exists() == false){
						filepath = null;
					} else {
						__avatarFilePath = filepath;
					}
				}
				HttpRequest request = __conn.post("session/new");
				request.userAgent(__conn.ua());
				request.part("username",username);
				request.part("email",email);
				request.part("password",password);
				request.part("fullname",fullname);
				request.part("birthday",birthday);
				request.part("gender",gender);
				if(filepath != null){
					File upload = new File(filepath);
					request.part("file",upload.getName(),upload);
					request.part("avatar",String.valueOf(true));
				} else {
					request.part("avatar",String.valueOf(false));
				}
				try {
					if(request.code()==200){
						String body = request.body();
						result.setStatus(true);
						result.setResult(body);
					} else {
						result.setResult("Server currently on trouble, please try again in a few seconds (or minutes (or hours)).");
					}
				} catch (HttpRequestException e) {
					result.setResult(e.getMessage());
				} catch (Exception e) {
					result.setResult(e.getMessage());
				}
			} else {
				result.setResult("Internet connection is unavailable");
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Object object){
			ReturnData result = (ReturnData) object;
			__signup = new SignUpTask();
//			__landing.setWorking(false);
			__landing.stopProgressBarIndeterminate();
			__signup_button.setEnabled(true);
			__usernameBox.setEnabled(true);
			__emailBox.setEnabled(true);
			__fullnameBox.setEnabled(true);
			__birthdayBox.setEnabled(true);
			__birthday_button.setEnabled(true);
			__passwordBox.setEnabled(true);
			__retypePasswordBox.setEnabled(true);
			__take_avatar.setEnabled(true);
			__take_avatar_button.setEnabled(true);
			__gender.setEnabled(true);
			__gender.getChildAt(0).setSelected(true);
			if(result.status()){
				try {
					JSONObject json = new JSONObject(result.result());
					boolean success = json.getBoolean("success");
					if(success){
						__usernameBox.setText("");
						__emailBox.setText("");
						__fullnameBox.setText("");
						__birthdayBox.setText("");
						__passwordBox.setText("");
						__retypePasswordBox.setText("");
						JSONObject user = json.getJSONObject("user");
						String username = user.getString("username");
						String email = user.getString("email");
						if(__isAvatarProvided){
							String filename = user.getString("avatar");
							if(FileHelper.createUserDirectory(username)){
								if(FileHelper.createUserAvatarDirectory(username)){
									File move = new File(__avatarFilePath);
									move.renameTo(new File(Environment.getExternalStorageDirectory()+"/"+FileHelper.SOCIAL_VOTING_DIRECTORY+"/"+username+"/avatar/"+filename));
								}
							}
							__isAvatarProvided = false;
							__avatarFilePath = null;
							Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.default_avatar);
							__take_avatar.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 4));
							__take_avatar.setAdjustViewBounds(true);
							__take_avatar.setScaleType(ScaleType.FIT_XY);
							__take_avatar_button.setVisibility(View.VISIBLE);
						}
						Intent intent = new Intent(getSherlockActivity(),Verify.class);
						Bundle extra = new Bundle();
						extra.putString("after","signup");
						extra.putString("email",email);
						intent.putExtras(extra);
						startActivity(intent);
					} else {
						if(__isAvatarProvided && __avatarFilePath != null){
							try {
								File delete = new File(__avatarFilePath);
								if(delete.delete()){
									__avatarFilePath = null;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						String message = json.getString("message");
						String field = json.getString("field");
						if(field.equals("username")){
							__usernameBox.requestFocus();
						} else if (field.equals("email")){
							__emailBox.requestFocus();
						} else if (field.equals("fullname")){
							__fullnameBox.requestFocus();
						} else if (field.equals("password")){
							__passwordBox.requestFocus();
						} else if (field.equals("birthday")){
							__birthdayBox.requestFocus();
						}
						__utils.toast(message);
					}
				} catch (Exception e) {
					__utils.toast(e.getMessage());
				}
			} else {
				if(__isAvatarProvided && __avatarFilePath != null){
					try {
						File delete = new File(__avatarFilePath);
						if(delete.delete()){
							__avatarFilePath = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				__utils.toast(result.result());
			}
		}
		
	}
	
	private String moveAvatarImageToTemporary(Bitmap bitmap){
		String string = null;
		if(FileHelper.isSDCardPresent()){
			String tmp = null;
			if(FileHelper.isAppsDirectoryExists()){
				tmp = Environment.getExternalStorageDirectory()+"/"+FileHelper.SOCIAL_VOTING_DIRECTORY_TMP;
			} else {
				if(FileHelper.createAppsDirectory()){
					tmp = Environment.getExternalStorageDirectory()+"/"+FileHelper.SOCIAL_VOTING_DIRECTORY_TMP;
				}
			}
			if(tmp==null){
				return string;
			} else {
				String filename = tmp+"/"+String.valueOf(System.currentTimeMillis())+".jpg";
				try {
					FileOutputStream fos = new FileOutputStream(filename);
					bitmap.compress(Bitmap.CompressFormat.JPEG,95,fos);
					try {
						fos.close();
						return filename;
					} catch (IOException e) {
						return string;
					}
				} catch (FileNotFoundException e) {
					return string;
				}
			}
		} else {
			return string;
		}
	}
}
