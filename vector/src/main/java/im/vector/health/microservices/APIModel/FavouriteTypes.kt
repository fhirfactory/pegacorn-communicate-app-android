package im.vector.health.microservices.APIModel

enum class FavouriteTypes(val path: String) {
    Service("healthcareServiceFavourites"),
    Practitioner("practitionerFavourites"),
    PractitionerRoles("practitionerRoleFavourites")
}