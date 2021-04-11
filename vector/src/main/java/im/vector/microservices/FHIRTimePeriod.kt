package im.vector.microservices

data class FHIRTimePeriod (
        @JvmField
        var startDate: Long,
        @JvmField
        var endDate: Long?
)