package com.stutern.githublagos.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GitHubUser implements Parcelable {

    public static final String TAG = GitHubUser.class.getSimpleName();


        @SerializedName("login")
        private String login;
        @SerializedName("id")
        private Integer id;
        @SerializedName("avatar_url")
        private String avatarUrl;
        @SerializedName("followers")
        private int followers;
        @SerializedName("following")
        private int following;
        @SerializedName("bio")
        private String bio;
        @SerializedName("repos_url")
        private String repoUrl;

    public GitHubUser(Parcel in) {
        login = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        avatarUrl = in.readString();
        followers = in.readInt();
        following = in.readInt();
        bio = in.readString();
        repoUrl = in.readString();
    }

    public static final Creator<GitHubUser> CREATOR = new Creator<GitHubUser>() {
        @Override
        public GitHubUser createFromParcel(Parcel in) {
            return new GitHubUser(in);
        }

        @Override
        public GitHubUser[] newArray(int size) {
            return new GitHubUser[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(login);
        parcel.writeInt(id);
        parcel.writeString(avatarUrl);
        parcel.writeInt(followers);
        parcel.writeInt(following);
        parcel.writeString(bio);
        parcel.writeString(repoUrl);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @JsonProperty("login")
    public String getLogin() {
            return login;
        }

    @JsonProperty("id")
    public Integer getId() {
            return id;
        }

    @JsonProperty("id")
    public void setId(Integer id) {
            this.id = id;
        }

    @JsonProperty("avatar_url")
    public String getAvatarUrl() {
            return avatarUrl;
        }

    @JsonProperty("blog")
    public int getFollowers() {
        return followers;
    }

    @JsonProperty("following")
    public int getFollowing() {
        return following;
    }

    @JsonProperty("bio")
    public String getBio() {
        return bio;
    }

    @JsonProperty("repos_url")
    public String getRepoUrl() {
        return repoUrl;
    }

        @JsonProperty("avatar_url")
        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        @NonNull
        public static  DiffUtil.ItemCallback<GitHubUser> diffCallback = new DiffUtil.ItemCallback<GitHubUser>() {

            @Override
            public boolean areItemsTheSame(@NonNull GitHubUser oldItem, @NonNull GitHubUser newItem) {
                return oldItem.getLogin() == newItem.getLogin();
            }

            @Override
            public boolean areContentsTheSame(@NonNull GitHubUser oldItem, @NonNull GitHubUser newItem) {
                return oldItem.equals(newItem);
            }
        };

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == this)
                return true;

            GitHubUser user = (GitHubUser) obj;
            return user.getId() == user.getId();
        }

    public class GitHubResponse {
            public long total_count;
            public boolean incomplete_results;
            public List<GitHubUser> items;

        }
}
