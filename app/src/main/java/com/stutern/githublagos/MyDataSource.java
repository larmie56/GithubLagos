package com.stutern.githublagos;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.stutern.githublagos.model.GitHubUser;
import com.stutern.githublagos.utils.NetworkState;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MyDataSource extends PageKeyedDataSource<Integer, GitHubUser> {

    public static final int FIRST_PAGE = 1;
    private Api mApi;
    private static final String LOCATION_QUERY_PARAMETER = "location:lagos";
    private MutableLiveData initialLoading;
    private MutableLiveData networkState;

    public MyDataSource() {
        ApiFactory apiFactory = new ApiFactory();
        mApi = apiFactory.getApi();
        initialLoading = new MutableLiveData();
        networkState = new MutableLiveData();
    }
    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, GitHubUser> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        mApi.getUsers(LOCATION_QUERY_PARAMETER, FIRST_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GitHubUser.GitHubResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(GitHubUser.GitHubResponse gitHubResponse) {
                        initialLoading.postValue(NetworkState.LOADED);
                        networkState.postValue(NetworkState.LOADED);
                        callback.onResult(gitHubResponse.items, null, FIRST_PAGE + 1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage;
                        errorMessage = e.getMessage() == null ? "unknown error" : e.getMessage();

                        initialLoading.postValue(new NetworkState(NetworkState.State.FAILED, errorMessage));
                        networkState.postValue(new NetworkState(NetworkState.State.FAILED, errorMessage));
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, GitHubUser> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, GitHubUser> callback) {
        networkState.postValue(NetworkState.LOADING);
        mApi.getUsers(LOCATION_QUERY_PARAMETER, params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GitHubUser.GitHubResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(GitHubUser.GitHubResponse gitHubResponse) {
                        networkState.postValue(NetworkState.LOADED);
                        callback.onResult(gitHubResponse.items, params.key + 1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage;
                        errorMessage = e.getMessage() == null ? "unknown message" : e.getMessage();

                        networkState.postValue(new NetworkState(NetworkState.State.FAILED, errorMessage));
                    }
                });
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }
    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }
}

