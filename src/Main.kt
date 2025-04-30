import constants.*
import printer.JsonPrinter
import java.sql.Date

fun main() {
    val originA = Country(  country = COUNTRY.PT,
                            weather = "Warm",
                            currencies = listOf(Currency(CURRENCY.EUR)),
                            languages = listOf(Language_Ext(LANGUAGE.PT))
                        )
    val destinyA = Country( country = COUNTRY.CA,
                            weather = "Warm",
                            currencies = listOf(Currency(CURRENCY.CAD)),
                            languages = listOf(Language_Ext(LANGUAGE.EN), Language_Ext(LANGUAGE.FR))
                        )

    val travelA = Travel(   id = 1,
                            origin = originA,
                            destiny = destinyA,
                            arrivalDate = Date.valueOf("2025-04-01"),
                            leaveDate = Date.valueOf("2025-04-05")
                        )

    val serialized = JsonPrinter().serialize(travelA)

    JsonPrinter().print(serialized)
}