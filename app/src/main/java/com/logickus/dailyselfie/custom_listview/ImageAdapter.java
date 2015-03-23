package com.logickus.dailyselfie.custom_listview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.logickus.dailyselfie.R;
import com.logickus.dailyselfie.model.SelfiePicture;


public class ImageAdapter extends BaseAdapter {

    private List<SelfiePicture> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public ImageAdapter(Context context, List<SelfiePicture> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public SelfiePicture getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_image, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageViewPictureImg = (ImageView) convertView.findViewById(R.id.imageView_pictureImg);
            viewHolder.textViewTitlePicture = (TextView) convertView.findViewById(R.id.textView_titlePicture);
            viewHolder.textViewPictureDate = (TextView) convertView.findViewById(R.id.textView_pictureDate);

            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(SelfiePicture object, ViewHolder holder) {


        setPic(holder.imageViewPictureImg,object.getPictureFilePath());

       holder.textViewPictureDate.setText(object.getDate());
       holder.textViewTitlePicture.setText(object.getTitle());

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
        int scaleFactor = 8;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;


		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);

    }

    protected class ViewHolder {
    private ImageView imageViewPictureImg;
    private TextView textViewTitlePicture;
    private TextView textViewPictureDate;
    }
}
