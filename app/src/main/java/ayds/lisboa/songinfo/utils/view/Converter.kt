interface Converter {
    fun convert(date: String): String
}

internal class ConverterDefaultImpl : Converter {
    override fun convert(date: String): String {
        return date
    }
}

internal class ConverterDayImpl : Converter {
    override fun convert(date: String): String {
        val dateWithSlash = date.replace("-", "/")
        val dateMembersArray = dateWithSlash.split("/")
        val dateMembersArrayReversed = dateMembersArray.reversed()
        val result = dateMembersArrayReversed.joinToString(separator = "/")
        return result
    }
}

internal class ConverterMonthImpl : Converter {
    override fun convert(date: String): String {
        val month = date.split("-")[1].toInt()
        val monthName = getMonthName(month)
        val year = date.split("-").first()
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
    override fun convert(date: String): String {
        val year = date.split("-").first().toInt()

        return if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0))
            "$year (leap year)"
        else
            "$year (not a leap year)"
    }
}
