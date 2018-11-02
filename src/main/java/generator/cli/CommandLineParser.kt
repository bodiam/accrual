package generator.cli

import picocli.CommandLine
import picocli.CommandLine.*
import picocli.CommandLine.Model.CommandSpec
import java.io.File


@CommandLine.Command(description = ["Prints the checksum (MD5 by default) of a file to STDOUT."], name = "checksum",
	 mixinStandardHelpOptions = true, subcommands = [CommandLine.HelpCommand::class]
)

class Args : Runnable {
	override fun run() {
		file.validateFilename()
		println(file)
		parseBonds(file.name)
	}

	@Spec
	private val spec: CommandSpec? = null // injected by picocli

	@Parameters(index = "0", description = [" name of file containing portfolio bond data"])
	lateinit var file: File


	private fun File.validateFilename() {
		if (file.name.isEmpty()) {
			throw ParameterException(spec?.commandLine(), "Empty file name")

		}
	}
}

fun main(args: Array<String>) {
	val workingDir = System.getProperty("user.dir")
	val combinedArgs = arrayOf(*args, "$workingDir/src/test/test-data/input/data.csv")
	CommandLine.run(Args(), *combinedArgs)

}

//fun main(args: Array<String>) {
//	val userArgs = Args()
//	val workingDir = System.getProperty("user.dir")
//	val argv = arrayOf(*args, "-f", "$workingDir/src/test/test-data/input/data.csv")
//	val jct = JCommander.newBuilder()
//		 .addObject(userArgs)
//		 .build()
//
//	if (userArgs.help || argv.isEmpty()) {
//		jct.usage()
//		System.exit(0)
//	}
//
//	try {
//		jct.parse(*argv)
//	} catch (e: ParameterException) {
//		println(e.message)
//		println("Pass --help to see information for all options")
//	}
//
//
//	userArgs.run()
//
////	println(System.getProperty("user.dir"))
//
//
//}