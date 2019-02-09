package portfolioanalytics.cli

import picocli.CommandLine
import picocli.CommandLine.*
import picocli.CommandLine.Model.CommandSpec
import portfolioanalytics.TEST_DATA_DEFAULT_FILEPATH
import java.io.File

@CommandLine.Command(description = ["Prints the checksum (MD5 by default) of a file to STDOUT."], name = "checksum",
	 mixinStandardHelpOptions = true, subcommands = [CommandLine.HelpCommand::class]
)
class CommandLineParser : Runnable {
	override fun run() {
		validateFilename()
		println(file.name)
		val csvParser = CsvParser(file.absolutePath)
//		readBondsFromFile(file.absolutePath)
//		csvParser.parseBonds
	}

	@Spec
	private val spec: CommandSpec? = null // injected by picocli

	@Parameters(index = "0", description = [" name of file containing portfolioanalytics bond data"])
	lateinit var file: File

	private fun validateFilename() {
		if (file.name.isEmpty()) {
			throw ParameterException(spec?.commandLine(), "Empty file name")

		}
	}
}

fun main(args: Array<String>) {
	val combinedArgs = arrayOf(*args, TEST_DATA_DEFAULT_FILEPATH)
	CommandLine.run(CommandLineParser(), *combinedArgs)
}

