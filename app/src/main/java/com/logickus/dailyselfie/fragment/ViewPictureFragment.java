package com.logickus.dailyselfie.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.logickus.dailyselfie.ExifUtil;
import com.logickus.dailyselfie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPictureFragment extends Fragment {


    public ViewPictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_picture, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView pictureFrame = (ImageView) getActivity().findViewById(R.id.imageView_fullScrPicture);
        Bundle args = getArguments();
        if (args  != null && args.containsKey("fileURL")) {
            String fileURL = args.getString("fileURL");
            Toast.makeText(getActivity(),fileURL, Toast.LENGTH_SHORT).show();
            setPic(pictureFrame,fileURL);

        }



    }

    private void setPic(ImageView mImageView, String mCurrentPhotoPath) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 5;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;


		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Bitmap orientedBitmap =  ExifUtil.rotateBitmap(mCurrentPhotoPath, bitmap);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(orientedBitmap);

    }


}
