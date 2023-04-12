interface Converter {
    fun convert(date: String): String
}

class ConverterDefaultImpl : Converter {
    override fun convert(date: String): String {
        return date
    }
}

class ConverterDayImpl : Converter {
    override fun convert(date: String): String {
        val replace = date.replace("-", "/")
        val split = replace.split("/")
        val reversed = split.reversed()
        val result = reversed.joinToString(separator = "/")
        
        return result
    }
}

class ConverterMonthImpl : Converter {
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

class ConverterYearImpl : Converter {
    override fun convert(date: String): String {
        val year = date.split("-").first().toInt()
        var result: String

        if( (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0) )
            result = "$year (leap year)"
        else
            result = "$year (not a leap year)"

        return result
    }
}
