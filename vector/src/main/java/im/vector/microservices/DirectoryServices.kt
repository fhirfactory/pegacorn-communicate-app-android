package im.vector.microservices
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DirectoryServices {
    @GET("pegacorn/operations/directory/r1/PractitionerRole")
    fun getRoles(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRPractitionerRole>>
    @GET("pegacorn/operations/directory/r1/Practitioner")
    fun getPractitioners(@Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<List<FHIRPractitioner>>
}