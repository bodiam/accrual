package portfolio_analysis.cli

import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import portfolio_analysis.Bonds
import portfolio_analysis.Portfolio
import portfolio_analysis.toPercent
import portfolio_analysis.withCommas

fun printStats(portfolio: Portfolio) {
	val textTable = TextTable()
	textTable.addTitle("Portfolio Statistics <br/> As of __date__", 2)

	textTable.addKeyValue("Par", portfolio.par.withCommas())
	textTable.addKeyValue("Market Value", portfolio.marketValue.withCommas())
	textTable.addKeyValue("Amortized Cost", portfolio.amortizerdCost.withCommas())
	textTable.addKeyValue("Original Cost", portfolio.originalCost.withCommas())
	textTable.addKeyValue("Accrued Interest", portfolio.accruedInterest.withCommas())
	textTable.addKeyValue("Yield At Cost", portfolio.yieldAtCost.toPercent())
	textTable.addKeyValue("Average Maturity", "${portfolio.maturity.withCommas()} Years")

	textTable.setMediumColumnWidth()
	textTable.render()
}

fun <T> Map<T, Bonds>.printBonds() {
	val textTable = TextTable()
	this.forEach { (key, bonds) ->
		run {
			textTable.addBondsInDetail("$key", bonds)
		}
	}
	textTable.setSmallColumnWidth()
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

fun printlnWithPrefix(prefix: String = Shell.MARGIN_MENU_OPTION, str: String) {
	println("$prefix${Shell.MARGIN_MENU_OPTION}$str")
}

fun printOption(prefix: String = Shell.MARGIN_MENU_OPTION, bullet: String, content: String) {
	printlnWithPrefix(prefix, "${bullet.addBulletStyling()} $content")
}

fun String.addBulletStyling(): String {
	return """[$this]"""
}

