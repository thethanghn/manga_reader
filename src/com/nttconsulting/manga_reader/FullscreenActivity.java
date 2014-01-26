package com.nttconsulting.manga_reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.nttconsulting.data.Chapter;
import com.nttconsulting.data.Manga;
import com.nttconsulting.helpers.PathHelper;
import com.nttconsulting.helpers.SimpleGestureFilter;
import com.nttconsulting.helpers.SimpleGestureFilter.SimpleGestureListener;
import com.nttconsulting.manga_reader.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity implements ViewFactory, SimpleGestureListener {
	
	protected static final float SWIPE_MAX_OFF_PATH =550;

	protected static final float SWIPE_MIN_DISTANCE = 100;

	protected static final float SWIPE_THRESHOLD_VELOCITY = 100;
	
	private int current_position = 0;
	private ArrayList<Uri> all_images = new ArrayList<Uri>();

	private SimpleGestureFilter detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		getActionBar().hide();
		
		// load the image
		Bundle bundle = getIntent().getExtras();
		Manga manga = (Manga) bundle.getSerializable("manga");
		Chapter chapter = (Chapter) bundle.getSerializable("chapter");
		// load images
		String dir = PathHelper.getStorageDir(manga, chapter);
		String[] files = new File(dir).list();
		for(String file : files) {
			all_images.add(Uri.parse(dir + file));
		}

		Uri uri = all_images.get(current_position++);
		
		
		final ImageSwitcher is = (ImageSwitcher) findViewById(R.id.image_switcher);
		is.setFactory(this);
		is.setImageURI(uri);
		
		detector = new SimpleGestureFilter(this, this);
	}
	
	@Override
	public View makeView() {
		ImageView iView = new ImageView(this);
        iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iView.setLayoutParams(new 
                ImageSwitcher.LayoutParams(
                        LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        iView.setBackgroundColor(0xFF000000);
        Uri uri = all_images.get(current_position++);
        Drawable drawable = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            drawable = Drawable.createFromStream(inputStream, uri.toString() );
        } catch (FileNotFoundException e) {
            //drawable = getResources().getDrawable(R.drawable.default_image);
        }
        
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());

        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        int[] dim = getScreenD(); //this method should return the width of device screen.
        float scaleFactor = (float)newWidth/(float)imageWidth;
        int newHeight = (int)(imageHeight * scaleFactor);

        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        iView.setImageBitmap(bitmap);
        
        
        //iView.setScaleType(ScaleType.FIT_XY);
        //iView.setImageDrawable(drawable);
        
        //iView.setImageURI(uri);
        return iView;
	}
	
	private int[] getScreenD() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		return new int[]{width, height};
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		this.detector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onSwipe(int direction) {
		// TODO Auto-generated method stub
		ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);
		//imageSwitcher.setImageURI(all_images.get(current_position++));
		imageSwitcher.showNext();
		Toast.makeText(this, "swipe", 3000).show();
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "double tap", 3000).show();
	}
}
