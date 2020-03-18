package com.stutern.githublagos;

import com.stutern.githublagos.model.GitHubUser;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    @GET("search/users")
    Single<GitHubUser.GitHubResponse> getUsers(@Query("q") String q,
                                               @Query("page") int page);

    @GET()
    Single<GitHubUser> getUser(@Url String url);
}
