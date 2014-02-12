package com.nttconsulting.helpers;

import uk.co.senab.photoview.PhotoView;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class MangaPagerAdapter extends PagerAdapter {

	private Uri[] uris;
	
	public MangaPagerAdapter(Uri[] uris) {
		this.uris = uris;
	}

	@Override
	public int getCount() {
		return uris.length;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photoView = new PhotoView(container.getContext());
		photoView.setImageURI(uris[position]);

		// Now just add PhotoView to ViewPager and return it
		container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
}
