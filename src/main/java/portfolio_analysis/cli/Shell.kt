package portfolio_analysis.cli

import portfolio_analysis.APP_NAME
import portfolio_analysis.Portfolio
import portfolio_analysis.bonds.createSampleBonds
import portfolio_analysis.cli.ReportOption.Companion.createSubMenu
import java.util.*
import kotlin.collections.LinkedHashMap

fun main(args: Array<String>) {
	val portfolio = Portfolio(createSampleBonds())
	val shell = Shell(portfolio)
	shell.init()
}

class Shell(val portfolio: Portfolio) {
	internal val mainMenu: Menu = createMainMenu()
	internal var currentMenu: Menu = mainMenu

	fun init() {
		this.introToConsole()
		this.printMainMenuScreen()
		this.setUpResponseReader()
	}


	companion object {
		val MAIN_MENU_INPUT = KeyInput("main", "m")
		val QUIT_INPUT = KeyInput("quit", "q")
		val HELP_INPUT = KeyInput("help", "h")

		const val MARGIN_MENU_OPTION = "   "
		const val MARGIN_MENU_SUBOPTION = "     "

	}

	private fun setUpResponseReader() {
		val scanner = Scanner(System.`in`)
		while (true) {
			val input = scanner.askForUserInput().normalizeInput()
			chooseAction(input)
		}
	}

	private fun createMainMenu(): Menu {
		val options = LinkedHashMap<String, ReportOption>()
		var optionCount = 1
		options[optionCount++.toString()] = ReportOption(
			 name = "Portfolio Stats",
			 primaryAction = { printStats(portfolio) }
		)

		val distributionName = "Distributions"
		options[optionCount++.toString()] = ReportOption(
			 name = distributionName,
			 subMenu = createSubMenu(distributionName, *createDistributionOptions(portfolio))
		)

		val bondListName = "Detailed Bond Listings"
		options[optionCount++.toString()] = ReportOption(
			 name = bondListName,
			 subMenu = createSubMenu(bondListName, *createBondOptions(portfolio))
		)

		return Menu("Main Menu", options)
	}

	internal fun chooseAction(input: String) {
		when (input) {
			QUIT_INPUT.long, QUIT_INPUT.short -> shutdown()
			MAIN_MENU_INPUT.long, MAIN_MENU_INPUT.short -> printMainMenuScreen()
			HELP_INPUT.long, HELP_INPUT.short -> printHelpScreen()
			else -> respond(input)
		}
	}

	internal fun respond(input: String): ReportOption? {
		val option = parseInput(input, input.toCharArray())
		when {
			option == null -> printInvalidInput(input)
			option.primaryAction != null -> option.printPrimary()
			option.subMenu != null -> setAndPrintCurrentMenu(option.subMenu)
		}
		printExtraOptionsMsg()

		return option
	}

	fun shutdown() {
		println("Shutting down...")
		System.exit(0)
	}

	internal fun printHelpScreen() {
		println("--- Help Page ---")
		println("Choose an option by entering the value corresponding to the option")
		println("Example:")
		printMainMenuScreen(false)
		printlnWithPrefix(str = "Enter \"1\" to choose the [1]st option.")
		printlnWithPrefix(str = "Enter \"2a\" to choose the first item under the [2]nd option.")
	}


	private fun parseInput(input: String, inputChars: CharArray): ReportOption? {
		val firstInputVal = inputChars[0].toString()
		var option = currentMenu.options[firstInputVal]
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
		currentMenu.print()
	}

	private fun ReportOption.printPrimary() {
		executePrimary()
		currentMenu.print()
	}

	private fun printExtraOptionsMsg() {
		val mainKey = MAIN_MENU_INPUT.short.addBulletStyling()
		val helpKey = HELP_INPUT.short.addBulletStyling()
		val quitKey = QUIT_INPUT.short.addBulletStyling()
		val str = "Or enter ${mainKey}ain menu, ${helpKey}elp, or " +
			 "${quitKey}uit the application"

		println(str)
	}

	internal fun setAndPrintCurrentMenu(menu: Menu) {
		currentMenu = menu
		currentMenu.print()
	}

	private fun Scanner.askForUserInput(): String {
		print(">> ")
		return this.next()
	}

	private fun introToConsole() {
		println("--- Welcome to the $APP_NAME ---")
		println()
		println("Choose from the following options to view portfolio_analysis and bond analytics:")
	}


	internal fun printMainMenuScreen(printExtraOpts: Boolean = true) {
		mainMenu.print()
		currentMenu = mainMenu

		if (printExtraOpts) printExtraOptionsMsg()
	}
}

internal fun String.normalizeInput(): String {
	return this.toLowerCase().trim()
}

class KeyInput(
	 val long: String,
	 val short: String
)
