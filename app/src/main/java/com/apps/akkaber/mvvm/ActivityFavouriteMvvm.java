package com.apps.akkaber.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.akkaber.model.FavouriteDataModel;
import com.apps.akkaber.model.ProductModel;
import com.apps.akkaber.model.UserModel;
import com.apps.akkaber.remote.Api;
import com.apps.akkaber.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityFavouriteMvvm extends AndroidViewModel {

    private MutableLiveData<List<ProductModel>> listMutableLiveData;

    private MutableLiveData<Boolean> isLoadingLiveData;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityFavouriteMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<ProductModel>> getFavouriteList() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLiveData == null) {
            isLoadingLiveData = new MutableLiveData<>();
        }
        return isLoadingLiveData;
    }

    public void getFavourites(UserModel userModel, String lang) {

        if (userModel == null) {
            isLoadingLiveData.setValue(false);
            listMutableLiveData.setValue(new ArrayList<>());
            return;
        }
        isLoadingLiveData.setValue(true);

        Api.getService(Tags.base_url)
                .getFavourites(lang, userModel.getData().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<FavouriteDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<FavouriteDataModel> response) {
                        isLoadingLiveData.postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                               // List<ProductModel> list = response.body().getData();
                                listMutableLiveData.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLiveData.setValue(false);
                    }
                });
    }
}