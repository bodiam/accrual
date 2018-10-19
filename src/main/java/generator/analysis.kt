package generator

import java.lang.Double.sum

typealias Bonds = List<TradedBond>

class PortfolioStatistics(val bonds: Bonds) {
	val par: Double = bonds.sumByDouble { it.par }
	val originalCost: Double = bonds.sumByDouble { it.originalCost }
	val amortizerdCost: Double = bonds.sumByDouble { it.amortizedCost }
	val marketValue: Double = bonds.sumByDouble { it.marketValue }
	val accruedInterest: Double = bonds.sumByDouble { it.accruedInterest }

	//averages
	val maturity: Double = bonds.sumByDouble { it.marketValue * it.daysToMaturity } / (marketValue * 365)


	// list of bonds by property
	val bondsBySecurityType: Map<SecurityType, Bonds> = mapBondsByProperty { it.securityType }
	val bondsByMaturityRange: Map<MaturityRange, Bonds> = mapBondsByProperty { it.maturityRange }
	val bondsBySpRating: Map<SpRating, Bonds> = mapBondsByProperty { it.spRating }

	//allocations by property
	val allocationBySecurityType: Map<SecurityType, Double> = getAllocationByProperty { it.securityType }
	val allocationByMaturityRange: Map<MaturityRange, Double> = getAllocationByProperty { it.maturityRange }
	val allocationsBySpRating: Map<SpRating, Double> = getAllocationByProperty { it.spRating }

	private fun <T> getAllocationByProperty(
		 getBondProperty: (bond: TradedBond) -> T
	): Map<T, Double> {
		val map = HashMap<T, Double>()
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

	override fun toString(): String {
		return "PortfolioStatistics( par=par, originalCost=$originalCost, marketValue=$marketValue, bondsBySecurityType=$bondsBySecurityType)"
	}
}

