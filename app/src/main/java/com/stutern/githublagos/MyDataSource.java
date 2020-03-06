package com.stutern.githublagos;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.stutern.githublagos.model.GitHubUser;
import com.stutern.githublagos.utils.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
                .enqueue(new Callback<GitHubUser.GitHubResponse>() {
                    @Override
                    public void onResponse(Call<GitHubUser.GitHubResponse> call, Response<GitHubUser.GitHubResponse> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            initialLoading.postValue(NetworkState.LOADED);
                            networkState.postValue(NetworkState.LOADED);
                            callback.onResult(response.body().items, null, FIRST_PAGE + 1);
                        }
                        else {
                            initialLoading.postValue(new NetworkState(NetworkState.State.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.State.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<GitHubUser.GitHubResponse> call, Throwable t) {
                        String errorMessage;
                        errorMessage = t.getMessage() == null ? "unknown error" : t.getMessage();

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
                .enqueue(new Callback<GitHubUser.GitHubResponse>() {
                    @Override
                    public void onResponse(Call<GitHubUser.GitHubResponse> call, Response<GitHubUser.GitHubResponse> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            networkState.postValue(NetworkState.LOADED);
                            callback.onResult(response.body().items, params.key + 1);
                        }
                        else {
                            networkState.postValue(new NetworkState(NetworkState.State.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<GitHubUser.GitHubResponse> call, Throwable t) {
                        String errorMessage;
                        errorMessage = t.getMessage() == null ? "unknown message" : t.getMessage();

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

