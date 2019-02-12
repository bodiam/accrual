package portfolioanalytics

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Formats a double to a number string with commas
 */
internal fun Double.withCommas(): String {
	return String.format("%,.2f", this)
}

/**
 * Formats a double to percentage string
 */
internal fun Double.toPercent(decimalPlaces: Int): String {
	return String.format("%." + decimalPlaces + "f", this * 100) + "%"
}

/**
 * Rounds a double to N decimal decimalPlaces
 */
internal fun round(value: Double, decimalPlaces: Int): Double {
	if (decimalPlaces < 0) throw IllegalArgumentException()

	var bd = BigDecimal(java.lang.Double.toString(value))
	bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP)
	return bd.toDouble()
}

/**
 * Normalizes a string
 */
internal fun String.normalize(): String {
	return this.toLowerCase().trim()
}