package elapor.application.com.elapor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import elapor.application.com.libs.AlertDialogManager;
import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.libs.Preview;
import elapor.application.com.libs.ServerUtilities;

public class KirimLaporanActivity extends AppCompatActivity {

	String[] selectedPath = new String[]{null, null};

	Context context;
	ImageLoader imageLoader;

	ProgressDialog progDailog;
	AlertDialogManager alert;

	Preview preview;
	FrameLayout layout;
	SurfaceView surface;

	static MediaRecorder mMediaRecorder;
	static Camera mCamera;
	int current_camera;
	int nums_of_camera;
	boolean recording = false;
	String tipe_file;

	ImageButton btn_capture, btn_record;
	TextView count_down;
	ProgressBar progress_bar;
	Handler handler_capture;
	Handler handler_gps;

	Dialog dialog_laporan;
	TextView textTitleDialog;
	TextView textDialog;
	EditText textLaporan;
	Button btn_yes, btn_no;
	Boolean isOpenDialog = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kirim_laporan);

		context = KirimLaporanActivity.this;
		alert = new AlertDialogManager();

		handler_capture = new Handler();
		handler_gps = new Handler();

		CommonUtilities.initImageLoader(context);
        imageLoader = ImageLoader.getInstance();

		layout = findViewById(R.id.layout);
		surface = findViewById(R.id.surfaceView);
		preview = new Preview(context, surface);

		layout.addView(preview);
		preview.setKeepScreenOn(true);

		btn_capture = findViewById(R.id.btnCapture);
		btn_capture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tipe_file = "G";
				btn_capture.setVisibility(View.INVISIBLE);
				btn_record.setVisibility(View.INVISIBLE);
				progress_bar.setVisibility(View.VISIBLE);
				mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		});

		btn_record = findViewById(R.id.btnRecord);
		btn_record.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tipe_file = "V";

				if(recording) {
					int coutdown = Integer.parseInt(count_down.getText().toString());
					if(coutdown<=9) {
						handler_capture.removeCallbacks(capture);
						mMediaRecorder.stop();
						releaseMediaRecorder();
						refreshGallery(new File(selectedPath[0]));
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

						recording = false;
						isOpenDialog = true;
						openDialogLaporan();
					}
				} else {
					btn_record.setImageResource(R.drawable.stop_record);
					btn_capture.setVisibility(View.INVISIBLE);
					count_down.setVisibility(View.VISIBLE);

					count_down.setText("10");
					handler_capture.postDelayed(capture, 1000);

					int rotation = getWindowManager().getDefaultDisplay().getRotation();
					switch (rotation) {
						case Surface.ROTATION_0: setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); break;
						case Surface.ROTATION_90: setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); break;
						case Surface.ROTATION_180: setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); break;
						case Surface.ROTATION_270: setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); break;
					}

					releaseMediaRecorder();
					prepareMediaRecorder();
					mMediaRecorder.start();

					recording = true;
				}
			}
		});

		count_down = findViewById(R.id.countdown);
		progress_bar = findViewById(R.id.progressBar);

		progDailog = new ProgressDialog(context);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);

		dialog_laporan = new Dialog(context);
		dialog_laporan.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_laporan.setCancelable(false);
		dialog_laporan.setContentView(R.layout.dialog_isi_laporan);

		btn_yes = dialog_laporan.findViewById(R.id.btn_yes);
		btn_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new prosesSimpan().execute();
			}
		});

		btn_no = dialog_laporan.findViewById(R.id.btn_no);
		btn_no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog_laporan.dismiss();
				finish();
		}
		});

		textTitleDialog = dialog_laporan.findViewById(R.id.text_title_dialog);
		textDialog = dialog_laporan.findViewById(R.id.text_dialog);
		textLaporan = dialog_laporan.findViewById(R.id.text_laporan);

	}

	Runnable capture = new Runnable() {
		public void run() {
			int coutdown = Integer.parseInt(count_down.getText().toString());
			coutdown--;

			if(coutdown==0) {
				count_down.setVisibility(View.INVISIBLE);
				progress_bar.setVisibility(View.VISIBLE);
				handler_capture.removeCallbacks(this);
				if(tipe_file.equalsIgnoreCase("G")) {
					mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
				} else {
					mMediaRecorder.stop();
					releaseMediaRecorder();
					refreshGallery(new File(selectedPath[0]));
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

					recording = false;
					isOpenDialog = true;
					openDialogLaporan();
				}
			} else {
				count_down.setText(coutdown+"");
				handler_capture.postDelayed(this, 1000);
			}
		}
	};

	private void releaseCameraAndPreview() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();

		if(!isOpenDialog) {
			if(current_camera>0) {
				count_down.setVisibility(View.VISIBLE);
				progress_bar.setVisibility(View.INVISIBLE);
				btn_capture.setVisibility(View.INVISIBLE);
				count_down.setText("3");
				handler_capture.postDelayed(capture, 1000);
			}

			nums_of_camera = Camera.getNumberOfCameras();
			getCamera();
		} else {
			openDialogLaporan();
			btn_capture.setVisibility(View.INVISIBLE);
			btn_record.setVisibility(View.INVISIBLE);
		}
	}

	private void getCamera() {
		releaseCameraAndPreview();
		try {
			mCamera = Camera.open(current_camera);
			preview.setCamera(mCamera);

		} catch (RuntimeException e) {
			e.printStackTrace();
			alert.showAlertDialog(context, "Kesalahan", e.getMessage(), false);
		} catch (IOException e) {
			e.printStackTrace();
			alert.showAlertDialog(context, "Kesalahan", e.getMessage(), false);
		}
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		try {
			handler_capture.removeCallbacks(capture);
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onPause();
	}

	private void refreshGallery(File file) {
		Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(Uri.fromFile(file));
		sendBroadcast(mediaScanIntent);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			//			 Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			//			 Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {

			new SaveImageTask().execute(data);
		}
	};

	class SaveImageTask extends AsyncTask<byte[], Void, Boolean> {

		@Override
		protected Boolean doInBackground(byte[]... data) {

			Bitmap storedBitmap = BitmapFactory.decodeByteArray(data[0], 0, data[0].length, null);

			int width = storedBitmap.getWidth();
			int height = storedBitmap.getHeight();

			float aspectRatio = storedBitmap.getWidth() / (float) storedBitmap.getHeight();
			int newWidth_temps = 800;
			int newHeight_temps = Math.round(newWidth_temps / aspectRatio);

			//IF FROM HEIGHT
			int degrees = preview.getDegrees();
			if(degrees==0 || degrees==180) {
				newHeight_temps = 800;
				newWidth_temps = Math.round(newHeight_temps * aspectRatio);
			}
			int newWidth = newWidth_temps;
			int newHeight = newHeight_temps;

			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;

			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			matrix.postRotate((current_camera==1?-1:1)*degrees);

			try {
				storedBitmap = Bitmap.createBitmap(storedBitmap, 0, 0, width, height, matrix, true);
				FileOutputStream outStream;
				File dir = new File (CommonUtilities.getOutputPath(context, "images"));
				String fileName = String.format("%d.jpg", System.currentTimeMillis());
				File outFile = new File(dir, "temps_"+fileName);

				outStream = new FileOutputStream(outFile);
				storedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				if (storedBitmap != null) {
					storedBitmap.recycle();
					storedBitmap = null;
				}

				outStream.flush();
				outStream.close();

				selectedPath[current_camera] = outFile.getAbsolutePath();
				String dest = CommonUtilities.getOutputPath(context, "images")+File.separator+fileName;
				CommonUtilities.compressImage(context, selectedPath[current_camera], dest);
				outFile.delete();
				outFile = null;
				selectedPath[current_camera] = dest;

				refreshGallery(new File(selectedPath[current_camera]));

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*************/

			/*FileOutputStream outStream = null;
			try {
				File dir = new File (CommonUtilities.getOutputPath(context, "images"));
				String fileName = String.format("%d.jpg", System.currentTimeMillis());
				File outFile = new File(dir, "temps_"+fileName);

				outStream = new FileOutputStream(outFile);
				outStream.write(data[0]);
				outStream.flush();
				outStream.close();

				selectedPath[current_camera] = outFile.getAbsolutePath();
				String dest = CommonUtilities.getOutputPath(context, "images")+File.separator+fileName;
				CommonUtilities.compressImage(context, selectedPath[current_camera], dest);
				outFile.delete();
				outFile = null;
				selectedPath[current_camera] = dest;

				refreshGallery(new File(dest));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}*/

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			progress_bar.setVisibility(View.INVISIBLE);
			/*if(current_camera<nums_of_camera-1) {
				current_camera++;
				//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				preview.destroyHolder();
				getCamera();
				preview.surfaceChanged(preview.getPreviewHolder(), 0, preview.getSurfaceView().getWidth(), preview.getSurfaceView().getHeight());

				count_down.setVisibility(View.VISIBLE);
				count_down.setText("2");
				handler_capture.postDelayed(capture, 1000);
			} else {
				isOpenDialog = true;
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				openDialogLaporan();
			}*/

			isOpenDialog = true;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			openDialogLaporan();
		}
	}

	public void openDialogLaporan() {
		dialog_laporan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_laporan.show();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("current_camera", current_camera);
		savedInstanceState.putString("tipe_file", tipe_file);
		savedInstanceState.putBoolean("recording", recording);
		savedInstanceState.putBoolean("isOpenDialog", isOpenDialog);
	    savedInstanceState.putStringArray("selectedPath", selectedPath);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
		current_camera = savedInstanceState.getInt("current_camera");
		tipe_file = savedInstanceState.getString("tipe_file");
		recording = savedInstanceState.getBoolean("recording");
		isOpenDialog = savedInstanceState.getBoolean("isOpenDialog");
	    selectedPath = savedInstanceState.getStringArray("selectedPath");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			handler_capture.removeCallbacks(capture);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}


	public class prosesSimpan extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progDailog.setMessage("Kirim...");
	        progDailog.show();
	    }

		@Override
	    protected JSONObject doInBackground(String... urls) {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datetime = formatter.format(new Date());

			if(tipe_file.equalsIgnoreCase("V")) {
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(selectedPath[0], MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

				FileOutputStream outStream;
				try {
					File dir = new File (CommonUtilities.getOutputPath(context, "images"));
					String fileName = String.format("%d.jpg", System.currentTimeMillis());
					File outFile = new File(dir, "temps_"+fileName);

					outStream = new FileOutputStream(outFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}

					outStream.flush();
					outStream.close();

					selectedPath[1] = outFile.getAbsolutePath();
					String dest = CommonUtilities.getOutputPath(context, "images")+File.separator+fileName;
					CommonUtilities.compressImage(context, selectedPath[1], dest);
					outFile.delete();
					outFile = null;
					selectedPath[1] = dest;

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			/*System.out.println(selectedPath[0]);
			System.out.println(selectedPath[1]);
			File nFile_1 = new File(selectedPath[0]);
			File nFile_2 = new File(selectedPath[1]);
			if(!nFile_1.exists()) return null;
			if(!nFile_2.exists()) return null;*/

			return ServerUtilities.readAndFragmentSmartCity(context, datetime, textLaporan.getText().toString(), selectedPath[0], selectedPath[1], tipe_file, 0, 0);
		}

		@Deprecated
	    @Override
	    protected void onPostExecute(JSONObject result) {

	    	progDailog.dismiss();

	    	Boolean success = false;
	    	String message = "Gagal melakukan proses kirim data. Cobalah lagi.";
	    	if(result!=null) {
	    		try {
					success = result.isNull("success")?false:result.getBoolean("success");
					message = result.isNull("message")?message:result.getString("message");
	    		} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}

	    	if(!success) {
	    		alert.showAlertDialog(context, "Kesalahan", message, false);
	    	} else {
				if(tipe_file.equalsIgnoreCase("V")) {
					File file = new File(selectedPath[1]);
					if(file.exists()) file.delete();
				}

	    		Toast.makeText(context, "Laporan Berhasil dikirim.", Toast.LENGTH_LONG).show();
	    		dialog_laporan.dismiss();

	    		setResult(RESULT_OK, new Intent());
		        finish();
	    	}
	    }
	}

	public void prepareMediaRecorder(){

		mMediaRecorder = new MediaRecorder();

		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		//mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		//mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// MPEG_4_SP
		//mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		mMediaRecorder.setVideoEncodingBitRate(3000000);

		selectedPath[0] = CommonUtilities.getOutputPath(context, "videos")+File.separator+"temps_"+String.format("%d.mp4", System.currentTimeMillis());
		mMediaRecorder.setOutputFile(selectedPath[0]);

		mMediaRecorder.setOrientationHint(preview.getDegrees());

		//mMediaRecorder.setMaxDuration(100000); // set maximum duration
		//mMediaRecorder.setMaxFileSize(50000000); // set maximum file size

		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			alert.showAlertDialog(context, "e", e.getMessage(), false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			alert.showAlertDialog(context, "e", e.getMessage(), false);
		}
	}

	private void releaseMediaRecorder() {
		if (mMediaRecorder != null) {
			mMediaRecorder.reset(); // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock(); // lock camera for later use
		}
	}
}
