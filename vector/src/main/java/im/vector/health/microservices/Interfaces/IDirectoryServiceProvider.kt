package im.vector.health.microservices.interfaces

import im.vector.health.microservices.APIModel.FavouriteTypes

interface IDirectoryServiceProvider {
    fun GetPractitioners(query: String?, page: Int, pageSize: Int, callback: (List<IPractitioner>?, Int) -> Unit)
    fun GetPractitionerFavourites(query: String?, page: Int, pageSize: Int, callback: (List<IPractitioner>?, Int) -> Unit)
    fun GetPractitionerRoles(query: String?, page: Int, pageSize: Int, callback: (List<IPractitionerRole>?, Int) -> Unit)
    fun GetPractitionerRoleFavourites(query: String?, page: Int, pageSize: Int, callback: (List<IPractitionerRole>?, Int) -> Unit)
    fun GetHealthcareServices(query: String?, page: Int, pageSize: Int, callback: (List<IHealthcareService>?, Int) -> Unit)
    fun GetHealthcareServiceFavourites(query: String?, page: Int, pageSize: Int, callback: (List<IHealthcareService>?, Int) -> Unit)
    fun GetPatients(query: String?, page: Int, pageSize: Int, callback: (List<IPatient>?, Int) -> Unit)
    fun GetPractitioner(id: String, callback: (IPractitioner?) -> Unit)
    fun GetPractitionerRole(id: String, callback: (IPractitionerRole?) -> Unit)
    fun GetHealthcareService(id: String, callback: (IHealthcareService?) -> Unit)
    fun SetBaseURL(url: String)
    fun SetPractitionerID(id: String)
    fun GetActiveRoles(callback: (List<IPractitionerRole>?) -> Unit)
    fun CheckFavourite(favouriteTypes: FavouriteTypes, favourite: String, callback: (Boolean) -> Unit)
    fun RemoveFavourite(favouriteTypes: FavouriteTypes, favourite: String)
    fun AddFavourite(favouriteTypes: FavouriteTypes, favourite: String)
}