package com.stutern.githublagos.paging;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.stutern.githublagos.IMainActivity;
import com.stutern.githublagos.model.GitHubUser;
import com.stutern.githublagos.R;
import com.stutern.githublagos.ui_details.DetailsFragment;
import com.stutern.githublagos.utils.ImageUtils;
import com.stutern.githublagos.utils.NetworkState;

import de.hdodenhof.circleimageview.CircleImageView;

public class GithubUserAdapter extends PagedListAdapter<GitHubUser, RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private NetworkState networkState;
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;

    public GithubUserAdapter(Context context) {
        super(GitHubUser.diffCallback);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.github_user_recycler_items) {
            Log.d(TAG, "onCreateViewHolder--called, GitHubUser View inflated");
            View view = mInflater.inflate(R.layout.github_user_recycler_items, parent, false);
            return new GitHubUserViewHolder(view);
        }
        else {
            Log.d(TAG, "onCreateViewHolder--called, ProgressBar View inflated");
            View view = mInflater.inflate(R.layout.network_state_item, parent, false);
            return new ProgressBarViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GitHubUserViewHolder) {
            Log.d("Adapter class", "onCreateViewHolder--called");
            GitHubUser gitHubUser = getItem(position);
            ((GitHubUserViewHolder)holder).bind(gitHubUser);

            holder.itemView.setOnClickListener( (view) -> {
                    DetailsFragment detailsFragment = new DetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(GitHubUser.TAG, gitHubUser);;
                    detailsFragment.setArguments(bundle);
                    ((IMainActivity) mContext).onItemClick(detailsFragment);
                });
        }
        else
            ((ProgressBarViewHolder) holder).bindView(networkState);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.network_state_item;
        }
        else {
            return R.layout.github_user_recycler_items;
        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }

    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    class GitHubUserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView userName;

        public GitHubUserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.imageView_userImage);
            userName = itemView.findViewById(R.id.textView_userName);
        }

        public void bind(GitHubUser gitHubUser) {
            ImageUtils.loadImage(userImage, gitHubUser.getAvatarUrl());
            userName.setText(gitHubUser.getLogin());
        }
    }

    class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;

        public ProgressBarViewHolder(View view) {
            super(view);
            mProgressBar = view.findViewById(R.id.progressBar_network);
        }

        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.State.LOADING) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.State.FAILED) {
                Snackbar.make(itemView.getRootView(), networkState.getMsg(), Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }
}
