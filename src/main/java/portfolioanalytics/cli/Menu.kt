package portfolioanalytics.cli

import portfolioanalytics.Portfolio
import java.util.*

/**
 * A menu containing options the user can choose from.
 * @property name name of the menu
 * @property options the list of printMenu options available
 */
class Menu(
	 private val name: String,
	 val options: Map<String, PrintOption>,
	 var shouldWithPrintSubMenu: Boolean = true,
	 var prefix: String = Shell.MARGIN_MENU_OPTION
) {

	/**
	 * Prints the menu options
	 */
	fun printMenu() {
		println()
		printlnWithPrefix(prefix, name)
		printlnWithPrefix(prefix, "-".repeat(name.length))
		this.options.forEach { key, option ->
			printOption(prefix, key, option.name)
			if (shouldWithPrintSubMenu) option.printSubOptions(Shell.MARGIN_MENU_SUBOPTION)
		}
		println()
	}
}

internal fun createDistributionOptions(portfolio: Portfolio): Array<PrintOption> {
	return arrayOf(
		 PrintOption(
				name = "Sector",
				primaryAction = { portfolio.allocation.percentagesByMaturityRange.printDistribution("Sector Distribution") }
		 ),
		 PrintOption(
				name = "Maturity",
				primaryAction = { portfolio.allocation.percentagesByMaturityRange.printDistribution("Maturity Distribution") }
		 ),
		 PrintOption(
				name = "Credit (S&P Ratings)",
				primaryAction = { portfolio.allocation.percentagesBySpRating.printDistribution("Credit Distribution <br/> Ratings by S&P") }
		 )
	)
}

internal fun createBondOptions(portfolio: Portfolio): Array<PrintOption> {
	return arrayOf(
		 PrintOption(name = "Sector", primaryAction = { portfolio.bondDetails.bondsBySecurityType.printBonds() }),
		 PrintOption(name = "Maturity", primaryAction = { portfolio.bondDetails.bondsByMaturityRange.printBonds() }),
		 PrintOption(name = "Credit (S&P Ratings)", primaryAction = { portfolio.bondDetails.bondsByMaturityRange.printBonds() })
	)
}

/**
 * A class representing a printMenu option in the CLI
 * @property name name of the printMenu option
 * @property primaryAction primary action of the printMenu option
 * @property subMenu a sub menu containing other related printMenu options
 */
class PrintOption(
	 val name: String,
	 val primaryAction: (() -> Unit)? = null,
	 val subMenu: Menu? = null
) {
	fun executePrimary() {
		primaryAction?.invoke()
	}

	fun getSubMenuOptions(): Map<String, PrintOption>? {
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
		return "PrintOption(name='$name', primaryAction=$primaryAction, subMenu=$subMenu)"
	}

	companion object {
		fun createSubMenu(menuName: String, vararg opts: PrintOption): Menu? {
			val options = HashMap<String, PrintOption>()
			var index = 'a' //starting value for sub menu items
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


