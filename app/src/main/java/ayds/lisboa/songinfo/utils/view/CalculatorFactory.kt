interface CalculatorFactory {
    fun create(precision: String): Converter
}

object CalculatorFactoryImpl : CalculatorFactory {
    override fun create(precision: String): Converter{
        when(precision){
            "year" -> ConverterYear()
            "month" -> ConverterMonth()
            "day" -> ConverterDay()
            else -> ConverterDefault()
        }
    }
}