package halim.beta.ta.arduino;


import halim.beta.ta.arduino.model.UtilityMethoda;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;


public class VideoView extends Activity{
	android.widget.VideoView myVideoView;
	ProgressDialog progDailog;
	AudioManager audio;
	MediaController mediaController;
	String unStringUrl="rtsp://live.mytrans.com/live/transtv@5.sdp"; 
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.video_view);
	//UtilityMethoda urlVideos=new UtilityMethoda();
	
   myVideoView = (android.widget.VideoView)findViewById(R.id.videoView1);        
   progDailog = ProgressDialog.show(VideoView.this, null, "Video loading...", true);        
   audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

   mediaController = new MediaController(this);
   myVideoView.setMediaController(mediaController);
   myVideoView.setVideoURI(Uri.parse(unStringUrl));
   myVideoView.requestFocus();

   myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
       public void onPrepared(MediaPlayer arg0) {

                   // called too soon with rtsp in 4.1

           if(progDailog != null) {
               progDailog.dismiss();
           }

           myVideoView.start();
       }
   });

   myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
       public void onCompletion(MediaPlayer mp) {
           /*Intent intent = new Intent(MyVideoView.this, lastActivity);
           intent.putExtra("cleTitre", activityTitle);
           intent.putExtra("cleSegment", activityCat);
           startActivity(intent);*/
       }
   });

   myVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
       public boolean onError(MediaPlayer mp, int what, int extra) {
           if(progDailog != null) {
               progDailog.dismiss();
           }

           return false;
       }
   });

}
}

