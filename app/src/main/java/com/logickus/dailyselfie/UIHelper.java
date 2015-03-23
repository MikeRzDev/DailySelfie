package com.logickus.dailyselfie;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 8/1/2014.
 */
public class UIHelper {



    /*

        OJO : REVISAR QUE EL ACTIVITY HEREDE DE ACTIVITY Y NO DE ACTIONBARACTIVITY SINO LA NAVEGACION CON EL BOTON
        ATRAS NO FUNCIONARA!

     */

    public static void changeFragment(int idViewToChange,
                                      FragmentManager fm,
                                      Fragment nextFragment,
                                      String fragmentTag,
                                      Bundle bundle) {
        final FragmentTransaction ft = fm.beginTransaction();

        if (bundle != null) {
            nextFragment.setArguments(bundle);
        }

        // The fragment is changed
        ft.replace(idViewToChange, nextFragment);

        /*
		 * IMPORTANT: The following lines allow us to add the fragment
		 * to the stack and return to it later, by pressing back
		 */
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(fragmentTag);
        ft.commit();
    }

    public static void changeFragmentInit(int idViewToChange,
                                      FragmentManager fm,
                                      Fragment nextFragment,
                                      Bundle bundle) {
        final FragmentTransaction ft = fm.beginTransaction();

        if (bundle != null) {
            nextFragment.setArguments(bundle);
        }

        // The fragment is changed
        ft.replace(idViewToChange, nextFragment);

        /*
		 * IMPORTANT: The following lines allow us to add the fragment
		 * to the stack and return to it later, by pressing back
		 */
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }



    public static void changeFragmentWithAnim(int idViewToChange,
                                              FragmentManager fm,
                                              Fragment nextFragment,
                                              Bundle bundle,
                                              int initialAnimationResourceID,
                                              int endAnimationResourceID) {
        final FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(initialAnimationResourceID,endAnimationResourceID, 0, 0);

        if (bundle != null) {
            nextFragment.setArguments(bundle);
        }

        // The fragment is changed
        ft.replace(idViewToChange, nextFragment);
        /*
		 * IMPORTANT: The following lines allow us to add the fragment
		 * to the stack and return to it later, by pressing back
		 */
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public static void displayText(Activity activity, int TextView_id, String text, String format) {
        TextView tv = (TextView) activity.findViewById(TextView_id);
        tv.setText(text);
        if (format != null && format.equals("underline"))
            tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    public static void displayTextToView(View view, int TextView_id, String text, String format) {
        TextView tv = (TextView) view.findViewById(TextView_id);
        tv.setText(text);
        if (format != null && format.equals("underline"))
            tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    public static void changeTextCFArrayAdapter(View row, int textview_id, String text, String color )
    {
        Typeface tf = Typeface.createFromAsset(row.getContext().getAssets(),
                "fonts/Cocon.ttf");
        TextView titulo = (TextView) row.findViewById(textview_id);
        titulo.setTypeface(tf);
        titulo.setTextColor(Color.parseColor(color));
        titulo.setText(text);
    }

    public static void changeImageArrayAdapter(View row, int imageview_id, int image_drawable_id )
    {
        ImageView imagen = (ImageView)  row.findViewById(imageview_id);
        imagen.setImageDrawable(row.getContext().getResources().getDrawable(image_drawable_id));
    }

    public static void changePicture(Activity activity, int ImageView_id, int picture_drawable_id) {

        ImageView iv = (ImageView) activity.findViewById(ImageView_id);
        iv.setImageDrawable(activity.getResources().getDrawable(picture_drawable_id));
    }

    public static void displayTextWithFont(Activity activity, int TextView_id, String text,String colorString) {
        TextView tv = (TextView) activity.findViewById(TextView_id);
        if (text != null)
        tv.setText(text);

        Typeface tf = Typeface.createFromAsset(activity.getBaseContext().getAssets(),
                "fonts/Cocon.ttf");
        tv.setTypeface(tf);
        tv.setTextColor(Color.parseColor(colorString));

    }

    public static String getText(Activity activity, int id) {


        String text = null;

        if (activity.findViewById(id) instanceof EditText)
        {
            EditText et = (EditText) activity.findViewById(id);
            text = et.getText().toString();
        }
        else if (activity.findViewById(id) instanceof TextView)
        {
            TextView tv = (TextView) activity.findViewById(id);
            text = tv.getText().toString();
        }


        return text;
    }

    public static String getText(View view, int id) {

        String text = null;

        if (view.findViewById(id) instanceof EditText)
        {
            EditText et = (EditText) view.findViewById(id);
            text = et.getText().toString();
        }
        else if (view.findViewById(id) instanceof TextView)
        {
            TextView tv = (TextView) view.findViewById(id);
            text = tv.getText().toString();
        }


        return text;
    }


}





