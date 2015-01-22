package halim.beta.ta.arduino;


import halim.beta.ta.arduino.model.Actionbaractvitys;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

public class PrefUrl extends PreferenceActivity{
@SuppressWarnings("deprecation")
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.pref_url);
	Actionbaractvitys actionbaractvitys=new Actionbaractvitys(this,R.drawable.ic_communities,"Preferences");
}
@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case android.R.id.home:
		Intent home=new Intent(this.getApplicationContext(),MainActivity.class);
		if(NavUtils.shouldUpRecreateTask(this,home)){
			TaskStackBuilder.from(this).addNextIntent(home).startActivities();
		}else{
			NavUtils.navigateUpTo(this, home);
		}
	}
		return super.onMenuItemSelected(featureId, item);
	}
}
