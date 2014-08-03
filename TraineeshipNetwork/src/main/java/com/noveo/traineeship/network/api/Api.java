package com.noveo.traineeship.network.api;

import com.noveo.traineeship.network.models.News;

import java.util.List;

import retrofit.http.GET;

public interface Api {
    public static final String END_POINT = "http://androidtraining.noveogroup.com/news";
    public static final String ALL_NEWS = "/getAll";

    @GET(ALL_NEWS)
    List<News> listNews();
}
