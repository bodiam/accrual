package portfolioanalytics.cli

import portfolioanalytics.APP_NAME
import portfolioanalytics.Portfolio
import portfolioanalytics.bonds.createSampleBonds
import portfolioanalytics.cli.PrintOption.Companion.createSubMenu
import portfolioanalytics.normalize
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Interactive user-interface.
 * @property mainMenu the main menu for the application
 * @property currentMenu the menu currently selected by the user
 */
class Shell(private val portfolio: Portfolio) {
	internal val mainMenu: Menu = createMainMenu()
	internal var currentMenu: Menu = mainMenu

	/**
	 * Initializes the shell.
	 */
	fun init() {
		this.introToConsole()
		this.printMainMenuScreen()
		this.setUpResponseReader()
	}

	companion object {
		class KeyInput(
			 val name: String,
			 val key: String
		)

		//menu input keys
		val MAIN_MENU_INPUT = KeyInput("main", "m")
		val QUIT_INPUT = KeyInput("quit", "q")
		val HELP_INPUT = KeyInput("help", "h")


		const val USER_INPUT_SYMBOL = ">> "

		//margin sizes
		const val MARGIN_MENU_OPTION = "   "
		const val MARGIN_MENU_SUBOPTION = "     "
	}

	/**
	 * Sets up response reader to read user input until user exits the application.
	 */
	private fun setUpResponseReader() {
		val scanner = Scanner(System.`in`)
		while (true) {
			val input = scanner.askForUserInput().normalize()
			inputHandler(input)
		}
	}

	private fun createMainMenu(): Menu {
		val options = LinkedHashMap<String, PrintOption>()
		var optionCount = 1
		options[optionCount++.toString()] = PrintOption(
			 name = "Portfolio Stats",
			 primaryAction = { printStats(portfolio) }
		)

		val distributionName = "Distributions"
		options[optionCount++.toString()] = PrintOption(
			 name = distributionName,
			 subMenu = createSubMenu(distributionName, *createDistributionOptions(portfolio))
		)

		val bondListName = "Detailed Bond Listings"
		options[optionCount++.toString()] = PrintOption(
			 name = bondListName,
			 subMenu = createSubMenu(bondListName, *createBondOptions(portfolio))
		)

		return Menu("Main Menu", options)
	}

	/**
	 * Performs an action based on user input.
	 * @param input the user input
	 */
	internal fun inputHandler(input: String) {
		when (input) {
			QUIT_INPUT.name, QUIT_INPUT.key -> shutdown()
			MAIN_MENU_INPUT.name, MAIN_MENU_INPUT.key -> printMainMenuScreen()
			HELP_INPUT.name, HELP_INPUT.key -> printHelpScreen()
			else -> respond(input)
		}
	}

	/**
	 * Prints out a response given the user input.
	 * @input the user input
	 */
	internal fun respond(input: String): PrintOption? {
		val option = parseInput(input, input.toCharArray())
		when {
			option == null -> printInvalidInput(input)
			option.primaryAction != null -> option.printPrimary()
			option.subMenu != null -> setAndPrintCurrentMenu(option.subMenu)
		}
		printExtraOptionsMsg()

		return option
	}

	/**
	 * Exits the application!
	 */
	fun shutdown() {
		println("Shutting down...Bye!")
		System.exit(0)
	}

	/**
	 * Prints help text in case the user is confused on how to use the UI.
	 */
	internal fun printHelpScreen() {
		println("--- Help Page ---")
		println("Choose an option by entering the value corresponding to the option")
		println("Example:")
		printMainMenuScreen(false)
		printlnWithPrefix(input = "Enter \"1\" to choose the [1]st option.")
		printlnWithPrefix(input = "Enter \"2a\" to choose the first item under the [2]nd option.")
	}

	private fun parseInput(input: String, inputChars: CharArray): PrintOption? {
		val firstInputVal = inputChars[0].toString()
		var option = currentMenu.options[firstInputVal]
		//let's the user select a submenu option in one input, ex. 'input: 1a'
		if (input.length > 1) {
			val secondInputVal = inputChars[1].toString()
			option = option?.getSubMenuOptions()?.get(secondInputVal)
		}
		if (input.length > 2) {
			option = null
		}
		return option
	}

	internal fun printInvalidInput(input: String) {
		println("$input is invalid. Try another input...\n")
		currentMenu.printMenu()
	}

	private fun PrintOption.printPrimary() {
		executePrimary()
		currentMenu.printMenu()
	}

	/**
	 * Prints options that are always available to the user.
	 */
	private fun printExtraOptionsMsg() {
		val mainKey = MAIN_MENU_INPUT.key.addBulletStyling()
		val helpKey = HELP_INPUT.key.addBulletStyling()
		val quitKey = QUIT_INPUT.key.addBulletStyling()
		val str = "Or enter ${mainKey}ain menu, ${helpKey}elp, or " +
			 "${quitKey}uit the application"

		println(str)
	}

	internal fun setAndPrintCurrentMenu(menu: Menu) {
		currentMenu = menu
		currentMenu.printMenu()
	}

	private fun Scanner.askForUserInput(): String {
		print(USER_INPUT_SYMBOL)
		return this.next()
	}

	/**
	 * Prints intro when user launches the application.
	 */
	private fun introToConsole() {
		println("+=====================================================+")
		println("--- Welcome to $APP_NAME ---")
		println("+=====================================================+\n")
		println("Choose from the following menu to view portfolio analytics and bond data:")
	}


	internal fun printMainMenuScreen(printExtraOpts: Boolean = true) {
		mainMenu.printMenu()
		currentMenu = mainMenu

		if (printExtraOpts) printExtraOptionsMsg()
	}
}


//main funciton for testing the shell
fun main(args: Array<String>) {
	val portfolio = Portfolio(createSampleBonds())
	val shell = Shell(portfolio)
	shell.init()
}
