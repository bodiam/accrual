package portfolioanalytics

import java.math.BigDecimal
import java.math.RoundingMode

internal fun Double.withCommas(): String {
	return String.format("%,.2f", this)
}

internal fun Double.toPercent(): String {
	return String.format("%." + 2 + "f", this * 100) + "%"
}

internal fun round(value: Double, places: Int): Double {
	if (places < 0) throw IllegalArgumentException()

	var bd = BigDecimal(java.lang.Double.toString(value))
	bd = bd.setScale(places, RoundingMode.HALF_UP)
	return bd.toDouble()
}
