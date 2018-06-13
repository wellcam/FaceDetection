package com.opencv.welca.facedetection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Galeria extends AppCompatActivity {

    ImageView imagemView;
    Button btnCarregar;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        btnCarregar = (Button) findViewById(R.id.btnCarregar);
        imagemView = (ImageView) findViewById(R.id.imagemView);

        btnCarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            Uri imgUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imgUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imagemView.setImageBitmap(bitmap);
                //GaussianBlur (bitmap);
            } catch (FileNotFoundException e){
                    e.printStackTrace();
                }
        }
    }

    public void GaussianBlur (Bitmap bitmap){
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Mat src = new Mat(bitmap.getHeight(),bitmap.getWidth(), CvType.CV_8UC4);
        Mat dest = new Mat(bitmap.getHeight(),bitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap,src);
        Imgproc.GaussianBlur(src,dest, new Size(21,21),0);
        //Imgproc.medianBlur(src,dest,3);
        Bitmap processarImagem = Bitmap.createBitmap(dest.cols(),dest.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dest,processarImagem);
        imagemView.setImageBitmap(processarImagem);

    }

}
