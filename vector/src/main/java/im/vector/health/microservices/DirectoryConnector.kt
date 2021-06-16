package im.vector.health.microservices

import im.vector.health.microservices.APIModel.*
import im.vector.health.microservices.interfaces.APIError
import im.vector.health.microservices.interfaces.*
import im.vector.health.microservices.mock.MockPatient
import im.vector.health.microservices.model.HealthcareService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import im.vector.health.microservices.model.PractitionerRole
import im.vector.health.microservices.model.Practitioner


class DirectoryConnector: IDirectoryServiceProvider {

    lateinit var baseURL: String
    var practitionerId: String = ""
    set(value){
        try {
            if (value == this.practitionerId) return
        } catch (e: NullPointerException) {
            //this is fine, because we're setting the value
        } finally {
            this.cachedFavourites = EnumMap(FavouriteTypes::class.java)
            field = value
        }
    }
    get() {
        return field
    }

    private fun listRoles(page: Int, pageSize: Int, callback: (List<PractitionerRole>?, Int) -> Unit, failure: (APIError) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitionerRoles(pageSize,page)
        response.enqueue(object : Callback<List<FHIRPractitionerRole>> {
            override fun onFailure(call: Call<List<FHIRPractitionerRole>>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
            }

            override fun onResponse(call: Call<List<FHIRPractitionerRole>>, response: Response<List<FHIRPractitionerRole>>) {
                //TODO("Not yet implemented")

                val practitionerRoles = response.body()?.map { PractitionerRole(it) }
                val count = response.headers().get("X-Total-Count") ?: "0"
                callback(practitionerRoles,Integer.parseInt(count))
            }

        })
    }
    private fun getPractitionerRoles(page: Int, pageSize: Int, name:String?, callback: (List<IPractitionerRole>?, Int) -> Unit, failure: (APIError) -> Unit) {
        name?.let {query ->
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            // Create Service
            val service = retrofit.create(DirectoryServices::class.java)
            val response = service.getPractitionerRoles(pageSize,page,query)
            response.enqueue(object : Callback<List<FHIRPractitionerRole>> {
                override fun onFailure(call: Call<List<FHIRPractitionerRole>>, t: Throwable) {
                    failure(BasicAPIError(t.localizedMessage))
                }

                override fun onResponse(call: Call<List<FHIRPractitionerRole>>, response: Response<List<FHIRPractitionerRole>>) {
                    val practitionerRoles = response.body()?.map { PractitionerRole(it) }
                    val count = response.headers().get("X-Total-Count") ?: "0"
                    callback(practitionerRoles,Integer.parseInt(count))
                }

            })
        }
        if (name == null) listRoles(page,pageSize,callback,failure)
    }

    private fun listRoleFavourites(page: Int, pageSize: Int, callback: (List<PractitionerRole>?, Int) -> Unit, failure: (APIError) -> Unit) {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitionerRoleFavourites(practitionerId,pageSize,page)
        response.enqueue(object : Callback<List<FHIRPractitionerRole>> {
            override fun onFailure(call: Call<List<FHIRPractitionerRole>>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
            }

            override fun onResponse(call: Call<List<FHIRPractitionerRole>>, response: Response<List<FHIRPractitionerRole>>) {
                val practitionerRoles = response.body()?.map { PractitionerRole(it) }
                val count = response.headers().get("X-Total-Count") ?: "0"
                callback(practitionerRoles,Integer.parseInt(count))
            }

        })
    }
    private fun getPractitionerRoleFavourites(page: Int, pageSize: Int, name:String?, callback: (List<IPractitionerRole>?, Int) -> Unit, failure: (APIError) -> Unit) {
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        name?.let {query ->
            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Create Service
            val service = retrofit.create(DirectoryServices::class.java)
            val response = service.getPractitionerRoleFavourites(practitionerId,pageSize,page,query)
            response.enqueue(object : Callback<List<FHIRPractitionerRole>> {
                override fun onFailure(call: Call<List<FHIRPractitionerRole>>, t: Throwable) {
                    failure(BasicAPIError(t.localizedMessage))
                }

                override fun onResponse(call: Call<List<FHIRPractitionerRole>>, response: Response<List<FHIRPractitionerRole>>) {
                    val practitionerRoles = response.body()?.map { PractitionerRole(it) }
                    val count = response.headers().get("X-Total-Count") ?: "0"
                    callback(practitionerRoles,Integer.parseInt(count))
                }

            })
        }
        if (name == null) listRoleFavourites(page,pageSize,callback,failure)
    }

    private fun getPractitioners(page: Int, pageSize: Int, callback: (List<Practitioner>?, Int) -> Unit, failure: (APIError) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitioners(pageSize,page)
        response.enqueue(object : Callback<List<FHIRPractitioner>> {
            override fun onFailure(call: Call<List<FHIRPractitioner>>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
            }

            override fun onResponse(call: Call<List<FHIRPractitioner>>, response: Response<List<FHIRPractitioner>>) {
                val people = response.body()?.map { Practitioner(it) }
                val count = response.headers().get("X-Total-Count") ?: "0"
                callback(people,Integer.parseInt(count))
            }

        })
    }
    private fun getPractitioners(page: Int, pageSize: Int, name: String?, callback: (List<IPractitioner>?, Int) -> Unit, failure: (APIError) -> Unit) {
        name?.let { query ->
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            // Create Service
            val service = retrofit.create(DirectoryServices::class.java)
            val response = service.getPractitioners(pageSize,page,query)
            response.enqueue(object : Callback<List<FHIRPractitioner>> {
                override fun onFailure(call: Call<List<FHIRPractitioner>>, t: Throwable) {
                    failure(BasicAPIError(t.localizedMessage))
                }

                override fun onResponse(call: Call<List<FHIRPractitioner>>, response: Response<List<FHIRPractitioner>>) {
                    //TODO("Not yet implemented")
                    val people = response.body()?.map { Practitioner(it) }
                    val count = response.headers().get("X-Total-Count") ?: "0"
                    callback(people,Integer.parseInt(count))
                }

            })
        }
        if (name == null) return getPractitioners(page,pageSize,callback,failure)
    }

    private fun getPractitionerFavourites(page: Int, pageSize: Int, callback: (List<Practitioner>?, Int) -> Unit, failure: (APIError) -> Unit) {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getPractitionerFavourites(practitionerId,pageSize,page)
        response.enqueue(object : Callback<List<FHIRPractitioner>> {
            override fun onFailure(call: Call<List<FHIRPractitioner>>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
            }

            override fun onResponse(call: Call<List<FHIRPractitioner>>, response: Response<List<FHIRPractitioner>>) {
                val people = response.body()?.map { Practitioner(it) }
                val count = response.headers().get("X-Total-Count") ?: "0"
                callback(people,Integer.parseInt(count))
            }

        })
    }
    private fun getPractitionerFavourites(page: Int, pageSize: Int, name: String?, callback: (List<IPractitioner>?, Int) -> Unit, failure: (APIError) -> Unit) {
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        name?.let { query ->
            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Create Service
            val service = retrofit.create(DirectoryServices::class.java)
            val response = service.getPractitionerFavourites(practitionerId,pageSize,page,query)
            response.enqueue(object : Callback<List<FHIRPractitioner>> {
                override fun onFailure(call: Call<List<FHIRPractitioner>>, t: Throwable) {
                    failure(BasicAPIError(t.localizedMessage))
                }

                override fun onResponse(call: Call<List<FHIRPractitioner>>, response: Response<List<FHIRPractitioner>>) {
                    val people = response.body()?.map { Practitioner(it) }
                    val count = response.headers().get("X-Total-Count") ?: "0"
                    callback(people,Integer.parseInt(count))
                }

            })
        }
        if (name == null) return getPractitionerFavourites(page,pageSize,callback,failure)
    }


    fun getLocations(page: Int, pageSize: Int, callback: (List<FHIRLocation>?) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
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
                callback(response.body())
            }

        })
    }

    fun getLocation(locationId: String, callback: (FHIRLocation) -> Unit) {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
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

    fun getRoles(page: Int, pageSize: Int, callback: (List<FHIRRole>?) -> Unit) {

        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
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
                callback(response.body())
            }

        })
    }

    var cachedFavourites: EnumMap<FavouriteTypes,List<String>> = EnumMap(FavouriteTypes::class.java)

    private fun getFavourites(favouriteTypes: FavouriteTypes, callback: (List<String>) -> Unit, failure: (APIError) -> Unit){
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getFavourites(practitionerId,favouriteTypes.path)
        response.enqueue(object: Callback<FavouritesObject>{
            override fun onFailure(call: Call<FavouritesObject>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
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

    private fun setFavourites(favouriteTypes: FavouriteTypes, newList: ArrayList<String>, failure: (APIError) -> Unit) {
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.putFavourites(practitionerId,favouriteTypes.path, FavouritesObject(favourites = newList))
        response.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                //
            }

        })
        cachedFavourites[favouriteTypes] = newList
    }

    private fun addFavourite(favouriteTypes: FavouriteTypes, favourite: String, failure: (APIError) -> Unit) {
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        getFavourites(favouriteTypes, {
            if (!it.contains(favourite)) {
                val newList = ArrayList(it)
                newList.add(favourite)
                setFavourites(favouriteTypes,newList,failure)
            }
        }, failure)
    }

    private fun removeFavourite(favouriteTypes: FavouriteTypes, favourite: String, failure: (APIError) -> Unit) {
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        getFavourites(favouriteTypes, {
            if (it.contains(favourite)) {
                val newList = ArrayList(it)
                newList.removeAll { itm -> itm == favourite }
                setFavourites(favouriteTypes,newList, failure)
            }
        }, failure)
    }

    private fun checkFavourite(favouriteTypes: FavouriteTypes, favourite: String, callback: (Boolean) -> Unit, failure: (APIError) -> Unit) {
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        if (cachedFavourites[favouriteTypes]?.contains(favourite) == true) {
            callback(true)
        } else if (cachedFavourites[favouriteTypes]?.count() ?: 0 > 0) {
            callback(false)
        } else {
            getFavourites(favouriteTypes, {
                if (it.contains(favourite)) callback(true)
                else callback(false)
            }, failure)
        }
    }

    private fun getActiveRoles(callback: (List<IPractitionerRole>?) -> Unit, failure: (APIError) -> Unit) {
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        GetPractitioner(practitionerId, { prac ->
            if (prac==null) return@GetPractitioner
            prac.GetRoles(callback)
        }, failure)
    }

    private fun listHealthcareServices(page: Int, pageSize: Int, callback: (List<IHealthcareService>?, Int) -> Unit, failure: (APIError) -> Unit) {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getHealthcareServices(pageSize,page)
        response.enqueue(object : Callback<List<FHIRHealthcareService>> {
            override fun onFailure(call: Call<List<FHIRHealthcareService>>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
            }

            override fun onResponse(call: Call<List<FHIRHealthcareService>>, response: Response<List<FHIRHealthcareService>>) {
                val practitionerRoles = response.body()?.map { HealthcareService(it) }
                val count = response.headers().get("X-Total-Count") ?: "0"
                callback(practitionerRoles,Integer.parseInt(count))
            }

        })
    }
    private fun getHealthcareServices(page: Int, pageSize: Int, name:String?, callback: (List<IHealthcareService>?, Int) -> Unit, failure: (APIError) -> Unit) {
        name?.let {query ->
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            // Create Service
            val service = retrofit.create(DirectoryServices::class.java)
            val response = service.getHealthcareServices(pageSize,page,query)
            response.enqueue(object : Callback<List<FHIRHealthcareService>> {
                override fun onFailure(call: Call<List<FHIRHealthcareService>>, t: Throwable) {
                    failure(BasicAPIError(t.localizedMessage))
                }

                override fun onResponse(call: Call<List<FHIRHealthcareService>>, response: Response<List<FHIRHealthcareService>>) {
                    val practitionerRoles = response.body()?.map { HealthcareService(it) }
                    val count = response.headers().get("X-Total-Count") ?: "0"
                    callback(practitionerRoles,Integer.parseInt(count))
                }

            })
        }
        if (name == null) listHealthcareServices(page,pageSize,callback,failure)
    }

    private fun listHealthcareServiceFavourites(page: Int, pageSize: Int, callback: (List<IHealthcareService>?, Int) -> Unit, failure: (APIError) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(DirectoryServices::class.java)
        val response = service.getHealthcareServiceFavourites(practitionerId,pageSize,page)
        response.enqueue(object : Callback<List<FHIRHealthcareService>> {
            override fun onFailure(call: Call<List<FHIRHealthcareService>>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
            }

            override fun onResponse(call: Call<List<FHIRHealthcareService>>, response: Response<List<FHIRHealthcareService>>) {
                val practitionerRoles = response.body()?.map { HealthcareService(it) }
                val count = response.headers().get("X-Total-Count") ?: "0"
                callback(practitionerRoles,Integer.parseInt(count))
            }

        })
    }
    private fun getHealthcareServiceFavourites(page: Int, pageSize: Int, name:String?, callback: (List<IHealthcareService>?, Int) -> Unit, failure: (APIError) -> Unit) {
        if (practitionerId == ""){
            failure(BasicAPIError("Not signed in."))
            return
        }
        name?.let {query ->
            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Create Service
            val service = retrofit.create(DirectoryServices::class.java)
            val response = service.getHealthcareServiceFavourites(practitionerId,pageSize,page,query)
            response.enqueue(object : Callback<List<FHIRHealthcareService>> {
                override fun onFailure(call: Call<List<FHIRHealthcareService>>, t: Throwable) {
                    failure(BasicAPIError(t.localizedMessage))
                }

                override fun onResponse(call: Call<List<FHIRHealthcareService>>, response: Response<List<FHIRHealthcareService>>) {
                    val practitionerRoles = response.body()?.map { HealthcareService(it) }
                    val count = response.headers().get("X-Total-Count") ?: "0"
                    callback(practitionerRoles,Integer.parseInt(count))
                }

            })
        }
        if (name == null) listHealthcareServiceFavourites(page,pageSize,callback,failure)
    }

    fun getDirectoryServices(): DirectoryServices = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build().create(DirectoryServices::class.java)

    fun <T> getItemFromID(call: Call<FHIRDirectoryResponse<T>>, callback: (T?) -> Unit, failure: (APIError) -> Unit) {
        call.enqueue(object: Callback<FHIRDirectoryResponse<T>> {
            override fun onFailure(call: Call<FHIRDirectoryResponse<T>>, t: Throwable) {
                failure(BasicAPIError(t.localizedMessage))
            }

            override fun onResponse(call: Call<FHIRDirectoryResponse<T>>, response: Response<FHIRDirectoryResponse<T>>) {
                response.body()?.let {
                    it.entry?.let{value ->
                        callback(value)
                        return
                    }
                }
                callback(null)
            }

        })
    }

    override fun GetPractitioners(query: String?, page: Int, pageSize: Int, callback: (List<IPractitioner>?, Int) -> Unit, failure: (APIError) -> Unit) = getPractitioners(page,pageSize,query,callback,failure)
    override fun GetPractitionerFavourites(
        query: String?,
        page: Int,
        pageSize: Int,
        callback: (List<IPractitioner>?, Int) -> Unit,
        failure: (APIError) -> Unit

    ) {
        getPractitionerFavourites(page,pageSize,query,callback,failure)
    }

    override fun GetPractitionerRoles(query: String?, page: Int, pageSize: Int, callback: (List<IPractitionerRole>?, Int) -> Unit, failure: (APIError) -> Unit) = getPractitionerRoles(page,pageSize,query,callback,failure)
    override fun GetPractitionerRoleFavourites(
        query: String?,
        page: Int,
        pageSize: Int,
        callback: (List<IPractitionerRole>?, Int) -> Unit,
        failure: (APIError) -> Unit
    ) {
        getPractitionerRoleFavourites(page,pageSize,query,callback,failure)
    }

    override fun GetHealthcareServices(query: String?, page: Int, pageSize: Int, callback: (List<IHealthcareService>?, Int) -> Unit, failure: (APIError) -> Unit) = getHealthcareServices(page,pageSize,query,callback,failure)
    override fun GetHealthcareServiceFavourites(
        query: String?,
        page: Int,
        pageSize: Int,
        callback: (List<IHealthcareService>?, Int) -> Unit,
        failure: (APIError) -> Unit
    ) {
        getHealthcareServiceFavourites(page,pageSize,query,callback,failure)
    }

    override fun GetPatients(query: String?, page: Int, pageSize: Int, callback: (List<IPatient>?, Int) -> Unit, failure: (APIError) -> Unit) {
        val list = (1..275)
        val filtered = list.filter {itm ->
            query?.let {
                return@filter itm.toString().contains(it
                    .toLowerCase()
                    .replace("patient","")
                    .trim())
            }
            return@filter true
        }
        val final = filtered
            .drop(pageSize * page)
            .take(pageSize)
            .map { MockPatient("Patient "+it.toString(), it.toString(), Date()) }

        callback(final,filtered.count())
    }

    override fun GetPractitioner(id: String, callback: (IPractitioner?) -> Unit, failure: (APIError) -> Unit) = getItemFromID(getDirectoryServices().getPractitioner(id), {
        it?.let {
            callback(Practitioner(it))
            return@getItemFromID
        }
        callback(null)
    },failure)

    override fun GetPractitionerRole(id: String, callback: (IPractitionerRole?) -> Unit, failure: (APIError) -> Unit) = getItemFromID(getDirectoryServices().getPractitionerRole(id), {
        it?.let {
            callback(PractitionerRole(it))
            return@getItemFromID
        }
        callback(null)
    }, failure)

    override fun GetHealthcareService(id: String, callback: (IHealthcareService?) -> Unit, failure: (APIError) -> Unit) = getItemFromID(getDirectoryServices().getHealthcareService(id), {
        it?.let {
            callback(HealthcareService(it))
            return@getItemFromID
        }
        callback(null)
    }, failure)

    override fun SetBaseURL(url: String, failure: (APIError) -> Unit) {
        baseURL = url
    }

    override fun SetPractitionerID(id: String, callback: () -> Unit, failure: (APIError) -> Unit) {
        practitionerId = id
        signedIn = false
        if (id != "") {
            GetPractitioner(id, { practitioner ->
                practitioner?.let {
                    signedIn = true
                    callback()
                }
            }, failure)
        }
    }

    override fun GetActiveRoles(callback: (List<IPractitionerRole>?) -> Unit, failure: (APIError) -> Unit) = getActiveRoles(callback, failure)

    override fun CheckFavourite(favouriteTypes: FavouriteTypes, favourite: String, callback: (Boolean) -> Unit, failure: (APIError) -> Unit) = checkFavourite(favouriteTypes,favourite,callback,failure)

    override fun RemoveFavourite(favouriteTypes: FavouriteTypes, favourite: String, failure: (APIError) -> Unit) = removeFavourite(favouriteTypes,favourite,failure)

    override fun AddFavourite(favouriteTypes: FavouriteTypes, favourite: String, failure: (APIError) -> Unit) = addFavourite(favouriteTypes,favourite,failure)

    private var signedIn: Boolean = false
    override fun CheckValidPractitionerSignIn(): Boolean = signedIn
}