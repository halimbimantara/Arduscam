package halim.beta.ta.arduino;

import halim.beta.ta.arduino.model.Actionbaractvitys;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class TentangSayaFragment extends Fragment{
	private Actionbaractvitys actionbaractvitys;
@Override
public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
	setHasOptionsMenu(true);
}

@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	View rooView=inflater.inflate(R.layout.tentangsaya_activity,container,false);
	actionbaractvitys=new Actionbaractvitys(this.getActivity(),R.drawable.ic_home,"Tentang Pengembang");
	return rooView;
	}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case android.R.id.home:
		Toast.makeText(getActivity(), "back",Toast.LENGTH_SHORT).show();
		break;
		
	//return super.onOptionsItemSelected(item);
	}
	return true;
}
}
