package com.stutern.githublagos.ui_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stutern.githublagos.R;
import com.stutern.githublagos.model.GitHubUser;
import com.stutern.githublagos.paging.GithubUserAdapter;
import com.stutern.githublagos.utils.NetworkState;

public class MainFragment extends Fragment {
    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GithubUserAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(getActivity()).get(GithubUsersViewModel.class);
        mProgressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new GithubUserAdapter(getActivity());

        ((GithubUsersViewModel) mViewModel).mInitialLoadingMutableLiveData.observe(getViewLifecycleOwner(), initialLoading -> {
            if (initialLoading != null && initialLoading.getStatus() == NetworkState.State.SUCCESS) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

        ((GithubUsersViewModel) mViewModel).getGithubUsersLive().observe(getViewLifecycleOwner(), new Observer<PagedList<GitHubUser>>() {
            @Override
            public void onChanged(PagedList<GitHubUser> gitHubUsers) {
                mAdapter.submitList(gitHubUsers);
            }
        });

        ((GithubUsersViewModel) mViewModel).mNetworkStateMutableLiveData.observe(getViewLifecycleOwner(), networkState -> {
            mAdapter.setNetworkState(networkState);
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}