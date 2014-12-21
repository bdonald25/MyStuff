package com.dan.danscolormixer;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SavedColors extends Activity {
	private Button ok;
	private Button cancel;
	private ListView favorites;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites);
		ok = (Button) findViewById(R.id.bOk);
		favorites = (ListView) findViewById(R.id.lvFavorites);
		
		// This is the array adapter, it takes the context of the activity as a 
        // first parameter, the type of list view as a second parameter and your 
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Color.favoriteColors );
        favorites.setAdapter(arrayAdapter); 
        registerForContextMenu(favorites);

        favorites.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				Color.screen.setBackgroundColor((int) Long.parseLong(Color.favoriteColors.get(position), 16));
			}
        	
        });
		
		ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}			
		});
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
		 super.onCreateContextMenu(menu, v, menuInfo);
		 if (v.getId()==R.id.lvFavorites) {
	          MenuInflater inflater = getMenuInflater();
	          inflater.inflate(R.menu.list_menu, menu);
	      }
	}
	
	/*Delete favorite color*/
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      switch(item.getItemId()) {
	         case R.id.MENU_ITEM_DELETE:
	        	 Color.favoriteColors.remove(info.position);
	        	 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Color.favoriteColors );
	             favorites.setAdapter(arrayAdapter); 
	             registerForContextMenu(favorites);
	             
	             return true;
	         
	          default:
	        	  return super.onContextItemSelected(item);
	      }
	}
}
