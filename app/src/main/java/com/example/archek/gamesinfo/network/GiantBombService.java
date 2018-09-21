<<<<<<< HEAD
package com.example.archek.gamesinfo.network;
=======
package com.example.archek.geyms.network;
>>>>>>> 35643a588e3f291af97a0821c622af3dfbebc4c8

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GiantBombService {

    @GET("games/?api_key=3e6913417312c03f2843d49d84422d9cf7105f85&format=json&field_list=deck,image,name,guid")
    Call<GbObjectsListResponse> getGames(@Query( "limit" ) int limit, @Query( "offset" ) int offset);

    @GET("game/{guid}/?api_key=3e6913417312c03f2843d49d84422d9cf7105f85&format=json&field_list=description")
    Call<GbSingleObjectResponse> getGameDetails(@Path("guid") String guid);

    @GET("search/?api_key=3e6913417312c03f2843d49d84422d9cf7105f85&format=json&field_list=name,image,deck,guid&resources=game")
    Call<GbObjectsListResponse> searchGames(@Query("query") String query,@Query( "limit" ) int limit);

    @GET("companies/?api_key=3e6913417312c03f2843d49d84422d9cf7105f85&format=json&field_list=name,image,deck,location_country,location_city&resources=company")
    Call<GbObjectsListResponse> getCompanies(@Query( "limit" ) int limit, @Query( "offset" ) int offset);

    @GET("company/{guid}/?api_key=3e6913417312c03f2843d49d84422d9cf7105f85&format=json&field_list=description")
    Call<GbSingleObjectResponse> getCompanyDetails(@Path("guid") String guid);

    @GET("search/?api_key=3e6913417312c03f2843d49d84422d9cf7105f85&format=json&field_list=name,image,deck,location&resources=company")
    Call<GbObjectsListResponse> searchCompanies(@Query("query") String query,@Query("limit") int limit);

}
