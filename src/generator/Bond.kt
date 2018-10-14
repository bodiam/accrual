package generator

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

class TradedBond(
	 val cusip: String,
	 val currentDate: LocalDate,
	 val tradeDate: LocalDate,
	 val settleDate: LocalDate,
	 val originalCost: Double,
	 val amortizedCost: Double,
	 val marketValue: Double,
	 val accruedInterest: Double,
	 val yieldAtCost: Double,
	 securityType: SecurityType,
	 description: String,
	 maturityDate: LocalDate,
	 par: Double,
	 coupon: Double,
	 spRating: SpRating,
	 moodysRating: MoodysRating
) : Bond(
	 securityType = securityType,
	 description = description,
	 maturityDate = maturityDate,
	 par = par,
	 coupon = coupon,
	 spRating = spRating,
	 moodysRating = moodysRating
) {
	val daysToMaturity: Long = DAYS.between(currentDate, maturityDate)
	val maturityRange: MaturityRange = MaturityRange.getRange(daysToMaturity.toInt())


	override fun toString(): String {
		return "TradedBond(" +
			 "cusip='$cusip'," +
			 "currentDate=$currentDate," +
			 "tradeDate=$tradeDate," +
			 "settleDate=$settleDate," +
			 "originalCost=$originalCost," +
			 "amortizedCost=$amortizedCost," +
			 "marketValue=$marketValue," +
			 "accruedInterest=$accruedInterest," +
			 "yieldAtCost=$yieldAtCost" +
			 ")" +
			 " ${super.toString()}"
	}
}

open class Bond(
	 val securityType: SecurityType,
	 val description: String,
	 val maturityDate: LocalDate,
	 val par: Double,
	 val coupon: Double,
	 val spRating: SpRating,
	 val moodysRating: MoodysRating
) {
	override fun toString(): String {
		return "Bond(" +
			 "securityType=$securityType," +
			 "description='$description'," +
			 "maturityDate=$maturityDate," +
			 "par=$par," +
			 "coupon=$coupon," +
			 "spRating=$spRating," +
			 "moodysRating=$moodysRating" +
			 ")"
	}
}