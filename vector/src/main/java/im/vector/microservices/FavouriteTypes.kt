package im.vector.microservices

enum class FavouriteTypes(val path: String) {
    Service("healthcareServiceFavourites"),
    Practitioner("practitionerFavourites"),
    PractitionerRoles("practitionerRoleFavourites")
}