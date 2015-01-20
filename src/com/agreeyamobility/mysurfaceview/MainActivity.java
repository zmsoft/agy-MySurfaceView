package com.agreeyamobility.mysurfaceview;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity implements PreviewCallback{

	private static final String TAG = "MainActivity";
	// 定义对象
	private SurfaceView mLeftSurfaceview = null; // SurfaceView对象：(视图组件)视频显示
	private SurfaceView mRightSurfaceview = null; // SurfaceView对象：(视图组件)视频显示
	private SurfaceHolder mLeftSurfaceHolder = null; // SurfaceHolder对象：(抽象接口)SurfaceView支持类
	private SurfaceHolder mRightSurfaceHolder = null; // SurfaceHolder对象：(抽象接口)SurfaceView支持类
	private Camera mLeftCamera = null;   // Camera对象1，相机预览
	private Camera mRightCamera = null;   // Camera对象1，相机预览
	private boolean isPreview;       //是否正在预览
	private SurfaceHolder leftHolder;
	private SurfaceHolder rightHolder;
	private int leftCam = 0;
	private int rightCam = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);   
		//全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//设置像素格式透明
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_main);
		initSurfaceView();
	}

	

	// InitSurfaceView
	private void initSurfaceView() {
		mLeftSurfaceview = (SurfaceView) this.findViewById(R.id.LeftSurfaceview);
		mRightSurfaceview = (SurfaceView) this.findViewById(R.id.RightSurfaceview);
		mLeftSurfaceHolder = mLeftSurfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
		mRightSurfaceHolder = mRightSurfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
		mLeftSurfaceHolder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(mLeftCamera!=null){
					if(isPreview){
						mLeftCamera.stopPreview();
						isPreview = false;
					}
					mLeftCamera.release();
					mLeftCamera = null;
				}
				mLeftSurfaceview = null;
				mLeftSurfaceHolder = null;
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					mLeftCamera = Camera.open(leftCam);
					Camera.Parameters parameters = mLeftCamera.getParameters();
					parameters.setPreviewFrameRate(5);   //每秒5帧
					parameters.setPictureFormat(PixelFormat.JPEG);
					parameters.set("jpeg-quality", 85);
					// 横竖屏镜头自动调整
					if (MainActivity.this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
						parameters.set("orientation", "portrait"); //
						parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
						mLeftCamera.setDisplayOrientation(90); // 在2.2以上可以使用
					} else// 如果是横屏
					{
						parameters.set("orientation", "landscape"); //
						mLeftCamera.setDisplayOrientation(0); // 在2.2以上可以使用
					}
					mLeftCamera.setParameters(parameters);
					mLeftCamera.setPreviewDisplay(holder);
					mLeftCamera.setPreviewCallbackWithBuffer(MainActivity.this);
					mLeftCamera.startPreview();
					isPreview = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				mLeftSurfaceHolder = holder;
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				mLeftSurfaceHolder = holder;
				
			}
		}); // SurfaceHolder加入回调接口
		
		mRightSurfaceHolder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(mRightCamera!=null){
					if(isPreview){
						mRightCamera.stopPreview();
						isPreview = false;
					}
					mRightCamera.release();
					mRightCamera = null;
				}
				mRightSurfaceview = null;
				mRightSurfaceHolder = null;
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					mRightCamera = Camera.open(rightCam);
					Camera.Parameters parameters = mRightCamera.getParameters();
					parameters.setPreviewFrameRate(5);   //每秒5帧
					parameters.setPictureFormat(PixelFormat.JPEG);
					parameters.set("jpeg-quality", 85);
					// 横竖屏镜头自动调整
					if (MainActivity.this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
						parameters.set("orientation", "portrait"); //
						parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
						mRightCamera.setDisplayOrientation(90); // 在2.2以上可以使用
					} else// 如果是横屏
					{
						parameters.set("orientation", "landscape"); //
						mRightCamera.setDisplayOrientation(0); // 在2.2以上可以使用
					}
					mRightCamera.setParameters(parameters);
					mRightCamera.setPreviewDisplay(holder);
					mRightCamera.setPreviewCallbackWithBuffer(MainActivity.this);
					mRightCamera.startPreview();
					isPreview = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				mRightSurfaceHolder = holder;
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				mRightSurfaceHolder = holder;
				
			}
		}); // SurfaceHolder加入回调接口
		mLeftSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 設置顯示器類型，setType必须设置
		mRightSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 設置顯示器類型，setType必须设置
	}



	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		
		mLeftCamera.addCallbackBuffer(data);
		mRightCamera.addCallbackBuffer(data);
	}

	
}
