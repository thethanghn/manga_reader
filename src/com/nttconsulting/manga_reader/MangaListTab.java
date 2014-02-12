package com.nttconsulting.manga_reader;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.view.Menu;

public class MangaListTab extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manga_list_tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manga_list_tab, menu);
		return true;
	}

}
