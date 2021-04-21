package im.vector.microservices
import retrofit2.Call
import retrofit2.http.*

interface DirectoryServices {
    @GET("pegacorn/operations/directory/r1/PractitionerRole")
    fun getRoles(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRPractitionerRole>>
    @GET("pegacorn/operations/directory/r1/PractitionerRole/search")
    fun getRoles(@Query("pageSize") pageSize: Int, @Query("page") page: Int, @Query("shortName") shortName: String): Call<List<FHIRPractitionerRole>>
    @GET("pegacorn/operations/directory/r1/Practitioner")
    fun getPractitioners(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRPractitioner>>
    @GET("pegacorn/operations/directory/r1/Practitioner/{id}")
    fun getPractitioner(@Path("id") userID: String): Call<FHIRDirectoryResponse<FHIRPractitioner>>
    @GET("pegacorn/operations/directory/r1/PractitionerRole/{id}")
    fun getRole(@Path("id") roleId: String): Call<FHIRDirectoryResponse<FHIRPractitionerRole>>
    @GET("pegacorn/operations/directory/r1/Practitioner/{id}/{favouritesField}")
    fun getFavourites(@Path("id") userID: String, @Path("favouritesField") favouritesType: String): Call<FavouritesObject>
    @PUT("pegacorn/operations/directory/r1/Practitioner/{id}/{favouritesField}")
    fun putFavourites(@Path("id") userID: String, @Path("favouritesField") favouritesType: String, @Body favouritesObject: FavouritesObject): Call<Void>
}