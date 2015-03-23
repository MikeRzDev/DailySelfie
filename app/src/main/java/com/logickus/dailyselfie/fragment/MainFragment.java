package com.logickus.dailyselfie.fragment;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.logickus.dailyselfie.NotificationAlarmReceiver;
import com.logickus.dailyselfie.R;
import com.logickus.dailyselfie.UIHelper;
import com.logickus.dailyselfie.custom_listview.ImageAdapter;
import com.logickus.dailyselfie.model.SelfiePicture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private final int TAKE_PICTURE_ACTION = 1;
    private final int TWO_MINUTES = 120000 ;
    private PendingIntent pendingIntent;
    Context mContext = null;
    ListView imageList = null;
    private String mCurrentPhotoPath;
    private final String ALBUM_NAME = "MyDairySelfiePictures";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    List<SelfiePicture> pictures;
    ImageAdapter mAdapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeAlerts();

        mContext = getActivity().getApplicationContext();

        setupPicturesListView(mContext);



        //show actionbar
        getActivity().getActionBar().show();



    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main,menu);

    }

    private File[] getFileListInFolder(String absolutePath)
    {

        File f = new File(absolutePath);
        File file[] = f.listFiles();
        return file;
    }

    private  List<SelfiePicture> loadPictures()
    {
        File [] pics = getFileListInFolder(getAlbumDir().getAbsolutePath());

        if (pics != null)
        {
            List<SelfiePicture> pictureList = new ArrayList<>();

            for (File pic : pics) {
                String name = pic.getName();
                String date = getDate(name);
                pictureList.add(new SelfiePicture(name,date,pic.getAbsolutePath()));

            }
            return pictureList;

        }

        return new ArrayList<>();

    }

    void deleteAllFromFolder(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteAllFromFolder(child);
        fileOrDirectory.delete();
    }

    private String getDate (String pictureName)
    {
        String date = pictureName.split("_")[1];
        String y = date.substring(0, 4);
        String m = date.substring(4,6 );
        String d = date.substring(6,8);
        date = y+"-"+m+"-"+d;
        return date;
    }

    private  List<SelfiePicture> loadMockData()
    {
        List<SelfiePicture> mockData = new ArrayList<>();
        mockData.add(new SelfiePicture("img1","2015-01-01","url1") );
        mockData.add(new SelfiePicture("img2","2015-01-01","url2") );
        mockData.add(new SelfiePicture("img3","2015-01-01","url3") );
        return mockData;
    }




    private void setupPicturesListView(Context context)
    {
        imageList = (ListView) getActivity().findViewById(R.id.listView_images);
        pictures = loadPictures();


             mAdapter = new ImageAdapter(context, pictures);
            imageList.setAdapter(mAdapter);

            imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SelfiePicture picture = (SelfiePicture) imageList.getAdapter().getItem(position);




                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("fileURL", picture.getPictureFilePath());


                    UIHelper.changeFragment(R.id.container, getFragmentManager(),
                            new ViewPictureFragment(), "VIEW_PICTURE_FRAGMENT", bundle);
                }
            });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.take_picture) {

            if (isIntentAvailable(mContext, MediaStore.ACTION_IMAGE_CAPTURE))
            {
                dispatchTakePictureIntent();


            }
            return true;
        }
        if (id == R.id.delete_all) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Do you want to delete all selfies?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAllFromFolder(getAlbumDir());
                    pictures.removeAll(pictures);
                    mAdapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(mContext, "wise choise!", Toast.LENGTH_LONG).show();
                }
            });

            if (pictures.size() > 0)

            builder.show();
            else
            Toast.makeText(mContext,"there is nothing to delete :S", Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }


        startActivityForResult(takePictureIntent, TAKE_PICTURE_ACTION);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE_ACTION: {
                if (resultCode == Activity.RESULT_OK) {


                    handleCameraPhoto();



                }
                else
                {
                    File failedFile = new File(mCurrentPhotoPath);
                    failedFile.delete();
                    mCurrentPhotoPath = null;
                }

                break;
            }
        }
    }

    private void initializeAlerts() {

        Intent alarmIntent = new Intent(getActivity(), NotificationAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        int interval = TWO_MINUTES;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
    }

    private void handleCameraPhoto() {

        if (mCurrentPhotoPath != null) {

            File lastPic = new File(mCurrentPhotoPath);
                pictures.add(new SelfiePicture(lastPic.getName(), getDate(lastPic.getName()), lastPic.getAbsolutePath()));
                mAdapter.notifyDataSetChanged();

            mCurrentPhotoPath = null;
        }

    }


    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();
        Log.e("photo path", f.getAbsolutePath());

        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                    ),
                    ALBUM_NAME
            );

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }





}
