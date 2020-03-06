package com.stutern.githublagos;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.stutern.githublagos.model.GitHubUser;

public class MyDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, GitHubUser>> myDataSourceLive;

    public MyDataSourceFactory() {
        this.myDataSourceLive = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        MyDataSource myDataSource = new MyDataSource();
        myDataSourceLive.postValue(myDataSource);
        return myDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, GitHubUser>> getMyDataSourceLive() {
        return myDataSourceLive;
    }
}
