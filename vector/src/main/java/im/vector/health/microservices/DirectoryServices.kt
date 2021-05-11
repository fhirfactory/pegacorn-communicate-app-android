package im.vector.health.microservices
import retrofit2.Call
import retrofit2.http.*

interface DirectoryServices {
    @GET("pegacorn/operations/directory/r1/PractitionerRole")
    fun getPractitionerRoles(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRPractitionerRole>>

    @GET("pegacorn/operations/directory/r1/PractitionerRole/search")
    fun getPractitionerRoles(@Query("pageSize") pageSize: Int, @Query("page") page: Int, @Query("displayName") displayName: String): Call<List<FHIRPractitionerRole>>

    @GET("pegacorn/operations/directory/r1/Practitioner")
    fun getPractitioners(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRPractitioner>>

    @GET("pegacorn/operations/directory/r1/Practitioner/search")
    fun getPractitioners(@Query("pageSize") pageSize: Int, @Query("page") page: Int, @Query("displayName") displayName: String): Call<List<FHIRPractitioner>>

    @GET("pegacorn/operations/directory/r1/Practitioner/{id}")
    fun getPractitioner(@Path("id") userID: String): Call<FHIRDirectoryResponse<FHIRPractitioner>>

    @GET("pegacorn/operations/directory/r1/PractitionerRole/{id}")
    fun getPractitionerRole(@Path("id") practitionerRoleId: String): Call<FHIRDirectoryResponse<FHIRPractitionerRole>>

    @GET("pegacorn/operations/directory/r1/Practitioner/{id}/{favouritesField}")
    fun getFavourites(@Path("id") userID: String, @Path("favouritesField") favouritesType: String): Call<FavouritesObject>

    @PUT("pegacorn/operations/directory/r1/Practitioner/{id}/{favouritesField}")
    fun putFavourites(@Path("id") userID: String, @Path("favouritesField") favouritesType: String, @Body favouritesObject: FavouritesObject): Call<Void>

    @GET("pegacorn/operations/directory/r1/RoleCategory/{id}")
    fun getRoleCategory(@Path("id") userID: String): Call<FHIRDirectoryResponse<FHIRRoleCategory>>

    @GET("pegacorn/operations/directory/r1/Role/{id}")
    fun getRole(@Path("id") roleId: String): Call<FHIRDirectoryResponse<FHIRRole>>

    @GET("pegacorn/operations/directory/r1/Role")
    fun getRoles(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRRole>>

    @GET("pegacorn/operations/directory/r1/RoleCategory")
    fun getRoleCategories(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRRole>>

    @GET("pegacorn/operations/directory/r1/Location/{id}")
    fun getLocation(@Path("id") locationId: String): Call<FHIRDirectoryResponse<FHIRLocation>>

    @GET("pegacorn/operations/directory/r1/Location")
    fun getLocations(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRLocation>>
}