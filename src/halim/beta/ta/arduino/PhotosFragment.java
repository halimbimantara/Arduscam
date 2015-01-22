package halim.beta.ta.arduino;


import halim.beta.ta.arduino.model.Actionbaractvitys;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class PhotosFragment extends Fragment {
	
	
	
	SimpleCursorAdapter mAdapter;
	AsynchTaskLoadFileSd myAsyncTaskLoadFiles;
	
	private final int THUMBNAIL_LOADER_ID  = 0;
	private final int IMAGE_LOADER_ID  = 1;
	
	MatrixCursor mMatrixCursor;
	Cursor mThumbCursor;
	Cursor mImageCursor;
	
	String mThumbImageId="";
	String mThumbImageData="";
	String mImageSize="";
	String mImageTitle="";
	String mImageWidth="";
	String mImageHeight="";
	
	GridView gridview;
	ImageAdapter myImageAdapter;
	/*
	public PhotosFragment(){
	}
	*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        Actionbaractvitys actionbaractvitys=new Actionbaractvitys(getActivity(), R.drawable.ic_photos);
        //new AsynchTaskLoadFileSd(adapter)
        gridview = (GridView) rootView.findViewById(R.id.gridview);
		myImageAdapter = new ImageAdapter(this.getActivity());
		gridview.setAdapter(myImageAdapter);
	
		myAsyncTaskLoadFiles = new AsynchTaskLoadFileSd(myImageAdapter);
		myAsyncTaskLoadFiles.execute();

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String prompt = "remove " + (String) parent.getItemAtPosition(position);
				Toast.makeText(getActivity().getApplicationContext(), prompt, Toast.LENGTH_SHORT).show();
				
				myImageAdapter.remove(position);
				myImageAdapter.notifyDataSetChanged();
			}
		});		
        return rootView;
    }

	
public class AsynchTaskLoadFileSd extends AsyncTask<Void, String,Void>{

	File targetDirector;
	ImageAdapter myTaskAdapter;
	ProgressDialog dialog;
	
	
	public AsynchTaskLoadFileSd(ImageAdapter adapter){
		myTaskAdapter=adapter;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		
		myTaskAdapter.notifyDataSetChanged();
		dialog.cancel();
		super.onPostExecute(result);
		
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		myTaskAdapter.add(values[0]);
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		String targetPath = ExternalStorageDirectoryPath + "/Rumah/";
		targetDirector = new File(targetPath);
		myTaskAdapter.clear();
		dialog=ProgressDialog.show(getActivity(),"Loading Data","Please wait..",true);
		super.onPreExecute();
		
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		File fileSdcard,Directory;
		FileOutputStream ouStream=null;
		fileSdcard=Environment.getExternalStorageDirectory();
		Directory =new File(fileSdcard.getAbsolutePath()+"/Rumah");
		Directory.mkdirs();
		File[] files = targetDirector.listFiles();
		for (File file : files) {
			publishProgress(file.getAbsolutePath());
			if (isCancelled()) break;
		}
		return null;
	}
	
	
}
	public class ImageAdapter extends BaseAdapter{

		private Context mContext;
		ArrayList<String> itemList = new ArrayList<String>();
		
		public ImageAdapter(Context c) {
			mContext = c;
		}
		void add(String path) {
			itemList.add(path);
		}
		
		void clear() {
			itemList.clear();
		}
		
		void remove(int index){
			itemList.remove(index);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220,220);

			imageView.setImageBitmap(bm);
			return imageView;
		}
		public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,	int reqHeight) {

			Bitmap bm = null;
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(path, options);

			return bm;
		}
		public int calculateInSampleSize(

				BitmapFactory.Options options, int reqWidth, int reqHeight) {
					// Raw height and width of image
					final int height = options.outHeight;
					final int width  = options.outWidth;
					int inSampleSize = 1;

					if (height > reqHeight || width > reqWidth) {
						if (width > height) {
							inSampleSize = Math.round((float) height
									/ (float) reqHeight);
						} else {
							inSampleSize = Math.round((float) width / (float) reqWidth);
						}
					}

					return inSampleSize;
				}		
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.home_status, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.homestatus_refresh:
			myAsyncTaskLoadFiles.cancel(true);
			//new another ImageAdapter, to prevent the adapter have
			//mixed files
			myImageAdapter = new ImageAdapter(getActivity());
			gridview.setAdapter(myImageAdapter);
			myAsyncTaskLoadFiles = new AsynchTaskLoadFileSd(myImageAdapter);
			myAsyncTaskLoadFiles.execute();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
