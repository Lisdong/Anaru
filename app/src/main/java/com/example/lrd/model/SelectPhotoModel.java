package com.example.lrd.model;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.example.lrd.app.Constant;
import com.example.lrd.call.ISelectPhoto;
import com.orhanobut.logger.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created By LRD
 * on 2018/7/20
 */
public class SelectPhotoModel implements ISelectPhoto{
    private Activity activity;
    public  Uri imagePathUri;

    public SelectPhotoModel(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void openCamera() {
        imagePathUri = createImagePathUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        // 返回图片在onActivityResult中通过以下代码获取
        // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePathUri);
        activity.startActivityForResult(intent, Constant.REUQUEST_OPEN_CAMERA);
    }

    @Override
    public void openPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, Constant.REUQUEST_OPEN_GALLERY);
    }

    @Override
    public void selectMultiplyPics() { }

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     */
    private static Uri createImagePathUri(final Context context) {
        final Uri[] imageFilePath = {null};

        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }

        Logger.d("生成的照片输出路径：" + imageFilePath[0].toString());
        return imageFilePath[0];
    }
}
