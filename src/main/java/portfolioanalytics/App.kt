package portfolioanalytics

import portfolioanalytics.cli.CsvParser
import portfolioanalytics.cli.Shell

fun main(args: Array<String>) {
	val tradedBonds = CsvParser(filePath = TEST_DATA_DEFAULT_FILEPATH).getBonds()
	val portfolio = Portfolio(tradedBonds)
	val shell = Shell(portfolio)
	shell.init()
}
