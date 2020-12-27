package com.example.vizassist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Controller of main activity.
 */
public class MainActivityUIController {
    private final Activity activity;
    private final Handler mainThreadHandler;

    private TextView resultView;
    private ImageView imageView;

    public MainActivityUIController(Activity activity) {
        this.activity = activity;
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void resume() {
        resultView = activity.findViewById(R.id.resultView);
        imageView = activity.findViewById(R.id.capturedImage);
    }

    public void updateResultView(final String text) {
        mainThreadHandler.post(new Runnable(){
           @Override
           public void run(){
               resultView.setText(text);
           }
        });
    }

    public void updateImageViewWithBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    //good practice
    public void showErrorDialogWithMessage(int messageStringID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.error_dialog_title);
        builder.setMessage(messageStringID);
        builder.setPositiveButton(R.string.error_dialog_dismiss_button,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();;
                    }
                });
        builder.show();
    }

    //good practice
    public void showInternetError() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, R.string.internet_error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void askForPermission(String permission, Integer requestCode) { //request run time permission
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                //之前被拒，但是用户再次试图使用这个功能时解释一下为什么需要这个权限，但是单独弹出一个消息框很麻烦这里就不写了
                ActivityCompat.requestPermissions(activity, new String[] {permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[] {permission}, requestCode);
            }
        }
    }
}
