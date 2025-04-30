import java.util.Date

/**
 * Creates a new Travel object
 */
data class Travel (
    val id: Int,
    val origin: Country,
    val destiny: Country,
    val arrivalDate: Date,
    val leaveDate: Date
)