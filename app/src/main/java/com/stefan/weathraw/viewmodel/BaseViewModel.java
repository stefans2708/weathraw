package com.stefan.weathraw.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stefan.weathraw.utils.SingleLiveEvent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> errorMessage = new SingleLiveEvent<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
