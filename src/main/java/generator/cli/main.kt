package generator.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException


class Args {
	@Parameter
	var parameters: List<String> = arrayListOf()

	@Parameter(names = arrayOf("-f", "-file"), required = true, description = "name of file containing portfolio bond data")
	private var dataFile: String? = null

	@Parameter(names = arrayOf("--help", "-h"), help = true)
	var help = false

//	@Parameter(names = arrayOf("-log", "-verbose"), description = "Level of verbosity")
//	var verbose = 1
//
//	@Parameter(names = arrayOf("-groups"), description = "Comma-separated list of group names to be run")
//	var groups: String? = null
//
//	@Parameter(names = arrayOf("-debug"), description = "Debug mode")
//	private var debug = false

	fun run() {
		println("params: $parameters\n$dataFile")
	}
}

fun main(args: Array<String>) {
	val userArgs = Args()
	val workingDir = System.getProperty("user.dir")
	val argv = arrayOf(*args, "-f", "$workingDir/src/test/test-data/input/data.csv")
	val jct = JCommander.newBuilder()
		 .addObject(userArgs)
		 .build()

	if (userArgs.help || argv.isEmpty()) {
		jct.usage()
		System.exit(0)
	}

	try {
		jct.parse(*argv)
	} catch (e: ParameterException) {
		println(e.message)
		println("Pass --help to see information for all options")
	}


	userArgs.run()

//	println(System.getProperty("user.dir"))


}