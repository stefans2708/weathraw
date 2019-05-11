package com.example.stefan.weathraw.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.stefan.weathraw.utils.SingleLiveEvent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseApplicationViewModel extends AndroidViewModel {

    private SingleLiveEvent<Throwable> errorMessage = new SingleLiveEvent<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseApplicationViewModel(@NonNull Application application) {
        super(application);
    }

    public void addDisposable(Disposable disposable) {
        this.compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    public void setErrorMessage(Throwable message) {
        errorMessage.setValue(message);
    }

    public LiveData<Throwable> getErrorMessage() {
        return errorMessage;
    }

}
