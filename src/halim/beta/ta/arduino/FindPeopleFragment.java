package halim.beta.ta.arduino;

import halim.beta.ta.arduino.model.Actionbaractvitys;
import halim.beta.ta.arduino.model.UtilityMethoda;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class FindPeopleFragment extends Fragment {
	
	ImageView picture;
	Uri fileoutput;
	
	private final static String url = "http://192.168.1.1/arducam/hasil.jpg";
	halim.beta.ta.arduino.model.OpenHttpConnection HttpUtils=new halim.beta.ta.arduino.model.OpenHttpConnection();
	private UtilityMethoda utility=new UtilityMethoda();
	private ProgressDialog progressBar;
	private ShareActionProvider mShareActionProvider;
	private Bitmap bmp;
	ConnectivityManager connectivityManager;
	NetworkInfo WifinetworkInfo,MobilenetworkInfo;
	public static final int progress_bar_type = 0; 
	
	

	public FindPeopleFragment(){}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_people, container, false);
        picture=(ImageView)rootView.findViewById(R.id.Imv_photos);
        TextView tv=(TextView)rootView.findViewById(R.id.Tv_TittleFp);
        
        utility.SetFontFace(getActivity(),tv);
        Actionbaractvitys actionbaractvitys=new Actionbaractvitys(getActivity(), R.drawable.ic_people);
        
        return rootView;
    }
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.home_share, menu);
		mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.Home_share).getActionProvider();
		Intent localIntent = new Intent();
	    localIntent.setAction("android.intent.action.SEND");
	    localIntent.setType("image/jpeg/png");
	    localIntent.putExtra("android.intent.extra.TEXT","Fenomena Aneh :D");
	    localIntent.putExtra("android.intent.extra.STREAM", Uri.parse(url));
	    mShareActionProvider.setShareIntent(localIntent);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.Home_sendsms:
			SendSms();
			break;
		case R.id.Home_save:
			Save();
			break;
		case R.id.Home_Refresh:
			Refresh();
			break;
		case R.id.Home_Stream:
			Intent videortsp=new Intent(this.getActivity(),VideoView.class);
			startActivity(videortsp);
			Thread thread=new Thread(){
				public void run() {
					String times=utility.getDurationVideo(getActivity());
					int dur=Integer.parseInt(times);
					try {
						new excuteVideoTask().execute("VideoOn");
						sleep(dur*(1000*60));//input dari preference * 60 detik
						new excuteVideoTask().execute("VideoOff");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			};
			thread.start();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	private class excuteVideoTask extends AsyncTask<String, Integer,Double>{

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpUtils.PostData(params[0]);
			return null;
		}
		
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		
		protected Bitmap doInBackground(String... urls) {
			return HttpUtils.DownloadImage(urls[0]);
		
		}
		protected void onPostExecute(Bitmap result) {	
			picture.setImageBitmap(result);
			progressBar.cancel();
			}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressBar=ProgressDialog.show(getActivity(),"Loading Image","Please wait..",true);
			super.onPreExecute();
		}
		}
	private void Save() {
		CheckConnection();
		if (WifinetworkInfo.isConnected() || MobilenetworkInfo.isConnected()) {
			new DownloadFileFromURL().execute(utility.getUrlImgPref(getActivity()));
        } else{
        	utility.ShowDialog(getActivity(),"Network Is Not Available \nPlease Check Your Connection!!");
        }
	}
	private String getDrawable(int imvPhotos) {
		// TODO Auto-generated method stub
		return null;
	}
	void SendSms(){
		String nomor=utility.getNumberPhone(getActivity());
		if(nomor.length()>0){
		utility.SendMessage("Pak/Buk tolong ke rumah saya,saya lagi keluar rumah,di area depan rumah ada orang mencurigakan",nomor);
		utility.SetToast(getActivity(),"Mengirim Pesan . .  .");
		}else {
		utility.SetToast(getActivity(), "Nomor pengirim belum di isi");
		}
	}
	void Refresh(){
		String imgurl=utility.getUrlImgPref(getActivity());
		CheckConnection();
        if (WifinetworkInfo.isConnected() || MobilenetworkInfo.isConnected()) {
        	new jebretImage().execute("jebret");
			new DownloadImageTask().execute(imgurl);
        } else{
        	utility.ShowDialog(getActivity(),"Network Is Not Available \nPlease Check Your Connection!!");
        }
		
	}
	void Vlc(){
		CheckConnection();
		if (WifinetworkInfo.isConnected() || MobilenetworkInfo.isConnected()) {
			Intent inVlc = new Intent("org.videolan.vlc.VLCApplication.gui.video.VideoPlayerActivity");
	        inVlc.setAction(Intent.ACTION_VIEW); 
	        inVlc.setData(Uri.parse(utility.getUrlStrmPref(getActivity())));
	        startActivity(inVlc);
        } else{
        	utility.ShowDialog(getActivity(),"Network Is Not Available \nPlease Check Your Connection!!");
        }
		
	}
	
	void CheckConnection(){
		connectivityManager=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        WifinetworkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        MobilenetworkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	}
	private class jebretImage extends AsyncTask<String,Void,Double>{

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpUtils.PostData(params[0]);
			return null;
		}	
	
	}
	private class DownloadFileFromURL extends AsyncTask<String, String, String> {
		 
	    /**
	     * Before starting background thread
	     * Show Progress Bar Dialog
	     * */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressBar.cancel();
		}
		
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       // showDialog(progress_bar_type);
	        progressBar = new ProgressDialog(getActivity());
	        progressBar.setMessage("Downloading file. Please wait...");
	        progressBar.setIndeterminate(false);
	        progressBar.setMax(100);
	        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        progressBar.show();
	        
	        utility.SetToast(getActivity(), "Saved ..");
	    }
	 
	    /**
	     * Downloading file in background thread
	     * */
	    @Override
	    protected String doInBackground(String... f_url) {
	        int count;
	        try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = conection.getContentLength();
	 
	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
	            String tglnow=utility.getTanggal();
	            // Output stream to write file
	            OutputStream output = new FileOutputStream("/sdcard/Rumah/"+tglnow+".jpg");
	 
	            byte data[] = new byte[1024];
	 
	            long total = 0;
	 
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                // After this onProgressUpdate will be called
	                publishProgress(""+(int)((total*100)/lenghtOfFile));
	 
	                // writing data to file
	                output.write(data, 0, count);
	            }
	 
	            // flushing output
	            output.flush();
	 
	            // closing streams
	            output.close();
	            input.close();
	 
	        } catch (Exception e) {
	           
	        }
	 
	        return null;
	    }
	 
	    /**
	     * Updating progress bar
	     * */
	    protected void onProgressUpdate(String... progress) {
	        // setting progress percentage
	    	progressBar.setProgress(Integer.parseInt(progress[0]));
	   }
	 
	    /**
	     * After completing background task
	     * Dismiss the progress dialog
	     * **/
	    
	}
	
	

}
