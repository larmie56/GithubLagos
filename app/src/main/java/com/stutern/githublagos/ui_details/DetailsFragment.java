package com.stutern.githublagos.ui_details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;

import com.stutern.githublagos.Api;
import com.stutern.githublagos.ApiFactory;
import com.stutern.githublagos.R;
import com.stutern.githublagos.databinding.FragmentDetailsBinding;
import com.stutern.githublagos.model.GitHubUser;
import com.stutern.githublagos.utils.CustomTabsActivityHelper;
import com.stutern.githublagos.utils.ImageUtils;
import com.stutern.githublagos.utils.StringUtils;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsFragment extends DialogFragment {

    Api mApi;
    FragmentDetailsBinding mBinding;
    CustomTabsActivityHelper mCustomTabsActivityHelper = new CustomTabsActivityHelper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDetailsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //mBinding.progressBarNetworkDetails.setOutlineSpotShadowColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        mBinding.dividers.setVisibility(View.INVISIBLE);
        GitHubUser gitHubUser = getArguments().getParcelable(GitHubUser.TAG);
        ApiFactory apiFactory = new ApiFactory();
        mApi = apiFactory.getApi();

        mApi.getUser(StringUtils.buildUrl(gitHubUser.getLogin()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GitHubUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(GitHubUser gitHubUser) {
                        mBinding.dividers.setVisibility(View.VISIBLE);
                        mBinding.progressBarNetworkDetails.setVisibility(View.GONE);
                        mBinding.textViewFollowing.setText(StringUtils.appendFollowing((gitHubUser.getFollowing())));
                        mBinding.textViewFollowers.setText(StringUtils.appendFollowers(gitHubUser.getFollowers()));
                        mBinding.textViewUserBio.setText(gitHubUser.getBio());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        ImageUtils.loadImage(mBinding.imageViewUserImageDetails, gitHubUser.getAvatarUrl());
        mBinding.textViewUserNameDetails.setText(gitHubUser.getLogin());
        mBinding.textViewRepoLink.setOnClickListener( (textView) -> {
                onRepoUrlClick(StringUtils.formatGitHubUrl(gitHubUser.getLogin()));
        });
    }

    public void onRepoUrlClick(String repoUrl) {
        Uri uri = Uri.parse(repoUrl);
        mCustomTabsActivityHelper.openCustomTab(getActivity(), uri);
    }
}