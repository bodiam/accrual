package portfolioanalytics

import portfolioanalytics.cli.CsvParser
import portfolioanalytics.cli.Shell

fun main(args: Array<String>) {
	val bonds = CsvParser(filepath = TEST_DATA_DEFAULT_FILEPATH).getBonds()
	val portfolio = Portfolio(bonds)
	val shell = Shell(portfolio)
	shell.init()
}
