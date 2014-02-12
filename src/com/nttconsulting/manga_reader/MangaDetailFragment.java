package com.nttconsulting.manga_reader;

import java.io.IOException;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nttconsulting.manga_reader.FullscreenActivity;
import com.nttconsulting.manga_reader.R;
import com.nttconsulting.data.Chapter;
import com.nttconsulting.data.Manga;
import com.nttconsulting.helpers.DownloadTask;


@SuppressLint("ValidFragment")
public class MangaDetailFragment extends Fragment {
	private Manga manga;
	private Chapter selectedChapter;

	public MangaDetailFragment(Manga o) {
		// get the object
		this.manga = o;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 
		final Activity activity = getActivity();
		LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.manga_detail, container, false);
		TextView manga_name = (TextView) rootView.findViewById(R.id.manga_name);
		TextView manga_path = (TextView) rootView.findViewById(R.id.manga_path);
		manga_name.setText(manga.name);
		manga_path.setText(manga.path);
		
		Chapter[] chapters;
		try {
			chapters = Chapter.getChapterList(activity, manga.id);
			final ListView lv = (ListView) rootView.findViewById(R.id.chapter_list);
			lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					MenuInflater inf = getActivity().getMenuInflater();
					inf.inflate(R.menu.chapter_selected, menu);
				}
				
			});
			registerForContextMenu(lv);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adpater, View view, int position,
						long row_id) {
					// select the chapter
					// register the context menu
					selectedChapter = (Chapter) lv.getItemAtPosition(position);
					getActivity().openContextMenu(view);
				}
			} );
			lv.setAdapter(new ArrayAdapter<Chapter>(activity, R.layout.chapter_list_item, chapters));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return rootView;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inf = getActivity().getMenuInflater();
		
		inf.inflate(R.menu.chapter_selected, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.chapter_menu_download:
				downloadChapter();
				return true;
			case R.id.chapter_menu_read:
				readChapter();
				return true;
		}
		return super.onContextItemSelected(item);
	}

	private void readChapter() {
		final MainActivity activity = (MainActivity) getActivity();
		Intent intent = new Intent(activity, FullscreenActivity.class);
		Bundle data = new Bundle();
		data.putSerializable("manga", activity.selectedManga);
		data.putSerializable("chapter", selectedChapter);
		data.putInt("current", 10);
		intent.putExtras(data);
		startActivity(intent);
	}

	private void downloadChapter() {
		// declare the dialog as a member field of your activity
		if (selectedChapter == null) return;
		final MainActivity activity = (MainActivity) getActivity();

		// execute this when the downloader must be fired
		final DownloadTask downloadTask = new DownloadTask(activity, activity.selectedManga, selectedChapter);
		downloadTask.execute(selectedChapter.path);

//		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//		    @Override
//		    public void onCancel(DialogInterface dialog) {
//		        downloadTask.cancel(true);
//		    }
//		});
	}
}
