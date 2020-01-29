package elapor.application.com.elapor;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;

import elapor.application.com.adapter.InformasiAdapter;
import customfonts.MyTextView;
import elapor.application.com.fragment.InformasiFragment;
import elapor.application.com.fragment.ProfileFragment;
import elapor.application.com.fragment.pictureBrowserFragment;
import elapor.application.com.libs.AlertDialogManager;
import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.libs.GalleryFilePath;
import elapor.application.com.libs.ServerUtilities;
import elapor.application.com.model.informasi;
import elapor.application.com.model.informasi_list;
import elapor.application.com.libs.JSONParser;
import elapor.application.com.model.user;
import elapor.application.com.utils.PicHolder;
import elapor.application.com.utils.itemClickListener;
import elapor.application.com.utils.pictureFacer;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, itemClickListener {
	String[] selectedPath = new String[]{null, null};

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
	final int REQUEST_FROM_GALLERY    = 3;
	final int REQUEST_FROM_CAMERA           = 4;
	final int REQUEST_FROM_DETAIL_INFORMASI = 5;
	final int REQUEST_FROM_KIRIM_LAPORAN    = 6;

	Context context;
	user userLogin;

	CircleImageView avatar;
	MyTextView name_avatar;

	ImageView menu;
	Toolbar toolbar;
	DrawerLayout drawer;
	LinearLayout mDrawerPane;

	MyTextView main_title;

	LinearLayout lin_logout;
	MyTextView nav_logout;

	LinearLayout lin_profil;
	MyTextView nav_profil;

	LinearLayout linear_utama;
	
	LinearLayout menu_laporan;

	Dialog dialog_pilih_pihak;
	MyTextView internal, eksternal;

	Dialog dialog_pilih_gambar;
	MyTextView from_camera, from_galery;

	Dialog dialog_logout;
	MyTextView btn_no, btn_yes;
	ProgressDialog progDailog;

	AlertDialogManager alert;

	Dialog dialog_informasi;
	MyTextView btn_ok;
	MyTextView text_title;
	MyTextView text_informasi;
	
	public static ImageLoader imageLoader;
	public static DisplayImageOptions imageOptions;
	public static DisplayImageOptions imageOptionsUser;
	public static DisplayImageOptions imageOptionEvent;
	String tipe_file;

	Dialog dialog_laporan;
	TextView textTitleDialog;
	TextView textDialog;
	EditText textLaporan;
	Button btn_yes_laporan, btn_no_laporan;
	Boolean isOpenDialog = false;

	public static int menu_selected = 0;
	
	// INFORMASI LIST
	int next_page_informasi_list;
	public static ArrayList<informasi> informasilist = new ArrayList<>();
	public static InformasiAdapter informasiAdapter;


	private static Uri mImageCaptureUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

        if (Build.VERSION.SDK_INT >= 23) {
            insertDummyContactWrapper();
        }

        context = MainActivity.this;
		userLogin = CommonUtilities.getLoginUser(context);

		alert = new AlertDialogManager();

		CommonUtilities.initImageLoader(context);
		imageLoader  = ImageLoader.getInstance();
		imageOptions = CommonUtilities.getOptionsImage(R.drawable.shoppy_logo, R.drawable.shoppy_logo);
		imageOptionsUser = CommonUtilities.getOptionsImage(R.drawable.userdefault, R.drawable.userdefault);
		imageOptionEvent = CommonUtilities.getOptionsImage(R.drawable.shoppy_logo, R.drawable.shoppy_logo);

		progDailog = new ProgressDialog(context);
		progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDailog.setCancelable(false);

		dialog_laporan = new Dialog(context);
		dialog_laporan.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_laporan.setCancelable(false);
		dialog_laporan.setContentView(R.layout.dialog_isi_laporan);

		btn_yes_laporan = dialog_laporan.findViewById(R.id.btn_yes);
		btn_yes_laporan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new prosesSimpan().execute();
			}
		});

		btn_no_laporan = dialog_laporan.findViewById(R.id.btn_no);
		btn_no_laporan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog_laporan.dismiss();
				finish();
			}
		});

		textTitleDialog = dialog_laporan.findViewById(R.id.text_title_dialog);
		textDialog = dialog_laporan.findViewById(R.id.text_dialog);
		textLaporan = dialog_laporan.findViewById(R.id.text_laporan);

		dialog_logout = new Dialog(context);
		dialog_logout.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_logout.setCancelable(true);
		dialog_logout.setContentView(R.layout.signout_dialog);

		btn_yes = dialog_logout.findViewById(R.id.btn_yes);
		btn_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new prosesSignOut().execute();

			}
		});

		btn_no = dialog_logout.findViewById(R.id.btn_no);
		btn_no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog_logout.dismiss();

			}
		});
		
		dialog_pilih_pihak = new Dialog(context);
		dialog_pilih_pihak.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_pilih_pihak.setCancelable(true);
		dialog_pilih_pihak.setContentView(R.layout.pilih_pihak_dialog);

		internal = dialog_pilih_pihak.findViewById(R.id.txtInternal);
		internal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_pilih_pihak.dismiss();
			}
		});

		eksternal = dialog_pilih_pihak.findViewById(R.id.txtEksternal);
		eksternal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_pilih_pihak.dismiss();

			}
		});

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

		dialog_informasi = new Dialog(context);
		dialog_informasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_informasi.setCancelable(true);
		dialog_informasi.setContentView(R.layout.informasi_dialog);

		btn_ok = dialog_informasi.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog_informasi.dismiss();
			}
		});

		text_title = dialog_informasi.findViewById(R.id.text_title);
		text_informasi = dialog_informasi.findViewById(R.id.text_dialog);

		toolbar = findViewById(R.id.toolbar);
		menu = findViewById(R.id.menu);
		drawer = findViewById(R.id.drawer_layout);
		mDrawerPane = findViewById(R.id.drawerPane);

		main_title    = findViewById(R.id.eshop);
		linear_utama  = findViewById(R.id.linear_utama);
		
		avatar = findViewById(R.id.banar1);
		name_avatar = findViewById(R.id.name);

		lin_logout = findViewById(R.id.lin_logout);
		lin_profil = findViewById(R.id.lin_profil);

		nav_logout = findViewById(R.id.nav_logout);
		nav_profil = findViewById(R.id.nav_profil);

		lin_logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawer.closeDrawer(GravityCompat.START);
				openDialogSignout();
			}
		});

		lin_profil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawer.closeDrawer(GravityCompat.START);
				displayView(11);
			}
		});

		menu_laporan = findViewById(R.id.menu_informasi);

		menu_laporan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayView(5);
			}
		});

		int width = getResources().getDisplayMetrics().widthPixels;
		width = width - (width / 3);
		DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerPane.getLayoutParams();
		params.width = width;
		mDrawerPane.setLayoutParams(params);

		main_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayView(0);
			}
		});

		menu.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				if (drawer.isDrawerOpen(GravityCompat.START)) {
					drawer.closeDrawer(GravityCompat.START);
				} else {
					drawer.openDrawer(GravityCompat.START);
				}
				closeSoftKeyboard();
			}
		});

		avatar.setLayerType(View.LAYER_TYPE_HARDWARE, null);

		boolean notification = false;
		if(savedInstanceState==null) {
			checkGcmRegid();
			notification = getIntent().getBooleanExtra("notification", false);
			menu_selected = getIntent().getIntExtra("menu_select", 5);
		}

		//Toast.makeText(context, "menu select: "+menu_selected, Toast.LENGTH_LONG).show();
		if(notification) {
			if(menu_selected==5) {
				informasi dinformasi = (informasi) getIntent().getSerializableExtra("informasi");
				openDetailInformasi(dinformasi);
			}
		}

		setSignIn();
	}

	public void openDialogSignout() {
		dialog_logout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_logout.show();
	}

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

		Intent intent = new Intent(context, KirimLaporanActivity.class);
		startActivityForResult(intent, REQUEST_FROM_KIRIM_LAPORAN);

		//Intent intent = new Intent(context, AmbilFotoActivity.class);
		//startActivityForResult(intent, REQUEST_FROM_CAMERA);
	}

	public void titleSetText(String title) {
		main_title.setText(title);
	}

	public void displayView(int position) {
		menu_selected = position;

		closeSoftKeyboard();
		Fragment fragment = null;
		drawer.closeDrawer(GravityCompat.START);
		switch (position) {

			case 5: {
				next_page_informasi_list = 1;
				informasilist = new ArrayList<>();
				informasiAdapter = new InformasiAdapter(context, informasilist);

				fragment = new InformasiFragment();

				break;
			}
			case 11: {
				fragment = new ProfileFragment();

				break;
			}
			default:

				break;
		}


		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}
	}
	
	@Override
    protected void onDestroy() {
		try {
			unregisterReceiver(mHandleLoadListInformasiReceiver);
			unregisterReceiver(mHandleReloadInformasiReceiver);
			unregisterReceiver(mHandleEditDataProfileReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        super.onDestroy();
    }
	
	@Override
	protected void onPause() {
		try {
			unregisterReceiver(mHandleLoadListInformasiReceiver);
			unregisterReceiver(mHandleReloadInformasiReceiver);
			unregisterReceiver(mHandleEditDataProfileReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.onPause();
	}

	@Override
	protected void onResume() {

		registerReceiver(mHandleLoadListInformasiReceiver, new IntentFilter("elapor.application.com.elapor.LOAD_DATA_INFORMASI"));
		registerReceiver(mHandleReloadInformasiReceiver, new IntentFilter("elapor.application.com.elapor.RELOAD_DATA_INFORMASI"));
		registerReceiver(mHandleEditDataProfileReceiver, new IntentFilter("elapor.application.com.elapor.EDIT_DATA_PROFILE"));



		super.onResume();
	}

	private final BroadcastReceiver mHandleEditDataProfileReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Boolean success = intent.getBooleanExtra("success", false);
			String message = intent.getStringExtra("message");
			if(success) {
				setSignIn();
				displayView(11);
			}

			progDailog.dismiss();
			text_informasi.setText(message);
			text_title.setText(success?"BERHASIL":"GAGAL");
			dialog_informasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog_informasi.show();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data_intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data_intent);

		String fileName = new SimpleDateFormat("yyyyMMddhhmmss'.jpg'").format(new Date());
		String dest = CommonUtilities.getOutputPath(context, "images")+File.separator+fileName;

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_FROM_DETAIL_INFORMASI: {
					displayView(5);

					break;
				}
				case Crop.REQUEST_CROP: {
					mImageCaptureUri = Crop.getOutput(data_intent);
					ProfileFragment.image_profile.setImageURI(Crop.getOutput(data_intent));

					break;
				}
				case REQUEST_FROM_CAMERA: {
					CommonUtilities.compressImage(context, data_intent.getStringExtra("path"), dest);
					mImageCaptureUri = Uri.fromFile(new File(dest));
					beginCrop(mImageCaptureUri);


					break;
				}
				case REQUEST_FROM_GALLERY: {
					Uri selectedUri = data_intent.getData();
					CommonUtilities.compressImage(context, GalleryFilePath.getPath(context, selectedUri), dest);
					mImageCaptureUri = Uri.fromFile(new File(dest));
					selectedPath[0] = dest;

					refreshGallery(new File(selectedPath[0]));

					tipe_file = "G";
					isOpenDialog = true;
					openDialogLaporan();

					break;

				}
				case REQUEST_FROM_KIRIM_LAPORAN: {
					next_page_informasi_list = 1;
					informasilist = new ArrayList<>();
					informasiAdapter.notifyDataSetChanged();
					LoadDataInformasi();

					break;

				}
			}
		}
	}

	private void refreshGallery(File file) {
		Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(Uri.fromFile(file));
		sendBroadcast(mediaScanIntent);
	}

	public void openDialogLaporan() {
		dialog_laporan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_laporan.show();
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

				ReloadDataInformasi();
			}
		}
	}

	private final BroadcastReceiver mHandleReloadInformasiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (menu_selected == 5) {
				next_page_informasi_list = 1;
				informasilist = new ArrayList<>();
				informasiAdapter.notifyDataSetChanged();
				LoadDataInformasi();
			}
		}
	};

	private final BroadcastReceiver mHandleLoadListInformasiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Boolean success = intent.getBooleanExtra("success", false);

			ArrayList<informasi_list> temp = intent.getParcelableArrayListExtra("data_informasi_list");
			ArrayList<informasi> result = temp.get(0).getListData();

			for (informasi flist : result) {
				informasilist.add(flist);
			}

			InformasiFragment.loading.setVisibility(View.GONE);
			if(!success) {
				InformasiFragment.retry.setVisibility(View.VISIBLE);
			} else {
				informasiAdapter.UpdateInformasiAdapter(informasilist);
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void ReloadDataInformasi() {
		next_page_informasi_list = 1;
		informasilist = new ArrayList<>();
		informasiAdapter.notifyDataSetChanged();
		LoadDataInformasi();
	}

	public void LoadDataInformasi() {
		new LoadDataInformasi().execute();
	}

	public class LoadDataInformasi extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if(InformasiFragment.loading!=null) InformasiFragment.loading.setVisibility(View.VISIBLE);
			if(InformasiFragment.retry!=null) InformasiFragment.retry.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(String... urls) {

			ArrayList<informasi> result = null;
			String url = CommonUtilities.SERVER_URL + "/store/androidInformasiDataStore.php";

			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("user_id", userLogin.getId()+""));
			params.add(new BasicNameValuePair("page", next_page_informasi_list+""));

			JSONObject json = new JSONParser().getJSONFromUrl(url, params, null);
			if(json!=null) {
				try {
					result = new ArrayList<>();
					next_page_informasi_list = json.isNull("next_page") ? next_page_informasi_list : json.getInt("next_page");
					JSONArray topics = json.isNull("topics")?null:json.getJSONArray("topics");
					for (int i=0; i<topics.length(); i++) {
						JSONObject rec = topics.getJSONObject(i);

						int id = rec.isNull("id")?0:rec.getInt("id");
						String tanggal = rec.isNull("tanggal")?"":rec.getString("tanggal");
						String judul = rec.isNull("judul")?"":rec.getString("judul");
						String header = rec.isNull("header")?"":rec.getString("header");
						String konten = rec.isNull("konten")?"":rec.getString("konten");
						String link_download = rec.isNull("link_download")?"":rec.getString("link_download");
						String gambar = rec.isNull("gambar")?"":rec.getString("gambar");
						String status = rec.isNull("status")?"":rec.getString("status");
						String pic = rec.isNull("pic")?"":rec.getString("pic");
						String no_temuan = rec.isNull("no_temuan")?"":rec.getString("no_temuan");
						String tipe = rec.isNull("tipe")?"":rec.getString("tipe");

						result.add(new informasi(id, no_temuan, tanggal, judul, header, konten, link_download, gambar, status, pic, tipe));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Boolean success = result!=null;
			if(result==null) result = new ArrayList<>();
			ArrayList<informasi_list> temp = new ArrayList<>();
			temp.add(new informasi_list(result));

			Intent i = new Intent("elapor.application.com.elapor.LOAD_DATA_INFORMASI");
			i.putExtra("data_informasi_list", temp);
			i.putExtra("success", success);
			sendBroadcast(i);

			return null;
		}
	}



	public void selectPihak() {
		dialog_pilih_pihak.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_pilih_pihak.show();
	}

	public void LoadDataProfile() {

		mImageCaptureUri = null;
		ProfileFragment.edit_nama.setText(userLogin.getNama());
		ProfileFragment.edit_bagian.setText(userLogin.getBagian());
		ProfileFragment.edit_userid.setText(userLogin.getUserid());
		ProfileFragment.edit_pihak.setText(userLogin.getPihak());
		ProfileFragment.edit_alamat.setText(userLogin.getAlamat());
		ProfileFragment.edit_nohp.setText(userLogin.getPhone());
		ProfileFragment.edit_comdev.setText(userLogin.getComdiv());

		ProfileFragment.image_profile.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		imageLoader.displayImage(CommonUtilities.SERVER_URL+"/uploads/member/"+userLogin.getPhoto(), ProfileFragment.image_profile, imageOptionsUser);
	}

	public void simpanDataProfile() {
		new simpanDataProfile().execute();
	}

	public class simpanDataProfile extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progDailog.setMessage("Update...");
			progDailog.show();
		}

		@Override
		protected Void doInBackground(String... urls) {

			boolean success = false;
			String message = "Tidak bisa kontak ke server.";
			String url = CommonUtilities.SERVER_URL + "/store/androidSaveProfile.php";

			JSONObject jObj = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("id_user", new StringBody(userLogin.getId()+""));
				reqEntity.addPart("nama", new StringBody(ProfileFragment.edit_nama.getText().toString()));
				reqEntity.addPart("bagian", new StringBody(ProfileFragment.edit_bagian.getText().toString()));
				reqEntity.addPart("userid", new StringBody(ProfileFragment.edit_userid.getText().toString()));
				reqEntity.addPart("pihak", new StringBody(ProfileFragment.edit_pihak.getText().toString()));
				reqEntity.addPart("alamat", new StringBody(ProfileFragment.edit_alamat.getText().toString()));
				reqEntity.addPart("phone", new StringBody(ProfileFragment.edit_nohp.getText().toString()));
				reqEntity.addPart("comdiv", new StringBody(ProfileFragment.edit_comdev.getText().toString()));

				reqEntity.addPart("ganti_password", new StringBody(ProfileFragment.checkbox_ganti_password.isChecked()?"Y":""));
				reqEntity.addPart("password_lama", new StringBody(ProfileFragment.edit_old_password.getText().toString()));
				reqEntity.addPart("password_baru", new StringBody(ProfileFragment.edit_password.getText().toString()));
				reqEntity.addPart("password_konf", new StringBody(ProfileFragment.edit_konfirmasi.getText().toString()));

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

						userLogin = new user(
								jObj.isNull("id")?0:jObj.getInt("id"),
								jObj.isNull("nama")?"":jObj.getString("nama"),
								jObj.isNull("bagian")?"":jObj.getString("bagian"),
								jObj.isNull("userid")?"":jObj.getString("userid"),
								jObj.isNull("pihak")?"":jObj.getString("pihak"),
								jObj.isNull("alamat")?"":jObj.getString("alamat"),
								jObj.isNull("phone")?"":jObj.getString("phone"),
								jObj.isNull("comdiv")?"":jObj.getString("comdiv"),
								jObj.isNull("photo")?"":jObj.getString("photo")
						);

						CommonUtilities.setLoginUser(context, userLogin);

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Intent i = new Intent("elapor.application.com.elapor.EDIT_DATA_PROFILE");
			i.putExtra("success", success);
			i.putExtra("message", message);
			sendBroadcast(i);

			return null;
		}

	}


	class prosesSignOut extends AsyncTask<String, Void, JSONObject> {

		boolean success;
		String message;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			progDailog.setMessage("Sign Out...");
			progDailog.show();
		}

		@Override
		protected JSONObject doInBackground(String... urls) {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("id_user", userLogin.getId()+""));
			String url = CommonUtilities.SERVER_URL + "/store/androidSignout.php";
			JSONObject json = new JSONParser().getJSONFromUrl(url, params, null);

			return json;
		}

		@Deprecated
		@Override
		protected void onPostExecute(JSONObject result) {

			progDailog.dismiss();

			success = false;
			message = "Gagal melakukan sign out. Silahkan coba lagi!";
			if(result!=null) {
				try {
					success = result.isNull("success")?false:result.getBoolean("success");
					message = result.isNull("message")?message:result.getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(success) {
				dialog_logout.dismiss();
				userLogin = new user(0, "", "", "", "", "", "", "", "");
				CommonUtilities.setLoginUser(context, userLogin);
				checkGcmRegid();
				//menu_selected = 0;
				//setSignIn();
				Intent intent = new Intent(context, SignInActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

	private void checkGcmRegid() {
		String registrationId = getString(R.string.msg_token_fmt, FirebaseInstanceId.getInstance().getToken());
		registrationId = registrationId.equalsIgnoreCase("null") ? "" : registrationId;
		Log.d("Registration id", registrationId);
		//Toast.makeText(context, registrationId, Toast.LENGTH_SHORT).show();
		if (registrationId.length() > 0) {
			new prosesUpdateRegisterRegId(registrationId).execute();
		}
	}

	class prosesUpdateRegisterRegId extends AsyncTask<String, Void, JSONObject> {

		String registrationId;
		boolean success;
		String message;

		prosesUpdateRegisterRegId(String registrationId) {
			this.registrationId = registrationId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... urls) {
			return ServerUtilities.register(context, registrationId, userLogin.getId(), CommonUtilities.getGuestId(context));
		}

		@Deprecated
		@Override
		protected void onPostExecute(JSONObject result) {

			success = false;
			message = "Gagal melakukan proses take action. Cobalah lagi.";
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
				new prosesUpdateRegisterRegId(registrationId).execute();
			}
		}
	}

	private void setSignIn() {
		if(userLogin.getId()>0) {
			imageLoader.displayImage(CommonUtilities.SERVER_URL+"/uploads/member/"+userLogin.getPhoto(), avatar, imageOptionsUser);
		} else {
			avatar.setImageResource(R.drawable.userdefault);
		}

		name_avatar.setText(userLogin.getNama());
		nav_logout.setText(userLogin.getId()==0?"Login":"Logout");
		nav_profil.setText(userLogin.getId()==0?"Registrasi":"Profil");

		displayView(menu_selected);
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (menu_selected > 0) {
					displayView(0);
					return false;
				}

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void openDetailInformasi(informasi data) {
		//Toast.makeText(context, "OPEN INFORMASI: "+data.getJudul(), Toast.LENGTH_LONG).show();

		if(data.getTipe().equalsIgnoreCase("G")) {
			Intent intent = new Intent(context, DetailInformasiActivity.class);
			intent.putExtra("gallery", data.getLink_download());
			startActivity(intent);
		}

		if(data.getTipe().equalsIgnoreCase("V")) {
			Intent intent = new Intent(context, VideoPlayerActivity.class);
			intent.putExtra("video", data.getLink_download());
			startActivity(intent);
		}

	}

	void closeSoftKeyboard() {
		View view = getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public void buatLaporan() {
		selectImage();
	}

	public void openDetailPoster(View poster, int position, ArrayList<pictureFacer> pics) {
		//showDetailGallery = true;
		pictureBrowserFragment browser = pictureBrowserFragment.newInstance(pics,position,context);

		// Note that we need the API version check here because the actual transition classes (e.g. Fade)
		// are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
		// ARE available in the support library (though they don't do anything on API < 21)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//browser.setEnterTransition(new Slide());
			//browser.setExitTransition(new Slide()); uncomment this to use slide transition and comment the two lines below
			browser.setEnterTransition(new Fade());
			browser.setExitTransition(new Fade());
		}

		getSupportFragmentManager()
				.beginTransaction()
				//.addSharedElement(poster, position+"picture")
				.add(R.id.displayContainer, browser)
				.addToBackStack(null)
				.commit();
	}

	void openSoftKeyboard() {
		View view = getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}

	private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException {
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE,
					width, height, null);
		} catch (IllegalArgumentException Illegalargumentexception) {
			return null;
		}

		int bitMatrixWidth = bitMatrix.getWidth();
		int bitMatrixHeight = bitMatrix.getHeight();
		int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

		int colorWhite = 0xFFFFFFFF;
		int colorBlack = 0xFF000000;

		for (int y = 0; y < bitMatrixHeight; y++) {
			int offset = y * bitMatrixWidth;
			for (int x = 0; x < bitMatrixWidth; x++) {
				pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

		bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
		return bitmap;
	}

	/**
	 *
	 * @param holder The ViewHolder for the clicked picture
	 * @param position The position in the grid of the picture that was clicked
	 * @param pics An ArrayList of all the items in the Adapter
	 */
	@Override
	public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {
		//showDetailGallery = true;
		pictureBrowserFragment browser = pictureBrowserFragment.newInstance(pics,position,context);

		// Note that we need the API version check here because the actual transition classes (e.g. Fade)
		// are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
		// ARE available in the support library (though they don't do anything on API < 21)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//browser.setEnterTransition(new Slide());
			//browser.setExitTransition(new Slide()); uncomment this to use slide transition and comment the two lines below
			browser.setEnterTransition(new Fade());
			browser.setExitTransition(new Fade());
		}

		getSupportFragmentManager()
				.beginTransaction()
				.addSharedElement(holder.picture, position+"picture")
				.add(R.id.displayContainer, browser)
				.addToBackStack(null)
				.commit();

	}

	@Override
	public void onPicClicked(String pictureFolderPath, String folderName) {

	}

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();

        if (!addPermission(permissionsList, Manifest.permission.INTERNET))
            permissionsNeeded.add("INTERNET");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE))
            permissionsNeeded.add("ACCESS_NETWORK_STATE");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        //if (!addPermission(permissionsList, Manifest.permission.FLASHLIGHT))
        ////permissionsNeeded.add("FLASHLIGHT");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                //showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                //@Override
                //public void onClick(DialogInterface dialog, int which) {*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
                //}
                //});
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    // All Permissions Granted
                    Intent intent = new Intent(context, SignInActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // Permission Denied
                    Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}