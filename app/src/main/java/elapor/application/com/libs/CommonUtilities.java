package elapor.application.com.libs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import elapor.application.com.elapor.BuildConfig;
import elapor.application.com.elapor.R;
import elapor.application.com.model.event;
import elapor.application.com.model.remember;
import elapor.application.com.model.user;
import elapor.application.com.model.version;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public final class CommonUtilities {
	
	public static final String TAG = "E-Laporan";
	//public static final String SERVER_URL = "http://192.168.43.183/e-lapor";
	public static final String SERVER_URL = "http://kiezie.web.id/e-lapor";

	public static String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int)( milliseconds / (1000*60*60));
		int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		// Add hours if there
		if(hours > 0){
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if(seconds < 10){
			secondsString = "0" + seconds;
		}else{
			secondsString = "" + seconds;}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}

	public static version getAppVersion(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String version_name = prefs.getString("version_name", "");
		String version_no = prefs.getString("version_no", "");

		if(version_name.length()==0 && version_no.length()==0) {
			version_name = BuildConfig.VERSION_NAME;
			version_no = String.valueOf(BuildConfig.VERSION_CODE);

			Editor editor = prefs.edit();
			editor.putString("version_name", version_name);
			editor.putString("version_no", version_no);
		}

		return new version(version_no, version_name);
	}

	public static void setRegNo(Context context, String reg_no) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		editor.putString("registration_no", reg_no);
		editor.commit();
	}

	public static String getRegNo(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("registration_no", "");
	}

	public static void setGuestId(Context context, int guest_id) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		editor.putInt("guest_id", guest_id);
		editor.commit();
	}

	public static int getGuestId(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getInt("guest_id", 0);
	}

	public static void setRememberPassword(Context context, remember data) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		editor.putString("email_remember", data.getEmail());
		editor.putString("password_remember", data.getPassword());
		editor.putString("checklist_remember", data.getChecklist());
		editor.commit();
	}

	public static remember getRememberPassword(Context context) {
		SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(context);
		return new remember(
				data.getString("email_remember", ""),
				data.getString("password_remember", ""),
				data.getString("checklist_remember", "")
		);
	}

	public static void setSelectedEvent(Context context, event data) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putInt("event_id", data.getId());
		editor.putString("event_judul", data.getJudul());
		editor.putString("event_tanggal", data.getTanggal());
		editor.putString("event_lokasi", data.getLokasi());
		editor.putString("event_selesai", data.getSelesai());
		editor.putInt("total_peserta", data.getTotal_peserta());
		editor.putBoolean("event_is_open", data.getIs_open());
		editor.putString("event_banner1", data.getBanner1());
		editor.putString("event_banner2", data.getBanner2());
		editor.putString("event_banner3", data.getBanner3());
		editor.putString("event_logo", data.getLogo());

		editor.commit();
	}

	public static event getSelectedEvent(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return new event(
				prefs.getInt("event_id", 0),
				prefs.getString("event_judul", ""),
				prefs.getString("event_tanggal", ""),
				prefs.getString("event_lokasi", ""),
				prefs.getString("event_selesai", ""),
				prefs.getInt("total_peserta", 0),
				prefs.getBoolean("event_is_open", false),
				prefs.getString("event_banner1", ""),
				prefs.getString("event_banner2", ""),
				prefs.getString("event_banner3", ""),
				prefs.getString("event_logo", "")

		);
	}

	public static void setLoginUser(Context context, user data) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		editor.putInt("id", data.getId());
		editor.putString("nama", data.getNama());
		editor.putString("bagian", data.getBagian());
		editor.putString("userid", data.getUserid());
		editor.putString("pihak", data.getPihak());
		editor.putString("alamat", data.getAlamat());
		editor.putString("phone", data.getPhone());
		editor.putString("comdiv", data.getComdiv());
		editor.putString("photo", data.getPhoto());
		editor.commit();
	}

	public static user getLoginUser(Context context) {
		SharedPreferences data_user = PreferenceManager.getDefaultSharedPreferences(context);
		return new user(
				data_user.getInt("id", 0),
				data_user.getString("nama", ""),
				data_user.getString("bagian", ""),
				data_user.getString("userid", ""),
				data_user.getString("pihak", ""),
				data_user.getString("alamat", ""),
				data_user.getString("phone", ""),
				data_user.getString("comdiv", ""),
				data_user.getString("photo", "")
		);
	}

	public static void setGcm_regid(Context context, String regid) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString("gcm_regid", regid);
		editor.commit();
	}

	public static String getGcm_regid(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("gcm_regid", "");
	}

	public static File getTempFile(Context context, String ext) {
		//it will return /sdcard/image.tmp
		File path = new File(Environment.getExternalStorageDirectory()+File.separator + R.string.app_name, "temp");
		if(!path.exists()) {
			path.mkdirs();
		}
		
		//System.out.println("TEMP---> "+path.getPath());		
		return new File(path, "temp."+ext);
	}
	
	public static String getOutputPath(Context context, String dest) {
		File path = new File(Environment.getExternalStorageDirectory()+File.separator + R.string.app_name, dest);		
		if (!path.exists()) path.mkdirs();
		
		//System.out.println("PATH---> "+path.getPath());		
		return path.getPath();
	}
	
	public static void setSettingRegId(Context context, String regid) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);    	
		Editor editor = prefs.edit();
		editor.putString("regid", regid);		
		editor.commit();		
	}
	
	public static String getSettingRegId(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("regid", "");				
	}

	public static void loadImageUser(ImageLoader imageLoader, final ImageView imgPreview, String url) {
    	imageLoader.loadImage(url, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				imgPreview.setImageBitmap(loadedImage);
			}
		});
    }

	public static void loadImageProfil(ImageLoader imageLoader, final ImageView imgPreview, String url) {
    	imageLoader.loadImage(url, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				imgPreview.setImageBitmap(loadedImage);
			}
		});
    }

	public static void loadImageHeader(ImageLoader imageLoader, final ImageView imgPreview, String url, DisplayImageOptions options) {
    	imageLoader.loadImage(url, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				imgPreview.setImageBitmap(loadedImage);
			}
		});
    }

	public static DisplayImageOptions getOptionsImage(int stubImg, int imgRes) {
		return new DisplayImageOptions.Builder()
			.showStubImage(stubImg)		    //	Display Stub Image
			.showImageForEmptyUri(imgRes)	//	If Empty image found
			.cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		
	}
	
	public static void initImageLoader(Context context) {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) 
					context.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024;
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPoolSize(5)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(memoryCacheSize)
				.memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize-1000000))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();

		ImageLoader.getInstance().init(config);
	}
	
	public static boolean isFavoriteItem(ArrayList<String> data, String item_id) {
		
		for (String favorite : data) {
			if(favorite.equalsIgnoreCase(item_id)) return true;
		}
		
		return false;
	}
	
	public static String getRealPathFromURI(Context context, String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}
	
	public static Boolean compressImage(Context context, String imageUri, String imageDes) {
		
		ImageLoadingUtils utils = new ImageLoadingUtils(context);
		String filePath = getRealPathFromURI(context, imageUri);
		Bitmap scaledBitmap = null;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;						
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
		
		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;     
				
			}
		}
		
		options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16*1024];
			
		try {	
			bmp = BitmapFactory.decodeFile(filePath,options);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();
			
		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();
		}
						
		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float)options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;
			
		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

						
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);
		
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(imageDes);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
						
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
public static String getDateFrom(String datetime, Integer tipe) {
    	
    	if(datetime.length()==0) return "";
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat newDateFormatter = new SimpleDateFormat("dd MM yyyy");
    	SimpleDateFormat newTimeFormatter = new SimpleDateFormat("HH:mm");
    	
    	try {
     		Date date = formatter.parse(datetime);
     		return tipe==0?newDateFormatter.format(date):newTimeFormatter.format(date);
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	
    	return "";
    	
    }
    
    public static String getDateMassage(String datetime) {
    	
    	if(datetime.length()==0) return "";
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat newDateFormatter = new SimpleDateFormat("dd MM yyyy");
    	SimpleDateFormat newTimeFormatter = new SimpleDateFormat("HH:mm");
    	
    	try {
     		Date date = formatter.parse(datetime);
     		
     		String tanggal = newDateFormatter.format(date);
     		String waktu   = newTimeFormatter.format(date);
    	
     		date = new Date();
     		String current_tanggal = newDateFormatter.format(date);
     		
     		if(current_tanggal.equalsIgnoreCase(tanggal)) return waktu;
     		else return tanggal;     			
     		     		
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	
    	return "";
    	
    }
	
    public static String getTotal(Double total) {
		double result = Math.round(total);		
		DecimalFormat format = new DecimalFormat("#,###,###.##");
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(',');
		format.setDecimalFormatSymbols(symbols);
		
		NumberFormat formatter = format;
		return formatter.format(result);
	}

	public static Bitmap getBitmap(String urls) {
		try {
			URL url = new URL(urls);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}