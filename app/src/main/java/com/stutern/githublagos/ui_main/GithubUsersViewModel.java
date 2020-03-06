package com.stutern.githublagos.ui_main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.stutern.githublagos.MyDataSource;
import com.stutern.githublagos.MyDataSourceFactory;
import com.stutern.githublagos.model.GitHubUser;
import com.stutern.githublagos.utils.NetworkState;

public class GithubUsersViewModel extends ViewModel {

    MutableLiveData<PageKeyedDataSource<Integer, GitHubUser>> mPageKeyedDataSourceLiveData;
    private LiveData<PagedList<GitHubUser>> mGitHubUsersLive;
    LiveData<NetworkState> mNetworkStateMutableLiveData;
    LiveData<NetworkState> mInitialLoadingMutableLiveData;


    public GithubUsersViewModel() {
        init();
    }

    private void init() {
                MyDataSourceFactory factory = new MyDataSourceFactory();

                mPageKeyedDataSourceLiveData = factory.getMyDataSourceLive();

                mNetworkStateMutableLiveData = Transformations.switchMap(factory.getMyDataSourceLive(), dataSource ->
                        ((MyDataSource) dataSource).getNetworkState());

                mInitialLoadingMutableLiveData = Transformations.switchMap(factory.getMyDataSourceLive(), dataSource ->
                        ((MyDataSource) dataSource).getInitialLoading());

                PagedList.Config config = new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(25)
                        .build();
                mGitHubUsersLive = new LivePagedListBuilder(factory, config).build();
    }

    public LiveData<PagedList<GitHubUser>> getGithubUsersLive() {
        return mGitHubUsersLive;
    }
}
