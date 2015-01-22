package halim.beta.ta.arduino.model;

import android.app.ActionBar;
import android.app.Activity;

public class Actionbaractvitys {
ActionBar actionBar;
Activity activity;
private String Title="Rumah Siaga";
	public Actionbaractvitys(Activity activity,int Drawable){
		actionBar=activity.getActionBar();
		actionBar.setIcon(Drawable);
		actionBar.setTitle(Title);
}
	public Actionbaractvitys(Activity activity,int Drawable,String Title){
		this.Title=Title;
		actionBar=activity.getActionBar();
		actionBar.setIcon(Drawable);
		actionBar.setTitle(Title);
		actionBar.setDisplayHomeAsUpEnabled(true);
}
	public Actionbaractvitys(){
		
	}
	public Activity getActivity(){
		return activity;
	}
	public ActionBar getActionBar(){
		return actionBar;
	}
}
