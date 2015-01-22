package halim.beta.ta.arduino;




import java.util.Calendar;

import halim.beta.ta.arduino.model.Actionbaractvitys;
import halim.beta.ta.arduino.model.AlarmReceiver;
import halim.beta.ta.arduino.model.JSONParser;
import halim.beta.ta.arduino.model.UtilityMethoda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;






import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class HomeFragment extends Fragment{

//Alarm
	TimePicker myTimePicker;
	Button buttonstartSetDialog;
	TextView textAlarmPrompt;
	
	TimePickerDialog timePickerDialog;
	
	final static int RQS_1 = 1;

private static String URL_PUBLIC = "http://ruga.pagekite.me/arducam/beta_1.0.php";
private static String url = "http://192.168.1.1/arducam/beta_1.0.php";
private JSONArray jsonArray;
private JSONObject jsonO;
private String status="";
private String Lampu;
private ProgressDialog progressBar;


private static final String TAG_USER = "kondisi";
private static final String TAG_NAME = "lampu";
private static final String TAG_STATUS = "status";

private static final String STATUS_LAMPU1="1";
private static final String STATUS_LAMPU2="1";

UtilityMethoda toast=new UtilityMethoda();
ToggleButton Lamp1,Lamp2;
ImageView picture;
TextView title;
halim.beta.ta.arduino.model.OpenHttpConnection HttpUtils=new halim.beta.ta.arduino.model.OpenHttpConnection();
UtilityMethoda utiMethoda=new UtilityMethoda();
ConnectivityManager connectivityManager;
NetworkInfo WifinetworkInfo,MobilenetworkInfo;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	public HomeFragment(){}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Lamp1=(ToggleButton)rootView.findViewById(R.id.toggleButton1);
        Lamp2=(ToggleButton)rootView.findViewById(R.id.toggleButton2);
        title=(TextView)rootView.findViewById(R.id.txtLabel);
        textAlarmPrompt = (TextView)rootView.findViewById(R.id.alarmprompt);
        utiMethoda.SetFontFace(getActivity(),title);
        Actionbaractvitys actionbaractvitys=new Actionbaractvitys(getActivity(), R.drawable.ic_home);
        
       
        CheckConnectionStatus();
     
        Lamp1.setOnCheckedChangeListener(listenerLamp1);
        Lamp2.setOnCheckedChangeListener(listenerLamp1);
        
        return rootView;  
    }
	
	void CheckConnectionStatus(){
		connectivityManager=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        
        WifinetworkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        MobilenetworkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        
        if (WifinetworkInfo.isConnected() || MobilenetworkInfo.isConnected()) 
        	new AsynCekStatusLampu().execute(url);
        //else if(WifinetworkInfo.isConnectedOrConnecting()){
        	//utiMethoda.ShowDialog(getActivity(), "Network Is Not Available \nPlease Check Your Connection!!");
        //}
        else {
        	utiMethoda.ShowDialog(getActivity(), "Please Connect your network");
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.home_status, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.homestatus_refresh:
			CheckConnectionStatus();
			break;
		case R.id.home_alarmset:
			openTimePickerDialog(false);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void openTimePickerDialog(boolean is24r){
		Calendar calendar = Calendar.getInstance();
		
		timePickerDialog = new TimePickerDialog(getActivity(),onTimeSetListener, 
				calendar.get(Calendar.HOUR_OF_DAY), 
				calendar.get(Calendar.MINUTE), 
				is24r);
		timePickerDialog.setTitle("Set Alarm Time");  
        
		timePickerDialog.show();

	}
	OnTimeSetListener onTimeSetListener = new OnTimeSetListener(){

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			Calendar calNow = Calendar.getInstance();
			Calendar calSet = (Calendar) calNow.clone();

			calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calSet.set(Calendar.MINUTE, minute);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);
			
			if(calSet.compareTo(calNow) <= 0){
				//Today Set time passed, count to tomorrow
				calSet.add(Calendar.DATE, 1);
			}
			
			setAlarm(calSet);
		}};
		private void setAlarm(Calendar targetCal){

			textAlarmPrompt.setText(
					"\n\n***\n"
					+ "Alarm is set@ " + targetCal.getTime() + "\n"
					+ "***\n");
			
			Intent intent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(),RQS_1, intent, 0);
			AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
			
		}

	OnCheckedChangeListener listenerLamp1=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			switch (buttonView.getId()) {
			case R.id.toggleButton1:
				if(isChecked){
					//nyalakan
					buttonView.setBackgroundResource(R.drawable.lampu_on);
					toast.SetToast(getActivity(),"Lampu 1 Nyala ");
					new AsynSendCommandLampu().execute("lampu1on");
				}else {
					//Matikan
					buttonView.setBackgroundResource(R.drawable.lampu_off);
					toast.SetToast(getActivity(),"Lampu 1 Mati ");
					new AsynSendCommandLampu().execute("lampu1off");
				}
				break;
			case R.id.toggleButton2:
				if(isChecked){
					//nyalakan
					buttonView.setBackgroundResource(R.drawable.lampu_on);
					toast.SetToast(getActivity(),"Lampu 2 Nyala ");
					new AsynSendCommandLampu().execute("lampu2on");
				}else {
					//Matikan
					buttonView.setBackgroundResource(R.drawable.lampu_off);
					toast.SetToast(getActivity(),"Lampu 2 Mati ");
					new AsynSendCommandLampu().execute("lampu2off");
				}
				break;
			default:
				break;
			}
			
		}
	};
		
	private class AsynSendCommandLampu extends AsyncTask<String,Integer,Double>{

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpUtils.PostData(params[0]);
			return null;
		}
	}
	
	private class AsynCekStatusLampu extends AsyncTask<String,String,JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			JSONParser jsonPar=new JSONParser();
			jsonO=jsonPar.getJSONFromUrl(URL_PUBLIC);
			return jsonO;
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				jsonArray=result.getJSONArray(TAG_USER);
				
				JSONObject c1=jsonArray.getJSONObject(0);
				JSONObject c2=jsonArray.getJSONObject(1);

					status=c1.getString(TAG_STATUS);
					Lampu=c1.getString(TAG_NAME);
					
					String status2=c2.getString(TAG_STATUS);
					String Lampu2=c2.getString(TAG_NAME);
					
					//utiMethoda.SetToast(getActivity(), Lampu2);
					//utiMethoda.SetToast(getActivity(), status2);
					
			if(status.equals(STATUS_LAMPU1)){ 
					//merubah icon menjadi on
					     Lamp1.setChecked(true);
			}else if(status2.equals(STATUS_LAMPU2)) {
						 Lamp2.setChecked(true);
			}else {
						 Lamp1.setChecked(false);
						 Lamp2.setChecked(false);
					}
			} catch (JSONException e) {
				toast.ShowDialog(getActivity(), e.toString());
			}
			progressBar.cancel();
				}
		
		@Override
		protected void onPreExecute() {
			progressBar=ProgressDialog.show(getActivity(),"Loading Status Lampu","Please wait..",true);
			super.onPreExecute();
			
		
		}
	}
	
}
