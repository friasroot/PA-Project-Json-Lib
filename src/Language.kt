import constants.LANGUAGE

data class Language_Ext(val language: LANGUAGE) {
    private val lang = language

    override fun toString(): String {
        return when(this.lang) {
            LANGUAGE.PT -> "Portuguese"
            LANGUAGE.EN -> "English"
            LANGUAGE.FR -> "French"
            LANGUAGE.RUS -> "Russian"
        }
    }
}

