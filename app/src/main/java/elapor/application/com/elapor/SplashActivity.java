package elapor.application.com.elapor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.libs.JSONParser;
import elapor.application.com.model.user;
import elapor.application.com.model.version;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    Context context;
    version app_ver;
    boolean is_checked = false;
    boolean is_lewat = false;
    Handler handler = new Handler();

    Dialog dialog_update;
    MyTextView text_dialog;
    MyTextView btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setAnimation();
        context = SplashActivity.this;
        app_ver = CommonUtilities.getAppVersion(context);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //******change activity here*******
        dialog_update = new Dialog(context);
        dialog_update.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_update.setCancelable(false);
        dialog_update.setContentView(R.layout.update_dialog);

        btn_ok = (MyTextView) dialog_update.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                //Toast.makeText(context, appPackageName, Toast.LENGTH_SHORT).show();
                dialog_update.dismiss();
                finish();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        text_dialog = (MyTextView) dialog_update.findViewById(R.id.text_dialog);

        new prosesCekUpdate().execute();
        handler.postDelayed(mUpdateTimeTask, SPLASH_TIME_OUT);
    }

    public Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            handler.removeCallbacks(this);
            is_lewat = true;

            if(is_checked) {
                uiCallback.sendEmptyMessage(0);
            }
        }
    };

    private void setAnimation() {
        /*ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(2000);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(2000);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(2000);
        animatorSet.start();*/

        findViewById(R.id.imagelogo2).setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade);
        findViewById(R.id.imagelogo2).startAnimation(anim);

    }

    //ui thread callback handler
    private Handler uiCallback = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg) {
            user userLogin = CommonUtilities.getLoginUser(context);

            Intent i = new Intent(context, userLogin.getId()==0?SignInActivity.class:MainActivity.class);
            startActivity(i);
            finish();
        }
    };

    class prosesCekUpdate extends AsyncTask<String, Void, Boolean> {

        boolean success;
        String message;

        @Override
        protected Boolean doInBackground(String... urls) {
            JSONObject result;

            String url = CommonUtilities.SERVER_URL + "/store/cekUpdateAplikasi.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("version_no", app_ver.getNo()));
            params.add(new BasicNameValuePair("version_name", app_ver.getNama()));

            result = new JSONParser().getJSONFromUrl(url, params, null);

            success = false;
            message = "Gagal cek update.";
            if(result!=null) {
                try {
                    success = result.isNull("success")?false:result.getBoolean("success");
                    message = result.isNull("message")?message:result.getString("message");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            is_checked = true;
            if(success) {
                //if(!is_lewat) {
                //    handler.removeCallbacks(mUpdateTimeTask);
                //}

                //dialog_update.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog_update.show();
            } else  if(is_lewat) {
                uiCallback.sendEmptyMessage(0);
            }

        }
    }
}