package com.opencv.welca.facedetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity  implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraJava;
    Button btnCapturar;
    Mat imgRgba, imgResize, imgGray, imgCanny, imgResultado;

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    cameraJava.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraJava = (CameraBridgeViewBase) findViewById(R.id.cameraJava);
        cameraJava.setVisibility(SurfaceView.VISIBLE);
        cameraJava.setCvCameraViewListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (cameraJava!=null){
            cameraJava.disableView();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (cameraJava!=null){
            cameraJava.disableView();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else{
            Toast.makeText(getApplicationContext(),"OpenCV ERRO.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        imgRgba = new Mat(height, width, CvType.CV_8UC4);
        imgGray = new Mat(height, width, CvType.CV_8UC4);
        imgCanny = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        imgRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        imgResize = inputFrame.rgba();

        //Girando imagem
        imgRgba = imgResize.t();
        Core.flip(imgResize.t(),imgRgba,1);
        Imgproc.resize(imgRgba,imgRgba,imgResize.size());

        //Convertendo para cinza
        Imgproc.cvtColor(imgRgba, imgGray, Imgproc.COLOR_RGB2GRAY);

        //Convertendo para Canny
        Imgproc.Canny(imgGray, imgCanny,100,80);

        return imgCanny;
    }

}
