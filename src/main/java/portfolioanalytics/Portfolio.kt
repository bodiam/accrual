package portfolioanalytics

import portfolioanalytics.bonds.MaturityRange
import portfolioanalytics.bonds.SecurityType
import portfolioanalytics.bonds.SpRating
import portfolioanalytics.bonds.TradedBond
import java.lang.Double.sum
import java.time.LocalDate
import java.util.*

/**
 * A bond portfolio, including bond portfolio analytics.
 * @property evaluationDate the portfolio's evaluation date.
 * @property total totals for bond values
 * @property bondDetails bond listings by property
 * @property allocation portfolio allocations by property
 * @property maturity the average bond maturity
 * @property yieldAtCost the average yield at cost
 */
class Portfolio(val tradedBonds: TradedBonds) {
	val evaluationDate: LocalDate = tradedBonds.first().evaluationDate

	init {
		checkBondDates()
	}

	val total = Total()
	val bondDetails = BondDetails()
	val allocation = Allocation()

	//averages
	val maturity: Double = tradedBonds.sumByDouble { it.marketValue * it.daysToMaturity } / (total.marketValue * 365)
	val yieldAtCost: Double = tradedBonds.sumByDouble { it.yieldAtCost * it.originalCost } / total.originalCost

	inner class Total {
		val par: Double = tradedBonds.sumByDouble { it.par }
		val originalCost: Double = tradedBonds.sumByDouble { it.originalCost }
		val amortizerdCost: Double = tradedBonds.sumByDouble { it.amortizedCost }
		val marketValue: Double = tradedBonds.sumByDouble { it.marketValue }
		val accruedInterest: Double = tradedBonds.sumByDouble { it.accruedInterest }
	}

	/**
	 * List of tradedBonds by bond property
	 */
	inner class BondDetails {
		val bondsBySecurityType: Map<SecurityType, TradedBonds> = mapBondsByProperty { it.securityType }
		val bondsByMaturityRange: Map<MaturityRange, TradedBonds> = mapBondsByProperty { it.maturityRange }
		val bondsBySpRating: Map<SpRating?, TradedBonds> = mapBondsByProperty { it.spRating }

		private fun <T> mapBondsByProperty(
			 getBondProperty: (bond: TradedBond) -> T
		): Map<T, TradedBonds> {
			val map = HashMap<T, MutableList<TradedBond>>()
			tradedBonds.map {
				map.getOrPut(getBondProperty(it)) { ArrayList() }.add(it)
			}
			return map
		}
	}

	/**
	 * Percentage allocations by bond property
	 */
	inner class Allocation {
		val percentagesBySecurityType: Map<SecurityType, Double> = mapAllocationByProperty { it.securityType }
		val percentagesByMaturityRange: Map<MaturityRange, Double> = mapAllocationByProperty { it.maturityRange }
		val percentagesBySpRating: Map<SpRating?, Double> = mapAllocationByProperty { it.spRating }

		private fun <T> mapAllocationByProperty(
			 getBondProperty: (bond: TradedBond) -> T
		): Map<T, Double> {
			val map = TreeMap<T, Double>()
			tradedBonds.map {
				map.merge(getBondProperty(it), it.marketValue / total.marketValue, ::sum)
			}
			return map
		}
	}

	/**
	 * Verify all bonds are evaluated on the same evaluationDate.
	 */
	private fun checkBondDates() {
		tradedBonds.forEach {
			if (!it.evaluationDate.isEqual(evaluationDate))
				throw IllegalArgumentException("All bonds in the portfolio must have the same evaluation date.")
		}
	}
}

/**
 * Typealias for a list of tradedBonds
 * @see TradedBond
 */
typealias TradedBonds = List<TradedBond>



