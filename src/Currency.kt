import constants.CURRENCY

data class Currency(val currencyType: CURRENCY) {
    private val currency = currencyType

    override fun toString(): String {
        return when (this.currency) {
            CURRENCY.EUR -> "Euro"
            CURRENCY.USD -> "United States Dollar"
            CURRENCY.CAD -> "Canadian Dollar"
            CURRENCY.INR -> "Indian Rupee"
        }
    }
}
