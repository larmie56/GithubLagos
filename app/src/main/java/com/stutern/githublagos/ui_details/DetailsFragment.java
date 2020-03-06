package com.stutern.githublagos.ui_details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.stutern.githublagos.databinding.FragmentDetailsBinding;
import com.stutern.githublagos.model.GitHubUser;
import com.stutern.githublagos.ui_main.MainActivity;
import com.stutern.githublagos.utils.CustomTabsActivityHelper;
import com.stutern.githublagos.utils.ImageUtils;
import com.stutern.githublagos.utils.StringUtils;

public class DetailsFragment extends DialogFragment {

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
        GitHubUser gitHubUser = getArguments().getParcelable(GitHubUser.TAG);

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