package com.dan.danscolormixer;

import java.io.IOException;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Boolean paused = false;

    @SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
    	 
    	
    	if (holder == null){
    		Log.d("surfaceCreated", "holder is null");
    	}
    	try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                Toast resume = Toast.makeText(Color.mContext, "Touch Preview to Pause", 5);
                resume.show();
            }
        } catch (IOException exception) {
            Log.e("surfaceCreated in CameraPreview", "IOException caused by setPreviewDisplay() or startPreview()" + exception.getMessage());
        }
        

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	mCamera.stopPreview();
        mCamera.setPreviewCallback(null); 
		mCamera.release();
		mCamera = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

    	
    	
        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }
//        Log.d("CameraPreview", "Camera is:" + mCamera.toString());
//        Camera.Parameters parameters = mCamera.getParameters();
//        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
//        int height = sizeList.get(0).height;
//        int width = sizeList.get(0).width;
//        parameters.setPreviewSize(width, height);
//        mCamera.setParameters(parameters);
//        holder.setFixedSize(width, height);
        // set preview size and make any resize, rotate or
        // reformatting changes here
       
        
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("CameraPreview", "Error starting camera preview: " + e.getMessage());
        }

    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(paused == false) {
			Toast pause = Toast.makeText(Color.mContext, "Touch Preview to Resume", 3);
			pause.show();
			paused = true;
			mCamera.stopPreview();
		} else {
			paused = false;
			mCamera.startPreview();
		}
		
		return super.onTouchEvent(event);
	}
    
    
    
}