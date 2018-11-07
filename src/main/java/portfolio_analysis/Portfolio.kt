package portfolio_analysis

import portfolio_analysis.bonds.MaturityRange
import portfolio_analysis.bonds.SecurityType
import portfolio_analysis.bonds.SpRating
import portfolio_analysis.bonds.TradedBond
import java.lang.Double.sum
import java.util.*


typealias Bonds = List<TradedBond>

class Portfolio(val bonds: Bonds) {
	//totals
	val par: Double = bonds.sumByDouble { it.par }
	val originalCost: Double = bonds.sumByDouble { it.originalCost }
	val amortizerdCost: Double = bonds.sumByDouble { it.amortizedCost }
	val marketValue: Double = bonds.sumByDouble { it.marketValue }
	val accruedInterest: Double = bonds.sumByDouble { it.accruedInterest }

	//averages
	val maturity: Double = bonds.sumByDouble { it.marketValue * it.daysToMaturity } / (marketValue * 365)
	val yieldAtCost: Double = bonds.sumByDouble { it.yieldAtCost * it.originalCost } / originalCost

	// list of bonds by property
	val bondsBySecurityType: Map<SecurityType, Bonds> = mapBondsByProperty { it.securityType }
	val bondsByMaturityRange: Map<MaturityRange, Bonds> = mapBondsByProperty { it.maturityRange }
	val bondsBySpRating: Map<SpRating?, Bonds> = mapBondsByProperty { it.spRating }

	//percentages by property
	val percentagesBySecurityType: Map<SecurityType, Double> = getAllocationByProperty { it.securityType }
	val percentagesByMaturityRange: Map<MaturityRange, Double> = getAllocationByProperty { it.maturityRange }
	val percentagesBySpRating: Map<SpRating?, Double> = getAllocationByProperty { it.spRating }

	private fun <T> getAllocationByProperty(
		 getBondProperty: (bond: TradedBond) -> T
	): Map<T, Double> {
		val map = TreeMap<T, Double>()
		bonds.map {
			map.merge(getBondProperty(it), it.marketValue / marketValue, ::sum)
		}
		return map
	}

	private fun <T> mapBondsByProperty(
		 getBondProperty: (bond: TradedBond) -> T
	): Map<T, Bonds> {
		val map = HashMap<T, MutableList<TradedBond>>()
		bonds.map {
			map.getOrPut(getBondProperty(it)) { ArrayList() }.add(it)
		}
		return map
	}


}





