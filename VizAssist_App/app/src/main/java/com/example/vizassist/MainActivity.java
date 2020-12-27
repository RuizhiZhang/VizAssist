package com.example.vizassist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vizassist.imagepipeline.ImageActions;
import com.example.vizassist.utilities.HttpUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Time;

import org.apache.http.client.ClientProtocolException;

public class MainActivity extends AppCompatActivity {

    private static final String UPLOAD_HTTP_URL = "http://34.70.53.219:8080/vizassist/annotate"; //GCE external IP

    private static final int IMAGE_CAPTURE_CODE = 1;
    private static final int SELECT_IMAGE_CODE = 2;


    private static final int CAMERA_PERMISSION_REQUEST = 1001;

    private MainActivityUIController mainActivityUIController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityUIController = new MainActivityUIController(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityUIController.resume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //选中菜单栏项目
        switch(item.getItemId()){
            case R.id.action_capture:
                mainActivityUIController.updateResultView(getString(R.string.result_placeholder)); //R.是在res folder里找
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    mainActivityUIController.askForPermission(
                            Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST);
                } else {
                    ImageActions.startCameraActivity(this, IMAGE_CAPTURE_CODE);
                }
                break;
            case R.id.action_gallery:
                mainActivityUIController.updateResultView(getString(R.string.result_placeholder));
                ImageActions.startGalleryActivity(this, SELECT_IMAGE_CODE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item); //因为是override，因为后台可能要做一些另外的事情不可忽略
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            Bitmap bitmap = null;
            if (requestCode == IMAGE_CAPTURE_CODE){
                bitmap = (Bitmap) data.getExtras().get("data");
                mainActivityUIController.updateImageViewWithBitmap(bitmap);
            } else if (requestCode == SELECT_IMAGE_CODE){
                Uri selectedImage = data.getData();
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    mainActivityUIController.updateImageViewWithBitmap(bitmap);
                } catch (IOException e){
                    mainActivityUIController.showErrorDialogWithMessage(
                            R.string.reading_error_message
                    );
                }
            }

            if (bitmap != null){ //新建thread防止block前台运行，放到后台去运行。
                // 从onResme开始是main thread, main thread 负责界面渲染render,一个thread上可以排很多runnable
                final Bitmap bitmapToUpload = bitmap;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        uploadImage(bitmapToUpload);
                    }
                });
                thread.start();
            }
        }
    }

    private void uploadImage(Bitmap bitmap) {
        try{
            HttpURLConnection conn = HttpUtilities.makeHttpPostConnectionToUploadImage(bitmap,
                    UPLOAD_HTTP_URL);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                mainActivityUIController.updateResultView(HttpUtilities.parseOCRResponse(conn));
            } else {
                mainActivityUIController.showInternetError();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            mainActivityUIController.showInternetError();
        } catch (IOException e){
            e.printStackTrace();
            mainActivityUIController.showInternetError();
        } catch (JSONException e) {
            e.printStackTrace();
            mainActivityUIController.showInternetError();
        }
    }
}