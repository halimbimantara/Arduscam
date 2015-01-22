package halim.beta.ta.arduino;

import halim.beta.ta.arduino.model.Actionbaractvitys;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;


public class ActionBarInformasi extends Activity{
private Actionbaractvitys actionbaractvitys;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	actionbaractvitys=new Actionbaractvitys(this,R.drawable.ic_communities,"Informasi");
    setContentView(R.layout.informasi_activity);
	}


@SuppressWarnings("deprecation")
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case android.R.id.home:
		Intent home=new Intent(this,MainActivity.class);
		if(NavUtils.shouldUpRecreateTask(this,home)){
			TaskStackBuilder.from(this).addNextIntent(home).startActivities();
		}else{
			NavUtils.navigateUpTo(this, home);
		}
		return true;
	}
	return super.onOptionsItemSelected(item);
}
}
