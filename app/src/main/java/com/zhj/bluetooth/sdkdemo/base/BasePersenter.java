package com.zhj.bluetooth.sdkdemo.base;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePersenter<V extends IBaseView> {
    protected V mView;

    public void attachView(V view) {
        this.mView = view;
    }

    public void detachView() {
        this.mView = null;
        dispose();
    }

    //Add all subscriptions being processed to the CompositeSubscription. Log off observation at unified exit
    private CompositeDisposable mCompositeDisposable;

    protected void addDisposable(Disposable subscription) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            //If the csb is unbound, adding sb requires a new instance, otherwise binding is invalid
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    //Call this method to unbind the observer in case of interface exit and other situations to prevent memory leakage caused by Rx
    public void dispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

}
