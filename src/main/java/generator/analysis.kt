package generator

import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import generatoar.SecurityType
import generator.cli.TextTable
import java.lang.Double.sum
import java.util.*

typealias Bonds = List<TradedBond>

class PortfolioStatistics(val bonds: Bonds) {
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

	fun printStats() {
		val textTable = TextTable()
		textTable.addTitle("Portfolio Statistics <br/> As of __date__", 2)

		textTable.addKeyValue("Par", par.withCommas())
		textTable.addKeyValue("Market Value", marketValue.withCommas())
		textTable.addKeyValue("Amortized Cost", amortizerdCost.withCommas())
		textTable.addKeyValue("Original Cost", originalCost.withCommas())
		textTable.addKeyValue("Accrued Interest", accruedInterest.withCommas())
		textTable.addKeyValue("Yield At Cost", yieldAtCost.toPercent())
		textTable.addKeyValue("Average Maturity", "${maturity.withCommas()} Years")

		textTable.setMediumColumnWidth()
		textTable.render()
	}

	//start with maturity dist
	fun printDistribution() {
		val textTable = TextTable()
		textTable.addTitle("Portfolio Statistics <br/> As of __date__", 2)


	}


	override fun toString(): String {
		return "PortfolioStatistics( par=par, originalCost=$originalCost, marketValue=$marketValue, bondsBySecurityType=$bondsBySecurityType)"
	}
}

fun main(args: Array<String>) {
	val bonds = parseBonds(CSV_FILEPATH)
	val pStats = PortfolioStatistics(bonds)
//	pStats.bondsBySecurityType.printBonds()
//	pStats.bondsByMaturityRange.printBonds()
//	pStats.bondsBySpRating.printBonds()
//
	pStats.printStats()
//	println(pStats.yieldAtCost)

	pStats.percentagesByMaturityRange.printDistribution("Maturity Distribution")
	pStats.percentagesBySecurityType.printDistribution("Sector Distribution")
	pStats.percentagesBySpRating.printDistribution("Credit Distribution")


}

fun <T> Map<T, Bonds>.printBonds() {
	val textTable = TextTable()
	this.forEach { (key, bonds) ->
		run {
			textTable.addBondsInDetail("$key", bonds)
		}
	}
	textTable.render()
}


fun <T> Map<T, Double>.printDistribution(title: String) {
	val textTable = TextTable()
	textTable.addTitle(title, 2)
	this.forEach { (key, value) ->
		run {
			textTable.addKeyValue("$key", value.toPercent())
				 .setTextAlignment(TextAlignment.LEFT)
		}
	}
	textTable.setMediumColumnWidth()
	textTable.render()

}

