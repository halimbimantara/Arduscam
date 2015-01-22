package halim.beta.ta.arduino.Db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBopenHelperLogs extends SQLiteOpenHelper{
	
	public DBopenHelperLogs(Context context) {
		super(context,"Data",null,1);
	}
   
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="CREATE TABLE Lap (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
		"Tanggal TEXT NOT NULL);";
		try {
			db.execSQL(sql);			
		} catch (SQLException e) {
			Message(e.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("drop table if exist Lap");	
		} catch (SQLException e) {
		Message(e.toString());
		}finally{
			onCreate(db);
		}
	}
	 
	public void Message(String Message){
		Toast.makeText(null,Message,Toast.LENGTH_LONG).show();
	}

}
