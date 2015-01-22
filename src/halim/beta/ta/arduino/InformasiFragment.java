package halim.beta.ta.arduino;

import halim.beta.ta.arduino.model.Actionbaractvitys;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class InformasiFragment extends Fragment{
	private Actionbaractvitys actionbaractvitys;
	private ViewPager viewPager;
@Override

public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setHasOptionsMenu(true);
}

@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	View rootView=inflater.inflate(R.layout.informasi_activity, container, false);
	actionbaractvitys=new Actionbaractvitys(this.getActivity(),R.drawable.ic_communities,"Informasi");
		
	return rootView;
	}
	
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// TODO Auto-ge nerated method stub
		super.onCreateOptionsMenu(menu, inflater);
	inflater.inflate(R.menu.main, menu);
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case android.R.id.home:
		
		break;
	}
	return super.onOptionsItemSelected(item);
}
}
