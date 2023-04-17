const val YEAR = "year"
const val MONTH = "month"
const val DAY = "day"

interface DateConverterFactory {
    fun create(precision: String): Converter
}

internal class DateConverterFactoryImpl : DateConverterFactory {
    override fun create(precision: String): Converter{
        return when(precision) {
            YEAR -> ConverterYearImpl()
            MONTH -> ConverterMonthImpl()
            DAY -> ConverterDayImpl()
            else -> ConverterDefaultImpl()
        }
    }
}