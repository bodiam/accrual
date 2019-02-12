package portfolioanalytics.cli

import de.vandermeer.asciitable.AT_Row
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestWordMin
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import portfolioanalytics.TradedBonds
import portfolioanalytics.bonds.createSampleBonds
import portfolioanalytics.toPercent
import portfolioanalytics.withCommas

/**
 * Prints ASCII tables
 */
internal class TextTable {
	private val table = AsciiTable()

	init {
		table.addRule()
	}

	/**
	 * Adds the header row
	 */
	private fun addHeaders(title: String, headers: Array<String>) {
		/* setup title */
		addTitle(title, headers.size)

		// add value columns
		table.addRow(*headers)
			 .setTextAlignment(TextAlignment.CENTER)
		table.addRule()
	}

	/**
	 * Adds a title to the table
	 * @param title the title name
	 * @param numOfColumns the number of columns the title should span (should be equal to the max number of
	 * columns in the table)
	 */
	internal fun addTitle(title: String, numOfColumns: Int) {
		/* setup title */
		val nulls = arrayOfNulls<String>(numOfColumns)
		nulls[nulls.size - 1] = title
		table.addRow(*nulls)
			 .setTextAlignment(TextAlignment.CENTER)
		table.addRule()
	}


	private fun AsciiTable.formatTable() {
		setPaddingLeft(1)
		setPaddingRight(1)
	}

	internal fun setSmallColumnWidth() {
		table.renderer.cwc = CWC_LongestWordMin(10)
	}

	internal fun setMediumColumnWidth() {
		table.renderer.cwc = CWC_LongestWordMin(16)
	}

	internal fun render() {
		table.formatTable()
		println(table.render())
	}

	/**
	 * Adds a key-value row to the table
	 */
	internal fun addKeyValue(key: String, value: Any): AT_Row {
		val row = table.addRow(key, value)
		table.addRule()
		return row
	}

	/**
	 * Adds a list of tradedBonds to the table
	 */
	internal fun addBondsInDetail(title: String, tradedBonds: TradedBonds) {
		addHeaders(title, arrayOf("CUSIP", "Par", "Coupon", "Maturity Date", "YTM At Cost", "Original Cost", "Amortized Cost", "Market Value", "S&P Rating"))

		for (b in tradedBonds) {
			table.addRow(
				 b.cusip,
				 b.par.withCommas(),
				 b.coupon.toPercent(2),
				 b.maturityDate,
				 b.yieldAtCost.toPercent(2),
				 b.originalCost.withCommas(),
				 b.amortizedCost.withCommas(),
				 b.marketValue.withCommas(),
				 b.spRating?.rating ?: ""
			)
			table.addRule()
		}
	}
}

//main funciton for testing
fun main(args: Array<String>) {
	val bonds = createSampleBonds()
	val textTable = TextTable()
	textTable.addBondsInDetail("Portfolio TradedBonds", bonds)
	textTable.setMediumColumnWidth()
	textTable.render()
}
