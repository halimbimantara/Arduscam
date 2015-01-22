package halim.beta.ta.arduino;


import halim.beta.ta.arduino.Db.DBopenHelperLogs;
import halim.beta.ta.arduino.model.Actionbaractvitys;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class LogsFragment extends Fragment {
	SQLiteDatabase Db;
	private ListAdapter listAdapter;
	private ListView listLogs;
	public LogsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        listLogs=(ListView)rootView.findViewById(R.id.listLogs);
        Actionbaractvitys actionbaractvitys=new Actionbaractvitys(getActivity(), R.drawable.ic_communities); 
        Db=(new DBopenHelperLogs(this.getActivity())).getReadableDatabase();
 		Cursor c=Db.query("Lap",
 				new String[]{"id as _id","Tanggal"}, 
 				null,
 				null,
 				null,
 				null,
 				"Tanggal ASC");
 		//menampilkan  ke layout
 		listAdapter=new SimpleCursorAdapter(this.getActivity(),R.layout.list_logs,c,
 				new String[]{"_id","Tanggal"}
 		,new int[]{R.id.TvId,R.id.TvTanggal});
 		listLogs.setAdapter(listAdapter);
 		Db.close();
 		
        return rootView;
    }
	
}
