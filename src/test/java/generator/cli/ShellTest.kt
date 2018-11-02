package generator.cli

import generator.Portfolio
import generator.createTestBonds
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.*
import kotlin.test.assertEquals


internal class ShellTest {
	val portfolio = Portfolio(createTestBonds())
	lateinit var shell: Shell

	@BeforeEach
	fun setUp() {
		shell = Shell(portfolio)
	}

	@ParameterizedTest(name = "Input: \"{0}\"")
	@ValueSource(strings = ["quit", Shell.QUIT_KEY])
	fun chooseAction_shutdown(input: String) {
		val shellSpy = spy(shell)
		doNothing().`when`(shellSpy).shutdown()

		shellSpy.chooseAction(input)
		verify(shellSpy).shutdown()
	}

	@ParameterizedTest(name = "Input: \"{0}\"")
	@ValueSource(strings = ["main", Shell.MAIN_MENU_KEY])
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