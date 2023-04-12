import Converter

interface CalculatorFactory {
    fun create(precision: String): Converter
}

object CalculatorFactoryImpl : CalculatorFactory {
    override fun create(precision: String): Converter{
        return when(precision) {
            "year" -> ConverterYearImpl()
            "month" -> ConverterMonthImpl()
            "day" -> ConverterDayImpl()
            else -> ConverterDefaultImpl()
        }
    }
}