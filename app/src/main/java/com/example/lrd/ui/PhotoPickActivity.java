package com.example.lrd.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.lrd.R;
import com.example.lrd.app.Constant;
import com.example.lrd.model.CropUtil;
import com.example.lrd.model.SelectPhotoModel;
import com.example.lrd.model.ToolbarHelper;
import com.example.lrd.ui.base.BaseActivity;
import com.example.lrd.views.CircleImageView;
import com.yalantis.ucrop.UCrop;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created By LRD
 * on 2018/7/19  notes: 简单图片选择
 */
public class PhotoPickActivity extends BaseActivity {
    @BindView(R.id.head_photo)
    CircleImageView headPhoto;
    private SelectPhotoModel mSelectPhotoModel;
    private CropUtil mCropModel;

    @Override
    protected int getContentView() {
        return R.layout.photo_layout;
    }

    @Override
    public void init() {
        initView();
    }

    @Override
    protected void initToolbar(ToolbarHelper toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("系统选择图片");
    }

    private void initView() {
        mSelectPhotoModel = new SelectPhotoModel(this);
        mCropModel = new CropUtil(this);
    }

    @OnClick(R.id.head_photo)
    public void onViewClicked() {
        String[] perms = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(PhotoPickActivity.this, perms)) {
            showSelectDialog();
        } else {
            EasyPermissions.requestPermissions(this, "需要相机和相册文件读取权限",101, perms);
        }
    }

    private void showSelectDialog() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.photo_select_dialog, false)
                .show();
        TextView camera = (TextView) dialog.findViewById(R.id.camera_btn);
        TextView picture = (TextView) dialog.findViewById(R.id.picture_btn);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectPhotoModel.openCamera();
                dialog.dismiss();
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectPhotoModel.openPhoto();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REUQUEST_OPEN_CAMERA://选择相机之后的处理
                if (resultCode == RESULT_OK) {
                    mCropModel.initUCrop(mSelectPhotoModel.imagePathUri);
                }
                break;
            case Constant.REUQUEST_OPEN_GALLERY://相册
                if (resultCode == RESULT_OK) {
                    mCropModel.initUCrop(data.getData());
                }
                break;
            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    Glide.with(this).
                            load(resultUri).
                            thumbnail(0.5f).
                            into(headPhoto);
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
    }

}
