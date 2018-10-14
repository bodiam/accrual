package generator

import java.time.LocalDate

typealias Bonds = List<TradedBond>

class PortfolioStatistics(val bonds: Bonds) {
	val par: Double = bonds.sumByDouble { it.par }
	val originalCost: Double = bonds.sumByDouble { it.originalCost }
	val marketValue: Double = bonds.sumByDouble { it.marketValue }
	val bondsBySector: Map<SecurityType, Bonds> = mapBondsByProperty { it.securityType }
	val bondsByMaturityRange: Map<MaturityRange, Bonds> = mapBondsByProperty { it.maturityRange }
	val bondsBySpRating: Map<SpRating, Bonds> = mapBondsByProperty { it.spRating }

	private fun <T> mapBondsByProperty(
		 getBondPropertyValue: (bond: TradedBond) -> T
	): Map<T, Bonds> {
		val map = HashMap<T, MutableList<TradedBond>>()
		bonds.map {
			val ret = getBondPropertyValue(it)
			if (map[ret] == null) map[ret] = ArrayList()
			map[ret]!!.add(it)
		}
		return map
	}

	override fun toString(): String {
		return "PortfolioStatistics( par=par, originalCost=$originalCost, marketValue=$marketValue, bondsBySector=$bondsBySector)"
	}
}

fun main(args: Array<String>) {
	val bonds = createBonds()
	for (bond in bonds) println(bond)
	val pStats = PortfolioStatistics(bonds)
	println(pStats)

}

fun createBonds(): List<TradedBond> {
	return listOf(
		 TradedBond(
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
				moodysRating = MoodysRating.Aaa
		 ),
		 TradedBond(
				securityType = SecurityType.Treasury,
				description = "US Treasury Notes",
				cusip = "912828WL0",
				currentDate = LocalDate.of(2017, 6, 30),
				tradeDate = LocalDate.of(2016, 11, 10),
				settleDate = LocalDate.of(2016, 11, 14),
				maturityDate = LocalDate.of(2019, 5, 31),
				par = 3_020_000.0,
				coupon = 0.0150,
				originalCost = 3_053_031.25,
				amortizedCost = 3_044_954.89,
				marketValue = 3_026_725.54,
				accruedInterest = 3836.89,
				yieldAtCost = .0106,
				spRating = SpRating.AA_Plus,
				moodysRating = MoodysRating.Aaa
		 ),
		 TradedBond(
				securityType = SecurityType.FederalAgency_GSE,
				description = "FEDERAL FARM CREDIT BK (CALLABLE)",
				cusip = "3133ECCJ1",
				currentDate = LocalDate.of(2017, 6, 30),
				tradeDate = LocalDate.of(2016, 3, 30),
				settleDate = LocalDate.of(2016, 3, 31),
				maturityDate = LocalDate.of(2020, 12, 31),
				par = 2_500_500.0,
				coupon = .0093,
				originalCost = 1_000_000.0,
				amortizedCost = 1_000_000.0,
				marketValue = 998_679.0,
				accruedInterest = 4417.5,
				yieldAtCost = 0.0093,
				spRating = SpRating.AA_Plus,
				moodysRating = MoodysRating.Aaa
		 ),
		 TradedBond(
				securityType = SecurityType.Corporate,
				description = "GOLDMAN SACHS GROUP INC BONDS",
				cusip = "38145GAJ9",
				currentDate = LocalDate.of(2017, 6, 30),
				tradeDate = LocalDate.of(2016, 12, 8),
				settleDate = LocalDate.of(2016, 12, 13),
				maturityDate = LocalDate.of(2019, 12, 13),
				par = 1_700_000.0,
				coupon = .023,
				originalCost = 1_698_725.0,
				amortizedCost = 1_698_952.29,
				marketValue = 1_704_642.70,
				accruedInterest = 1955.0,
				yieldAtCost = .0233,
				spRating = SpRating.BBB_Plus,
				moodysRating = MoodysRating.A3
		 )
	)
}

