package im.vector.health.microservices

import android.content.Context
import im.vector.R
import im.vector.health.directory.people.model.DirectoryPeople
import im.vector.health.directory.role.model.DummyRole
import im.vector.health.directory.role.model.Location
import im.vector.health.directory.role.model.Role
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

object DirectoryConnector {
    fun convertPractitionerRole(role: FHIRPractitionerRole): DummyRole = DummyRole(role.identifiers.first().fhirIdentifier,
            getNameForIdentifier(role.identifiers,"LongName")?:role.displayName,role.displayName,
            null,role.primaryOrganizationID,role)

    fun convertRole(role: FHIRRole): Role = Role(role.simplifiedID,role.displayName,role.roleCategory, role.description)

    private fun listRoles(page: Int, pageSize: Int, context: Context, callback: (List<DummyRole>?) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitionerRoles(pageSize,page)
        response.enqueue(object : Callback<List<FHIRPractitionerRole>> {
            override fun onFailure(call: Call<List<FHIRPractitionerRole>>, t: Throwable) {
                //TODO("Not yet implemented")
                println("")
            }

            override fun onResponse(call: Call<List<FHIRPractitionerRole>>, response: Response<List<FHIRPractitionerRole>>) {
                //TODO("Not yet implemented")

                callback(response.body()?.map { x -> convertPractitionerRole(x) })
            }

        })
    }

    fun getPractitionerRoles(page: Int, pageSize: Int, context: Context, name:String?, callback: (List<DummyRole>?) -> Unit) {
        name?.let {query ->
            val retrofit = Retrofit.Builder()
                    .baseUrl(context.getString(R.string.microservice_server_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            // Create Service
            val service = retrofit.create(DirectoryServices::class.java)
            val response = service.getPractitionerRoles(pageSize,page,query)
            response.enqueue(object : Callback<List<FHIRPractitionerRole>> {
                override fun onFailure(call: Call<List<FHIRPractitionerRole>>, t: Throwable) {
                    //TODO("Not yet implemented")
                    println("")
                }

                override fun onResponse(call: Call<List<FHIRPractitionerRole>>, response: Response<List<FHIRPractitionerRole>>) {
                    //TODO("Not yet implemented")

                    callback(response.body()?.map { x -> convertPractitionerRole(x) })
                }

            })
        }
        if (name == null) listRoles(page,pageSize,context,callback)
    }

    fun getPractitionerRole(roleId: String, context: Context, callback: (FHIRPractitionerRole) -> Unit){
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitionerRole(roleId)
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

    private fun getNameForIdentifier(idList: List<FHIRIdentifier>, type: String): String? {
        return idList.find { x -> x.type == type }?.value
    }

    fun convertPractitioner(from: FHIRPractitioner): DirectoryPeople {
        return DirectoryPeople(from.identifiers.first().fhirIdentifier,
                from.displayName,"","","","",
                from.assignedPractitionerRoles)
    }

    fun getPractitioners(page: Int, pageSize: Int, context: Context, callback: (List<DirectoryPeople>?) -> Unit) {

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
                    convertPractitioner(x)
                }
                callback(people)
            }

        })
    }

    //Takes practitioner ID (email address)
    fun getPractitioner(practitionerId: String, context: Context, callback: (FHIRPractitioner) -> Unit) {
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitioner(practitionerId)
        response.enqueue(object : Callback<FHIRDirectoryResponse<FHIRPractitioner>> {
            override fun onFailure(call: Call<FHIRDirectoryResponse<FHIRPractitioner>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<FHIRDirectoryResponse<FHIRPractitioner>>, response: Response<FHIRDirectoryResponse<FHIRPractitioner>>) {
                response.body().toString()
                response.body()?.let {
                    it.entry?.let{
                        it.roles = ArrayList()
                        it.dataChangeEventListeners = ArrayList()
                        it.assignedPractitionerRoles?.forEach { x -> getPractitionerRole(x,context) { role ->
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

    fun convertLocation(location: FHIRLocation): Location = Location(location.simplifiedID,location.displayName, location.description)

    fun getLocations(page: Int, pageSize: Int, context: Context, callback: (List<Location>?) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getLocations(pageSize,page)
        response.enqueue(object : Callback<List<FHIRLocation>> {
            override fun onFailure(call: Call<List<FHIRLocation>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<List<FHIRLocation>>, response: Response<List<FHIRLocation>>) {
                //TODO("Not yet implemented")
                val locationList = response.body()?.map { x ->
                    convertLocation(x)
                }
                callback(locationList)
            }

        })
    }

    fun getLocation(locationId: String, context: Context, callback: (FHIRLocation) -> Unit) {
        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getLocation(locationId)
        response.enqueue(object : Callback<FHIRDirectoryResponse<FHIRLocation>> {
            override fun onFailure(call: Call<FHIRDirectoryResponse<FHIRLocation>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<FHIRDirectoryResponse<FHIRLocation>>, response: Response<FHIRDirectoryResponse<FHIRLocation>>) {
                response.body().toString()
                response.body()?.let {
                    it.entry?.let{
                        callback(it)
                    }
                }
            }

        })
    }

    fun getRoles(page: Int, pageSize: Int, context: Context, callback: (List<Role>?) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.microservice_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getRoles(pageSize,page)
        response.enqueue(object : Callback<List<FHIRRole>> {
            override fun onFailure(call: Call<List<FHIRRole>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<List<FHIRRole>>, response: Response<List<FHIRRole>>) {
                //TODO("Not yet implemented")
                val roleList = response.body()?.map { x ->
                    convertRole(x)
                }
                callback(roleList)
            }

        })
    }

    var cachedFavourites: EnumMap<FavouriteTypes,List<String>> = EnumMap(im.vector.health.microservices.FavouriteTypes::class.java)
    var practitionerId: String = ""
    fun initializeWithPractitionerId(practitionerId: String) {
        if (practitionerId == this.practitionerId) return
        this.practitionerId = practitionerId
        this.cachedFavourites = EnumMap(im.vector.health.microservices.FavouriteTypes::class.java)
    }

    private fun getFavourites(context: Context, favouriteTypes: FavouriteTypes, callback: (List<String>) -> Unit){
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
                    val itms = itm.favourites.distinct().toList()
                    cachedFavourites[favouriteTypes] = itms
                    callback(itms)
                }
            }

        })
    }

    private fun setFavourites(context: Context, favouriteTypes: FavouriteTypes, newList: ArrayList<String>) {
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

    fun addFavourite(context: Context, favouriteTypes: FavouriteTypes, favourite: String) {
         getFavourites(context,favouriteTypes) {
             if (!it.contains(favourite)) {
                 val newList = ArrayList(it)
                 newList.add(favourite)
                 setFavourites(context,favouriteTypes,newList)
             }
         }
    }

    fun removeFavourite(context: Context, favouriteTypes: FavouriteTypes, favourite: String) {
        getFavourites(context,favouriteTypes) {
            if (it.contains(favourite)) {
                val newList = ArrayList(it)
                newList.removeAll { itm -> itm == favourite }
                setFavourites(context,favouriteTypes,newList)
            }
        }
    }

    fun checkFavourite(context: Context, favouriteTypes: FavouriteTypes, favourite: String, callback: (Boolean) -> Unit) {
        if (cachedFavourites[favouriteTypes]?.contains(favourite) == true) {
            callback(true)
        } else if (cachedFavourites[favouriteTypes]?.count() ?: 0 > 0) {
            callback(false)
        } else {
            getFavourites(context,favouriteTypes) {
                if (it.contains(favourite)) callback(true)
                else callback(false)
            }
        }
    }

    fun getActiveRoles(context:Context, callback: (List<DummyRole>?) -> Unit) {
        getPractitioner(practitionerId,context) {practitioner ->
            convertPractitioner(practitioner).FetchRoles(context) {loadedRoles ->
                callback(loadedRoles)
            }
        }
    }
}