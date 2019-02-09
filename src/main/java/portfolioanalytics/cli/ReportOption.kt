package portfolioanalytics.cli

import portfolioanalytics.Portfolio
import java.util.*

class ReportOption(
	 val name: String,
	 val primaryAction: (() -> Unit)? = null,
	 val subMenu: Menu? = null
) {
	fun executePrimary() {
		primaryAction?.invoke()
	}

	fun getSubMenuOptions(): Map<String, ReportOption>? {
		if (subMenu != null) {
			return subMenu.options
		}
		return null
	}

	fun printSubOptions(prefix: String) {
		subMenu?.options?.forEach { key, option ->
			printOption(prefix, key, option.name)
		}
	}

	override fun toString(): String {
		return "ReportOption(name='$name', primaryAction=$primaryAction, subMenu=$subMenu)"
	}

	companion object {
		fun createSubMenu(menuName: String, vararg opts: ReportOption): Menu? {
			val options = HashMap<String, ReportOption>()
			var index = 'a'
			for (opt in opts) {
				options[index++.toString()] = opt
			}

			return Menu(
				 menuName,
				 options
			)
		}
	}
}

class Menu(
	 val name: String,
	 val options: Map<String, ReportOption>
) {
	fun print(prefix: String = Shell.MARGIN_MENU_OPTION, withSubMenu: Boolean = true) {
		println()
		printlnWithPrefix(prefix, name)
		printlnWithPrefix(prefix, "-".repeat(name.length))
		this.options.forEach { key, option ->
			printOption(prefix, key, option.name)
			if (withSubMenu) option.printSubOptions(Shell.MARGIN_MENU_SUBOPTION)
		}
		println()
	}
}

internal fun createDistributionOptions(portfolio: Portfolio): Array<ReportOption> {
	return arrayOf(
		 ReportOption(
				name = "Sector",
				primaryAction = { portfolio.percentagesByMaturityRange.printDistribution("Sector Distribution") }
		 ),
		 ReportOption(
				name = "Maturity",
				primaryAction = { portfolio.percentagesByMaturityRange.printDistribution("Maturity Distribution") }
		 ),
		 ReportOption(
				name = "Credit (S&P Ratings)",
				primaryAction = { portfolio.percentagesBySpRating.printDistribution("Credit Distribution <br/> Ratings by S&P") }
		 )
	)
}

internal fun createBondOptions(portfolio: Portfolio): Array<ReportOption> {
	return arrayOf(
		 ReportOption(name = "Sector", primaryAction = { portfolio.bondsBySecurityType.printBonds() }),
		 ReportOption(name = "Maturity", primaryAction = { portfolio.bondsByMaturityRange.printBonds() }),
		 ReportOption(name = "Credit (S&P Ratings)", primaryAction = { portfolio.bondsByMaturityRange.printBonds() })
	)
}

