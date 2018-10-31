package generator.shell

import generator.PortfolioStatistics
import generator.createTestBonds
import generator.printDistribution
import java.util.*
import kotlin.collections.LinkedHashMap


fun main(args: Array<String>) {
	val pStats = PortfolioStatistics(createTestBonds())

	val shell = Shell(pStats)
	shell.introToConsole()
	shell.printMainScreen()
	shell.setUpResponseReader()
}

class Shell(val pStats: PortfolioStatistics) {
	private var input: String = ""
	private val mainOptions: LinkedHashMap<String, ReportOption> = createMainOptions()
	private var currentOptions: Map<String, ReportOption> = mainOptions

	// create a scanner so we can read the command-line input
	private val scanner = Scanner(System.`in`)

	fun setUpResponseReader() {
		while (input != "quit" && input != "q") {
			input = askForUserInput()
			println("printing input: $input")

			respond(input)
		}
	}

	private fun respond(input: String) {
		val opt = currentOptions[input]
		if (opt == null) {
			println("Oops, something went wrong...")
			return
		}
		if (opt.mainOption != null) {
			opt.execMain()
		}
		if (opt.subOptions != null) {
			currentOptions = opt.subOptions
			opt.subOptions.printOptions()
		}

	}

	private fun askForUserInput(): String {
		println("\nEnter option:")
		return scanner.next()
	}

	fun introToConsole() {
		println("--- Welcome to the Portfolio Analyzer ---\n")
		println("Choose from the following mainOptions:")
	}


	private fun createDistributionOptions(): Array<ReportOption> {
		return arrayOf(
			 ReportOption(
					name = "Sector",
					mainOption = { pStats.percentagesByMaturityRange.printDistribution("Sector Distribution") }
			 ),
			 ReportOption(
					name = "Maturity",
					mainOption = { pStats.percentagesByMaturityRange.printDistribution("Maturity Distribution") }
			 ),
			 ReportOption(
					name = "Credit (S&P Ratings)",
					mainOption = { pStats.percentagesBySpRating.printDistribution("Credit Distribution <br/> Ratings by S&P") }
			 )
		)
	}

	fun printMainScreen() {
		mainOptions.forEach { t, u ->
			printOption(bullet = t, content = u.name)
			u.printSubOptions("   ")
		}
		currentOptions = mainOptions
	}

	private fun createMainOptions(): LinkedHashMap<String, ReportOption> {
		val result = LinkedHashMap<String, ReportOption>()
		var optionCount = 1
		result[optionCount++.toString()] = ReportOption(
			 name = "Portfolio Stats",
			 mainOption = pStats::printStats
		)
		result[optionCount++.toString()] = ReportOption(
			 name = "Distributions",
			 subOptions =
			 createSubOptionsList(
					*createDistributionOptions()
			 )
		)
		return result
	}
}


fun createSubOptionsList(vararg opts: ReportOption): Map<String, ReportOption>? {
	val result = HashMap<String, ReportOption>()
	var index = 'a'
	for (opt in opts) {
		result[index++.toString()] = opt
	}

	return result
}

fun String.addBulletStyling(): String {
	return """[$this]"""
}

class ReportOption(
	 val name: String,
	 val mainOption: (() -> Unit)? = null,
	 val subOptions: Map<String, ReportOption>? = null
) {
	fun execMain() {
		mainOption?.invoke()
	}

	fun printSubOptions(prefix: String = "") {
		subOptions?.printOptions(prefix)
	}


//	fun print() {
//		if (mainOption == null) printSubOptions()
//		else {
//			mainOption.invoke()
//		}
//	}

	override fun toString(): String {
		return "ReportOption(name='$name', mainOption=$mainOption, subOptions=$subOptions)"
	}


}

private fun printOption(prefix: String = "", bullet: String, content: String) {
	println("$prefix${bullet.addBulletStyling()} $content")
}

fun Map<String, ReportOption>.printOptions(prefix: String = "") {
	this.forEach { key, option ->
		printOption(prefix, key, option.name)
	}
}



