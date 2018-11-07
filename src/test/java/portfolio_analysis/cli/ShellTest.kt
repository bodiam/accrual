package portfolio_analysis.cli

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.*
import portfolio_analysis.Portfolio
import portfolio_analysis.bonds.createSampleBonds
import kotlin.test.assertEquals


internal class ShellTest {
	private val portfolio = Portfolio(createSampleBonds())
	private lateinit var shell: Shell

	@BeforeEach
	fun setUp() {
		shell = Shell(portfolio)
	}

	@ParameterizedTest(name = "Input: \"{0}\"")
	@ValueSource(strings = ["quit", "q"])
	fun chooseAction_shutdown(input: String) {
		val shellSpy = spy(shell)
		doNothing().`when`(shellSpy).shutdown()
		shellSpy.chooseAction(input)
		verify(shellSpy).shutdown()
	}

	@ParameterizedTest(name = "Input: \"{0}\"")
	@ValueSource(strings = ["help", "h"])
	internal fun chooseAction_help(input: String) {
		val shellSpy = spy(shell)

		shellSpy.chooseAction(input)

		verify(shellSpy).printHelpScreen()
	}

	@ParameterizedTest(name = "Input: \"{0}\"")
	@ValueSource(strings = ["main", "m"])
	fun chooseAction_mainMenu(input: String) {
		val shellSpy = spy(shell)

		//switch current menu off main menu
		shellSpy.currentMenu = Menu("fake menu", HashMap())

		shellSpy.chooseAction(input)

		verify(shellSpy).printMainMenuScreen()
		assertEquals(shellSpy.mainMenu, shellSpy.currentMenu)
	}

	@Test
	fun respondWithOnlySubOptionsMenu() {
		//assuming the option corresponding to "2" has no primary action and only suboptions
		val shellSpy = spy(shell)
		val option = shellSpy.respond(input = "2")
		verify(shellSpy).setAndPrintCurrentMenu(option?.subMenu!!)
	}

	@ParameterizedTest
	@ValueSource(strings = ["10001", "b"])
	fun invalidInput(input: String) {
		val shellSpy = spy(shell)
		shellSpy.printInvalidInput(input)
		verify(shellSpy).printInvalidInput(input)
	}

	@ParameterizedTest
	@CsvSource(" 1   , 1", " 2a , 2a")
	fun inputNormalization(input: String, expected: String) {
		val actual = input.normalizeInput()
		assertEquals(expected, actual)
	}

}