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
	// �������
	private SurfaceView mLeftSurfaceview = null; // SurfaceView����(��ͼ���)��Ƶ��ʾ
	private SurfaceView mRightSurfaceview = null; // SurfaceView����(��ͼ���)��Ƶ��ʾ
	private SurfaceHolder mLeftSurfaceHolder = null; // SurfaceHolder����(����ӿ�)SurfaceView֧����
	private SurfaceHolder mRightSurfaceHolder = null; // SurfaceHolder����(����ӿ�)SurfaceView֧����
	private Camera mLeftCamera = null;   // Camera����1�����Ԥ��
	private Camera mRightCamera = null;   // Camera����1�����Ԥ��
	private boolean isPreview;       //�Ƿ�����Ԥ��
	private SurfaceHolder leftHolder;
	private SurfaceHolder rightHolder;
	private int leftCam = 0;
	private int rightCam = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);   
		//ȫ����ʾ
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//�������ظ�ʽ͸��
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_main);
		initSurfaceView();
	}

	

	// InitSurfaceView
	private void initSurfaceView() {
		mLeftSurfaceview = (SurfaceView) this.findViewById(R.id.LeftSurfaceview);
		mRightSurfaceview = (SurfaceView) this.findViewById(R.id.RightSurfaceview);
		mLeftSurfaceHolder = mLeftSurfaceview.getHolder(); // ��SurfaceView��ȡ��SurfaceHolder����
		mRightSurfaceHolder = mRightSurfaceview.getHolder(); // ��SurfaceView��ȡ��SurfaceHolder����
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
					parameters.setPreviewFrameRate(5);   //ÿ��5֡
					parameters.setPictureFormat(PixelFormat.JPEG);
					parameters.set("jpeg-quality", 85);
					// ��������ͷ�Զ�����
					if (MainActivity.this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
						parameters.set("orientation", "portrait"); //
						parameters.set("rotation", 90); // ��ͷ�Ƕ�ת90�ȣ�Ĭ������ͷ�Ǻ��ģ�
						mLeftCamera.setDisplayOrientation(90); // ��2.2���Ͽ���ʹ��
					} else// ����Ǻ���
					{
						parameters.set("orientation", "landscape"); //
						mLeftCamera.setDisplayOrientation(0); // ��2.2���Ͽ���ʹ��
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
		}); // SurfaceHolder����ص��ӿ�
		
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
					parameters.setPreviewFrameRate(5);   //ÿ��5֡
					parameters.setPictureFormat(PixelFormat.JPEG);
					parameters.set("jpeg-quality", 85);
					// ��������ͷ�Զ�����
					if (MainActivity.this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
						parameters.set("orientation", "portrait"); //
						parameters.set("rotation", 90); // ��ͷ�Ƕ�ת90�ȣ�Ĭ������ͷ�Ǻ��ģ�
						mRightCamera.setDisplayOrientation(90); // ��2.2���Ͽ���ʹ��
					} else// ����Ǻ���
					{
						parameters.set("orientation", "landscape"); //
						mRightCamera.setDisplayOrientation(0); // ��2.2���Ͽ���ʹ��
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
		}); // SurfaceHolder����ص��ӿ�
		mLeftSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// �O���@ʾ����ͣ�setType��������
		mRightSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// �O���@ʾ����ͣ�setType��������
	}



	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		
		mLeftCamera.addCallbackBuffer(data);
		mRightCamera.addCallbackBuffer(data);
	}

	
}
