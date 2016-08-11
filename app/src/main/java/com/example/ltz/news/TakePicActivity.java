package com.example.ltz.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ltz on 2016/8/9.
 */
public class TakePicActivity extends Activity{

    private String mCurrentPhotoPath;
    private File mSavePhotoFile = new File("/sdcard/DCIM/Camera");

    private static final int REQUEST_TAKE_PHOTO = 1;// 拍照
    private static final int REQUEST_SELECT_PHOTO = 2;// 从相册中选择
    public static final int REQUEST_CROP_PHOTO = 0x3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startCamera();
    }

    private File createImgFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES),"Camera");
        if(!mediaStorageDir.exists()){

        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFile = "JPEG"+timeStamp;
        String suffix = ".jpg";
        File image = new File(mediaStorageDir + File.separator + imgFile + suffix);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * 启动拍照
     */
    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Continue only if the File was successfully created
            if (mSavePhotoFile != null) {
                Intent intent = takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(mSavePhotoFile));//设置文件保存的URI
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * 从图库选择照片
     */
    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:  //拍照
                    //注意，如果拍照的时候设置了MediaStore.EXTRA_OUTPUT，data.getData=null
                    startPhotoZoom(Uri.fromFile(mSavePhotoFile), 256, 256);
                    break;
                case REQUEST_SELECT_PHOTO://选择图片
                    startPhotoZoom(data.getData(), 256, 256);
                    break;
                case REQUEST_CROP_PHOTO:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        //把图片显示到ImgeView
//                        iv_diaplay.setImageBitmap(photo);
                        //把图片加入图库
                        galleryAddPic();
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪照片
     */
    public void startPhotoZoom(Uri uri, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", width/height);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:返回uri，false：不返回uri
        // 同一个地址下 裁剪的图片覆盖之前得到的图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mSavePhotoFile));
        startActivityForResult(intent,REQUEST_CROP_PHOTO);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);//
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);  //设置URI
        this.sendBroadcast(mediaScanIntent);  //发送广播
    }


    /**
     * 把图片保存到SD卡
     * @param bitmap
     * @param targetPath
     */
    public static void SavePhotoToSdCard(Bitmap bitmap, String targetPath) {

        FileOutputStream fileOutputStream = null;
        File file = new File(targetPath);
        try {
            fileOutputStream = new FileOutputStream(file);
            if (bitmap != null) {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        fileOutputStream)) {
                    fileOutputStream.flush();
                }
            }
        } catch (FileNotFoundException e) {
            file.delete();
            e.printStackTrace();
        } catch (IOException e) {
            file.delete();
            e.printStackTrace();
        } finally {
            try {
                // 到最后一定要关闭
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
