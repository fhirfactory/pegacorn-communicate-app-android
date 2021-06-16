package im.vector.health.microservices.interfaces

import im.vector.health.microservices.APIModel.FavouriteTypes

interface IDirectoryServiceProvider {
    fun GetPractitioners(query: String?, page: Int, pageSize: Int, callback: (List<IPractitioner>?, Int) -> Unit, failure: (APIError) -> Unit)
    fun GetPractitionerFavourites(query: String?, page: Int, pageSize: Int, callback: (List<IPractitioner>?, Int) -> Unit, failure: (APIError) -> Unit)
    fun GetPractitionerRoles(query: String?, page: Int, pageSize: Int, callback: (List<IPractitionerRole>?, Int) -> Unit, failure: (APIError) -> Unit)
    fun GetPractitionerRoleFavourites(query: String?, page: Int, pageSize: Int, callback: (List<IPractitionerRole>?, Int) -> Unit, failure: (APIError) -> Unit)
    fun GetHealthcareServices(query: String?, page: Int, pageSize: Int, callback: (List<IHealthcareService>?, Int) -> Unit, failure: (APIError) -> Unit)
    fun GetHealthcareServiceFavourites(query: String?, page: Int, pageSize: Int, callback: (List<IHealthcareService>?, Int) -> Unit, failure: (APIError) -> Unit)
    fun GetPatients(query: String?, page: Int, pageSize: Int, callback: (List<IPatient>?, Int) -> Unit, failure: (APIError) -> Unit)
    fun GetPractitioner(id: String, callback: (IPractitioner?) -> Unit, failure: (APIError) -> Unit)
    fun GetPractitionerRole(id: String, callback: (IPractitionerRole?) -> Unit, failure: (APIError) -> Unit)
    fun GetHealthcareService(id: String, callback: (IHealthcareService?) -> Unit, failure: (APIError) -> Unit)
    fun SetBaseURL(url: String, failure: (APIError) -> Unit)
    fun SetPractitionerID(id: String, failure: (APIError) -> Unit)
    fun GetActiveRoles(callback: (List<IPractitionerRole>?) -> Unit, failure: (APIError) -> Unit)
    fun CheckFavourite(favouriteTypes: FavouriteTypes, favourite: String, callback: (Boolean) -> Unit, failure: (APIError) -> Unit)
    fun RemoveFavourite(favouriteTypes: FavouriteTypes, favourite: String, failure: (APIError) -> Unit)
    fun AddFavourite(favouriteTypes: FavouriteTypes, favourite: String, failure: (APIError) -> Unit)
    fun CheckValidPractitionerSignIn(): Boolean
}