package halim.beta.ta.arduino.model;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;



public class OpenHttpConnection {

private static String URL="http://192.168.1.1/arducam/beta_1.0.php";


	public OpenHttpConnection(){	
	}
	public void PostData(String DataSend){
		//Membuat Http Client dan Post(url) sebagai target post data ke web openwrt 
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL);
		
		try {
			//  data yang akan dikirim
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("action", DataSend));//parameter yang dikirim
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
		} catch (Exception e) {
			
		}
	}
	
	public InputStream getRequest(String URLString) throws IOException {
		InputStream in = null;
		int response = -1;
		
		java.net.URL url = new java.net.URL(URLString);
		URLConnection conn = url.openConnection();
		
		if (!(conn instanceof HttpURLConnection))
		throw new IOException("Not an HTTP connection");
		
		try{
		HttpURLConnection httpConn = (HttpURLConnection) conn;
		httpConn.setAllowUserInteraction(false);
		httpConn.setInstanceFollowRedirects(true);
		httpConn.setRequestMethod("GET");
		httpConn.connect();
		response = httpConn.getResponseCode();
		if (response == HttpURLConnection.HTTP_OK) {
		in = httpConn.getInputStream();
		}
		}
		catch (Exception ex)
		{
		Log.d("Networking", ex.getLocalizedMessage());
		throw new IOException("Error connecting");
		}
		return in;
	}
	public Bitmap DownloadImage(String URL)
	{	
			Bitmap bitmap = null;
			InputStream in = null;
	try {
			in = getRequest(URL);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
	} catch (IOException e1) {
			Log.d("NetworkingActivity", e1.getLocalizedMessage());
	}
	return bitmap;
	}
	
	
}
