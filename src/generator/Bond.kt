package generator

import java.time.LocalDate

class Bond(val securityType: SecurityType,
           val description: String,
           val cusip: String,
           val currentDate: LocalDate,
           val tradeDate: LocalDate,
           val settleDate: LocalDate,
           val maturityDate: LocalDate,
           val par: Double,
           val coupon: Double,
           val originalCost: Double,
           val amortizedCost: Double,
           val marketValue: Double,
           val accruedInterest: Double,
           val yieldAtCost: Double,
           val spRating: SpRating,
           val moodysRating: MoodysRating
) {
    override fun toString(): String {
        return "Bond(securityType=$securityType, description='$description', cusip='$cusip', currentDate=$currentDate, tradeDate=$tradeDate, settleDate=$settleDate, maturityDate=$maturityDate, par=$par, coupon=$coupon, originalCost=$originalCost, amortizedCost=$amortizedCost, marketValue=$marketValue, accruedInterest=$accruedInterest, yieldAtCost=$yieldAtCost, spRating=$spRating, moodysRating=$moodysRating)"
    }


}