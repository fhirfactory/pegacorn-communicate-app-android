package im.vector.health.microservices

data class FHIRTimePeriod (
        @JvmField
        var startDate: Long,
        @JvmField
        var endDate: Long?
)