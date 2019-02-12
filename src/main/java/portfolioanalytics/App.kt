package portfolioanalytics

import portfolioanalytics.cli.CsvParser
import portfolioanalytics.cli.Shell

/**
 * Runs the application.
 */
fun main(args: Array<String>) {
	val filePath = if (args.isEmpty()) DEFAULT_DATA_FILEPATH else args[0]

	val tradedBonds = CsvParser(filePath).getBonds()

	val portfolio = Portfolio(tradedBonds)
	val shell = Shell(portfolio)

	shell.init()
}
