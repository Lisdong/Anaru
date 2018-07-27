package com.example.lrd.presenter;

import android.app.Activity;

import com.example.lrd.call.ISelectPhotoPresenter;
import com.example.lrd.model.SelectPhotoModel;

/**
 * Created By LRD
 * on 2018/7/20
 */
public class SelectPhotoPresenter implements ISelectPhotoPresenter {
	private Activity activity;
	private SelectPhotoModel selectPhotoModel;

	public SelectPhotoPresenter(Activity activity){
		this.activity = activity;
		selectPhotoModel = new SelectPhotoModel(activity);
	}
	@Override
	public void openCamera() {

	}

	@Override
	public void openGallery() {

	}

	@Override
	public void openMultyPic() {

	}
}
