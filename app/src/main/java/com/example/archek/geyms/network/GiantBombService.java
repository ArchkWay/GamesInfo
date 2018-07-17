package com.example.archek.geyms.network;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiantBombService {

    @GET("games/?api_key=0a80e1bbb07356d3658e6413b066b13824764567&format=json&field_list=deck,image,name")
    Call<GbObjectsListResponse> getGames(@Query( "limit" ) int limit, @Query( "offset" ) int offset);
}