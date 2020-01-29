/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package elapor.application.com.elapor;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.model.agenda;
import elapor.application.com.model.event;
import elapor.application.com.model.informasi;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String tipe  = remoteMessage.getData().get("tipe");
            String event = remoteMessage.getData().get("event");
            String mesg  = remoteMessage.getData().get("message");

            Log.i(TAG, "Received tipe: "+tipe);
            Log.i(TAG, "Received event: "+event);
            Log.i(TAG, "Received message: "+mesg);

            if(tipe.equalsIgnoreCase("informasi")) {

                try {
                    JSONObject rec = new JSONObject(event);
                    int ids = rec.isNull("id")?0:rec.getInt("id");
                    String juduls = rec.isNull("judul")?"":rec.getString("judul");
                    String tanggals = rec.isNull("tanggal")?"":rec.getString("tanggal");
                    String dateline = rec.isNull("dateline")?"":rec.getString("dateline");
                    String lokasis = rec.isNull("lokasi")?"":rec.getString("lokasi");
                    int jumlah_pesertas = rec.isNull("jumlah_atlit")?0:rec.getInt("jumlah_atlit");
                    boolean is_opens = rec.isNull("is_open")?false:rec.getBoolean("is_open");
                    String banners1 = rec.isNull("banner1")?"":rec.getString("banner1");
                    String banners2 = rec.isNull("banner2")?"":rec.getString("banner2");
                    String banners3 = rec.isNull("banner3")?"":rec.getString("banner3");
                    String logos = rec.isNull("logo")?"":rec.getString("logo");

                    event data_events =  new event(ids, juduls, tanggals, lokasis, dateline, jumlah_pesertas, is_opens, banners1, banners2, banners3, logos);

                    rec = new JSONObject(mesg);
                    int id = rec.isNull("id")?null:rec.getInt("id");
                    String judul = rec.isNull("judul")?null:rec.getString("judul");
                    String tanggal = rec.isNull("tanggal")?null:rec.getString("tanggal");
                    String header = rec.isNull("header")?null:rec.getString("header");
                    String konten = rec.isNull("konten")?null:rec.getString("konten");
                    String link_download = rec.isNull("link_download")?null:rec.getString("link_download");
                    String gambar = rec.isNull("gambar")?null:rec.getString("gambar");
                    String status = rec.isNull("status")?null:rec.getString("status");
                    String pic = rec.isNull("pic")?null:rec.getString("pic");
                    String no_temuan = rec.isNull("no_temuan")?null:rec.getString("no_temuan");
                    String tipes = rec.isNull("tipe")?null:rec.getString("tipe");

                    informasi data = new informasi(id, no_temuan, tanggal, judul, header, konten, link_download, gambar, status, pic, tipes);

                    sendInformasiNotification(getApplicationContext(), data.getGambar().length()>0?CommonUtilities.getBitmap(CommonUtilities.SERVER_URL+"/uploads/informasi/"+data.getGambar()):null, data, data_events);
                    Log.i(TAG, "Received Informasi: "+judul);

                    Intent i = new Intent("elapor.application.com.elapor.RELOAD_DATA_INFORMASI");
                    sendBroadcast(i);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();

                    Log.e(TAG, "Error : "+e.getMessage());
                }
            }

            if(tipe.equalsIgnoreCase("jadual")) {

                try {
                    JSONObject rec = new JSONObject(event);
                    int ids = rec.isNull("id")?0:rec.getInt("id");
                    String juduls = rec.isNull("judul")?"":rec.getString("judul");
                    String tanggals = rec.isNull("tanggal")?"":rec.getString("tanggal");
                    String dateline = rec.isNull("dateline")?"":rec.getString("dateline");
                    String lokasis = rec.isNull("lokasi")?"":rec.getString("lokasi");
                    int jumlah_pesertas = rec.isNull("jumlah_atlit")?0:rec.getInt("jumlah_atlit");
                    boolean is_opens = rec.isNull("is_open")?false:rec.getBoolean("is_open");
                    String banners1 = rec.isNull("banner1")?"":rec.getString("banner1");
                    String banners2 = rec.isNull("banner2")?"":rec.getString("banner2");
                    String banners3 = rec.isNull("banner3")?"":rec.getString("banner3");
                    String logos = rec.isNull("logo")?"":rec.getString("logo");
                    event data_events =  new event(ids, juduls, tanggals, lokasis, dateline, jumlah_pesertas, is_opens, banners1, banners2, banners3, logos);

                    rec = new JSONObject(mesg);
                    int id = rec.isNull("id")?null:rec.getInt("id");
                    String agenda = rec.isNull("agenda")?null:rec.getString("agenda");
                    String tanggal = rec.isNull("tanggal")?null:rec.getString("tanggal");
                    String dari_pukul = rec.isNull("dari_pukul")?null:rec.getString("dari_pukul");
                    String hingga_pukul = rec.isNull("hingga_pukul")?null:rec.getString("hingga_pukul");
                    String link_download = rec.isNull("link_download")?null:rec.getString("link_download");
                    
                    agenda data = new agenda(id, agenda, tanggal, dari_pukul, hingga_pukul, link_download);

                    sendJadualNotification(getApplicationContext(), data, data_events);
                    Log.i(TAG, "Received Agenda: "+data.getAgenda());

                    Intent i = new Intent("elapor.application.com.elapor.RELOAD_DATA_AGENDA");
                    sendBroadcast(i);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        /*Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 Request code, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.news_icon)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 ID of notification, notificationBuilder.build());*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendInformasiNotification(Context context, Bitmap b, informasi data_informasi, event data_event) {
        // Creates an explicit intent for an Activity in your app  
        // prepare intent which is triggered if the
        // notification is selected
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("notification", true);
        i.putExtra("menu_select", 5);
        i.putExtra("event", data_event);
        i.putExtra("informasi", data_informasi);

        //Intent intent = new Intent(context, DetailActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), i, 0);

        String app_name = context.getResources().getString(R.string.app_name);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "DICG_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setContentTitle(app_name+": "+data_informasi.getJudul())
                .setTicker(app_name+": "+data_informasi.getJudul())
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(b)
                .setContentIntent(pIntent)
                .setContentText(data_informasi.getKonten());

        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
        bigPicStyle.bigPicture(b);
        bigPicStyle.setBigContentTitle(app_name+": "+data_informasi.getJudul());
        mBuilder.setStyle(bigPicStyle);

        //  mBuilder.setOngoing(true);
        Notification n = mBuilder.build();
        n.defaults |= Notification.DEFAULT_SOUND;
        n.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(1, n);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendJadualNotification(Context context, agenda data_agenda, event data_event) {
        // Creates an explicit intent for an Activity in your app  
        // prepare intent which is triggered if the
        // notification is selected
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("notification", true);
        i.putExtra("menu_select", 2);
        i.putExtra("event", data_event);
        i.putExtra("agenda", data_agenda);

        //Intent intent = new Intent(context, DetailActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), i, 0);

        String app_name = context.getResources().getString(R.string.app_name);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "DICG_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setContentTitle("Rundown "+data_event.getJudul())
                .setTicker("Rundown "+data_event.getJudul())
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setContentText(data_agenda.getAgenda());

        //  mBuilder.setOngoing(true);
        Notification n = mBuilder.build();
        n.defaults |= Notification.DEFAULT_SOUND;
        n.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(1, n);
    }
}