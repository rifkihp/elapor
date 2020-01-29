package elapor.application.com.libs;

import android.content.Context;
import android.util.Log;

import elapor.application.com.libs.CommonUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ServerUtilities {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

	public static JSONObject readAndFragmentSmartCity(Context context, String datetime, String message, String attachment, String pelapor, String tipe_file, double lat, double lng) {

		JSONObject result = null;
		String url = CommonUtilities.SERVER_URL  + "/store/androidSimpanLaporan.php";

		int CHUNK_SIZE = 1024 * 1024 * 1; //3MB
		File willBeRead = new File ( attachment );
		String onlyNameFile = willBeRead.getName();
		int FILE_SIZE = (int) willBeRead.length();
		//System.out.println("Total File Size: "+FILE_SIZE);
		int NUMBER_OF_CHUNKS = 0;
		byte[] temporary = null;
		int totalBytesRead = 0;
		Boolean isLastPart = false;
		InputStream inStream = null;
		try {
			try {
				inStream = new BufferedInputStream( new FileInputStream( willBeRead ));
				while ( totalBytesRead < FILE_SIZE ) {
					result = null;

					int bytesRemaining = FILE_SIZE-totalBytesRead;
					if ( bytesRemaining < CHUNK_SIZE ) {
						CHUNK_SIZE = bytesRemaining;
						Log.d("CHUNK_SIZE: ", CHUNK_SIZE+"");
						isLastPart = true;
					}

					temporary = new byte[CHUNK_SIZE]; //Temporary Byte Array
					int bytesRead = inStream.read(temporary, 0, CHUNK_SIZE);
					if ( bytesRead > 0) { // If bytes read is not empty
						totalBytesRead += bytesRead;
						NUMBER_OF_CHUNKS++;
					}
					System.out.println("Total Bytes Read: "+totalBytesRead);

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);

					MultipartEntity reqEntity = new MultipartEntity();
					ByteArrayInputStream arrayStream = new ByteArrayInputStream(temporary);
					reqEntity.addPart("ax_file_input", new InputStreamBody(arrayStream, attachment));
					reqEntity.addPart("ax-file-path", new StringBody("../uploads/laporan/"));
					reqEntity.addPart("ax-allow-ext", new StringBody(""));
					reqEntity.addPart("ax-file-name", new StringBody(onlyNameFile));
					reqEntity.addPart("ax-max-file-size", new StringBody("10G"));
					reqEntity.addPart("ax-start-byte", new StringBody(String.valueOf(CHUNK_SIZE)));
					reqEntity.addPart("ax-number-of-chunk", new StringBody(String.valueOf(NUMBER_OF_CHUNKS)));
					reqEntity.addPart("ax-last-chunk", new StringBody(isLastPart?"true":"false"));

					if(isLastPart) {
						reqEntity.addPart("user_id", new StringBody((CommonUtilities.getLoginUser(context).getId() + "")));
						reqEntity.addPart("penjelasan", new StringBody((message)));
						reqEntity.addPart("tipe_file", new StringBody((tipe_file)));
						reqEntity.addPart("lat", new StringBody(lat + ""));
						reqEntity.addPart("lng", new StringBody(lng + ""));
						reqEntity.addPart("datetime", new StringBody(datetime));

						if (pelapor != null) {
							File file = new File(pelapor);
							if (file.exists()) {
								FileBody bin_gamber = new FileBody(file);
								reqEntity.addPart("pelapor", bin_gamber);
							}
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

					Log.d("JSON_RESPONSE: ", sb.toString());
					result = new JSONObject(sb.toString());
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				inStream.close();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	public static JSONObject kirimLaporan(Context context, String datetime, String message, String[] attachment, String tipe_file, double lat, double lng) {
		try {
			String url = CommonUtilities.SERVER_URL + "/store/androidSimpanLaporan.php";

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("user_id", new StringBody((CommonUtilities.getLoginUser(context).getId() + "")));
			reqEntity.addPart("penjelasan", new StringBody((message)));
			reqEntity.addPart("tipe_file", new StringBody((tipe_file)));
			reqEntity.addPart("lat", new StringBody(lat + ""));
			reqEntity.addPart("lng", new StringBody(lng + ""));
			reqEntity.addPart("datetime", new StringBody(datetime));

			if (attachment[0] != null) {
				File file = new File(attachment[0]);
				if (file.exists()) {
					FileBody bin_gamber = new FileBody(file);
					reqEntity.addPart("attachment", bin_gamber);
				}
			}

			if (attachment[1] != null) {
				File file = new File(attachment[1]);
				if (file.exists()) {
					FileBody bin_gamber = new FileBody(file);
					reqEntity.addPart("pelapor", bin_gamber);
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

			System.out.println(sb.toString());
			return new JSONObject(sb.toString());

		} catch (JSONException e) {
			e.printStackTrace();

		} catch (IOException ex) {
			ex.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

    public static JSONObject register(final Context context, String regId, int user_id, int guest_id) {
        Log.i(CommonUtilities.TAG, "registering device (regId = " + regId + ")");
        String url = CommonUtilities.SERVER_URL+ "/store/androidRegistrasiRegid.php";
        List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("user_id", user_id+""));
		params.add(new BasicNameValuePair("guest_id", guest_id+""));
		params.add(new BasicNameValuePair("reg_id", regId));
		
		JSONObject result = null;
		JSONParser jpar = new JSONParser();
		
		
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(CommonUtilities.TAG, "Attempt #" + i + " to register");
            //displayMessage(context, context.getString(R.string.server_registering, i, MAX_ATTEMPTS));

            result = jpar.getJSONFromUrl(url, params, null);
            if (result!=null) {

            	boolean success = false;
				try {
					success = result.isNull("success")?false:result.getBoolean("success");
					guest_id = result.isNull("guest_id")?guest_id:result.getInt("guest_id");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	if(success) {
	            	CommonUtilities.setGcm_regid(context, regId);
		            CommonUtilities.setGuestId(context, guest_id);

		            return result;
            	}
            } 

            	// Here we are simplifying and retrying on any error; in a real
	            // application, it should retry only on unrecoverable errors
	            // (like HTTP error code 503).
	            Log.e(CommonUtilities.TAG, "Failed to register on attempt " + i + ": Response NULL!");
	            if (i == MAX_ATTEMPTS) {
	            	break;
	            }

	            try {
	            	Log.d(CommonUtilities.TAG, "Sleeping for " + backoff + " ms before retry");
	                Thread.sleep(backoff);
	            } catch (InterruptedException e1) {
	            	// Activity finished before we complete - exit.
	            	Log.d(CommonUtilities.TAG, "Thread interrupted: abort remaining retries!");
	                Thread.currentThread().interrupt();
	                return null;
	            }
	                // increase backoff exponentially
	                backoff *= 2;
                
        }
        
		return null;        
    }
    
    

    
}