package generator

import java.math.BigDecimal
import java.math.RoundingMode


internal fun round(value: Double, places: Int): Double {
	if (places < 0) throw IllegalArgumentException()

	var bd = BigDecimal(java.lang.Double.toString(value))
	bd = bd.setScale(places, RoundingMode.HALF_UP)
	return bd.toDouble()
}

fun hello() {

}
