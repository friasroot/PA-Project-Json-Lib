import constants.COUNTRY
import constants.CURRENCY
import constants.LANGUAGE

data class Country (
    val country: COUNTRY?,
    val weather: String,
    val currencies: List<Currency?>,
    val languages: List<Language_Ext?>
)

