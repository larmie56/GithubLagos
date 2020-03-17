package com.stutern.githublagos;

import com.stutern.githublagos.model.GitHubUser;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("users")
    Single<GitHubUser.GitHubResponse> getUsers(@Query("q") String q,
                                               @Query("page") int page);
}
