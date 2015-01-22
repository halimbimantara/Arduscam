package halim.beta.ta.arduino;


import halim.beta.ta.arduino.gcmV2.WakeLocker;
import halim.beta.ta.arduino.gcmV2.AlertDialogManager;
import halim.beta.ta.arduino.gcmV2.ConnectionDetector;

import halim.beta.ta.arduino.Db.DBopenHelperLogs;
import halim.beta.ta.arduino.list.NavDrawerListAdapter;
import halim.beta.ta.arduino.model.NavDrawerItem;
import halim.beta.ta.arduino.model.OpenHttpConnection;
import halim.beta.ta.arduino.model.UtilityMethoda;

import java.io.IOException;
import java.util.ArrayList;


import com.google.android.gcm.GCMRegistrar;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

//GCM setting
	// Alert dialog manager
		AlertDialogManager alert = new AlertDialogManager();
		
		// Connection detector
		ConnectionDetector cd;
	AsyncTask<Void, Void, Void> mRegisterTask;
	public static String name;
	public static String email;
	
///end	
	
	private MediaPlayer mMediaPlayer; 
	private String Duration_alarm="";
	private AlarmManager am;
	
	private WakeLock mWakeLock;
	public final String TAG=this.getClass().getSimpleName();
	
	
private String MessageIn;
private final String keyword="Peringatan";
private IntentFilter intentFilter;

//
public final static int NOTI_MODE_BASIC = 0;
public final static int NOTI_MODE_BIG_PIC = 2;

private Bitmap Bmimages;

//notif
private static NotificationManager mNotificationManager;

//Fragment
private DrawerLayout mDrawerLayout;
private ListView mDrawerList;
private ActionBarDrawerToggle mDrawerToggle;

// nav drawer title
private CharSequence mDrawerTitle;

// used to store app title
private CharSequence mTitle;

// slide menu items
private String[] navMenuTitles;
private TypedArray navMenuIcons;

private ArrayList<NavDrawerItem> navDrawerItems;
private NavDrawerListAdapter adapter;
ImageView imView;

//Tanggal
UtilityMethoda utilities=new UtilityMethoda();

//Db
SQLiteDatabase dbLogs;
OpenHttpConnection DownImg=new OpenHttpConnection();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		
		//ActionBar actionBar=getActionBar();
		
		//�-intent untuk filter  SMS diterima �-
		intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        
        //merigister receiver
	    registerReceiver(SmsIntentReceiver, intentFilter);	
	
	    //Fragment
	    mTitle = mDrawerTitle = getTitle();

		// load slide menu items from String Array on  values folder
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources strings on values folder  
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// menambah item nav drawer ke bentuk array 
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Report
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Logs
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true,getDataLogs()));
		
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		//mengatur nav drawer bentuk list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}
	
	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_aboutme).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new FindPeopleFragment();
			break;
		case 2:
			fragment = new PhotosFragment();
			break;
		case 3:
			fragment = new LogsFragment();
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	private  BroadcastReceiver SmsIntentReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
		 MessageIn=intent.getExtras().getString("sms");
		 if(MessageIn.equals(keyword)){
			 System.out.println(utilities.getTanggal());
			 AddData(utilities.getTanggal());
			 setNotification(NOTI_MODE_BIG_PIC);
			 System.out.println("hasil "+Results);
		}else if(MessageIn.equals("nyala")){	
			am=(AlarmManager)(MainActivity.this.getSystemService(ALARM_SERVICE));
			//am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+20000,null);
			
			playSound(MainActivity.this, getAlarmUri());
			
			AlertDialog.Builder info=new AlertDialog.Builder(MainActivity.this);
			info.setMessage("Information").setTitle("Information").setIcon(R.drawable.important).
			setNegativeButton("OK",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mMediaPlayer.stop();
					dialog.cancel();
					
				}
			});
			
			info.show();
			//solution 1----------------
			
			Runnable  releaseWakelock=new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
					
					if (mWakeLock != null && mWakeLock.isHeld()) {
						mWakeLock.release();
					}
				}
			};
			new Handler().postDelayed(releaseWakelock,60*1000);
			
			//--------------------------
			//WindowManager.LayoutParams params=getWindow().getAttributes();
			//params.FLAG_TURN_SCREEN_ON
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
			//params.flags=WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
			//getWindow().setAttributes(params);
		}
		}
	};
	
	
	//GCM message
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString("message");
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
			
						
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	//end
	

		private void setNotification(int mode){
	
			int mId = 1;
    	String contentTitle ="Peringatan Siaga:";
    	String contentText = "Service Content";
    	String contentInfo = "Info";
    	String contentText2 = "Mohon Pantau Segera";
    	
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_whats_hot)
    	        .setContentTitle(contentTitle)
    	        .setContentText(contentText)
    	        .setContentInfo(contentInfo);
    	switch (mode){
    	case NOTI_MODE_BIG_PIC:
    		NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
    		
    		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.maling);
    		bigPicStyle.bigPicture(bm);
    		bigPicStyle.setSummaryText(contentText2);  
    		//memindah object tampilan besar ke notifikasi 
    		mBuilder.setStyle(bigPicStyle);
    		break;
    	case NOTI_MODE_BASIC:
    		break;
    	}
    	
    	Intent inVlc = new Intent("org.videolan.vlc.VLCApplication.gui.video.VideoPlayerActivity");
        inVlc.setAction(Intent.ACTION_VIEW);                
        inVlc.setData(Uri.parse(utilities.getUrlStrmPref(this)));
        TaskStackBuilder stackBuilderVlc=TaskStackBuilder.create(this);
        
    	// intent untuk aplikasi sendiri
    	Intent resultIntent = new Intent(this, MainActivity.class);

    	// The stack builder object will contain an artificial back stack
    	// for the started Activity
    	// This ensures that navigating backward from the Activity leads out of
    	// your application to the Home screen.
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	
    	// Adds the back stack for the Intent (but not the Intent itself)
    	stackBuilder.addParentStack(MainActivity.class);
    	// Adds the Intent that starts the Activity to the top of the stack
    	stackBuilder.addNextIntent(resultIntent);
    	stackBuilderVlc.addNextIntent(inVlc);
    	// A PendingIntent is used to specify the action which should be performed 
    	// once the user select the notification.
    	
    	
    	PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
    	PendingIntent InVlc=stackBuilderVlc.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    	// menambah action tombol pada notif bar maximal 3
    	mBuilder.addAction(R.drawable.ic_people, "Stream", InVlc);
    	mBuilder.addAction(R.drawable.ic_save, "Save", resultPendingIntent);
    	mBuilder.addAction(R.drawable.ic_socialshare, "Share", resultPendingIntent);
    	
    	//menyembunyikan notifikasi setelah icon tmbl di pilih
    	mBuilder.setAutoCancel(true);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(mId, mBuilder.build());
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		unregisterReceiver(SmsIntentReceiver);
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		
		//aq
		
		PowerManager pm=(PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
		if (mWakeLock == null) {
			mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP,TAG);
		}
		if (mWakeLock.isHeld()) {
			mWakeLock.acquire();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_informasi:
		startActivity(new Intent(this,ActionBarInformasi.class));
		
				break;
		case R.id.action_aboutme:
			//startActivity(new Intent(this,ActionBarTentangSaya.class));
			Fragment fragmentme=new TentangSayaFragment();
			if(fragmentme != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragmentme).commit();
			}
			break;
		case R.id.action_urlStream:
			startActivity(new Intent(this,PrefUrl.class));
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void AddData(String Date){
		dbLogs=(new DBopenHelperLogs(this)).getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("Tanggal",Date);
		dbLogs.insert("Lap",null,cv);
		dbLogs.close();
	}
	private String Results="";
	public String getDataLogs(){
		dbLogs=(new DBopenHelperLogs(MainActivity.this)).getReadableDatabase();
		Cursor c=dbLogs.query("Lap",
 				new String[]{"id as _id","Tanggal"}, 
 				null,
 				null,
 				null,
 				null,
 				"Tanggal ASC");
		while (c.moveToNext()) {
			int result=c.getColumnIndex("_id");
			Results=c.getString(result);
			System.out.println("hasil "+Results);
		}	
		dbLogs.close();
		return Results;
		
		}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	protected Bitmap doInBackground(String... urls) {
		return DownImg.DownloadImage(urls[0]);
		}
		protected void onPostExecute(Bitmap result) {
			Bmimages.createBitmap(result);
		}}
	
	
	
	
	
	private void playSound(Context context, Uri alert) {
		 
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification, 
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}
