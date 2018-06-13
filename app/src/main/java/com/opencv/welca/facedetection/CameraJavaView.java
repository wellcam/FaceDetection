package com.opencv.welca.facedetection;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;

public class CameraJavaView extends JavaCameraView implements android.hardware.Camera.PictureCallback{

    private String imagemFileName;

    public CameraJavaView(Context context, int cameraId) {super(context, cameraId);}

    public void capturarImagem (final String nomeArquivo){
        this.imagemFileName = nomeArquivo;
        mCamera.setPreviewCallback(null);
        mCamera.takePicture(null, null, this);
    }



    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        try {
            FileOutputStream fos = new FileOutputStream(imagemFileName);
            fos.write(data);
            fos.close();
        } catch (java.io.IOException e){
            Log.e("Imagem Demo","Excessão CallBack",e);
        }
    }
}
