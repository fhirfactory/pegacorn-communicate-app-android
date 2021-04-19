package im.vector.microservices

import android.content.Context
import im.vector.R
import im.vector.directory.people.model.DirectoryPeople
import im.vector.directory.role.model.DummyRole
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DirectoryConnector {
    fun GetRoles(page: Int, pageSize: Int, context: Context, callback: (List<DummyRole>?) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getRoles(pageSize,page)
        response.enqueue(object : Callback<List<FHIRPractitionerRole>> {
            override fun onFailure(call: Call<List<FHIRPractitionerRole>>, t: Throwable) {
                //TODO("Not yet implemented")
                println("")
            }

            override fun onResponse(call: Call<List<FHIRPractitionerRole>>, response: Response<List<FHIRPractitionerRole>>) {
                //TODO("Not yet implemented")

                callback(response.body()?.map { x -> DummyRole(x.identifiers.first().fhirIdentifier, GetNameForIdentifier(x.identifiers,"LongName")?:x.displayName,x.displayName,null,x.primaryOrganizationID, ArrayList(), ArrayList(), ArrayList(),ArrayList()) })
            }

        })
    }

    fun GetNameForIdentifier(idList: List<FHIRIdentifier>, type: String): String? {
        return idList.find { x -> x.type == type }?.value
    }

    fun GetPractitioners(page: Int, pageSize: Int, context: Context, callback: (List<DirectoryPeople>?) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitioners(pageSize,page)
        response.enqueue(object : Callback<List<FHIRPractitioner>> {
            override fun onFailure(call: Call<List<FHIRPractitioner>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<List<FHIRPractitioner>>, response: Response<List<FHIRPractitioner>>) {
                //TODO("Not yet implemented")

                callback(response.body()?.map { x -> DirectoryPeople(x.identifiers.first().fhirIdentifier,x.displayName,"","","","") })
            }

        })
    }
}