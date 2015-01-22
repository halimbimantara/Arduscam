package halim.beta.ta.arduino;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationReceiver extends Activity{
private TextView set_pesan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		set_pesan = (TextView) findViewById(R.id.text_hasil);
		Bundle b = getIntent().getExtras();
		set_pesan.setText(b.getString("pesan"));
	}

}
