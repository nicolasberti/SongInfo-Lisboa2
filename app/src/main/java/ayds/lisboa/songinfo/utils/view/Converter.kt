interface Converter {
    fun getReleaseDate(date: String): String
}

internal class ConverterDefaultImpl : Converter {
    override fun getReleaseDate(date: String): String {
        return date
    }
}

internal class ConverterDayImpl : Converter {
    override fun getReleaseDate(date: String): String {
        val dateWithSlash = date.replace("-", "/")
        val dateMembersArray = dateWithSlash.split("/")
        val dateMembersArrayReversed = dateMembersArray.reversed()
        val result = dateMembersArrayReversed.joinToString(separator = "/")
        return result
    }
}

internal class ConverterMonthImpl : Converter {
    override fun getReleaseDate(date: String): String {
        val dateArray = date.split("-")
        val month = dateArray[1].toInt()
        val year = dateArray[0].toInt()
        val monthName = getMonthName(month)

        return "$monthName, $year"
    }

    private fun getMonthName(month: Int): String{
        return when(month){
            1 -> "Enero"
            2 -> "Febrero"
            3 -> "Marzo"
            4 -> "Abril"
            5 -> "Mayo"
            6 -> "Junio"
            7 -> "Julio"
            8 -> "Agosto"
            9 -> "Septiembre"
            10 -> "Octubre"
            11 -> "Noviembre"
            12 -> "Diciembre"
            else -> ""
        }
    }
}

internal class ConverterYearImpl : Converter {
    override fun getReleaseDate(date: String): String {
        val dateArray = date.split("-")
        val yearString = dateArray[0]
        val yearInt = yearString.toInt()

        return if ((yearInt % 4 == 0) && (yearInt % 100 != 0 || yearInt % 400 == 0))
            "$yearInt (leap year)"
        else
            "$yearInt (not a leap year)"
    }
}
