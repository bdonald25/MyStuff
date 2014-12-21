package com.dan.danscolormixer;


import java.util.ArrayList;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Color extends Activity implements OnSeekBarChangeListener {
	SeekBar blue, green, red;
	public static int blueValue, greenValue, redValue, colorValue;
	public static RelativeLayout screen;
	TextView colorId;
	boolean previewOpen = false;
	public static Context mContext;
	private static Camera mCamera;
	private CameraPreview mPreview;
	private FrameLayout preview;
	public static ArrayList<String> favoriteColors;
	static final int FAVORITE_COLOR_REQUEST = 101;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_color);
		
		favoriteColors = readList (this, "colors");
		 
		preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.getLayoutParams().height = 0;
		initialize();
	}
	
	public static void writeList(Context context, ArrayList<String> list, String prefix)
	{
	    SharedPreferences prefs = context.getSharedPreferences("ColorMixer", Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();

	    int size = prefs.getInt(prefix+"_size", 0);

	    // clear the previous data if exists
	    for(int i=0; i<size; i++)
	        editor.remove(prefix+"_"+i);

	    // write the current list
	    for(int i=0; i<list.size(); i++)
	        editor.putString(prefix+"_"+i, list.get(i));

	    editor.putInt(prefix+"_size", list.size());
	    editor.commit();
	}
	
	public static ArrayList<String> readList (Context context, String prefix)
	{
	    SharedPreferences prefs = context.getSharedPreferences("ColorMixer", Context.MODE_PRIVATE);

	    int size = prefs.getInt(prefix+"_size", 0);

	    ArrayList<String> data = new ArrayList<String>(size);
	    for(int i=0; i<size; i++)
	        data.add(prefs.getString(prefix+"_"+i, null));

	    return data;
	}

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.color, menu);
	    return true;
	}
	
	// menu
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
		    switch (item.getItemId()) {
		        case R.id.MENU_ITEM_FAVORITES:
		        	Intent continuous = new Intent(getApplicationContext(),
							SavedColors.class);
		        	startActivityForResult( continuous, FAVORITE_COLOR_REQUEST);
		            return true;
		        case R.id.MENU_ITEM_SAVE:
		        	favoriteColors.add(Integer.toHexString(colorValue));
		    		writeList(this, favoriteColors, "colors");
		        	Toast save = Toast.makeText(this, "Saved!", 3);
		        	save.show();
		        	return true;
		        case R.id.MENU_ITEM_CAMERA:
		        	if (previewOpen == false){
		        		previewOpen = true;
		        		showPreview();
		        	} else {
		        		previewOpen = false;
		        		hidePreview();
		        	}
		        	
		        	
		        	return true;
		            
		        default:
		            return false;
		    }
		}

	private void initialize(){
		screen = (RelativeLayout) findViewById(R.id.myLayout);
		
		blue = (SeekBar) findViewById(R.id.seekBarBlue);
		green = (SeekBar) findViewById(R.id.seekBarGreen);
		red = (SeekBar) findViewById(R.id.seekBarRed);
		
		colorId = (TextView) findViewById(R.id.tvColorId);
		
		blue.setMax(255);
		green.setMax(255);
		red.setMax(255);
		
		blue.setOnSeekBarChangeListener(this);
		green.setOnSeekBarChangeListener(this);
		red.setOnSeekBarChangeListener(this);
	}

	
	public void updateBackgroundColor(){
		screen.setBackgroundColor(colorValue);
		
	}
	
	public void updateColorIdText() {
		colorId.setText("Hex value:" + Integer.toHexString(colorValue));
	}
	
	public void updateColorValue() {
		blueValue = blue.getProgress();
		greenValue = green.getProgress();
		redValue = red.getProgress();
		
		colorValue = 0xFF000000 + redValue * 0x10000 + greenValue * 0x100 + blueValue;
	}
	
	public void changeColorText(SeekBar seekBar){
		TextView text;
		int value;
		
		switch(seekBar.getId()) {
		case R.id.seekBarBlue:
			value = blue.getProgress();
			text = (TextView) findViewById(R.id.tvBlue);
			text.setText("Blue: " + value + " of 255");
		break;
		case R.id.seekBarGreen:
			value = green.getProgress();
			text = (TextView) findViewById(R.id.tvGreen);
			text.setText("Green: " + value + " of 255");
		break;
		case R.id.seekBarRed:
			value = red.getProgress();
			text = (TextView) findViewById(R.id.tvRed);
			text.setText("Red: " + value + " of 255");
		break;
	}
		
		
		
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		changeColorText(seekBar);
		updateColorValue();
		updateColorIdText();
		updateBackgroundColor();
	}

	

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	public void showPreview() {
		mCamera = Camera.open(0);
		
		preview.setVisibility(View.VISIBLE);
		mPreview = new CameraPreview(this, mCamera);
        preview.addView(mPreview);        
		
		int dps = 176;
		final float scale = getResources().getDisplayMetrics().density;
	    int pixels = (int) (dps * scale + 0.5f);
		preview.getLayoutParams().height = pixels;
		
		preview.setVisibility(View.VISIBLE);
	}
	
	public void hidePreview() {
		mCamera.stopPreview();
		preview.removeView(mPreview);
		mCamera.release();
	}

	@Override
	protected void onPause() {
		if(previewOpen == true) {
			hidePreview();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(previewOpen == true) {
			showPreview();
		}
		super.onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == FAVORITE_COLOR_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	           System.out.print("screen.getBackground().toString()");
	           //TODO
	        }
	    }
	}
	
	
	
}
