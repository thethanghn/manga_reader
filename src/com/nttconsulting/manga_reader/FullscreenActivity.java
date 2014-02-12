package com.nttconsulting.manga_reader;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.nttconsulting.data.Chapter;
import com.nttconsulting.data.Manga;
import com.nttconsulting.helpers.MangaPagerAdapter;
import com.nttconsulting.helpers.PathHelper;
import com.nttconsulting.helpers.SimpleGestureFilter;
import com.nttconsulting.manga_reader.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	
	protected static final float SWIPE_MAX_OFF_PATH =550;

	protected static final float SWIPE_MIN_DISTANCE = 100;

	protected static final float SWIPE_THRESHOLD_VELOCITY = 100;
	
	private int current_position = 0;
	private Uri[] all_images;

	private SimpleGestureFilter detector;
	private MangaPagerAdapter mangaPagerAdapter;

	private Toast pageNumberToast;

	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		getActionBar().hide();
		
		final Activity activity = this;
		
		// load the image
		Bundle bundle = getIntent().getExtras();
		Manga manga = (Manga) bundle.getSerializable("manga");
		Chapter chapter = (Chapter) bundle.getSerializable("chapter");
		
		SharedPreferences settings = getSharedPreferences("current", 0);
		final int current = settings.getInt("current", 1);
		
		// load images
		String dir = PathHelper.getStorageDir(manga, chapter);
		String[] files = new File(dir).list();
		all_images = new Uri[files.length];
		for(int i = 0; i < files.length; i++) {
			String file = files[i];
			all_images[i]  = Uri.parse(dir + file);
		}
		
		
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		mangaPagerAdapter = new MangaPagerAdapter(all_images);
		viewPager.setAdapter(mangaPagerAdapter);
		viewPager.setCurrentItem(current);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				showPageNumber(activity, (arg0 + 1));
			}});
		showPageNumber(activity, current);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		SharedPreferences settings = getSharedPreferences("current", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("current", viewPager.getCurrentItem());
		
		// Commit the edits!
		editor.commit();
	}
	
	private void showPageNumber(Context context, int pageNbr) {
		if (pageNumberToast == null) {
			pageNumberToast = Toast.makeText(context, "", 3000);
		}
		pageNumberToast.setText("Page: " + pageNbr + "/" + all_images.length);
		pageNumberToast.show();
	}
	
	private int[] getScreenD() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		return new int[]{width, height};
	}
}
