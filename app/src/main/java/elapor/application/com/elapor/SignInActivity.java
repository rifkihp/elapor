package elapor.application.com.elapor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.Toast;

import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.libs.JSONParser;
import elapor.application.com.libs.MCrypt;
import elapor.application.com.model.remember;
import elapor.application.com.model.user;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customfonts.MyEditText;
import customfonts.MyTextView;

public class SignInActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    MyTextView signin, signup, forgotpass;
    CheckBox checkboremember;

    Dialog dialog_informasi;
    MyTextView btn_ok;
    MyTextView text_title;
    MyTextView text_informasi;

    MyEditText userid;
    MyEditText password;

    ProgressDialog progDailog;
    Boolean success;
    String message;
    user data;
    Context context;

    int menu_selected;
    boolean from_checkout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SignInActivity.this;
        setContentView(R.layout.signin);

        if (Build.VERSION.SDK_INT >= 23) {
            insertDummyContactWrapper();
        }

        remember data_remember = CommonUtilities.getRememberPassword(context);

        signin     = findViewById(R.id.signin1);
        signup     = findViewById(R.id.signup);
        forgotpass = findViewById(R.id.forgotpass);
        checkboremember = findViewById(R.id.checkbocremember);
        userid     = findViewById(R.id.edit_userid);
        password   = findViewById(R.id.edit_password);

        userid.setText(data_remember.getEmail());
        password.setText(data_remember.getPassword());
        
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProsesRegistrasiActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkboremember.isChecked()) {
                        remember data = new remember(userid.getText().toString(), password.getText().toString(), (checkboremember.isChecked()?"Y":"N"));
                        CommonUtilities.setRememberPassword(context, data);
                    }
                    new prosesSingIn().execute();
                }
        });



        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        progDailog = new ProgressDialog(context);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);

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
        text_informasi =  dialog_informasi.findViewById(R.id.text_dialog);

        if(savedInstanceState==null) {
            menu_selected = getIntent().getIntExtra("menu_selected", 0);
            from_checkout = getIntent().getBooleanExtra("from_checkout", false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2:
                    if(data.getBooleanExtra("is_login", false)) {
                        Intent intent = new Intent();
                        intent.putExtra("is_login", true);
                        intent.putExtra("menu_selected", menu_selected);
                        intent.putExtra("from_checkout", from_checkout);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    break;
            }
        }
    }

    class prosesSingIn extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog.setMessage("Login...");
            progDailog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Boolean doInBackground(String... urls) {
            JSONParser token_json = new JSONParser();
            JSONObject token = token_json.getJSONFromUrl(CommonUtilities.SERVER_URL + "/store/token.php", null, null);
            String cookies = token_json.getCookies();

            String security_code = "";
            try {
                security_code = token.isNull("security_code")?"":token.getString("security_code");
                MCrypt mCrypt = new MCrypt();
                security_code = new String(mCrypt.decrypt(security_code));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            success = false;
            message = "Proses masuk gagal. Cobalah lagi.";

            if(security_code.length()>0) {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid.getText().toString()));
                params.add(new BasicNameValuePair("password", password.getText().toString()));
                params.add(new BasicNameValuePair("security_code", security_code));

                String url = CommonUtilities.SERVER_URL + "/store/androidSignin.php";
                JSONObject result = new JSONParser().getJSONFromUrl(url, params, cookies);

                if (result != null) {
                    try {
                        success = result.isNull("success") ? false : result.getBoolean("success");
                        message = result.isNull("message") ? message : result.getString("message");
                        if (success) {
                            data = new user(
                                    result.isNull("id")?0:result.getInt("id"),
                                    result.isNull("nama")?"":result.getString("nama"),
                                    result.isNull("bagian")?"":result.getString("bagian"),
                                    result.isNull("userid")?"":result.getString("userid"),
                                    result.isNull("pihak")?"":result.getString("pihak"),
                                    result.isNull("alamat")?"":result.getString("alamat"),
                                    result.isNull("phone")?"":result.getString("phone"),
                                    result.isNull("comdiv")?"":result.getString("comdiv"),
                                    result.isNull("photo")?"":result.getString("photo")
                            );

                            CommonUtilities.setLoginUser(context, data);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progDailog.dismiss();
            if(!success) {
                text_informasi.setText(message);
                text_title.setText(message.equalsIgnoreCase("Akun belum diaktivasi admin!")?"MENUNGGU":"KESALAHAN");
                dialog_informasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog_informasi.show();
            } else {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("RECORD_AUDIO");

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
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

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
