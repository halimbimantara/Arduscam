package halim.beta.ta.arduino.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class UtilityMethoda {
private SimpleDateFormat simDateFormat;
private Date Tgl;
String formatTgl="hh:mm ddMMyyyy";
private Activity activity;
private String Tanggal;

private boolean ConnectionStatus=false;

//pref
private Typeface TYPE_FONT;
private SmsManager sms;
private String keyUrlStream="";
private String keyurlImage="";
private String keyDuration="";
private String keyNumPhone="";
private String keyAlarm="";

private final String keystrm="url_stream";
private final String keyimg="url_image";
private final String keydur="duration";
private final String keyNumb="number_phone";
private final String keyAlrm="Alrm_Duration";

ConnectivityManager connectivityManager;
NetworkInfo WifinetworkInfo,MobilenetworkInfo;

	public UtilityMethoda(){}
	
	@SuppressLint("SimpleDateFormat")
	public String getTanggal() {
		Tgl=new Date();
		simDateFormat=new SimpleDateFormat(formatTgl);
		Tanggal=simDateFormat.format(Tgl);
		return Tanggal;
	}
	
	public void SetToast(Activity activity,String Message){
		Toast.makeText(activity.getBaseContext(),Message,Toast.LENGTH_SHORT).show();
	}
	public Activity getActivity() {
		return activity;
	}
	public void StartSound(){
		//menyalakan sound
		
	}
	public void StopSound(){
		//mematikan sound
	}
	public String getNumberPhone(Activity activity) {
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
		keyNumPhone=preferences.getString(keyNumb,"");
		return keyNumPhone;
	}
	public String getUrlStrmPref(Activity activity) {
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
		keyUrlStream=preferences.getString(keystrm,"");
		return keyUrlStream;
	}
	
	public String getUrlImgPref(Activity activity) {
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
		keyurlImage=preferences.getString(keyimg,"http://192.168.1.1/arducam/hasil.jpg");
		return keyurlImage;
	}
	public String getDurationVideo(Activity activity){
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
		keyDuration=preferences.getString(keydur,"0");
		return keyDuration;
	}
	public String getDurationAlarm(Context context){
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
		keyAlarm=preferences.getString(keyAlrm,"50");
		return keyAlarm;
	}
	
	public void SetFontFace(Activity activity,TextView tv){
		TYPE_FONT=Typeface.createFromAsset(activity.getAssets(),"Roboto-Thin.ttf");
		tv.setTypeface(TYPE_FONT);
	}
	
	public void ShowDialog(Activity activity,String Message){
		AlertDialog.Builder info=new AlertDialog.Builder(activity);
		info.setMessage(Message).setTitle("Information").setIcon(R.drawable.ic_dialog_alert).
		setNegativeButton("OK",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		info.show();
	}
	
	
	public void SendMessage(String messages,String Number){
		sms=SmsManager.getDefault();
		sms.sendTextMessage(Number, null,messages,null,null);
		//SetToast(activity,"Mengirim Pesan . . .");
	}
	
}
