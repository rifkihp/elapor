package elapor.application.com.elapor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import customfonts.MyTextView;
import elapor.application.com.adapter.PagerProsesRegistrasiAdapter;
import elapor.application.com.fragment.RegistrasiFormFragment;
import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.libs.GalleryFilePath;
import elapor.application.com.libs.LockableViewPager;

public class ProsesRegistrasiActivity extends AppCompatActivity {

	final int REQUEST_FROM_GALLERY    = 3;
	final int REQUEST_FROM_CAMERA     = 4;

    Context context;
	ImageView back;
	LockableViewPager viewpager;

	LinearLayout retry;
	MyTextView btnReload;

	Dialog dialog_loading;

	Dialog dialog_informasi;
	MyTextView btn_ok;
	MyTextView text_title;
	MyTextView text_informasi;

	public static ImageLoader imageLoader;
	public static DisplayImageOptions imageOption;

	//Dialog dialog_pilih_pihak;
	//MyTextView internal, eksternal;

	Dialog dialog_pilih_gambar;
	MyTextView from_camera, from_galery;
	private static Uri mImageCaptureUri;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_registrasi);

        context = ProsesRegistrasiActivity.this;

        CommonUtilities.initImageLoader(context);
		imageLoader = ImageLoader.getInstance();
		imageOption = CommonUtilities.getOptionsImage(R.drawable.userdefault, R.drawable.userdefault);

		back      = findViewById(R.id.back);
		viewpager = findViewById(R.id.pager);
		viewpager.setSwipeLocked(true);

		retry = findViewById(R.id.loadMask);
		btnReload =  findViewById(R.id.btnReload);

		btnReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app_close();
			}
		});
		
		/*dialog_pilih_pihak = new Dialog(context);
		dialog_pilih_pihak.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_pilih_pihak.setCancelable(true);
		dialog_pilih_pihak.setContentView(R.layout.pilih_pihak_dialog);

		internal = dialog_pilih_pihak.findViewById(R.id.txtInternal);
		internal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_pilih_pihak.dismiss();
				RegistrasiFormFragment.edit_pihak.setText("INTERNAL");
			}
		});

		eksternal = dialog_pilih_pihak.findViewById(R.id.txtEksternal);
		eksternal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_pilih_pihak.dismiss();
				RegistrasiFormFragment.edit_pihak.setText("EKSTERNAL");

			}
		});*/

		dialog_pilih_gambar = new Dialog(context);
		dialog_pilih_gambar.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_pilih_gambar.setCancelable(true);
		dialog_pilih_gambar.setContentView(R.layout.pilih_gambar_dialog);

		from_galery = dialog_pilih_gambar.findViewById(R.id.txtFromGalley);
		from_galery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_pilih_gambar.dismiss();
				fromGallery();
			}
		});

		from_camera = dialog_pilih_gambar.findViewById(R.id.txtFromCamera);
		from_camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_pilih_gambar.dismiss();
				fromCamera();
			}
		});

		dialog_loading = new Dialog(context);
		dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_loading.setCancelable(false);
		dialog_loading.setContentView(R.layout.loading_dialog);

		dialog_informasi = new Dialog(context);
		dialog_informasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_informasi.setCancelable(true);
		dialog_informasi.setContentView(R.layout.informasi_dialog);

		btn_ok = dialog_informasi.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog_informasi.dismiss();
			}
		});

		text_title = dialog_informasi.findViewById(R.id.text_title);
		text_informasi = dialog_informasi.findViewById(R.id.text_dialog);

		PagerProsesRegistrasiAdapter pagerProsesRegistrasiAdapter = new PagerProsesRegistrasiAdapter(getSupportFragmentManager());
		viewpager.setAdapter(pagerProsesRegistrasiAdapter);

	}

	/*public void selectPihak() {
		dialog_pilih_pihak.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_pilih_pihak.show();
	}*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data_intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data_intent);

		String fileName = new SimpleDateFormat("yyyyMMddhhmmss'.jpg'").format(new Date());
		String dest = CommonUtilities.getOutputPath(context, "images")+File.separator+fileName;
		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				
				case Crop.REQUEST_CROP:
					File file = new File(Crop.getOutput(data_intent).getPath());

					mImageCaptureUri = Uri.fromFile(file);
					RegistrasiFormFragment.image_profile.setImageURI(mImageCaptureUri);

					break;
				case REQUEST_FROM_CAMERA:
					CommonUtilities.compressImage(context, data_intent.getStringExtra("path"), dest);
					mImageCaptureUri = Uri.fromFile(new File(dest));
					beginCrop(mImageCaptureUri);


					break;
				case REQUEST_FROM_GALLERY:
					Uri selectedUri = data_intent.getData();
					CommonUtilities.compressImage(context, GalleryFilePath.getPath(context, selectedUri), dest);
					mImageCaptureUri = Uri.fromFile(new File(dest));
					beginCrop(mImageCaptureUri);

					break;
					
			}
		}
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_OK, new Intent());
			finish();

		    return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			app_close();

			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		try {
            unregisterReceiver(mHandleSubmitRegistrasiReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		try {
			unregisterReceiver(mHandleSubmitRegistrasiReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onPause();
	}

	@Override
	protected void onResume() {
		registerReceiver(mHandleSubmitRegistrasiReceiver, new IntentFilter("elapor.application.com.elapor.PROSES_SUBMIT_REGISTRASI"));

		super.onResume();
	}

	private final BroadcastReceiver mHandleSubmitRegistrasiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Boolean success = intent.getBooleanExtra("success", false);
			String message = intent.getStringExtra("message");

			dialog_loading.dismiss();
			if (success) {
				viewpager.setCurrentItem(1, true);
			} else {
				openDialogMessage(message, false);
			}

		}
	};

	public void selectImage() {
		dialog_pilih_gambar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_pilih_gambar.show();
	}

	private void beginCrop(Uri source) {
		Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
		Crop.of(source, destination).asSquare().start(this);
	}

	private void fromGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_FROM_GALLERY);
	}

	private void fromCamera() {

		Intent intent = new Intent(context, AmbilFotoActivity.class);
		startActivityForResult(intent, REQUEST_FROM_CAMERA);
	}

	public void openDialogLoading(boolean cancelable) {
		dialog_loading.setCancelable(cancelable);
		dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_loading.show();
	}

	public void openDialogMessage(String message, boolean status) {
		text_informasi.setText(message);
		text_title.setText(status?"BERHASIL":"KESALAHAN");
		dialog_informasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_informasi.show();
	}

	public void submitRegistrasi() {
		openDialogLoading(false);
		new prosesSubmitRegistrasi().execute();
	}

	public class prosesSubmitRegistrasi extends AsyncTask<String, Void, Void> {

		@SuppressLint("WrongThread")
		@Override
		protected Void doInBackground(String... urls) {

			boolean success = false;
			String message = "";
			
			String url = CommonUtilities.SERVER_URL + "/store/androidSubmitRegistrasi.php";

			JSONObject jObj = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				MultipartEntity reqEntity = new MultipartEntity();
				//DETAIL DATA
				reqEntity.addPart("nama", new StringBody(RegistrasiFormFragment.edit_nama.getText().toString()));
				reqEntity.addPart("bagian", new StringBody(RegistrasiFormFragment.edit_bagian.getText().toString()));

				/*reqEntity.addPart("pihak", new StringBody(RegistrasiFormFragment.edit_pihak.getText().toString()));
				reqEntity.addPart("alamat", new StringBody(RegistrasiFormFragment.edit_alamat.getText().toString()));
				reqEntity.addPart("phone", new StringBody(RegistrasiFormFragment.edit_nohp.getText().toString()));
				reqEntity.addPart("comdiv", new StringBody(RegistrasiFormFragment.edit_comdiv.getText().toString()));*/

				reqEntity.addPart("userid", new StringBody(RegistrasiFormFragment.edit_userid.getText().toString()));
				reqEntity.addPart("password", new StringBody(RegistrasiFormFragment.edit_password.getText().toString()));
				reqEntity.addPart("konfirmasi", new StringBody(RegistrasiFormFragment.edit_konfirmasi.getText().toString()));

				if (mImageCaptureUri != null) {
					File file = new File(mImageCaptureUri.getPath());
					if (file.exists()) {
						FileBody bin_gamber = new FileBody(file);
						reqEntity.addPart("photo", bin_gamber);
					}
				}
				httppost.setEntity(reqEntity);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				InputStream is = resEntity.getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				String json = sb.toString();
				System.out.println(json);

				jObj = new JSONObject(json);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(jObj!=null) {
				try {

					success = jObj.isNull("success")?false:jObj.getBoolean("success");
					message = jObj.isNull("message")?message:jObj.getString("message");

					if(success) {
						if (mImageCaptureUri != null) {
							File file = new File(mImageCaptureUri.getPath());
							if (file.exists()) {
								file.delete();
							}
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Intent i = new Intent("elapor.application.com.elapor.PROSES_SUBMIT_REGISTRASI");
			i.putExtra("success", success);
			i.putExtra("message", message);
			sendBroadcast(i);

			return null;
		}
	}

	public void app_close() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
}
