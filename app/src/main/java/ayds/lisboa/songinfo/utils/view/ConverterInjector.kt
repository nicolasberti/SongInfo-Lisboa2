class ConverterInjector {

    private val converter: Converter 
    
    fun getConverter(): Converter = converter

    fun initConverter(precision: String) {
        converter = CalculatorFactory.create(precision)
    }
    
}