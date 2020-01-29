package elapor.application.com.elapor;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import customfonts.MyTextView;
import elapor.application.com.libs.CommonUtilities;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, OnPreparedListener {

	Context context;
	FrameLayout layout;
	SurfaceHolder holder;
	SurfaceView surface;
	ProgressBar spinner;
	String vidAddress;

	RelativeLayout videoView;
	ImageView btnPlay;
	SeekBar seekbar;
	MyTextView timeTrack;
	MyTextView timeView;

	Handler mHandler;
	Handler mHidden;

	MediaPlayer mp;

	long progress = 0;
	long total = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_teaser);
		context = VideoPlayerActivity.this;

		layout = findViewById(R.id.layout);
		surface = findViewById(R.id.surface);

		videoView = findViewById(R.id.videoView);
		btnPlay   = (ImageView) findViewById(R.id.btnPlay);
		seekbar   = (SeekBar) findViewById(R.id.seekBar);
		timeTrack = (MyTextView) findViewById(R.id.timeTrack);
		timeView  = (MyTextView) findViewById(R.id.timeView);
		spinner   = (ProgressBar) findViewById(R.id.spinner);

		mHandler  = new Handler();
		mHidden   = new Handler();

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progres, boolean fromTouch) {
				timeTrack.setText(CommonUtilities.milliSecondsToTimer(progres));
			}

			/**
			 * When user starts moving the progress handler
			 * */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				if(mp.isPlaying()) {
					mHandler.removeCallbacks(mUpdateTimeTask);
					mp.pause();
				}
				mHidden.removeCallbacks(mHiddenProgresbar);
			}

			/**
			 * When user stops moving the progress hanlder
			 * */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				progress = (long) seekBar.getProgress();
				timeTrack.setText(CommonUtilities.milliSecondsToTimer(progress));

				if(!mp.isPlaying()) {
					mp.seekTo((int) progress);
					mp.start();
					updateProgressBar();
				}

				hiddenProgressBar();
			}
		});

		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				videoView.setVisibility(View.VISIBLE);

				mHidden.removeCallbacks(mHiddenProgresbar);
				hiddenProgressBar();
			}
		});

		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				playAndStopVideo();

				mHidden.removeCallbacks(mHiddenProgresbar);
				hiddenProgressBar();
			}
		});

		if(savedInstanceState==null) {
			vidAddress  = CommonUtilities.SERVER_URL+"/uploads/laporan/"+getIntent().getStringExtra("video");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.e("On Resume: ", "Progress: "+progress);
		Uri vidUri = Uri.parse(vidAddress.replaceAll("\\s", "%20"));

		try {
			mp = new MediaPlayer();
			mp.setDataSource(context, vidUri);
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.setOnInfoListener(onInfoToPlayStateListener);
			mp.setOnPreparedListener(this);
			mp.prepareAsync();

		} catch(Exception e){
			e.printStackTrace();
		}

		layout.removeAllViews();
		layout.addView(surface);
		holder = surface.getHolder();
		holder.addCallback(this);

		hiddenProgressBar();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("video_teaser", vidAddress);
		savedInstanceState.putLong("progress", progress);
		Log.e("onSaveInstanceState", "Progress: "+progress);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		vidAddress = savedInstanceState.getString("video_teaser");
		progress    = savedInstanceState.getLong("progress");
		Log.e("onRestoreInstanceState", "Progress: "+progress);

	}

	private final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
				spinner.setVisibility(View.GONE);
			}
			if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
				spinner.setVisibility(View.VISIBLE);
			}
			if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
				spinner.setVisibility(View.VISIBLE);
			}
			return false;
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Log.e("Surface Change", "Changed");

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {

		Log.e("Surface Create", "Create");
		if(mp!=null) {
			mp.setDisplay(arg0);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		Log.e("surface hancur", ":hancur...");



	}

	@Override
	protected void onPause() {
		super.onPause();

		mHandler.removeCallbacks(mUpdateTimeTask);
		mHidden.removeCallbacks(mHiddenProgresbar);
		if(mp.isPlaying()) {
			mp.stop();
		}

		holder.removeCallback(this);
		Log.e("Pause", "Pause Coy!");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mHandler.removeCallbacks(mUpdateTimeTask);
		mHidden.removeCallbacks(mHiddenProgresbar);
		if(mp.isPlaying()) {
			mp.stop();
		}
		holder.removeCallback(this);
		Log.e("Destroy", "Destroy Coy!");
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		//can play
		playAndStopVideo();
	}

	void playAndStopVideo() {
		if(mp.isPlaying()) {
			mp.pause();
			mHandler.removeCallbacks(mUpdateTimeTask);
			btnPlay.setImageResource(R.drawable.btn_play);
		} else {
			total = mp.getDuration();
			timeView.setText(CommonUtilities.milliSecondsToTimer(total));
			seekbar.setMax((int) total);
			seekbar.setProgress((int) progress);

			//Toast.makeText(context, "--> "+progress, Toast.LENGTH_LONG).show();
			mp.seekTo((int) progress);
			mp.start();

			btnPlay.setImageResource(R.drawable.btn_pause);
			updateProgressBar();
		}
	}

	public  void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	public Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			progress = mp.getCurrentPosition();
			seekbar.setProgress((int) progress);
			timeTrack.setText(CommonUtilities.milliSecondsToTimer(progress));

			if(CommonUtilities.milliSecondsToTimer(progress).equalsIgnoreCase(CommonUtilities.milliSecondsToTimer(total))) {

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}

				finish();
			} else {
				mHandler.postDelayed(this, 100);
			}
		}
	};

	public  void hiddenProgressBar() {
		mHidden.postDelayed(mHiddenProgresbar, 10000);
	}

	public Runnable mHiddenProgresbar = new Runnable() {
		public void run() {

			videoView.setVisibility(View.GONE);
			mHidden.removeCallbacks(this);
		}
	};
}
