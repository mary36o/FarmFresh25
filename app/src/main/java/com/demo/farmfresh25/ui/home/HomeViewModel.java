package com.demo.farmfresh25.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Order fresh and organic food items, directly from local farmers." +
                " Browse categories, view popular products, and enjoy fast delivery to your doorstep");
    }

    public LiveData<String> getText() {
        return mText;
    }
}