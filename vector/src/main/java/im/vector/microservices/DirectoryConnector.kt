package im.vector.microservices

import android.content.Context
import im.vector.R
import im.vector.directory.people.model.DirectoryPeople
import im.vector.directory.role.model.DummyRole
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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

    fun GetRole(roleId: String, context: Context, callback: (FHIRPractitionerRole) -> Unit){
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getRole(roleId)
        response.enqueue(object : Callback<FHIRDirectoryResponse<FHIRPractitionerRole>> {
            override fun onFailure(call: Call<FHIRDirectoryResponse<FHIRPractitionerRole>>, t: Throwable) {
                //TODO("Not yet implemented")

            }

            override fun onResponse(call: Call<FHIRDirectoryResponse<FHIRPractitionerRole>>, response: Response<FHIRDirectoryResponse<FHIRPractitionerRole>>) {
                //TODO("Not yet implemented")
                response.body()?.let {
                    it.entry?.let {
                        callback(it)
                    }
                }
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
                val people = response.body()?.map { x ->
                    DirectoryPeople(x.identifiers.first().fhirIdentifier,x.displayName,"","","","")
                }
                callback(people)
            }

        })
    }

    //Takes practitioner ID (email address)
    fun GetPractitioner(practitionerId: String, context: Context, callback: (FHIRPractitioner) -> Unit) {
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitioner(practitionerId)
        response.enqueue(object : Callback<FHIRDirectoryResponse<FHIRPractitioner>> {
            override fun onFailure(call: Call<FHIRDirectoryResponse<FHIRPractitioner>>, t: Throwable) {
                //TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<FHIRDirectoryResponse<FHIRPractitioner>>, response: Response<FHIRDirectoryResponse<FHIRPractitioner>>) {
                response.body().toString()
                response.body()?.let {
                    it.entry?.let{
                        it.currentPractitionerRoles?.forEach { x -> GetRole(x,context) {role ->
                            it.roles.add(role)
                            it.dataChanged()
                        }
                        }
                        callback(it)
                    }
                }
            }

        })
    }

    var cachedFavourites: EnumMap<FavouriteTypes,ArrayList<String>> = EnumMap(im.vector.microservices.FavouriteTypes::class.java)
    var practitionerId: String = ""
    fun InitializeWithPractitionerId(practitionerId: String) {
        if (practitionerId == this.practitionerId) return
        this.practitionerId = practitionerId
        this.cachedFavourites = EnumMap(im.vector.microservices.FavouriteTypes::class.java)
    }

    fun GetFavourites(context: Context, favouriteTypes: FavouriteTypes, callback: (List<String>) -> Unit){
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getFavourites(practitionerId,favouriteTypes.path)
        response.enqueue(object: Callback<FavouritesObject>{
            override fun onFailure(call: Call<FavouritesObject>, t: Throwable) {
                //TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<FavouritesObject>, response: Response<FavouritesObject>) {
                response.body()?.let { itm ->
                    cachedFavourites[favouriteTypes] = itm.favourites
                    callback(itm.favourites)
                }
            }

        })
    }

    fun SetFavourites(context: Context, favouriteTypes: FavouriteTypes, newList: ArrayList<String>) {
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.putFavourites(practitionerId,favouriteTypes.path, FavouritesObject(favourites = newList))
        response.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                //TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                //
            }

        })
        cachedFavourites[favouriteTypes] = newList
    }

    fun AddFavourite(context: Context, favouriteTypes: FavouriteTypes, favourite: String) {
         GetFavourites(context,favouriteTypes) {
             if (!it.contains(favourite)) {
                 val newList = ArrayList(it)
                 newList.add(favourite)
                 SetFavourites(context,favouriteTypes,newList)
             }
         }
    }

    fun RemoveFavourite(context: Context, favouriteTypes: FavouriteTypes, favourite: String) {
        GetFavourites(context,favouriteTypes) {
            if (it.contains(favourite)) {
                val newList = ArrayList(it)
                newList.removeAll { it == favourite }
                SetFavourites(context,favouriteTypes,newList)
            }
        }
    }

    fun CheckFavourite(context: Context, favouriteTypes: FavouriteTypes, favourite: String, callback: (Boolean) -> Unit) {
        if (cachedFavourites[favouriteTypes]?.contains(favourite) == true) {
            callback(true)
        } else if (cachedFavourites[favouriteTypes]?.count() ?: 0 > 0) {
            callback(false)
        } else {
            GetFavourites(context,favouriteTypes) {
                if (it.contains(favourite)) callback(true)
                else callback(false)
            }
        }
    }
}