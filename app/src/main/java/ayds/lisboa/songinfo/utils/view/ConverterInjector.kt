import CalculatorFactory

object ConverterInjector {

    private lateinit var converter: Converter

    fun getConverter(): Converter = converter

    fun initConverter(precision: String) {
        converter = CalculatorFactoryImpl.create(precision)
    }

}