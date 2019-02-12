package portfolioanalytics.cli

import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import portfolioanalytics.*

/**
 * Prints the portfolio statistics table
 */
fun printStats(portfolio: Portfolio) {
	val textTable = TextTable()
	textTable.addTitle("Portfolio Statistics <br/> As of ${portfolio.evaluationDate.format(dateFormatter)}", 2)

	textTable.addKeyValue("Par", portfolio.total.par.withCommas())
	textTable.addKeyValue("Market Value", portfolio.total.marketValue.withCommas())
	textTable.addKeyValue("Amortized Cost", portfolio.total.amortizerdCost.withCommas())
	textTable.addKeyValue("Original Cost", portfolio.total.originalCost.withCommas())
	textTable.addKeyValue("Accrued Interest", portfolio.total.accruedInterest.withCommas())
	textTable.addKeyValue("Yield At Cost", portfolio.yieldAtCost.toPercent())
	textTable.addKeyValue("Average Maturity", "${portfolio.maturity.withCommas()} Years")

	textTable.setMediumColumnWidth()
	textTable.render()
}

/**
 * Prints a table of all bonds in detail
 * @property T the type determining how the bonds are grouped (ex. maturityRange, securityType)
 */
fun <T> Map<T, TradedBonds>.printBonds() {
	val textTable = TextTable()
	this.forEach { (key, bonds) ->
		run {
			textTable.addBondsInDetail("$key", bonds)
		}
	}
	textTable.setSmallColumnWidth()
	textTable.render()
}

/**
 * Prints a table of the percentage bond distributions in the portfolio
 * @property T the type determining how the bonds are distributed in the portfolio (ex. maturityRange,
 * securityType)
 * @property Double percentage total for type T
 */
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

/**
 * Adds a prefix before the input string
 */
fun printlnWithPrefix(prefix: String = Shell.MARGIN_MENU_OPTION, input: String) {
	println("$prefix${Shell.MARGIN_MENU_OPTION}$input")
}

/**
 * Adds a prefix and bullet before the input string
 */
fun printOption(prefix: String = Shell.MARGIN_MENU_OPTION, bullet: String, input: String) {
	printlnWithPrefix(prefix, "${bullet.addBulletStyling()} $input")
}

/**
 * Adds styling to the bullet string
 */
fun String.addBulletStyling(): String {
	return """[$this]"""
}

