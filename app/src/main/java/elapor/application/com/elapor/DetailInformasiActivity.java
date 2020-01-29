package elapor.application.com.elapor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import customfonts.MyTextView;
import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.libs.TouchImageView;
import elapor.application.com.model.gallery;

public class DetailInformasiActivity extends AppCompatActivity {

	Context context;
	String data_gallery;

	MyTextView download;
	Dialog dialog_save_image;
	TextView save_image;

	TouchImageView imageGalleryDetail;

	Dialog dialog_informasi;
	MyTextView btn_ok;
	MyTextView text_title;
	MyTextView text_message;

	ImageLoader imageLoader;
	DisplayImageOptions imageOptionsGallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_informasi);

		context = DetailInformasiActivity.this;

		CommonUtilities.initImageLoader(context);
		imageLoader = ImageLoader.getInstance();

		imageOptionsGallery = CommonUtilities.getOptionsImage(R.drawable.logo_grayscale_large, R.drawable.logo_grayscale_large);

		imageGalleryDetail = findViewById(R.id.imgDisplay);
		imageGalleryDetail.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				showDialogSaveAs();
				return false;
			}
		});

		dialog_informasi = new Dialog(context);
		dialog_informasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_informasi.setCancelable(true);
		dialog_informasi.setContentView(R.layout.informasi_dialog);

		btn_ok =  dialog_informasi.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog_informasi.dismiss();
			}
		});

		text_title =  dialog_informasi.findViewById(R.id.text_title);
		text_message =  dialog_informasi.findViewById(R.id.text_dialog);

		download =  findViewById(R.id.download);

		dialog_save_image = new Dialog(context);
		dialog_save_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_save_image.setContentView(R.layout.save_image_dialog);
		save_image = (TextView) dialog_save_image.findViewById(R.id.txtSaveImage);

		download.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogSaveAs();
			}
		});
		save_image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_save_image.dismiss();

				imageLoader.loadImage(CommonUtilities.SERVER_URL + "/uploads/produk/" + data_gallery, imageOptionsGallery, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						saveImageFile(loadedImage, data_gallery);
						super.onLoadingComplete(imageUri, view, loadedImage);
					}
				});

			}
		});

		if(savedInstanceState==null) {
			data_gallery = getIntent().getStringExtra("gallery");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		//Toast.makeText(context, CommonUtilities.SERVER_URL + "/uploads/gallery/" + data_gallery.getGambar(), Toast.LENGTH_LONG).show();
		imageLoader.loadImage(CommonUtilities.SERVER_URL + "/uploads/laporan/" + data_gallery, imageOptionsGallery, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				imageGalleryDetail.setImageBitmap(loadedImage);
			}
		});
	}


	@Override
	protected void onPause() {

		super.onPause();

	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putSerializable("gallery", data_gallery);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		data_gallery = savedInstanceState.getString("gallery");
	}


	public void showDialogSaveAs() {
		dialog_save_image.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_save_image.show();
	}

	public String saveImageFile(Bitmap bitmap, String url) {
		String filename = null;
		Boolean is_saved = false;
		String error_message = "Failed to save image.";
		try {
			filename = getPathFilename();
			//if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			//File sdCard = Environment.getExternalStorageDirectory();
			//File dir = new File(sdCard.getAbsolutePath() + File.pathSeparator + "Pictures");
			//if (!dir.exists()) {
			//dir.mkdirs();
			//}

			File file = new File(filename, url);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			FileOutputStream f = null;

			f = new FileOutputStream(file);
			if (f != null) {
				f.write(baos.toByteArray());
				f.flush();
				f.close();
			}

			Toast.makeText(context, "File saved to "+filename+"/"+url, Toast.LENGTH_LONG).show();
			is_saved = true;

			MediaScannerConnection.scanFile(context,
					new String[] { file.toString() }, null,
					new MediaScannerConnection.OnScanCompletedListener() {

						@Override
						public void onScanCompleted(String path, Uri uri) {
							// TODO Auto-generated method stub

						}
					});
			//}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			error_message = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			error_message = e.getMessage();
			e.printStackTrace();
		}

		if(!is_saved) {
			text_message.setText(error_message);
			text_title.setText("ERROR");
			dialog_informasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog_informasi.show();
			//alert.showAlertDialog(context, "Kesalahan", error_message, is_saved);
		}

		return filename;
	}

	private String getPathFilename() {
		File file = new File(Environment.getExternalStorageDirectory().getPath(), "Pictures");
		if (!file.exists()) {
			file.mkdirs();
		}


		file = new File(file.getPath(), getResources().getString(R.string.app_name));
		if (!file.exists()) {
			file.mkdirs();
		}
		//String uriSting = (file.getAbsolutePath() + "/" + url);

		return file.getAbsolutePath();
	}
}
