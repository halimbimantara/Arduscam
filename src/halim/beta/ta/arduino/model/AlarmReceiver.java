package halim.beta.ta.arduino.model;



import halim.beta.ta.arduino.MainActivity;
import halim.beta.ta.arduino.R;

import java.io.IOException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.sax.StartElementListener;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class AlarmReceiver extends BroadcastReceiver {
	
	halim.beta.ta.arduino.model.OpenHttpConnection HttpUtils=new halim.beta.ta.arduino.model.OpenHttpConnection();
	Context now;
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		//metthode 1
		
		Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SMS_RECEIVED_ACTION");
        broadcastIntent.putExtra("sms","nyala");
        arg0.sendBroadcast(broadcastIntent);
        
 
        
		now=arg0;
		Intent intent = new Intent(arg0, MainActivity.class);
	    PendingIntent pIntent = PendingIntent.getActivity(arg0, 0, intent, 0);
		
		Notification n  = new Notification.Builder(arg0)
        .setContentTitle("Bangun . . .")
        .setContentText("Peringatan")
        .setSmallIcon(R.drawable.ic_home)
        .addAction(R.drawable.ic_content_email, "home", pIntent)
        .setAutoCancel(true).build();
		n.flags |= Notification.FLAG_AUTO_CANCEL;
NotificationManager notificationManager =  (NotificationManager)now.getSystemService(Context.NOTIFICATION_SERVICE);
notificationManager.notify(0, n); 
Handler mHandler=new Handler();
Thread thread=new Thread(){
			public void run() {
				try {
					HttpUtils.PostData("lampu1on");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};
		thread.start();	
		mHandler.post(thread);
	}
	private class AsynSendCommandLampu extends AsyncTask<String,Integer,Double>{

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpUtils.PostData(params[0]);
			return null;
		}
	}
	
	    
	    
}
