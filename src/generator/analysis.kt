package generator

import java.time.LocalDate

class PortfolioStatistics(override val bonds: List<Bond>) : Analyze {
    val totalPar: Double = bonds.sumByDouble { it.par }
    val totalOriginalCost: Double = bonds.sumByDouble { it.originalCost }
    val totalMarketValue: Double = bonds.sumByDouble { it.marketValue }

//    override fun getWeights(): Map<String, BigDecimal> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    override fun toString(): String {
        val sb = StringBuilder("<Portfolio Summary>\n")
        for (b in bonds) sb.appendln(b)
        sb.appendln("---stats---")
        sb.appendln("totalPar: $totalPar")
        return sb.toString()
    }
}


interface Analyze {
    val bonds: List<Bond>
//    fun parTotal() = bonds.sumBy{it.par}
}

fun createBonds(): List<Bond> {
    val bonds: List<Bond> = listOf(
            Bond(
                    securityType = SecurityType.Treasury,
                    description = "US Treasury Notes",
                    cusip = "912828A83",
                    currentDate = LocalDate.of(2017, 6, 30),
                    tradeDate = LocalDate.of(2016, 3, 30),
                    settleDate = LocalDate.of(2016, 3, 31),
                    maturityDate = LocalDate.of(2020, 12, 31),
                    par = 2_500_500.0,
                    coupon = .02375,
                    originalCost = 2_678_097.66,
                    amortizedCost = 2_645_083.05,
                    marketValue = 2_611_758.45,
                    accruedInterest = 164.57,
                    yieldAtCost = .0128,
                    spRating = SpRating.AA_Plus,
                    moodysRating = MoodysRating.AAA
            )
    )
    return bonds
}


fun main(args: Array<String>) {
    val bonds = createBonds()
    for (bond in bonds) println(bond)

//    val pStats = PortfolioStatistics(bonds)
//    println(pStats)
//    val sp = SpRating.AAA
//    println(sp)
//    val rating: Rating = sp
//    println(rating.rating)

}
