package generator

import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import generator.cli.TablePrinter
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
	val yieldAtCost: Double = bonds.sumByDouble { it.yieldAtCost * it.originalCost } / originalCost


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

	fun printStats() {
		val tablePrinter = TablePrinter()
		tablePrinter.addTitle("Portfolio Statistics <br/> As of __date__", 2)
//		table.addHeaders("Portfolio Statistics",
//			 arrayOf(
//					"Par",

//					"Market Value",
//					"AmortizedCost",
//					"Original Cost",
//					"Yield at Cost",
//					"Average Maturity",
//					"Accrued Interest"
//			 ))
		tablePrinter.addKeyValue("Par", par.withCommas())
		tablePrinter.addKeyValue("Market Value", marketValue.withCommas())
		tablePrinter.addKeyValue("Amortized Cost", amortizerdCost.withCommas())
		tablePrinter.addKeyValue("Original Cost", originalCost.withCommas())
		tablePrinter.addKeyValue("Accrued Interest", accruedInterest.withCommas())
		tablePrinter.addKeyValue("Yield At Cost", yieldAtCost.toPercent())
		tablePrinter.addKeyValue("Average Maturity", "${maturity.withCommas()} Years")


		tablePrinter.table.setTextAlignment(TextAlignment.CENTER)
		tablePrinter.render()
	}


	override fun toString(): String {
		return "PortfolioStatistics( par=par, originalCost=$originalCost, marketValue=$marketValue, bondsBySecurityType=$bondsBySecurityType)"
	}
}

fun main(args: Array<String>) {
	val pStats = PortfolioStatistics(createTestBonds())
	pStats.bondsBySecurityType.printTable()
	pStats.bondsByMaturityRange.printTable()
	pStats.bondsBySpRating.printTable()

	pStats.printStats()
	println(pStats.yieldAtCost)
}

private fun <T> Map<T, Bonds>.printTable() {
	val tablePrinter = TablePrinter()
	this.forEach { (key, bonds) ->
		run {
			tablePrinter.addBondsInDetail("$key", bonds)
		}
	}
	tablePrinter.render()
}

