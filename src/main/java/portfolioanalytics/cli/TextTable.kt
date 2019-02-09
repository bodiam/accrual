package portfolioanalytics.cli

import de.vandermeer.asciitable.AT_Row
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestWordMin
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import portfolioanalytics.Bonds
import portfolioanalytics.bonds.createSampleBonds
import portfolioanalytics.toPercent
import portfolioanalytics.withCommas

fun main(args: Array<String>) {
	val bonds = createSampleBonds()
	val textTable = TextTable()
	textTable.addBondsInDetail("Portfolio Bonds", bonds)
	textTable.setMediumColumnWidth()
	textTable.render()
}

internal class TextTable {
	private val table = AsciiTable()

	init {
		table.addRule()
	}

	private fun addHeaders(title: String, headers: Array<String>) {
		/* setup title */
		addTitle(title, headers.size)

		// add value columns
		table.addRow(*headers)
			 .setTextAlignment(TextAlignment.CENTER)
		table.addRule()
	}

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

	internal fun addKeyValue(key: String, value: Any): AT_Row {
		val row = table.addRow(key, value)
		table.addRule()
		return row
	}

	internal fun addBondsInDetail(title: String, bonds: Bonds) {
		addHeaders(title, arrayOf("CUSIP", "Par", "Coupon", "Maturity Date", "YTM At Cost", "Original Cost", "Amortized Cost", "Market Value", "S&P Rating"))

		for (b in bonds) {
			table.addRow(
				 b.cusip,
				 b.par.withCommas(),
				 b.coupon.toPercent(),
				 b.maturityDate,
				 b.yieldAtCost.toPercent(),
				 b.originalCost.withCommas(),
				 b.amortizedCost.withCommas(),
				 b.marketValue.withCommas(),
				 b.spRating?.rating ?: ""
			)
			table.addRule()
		}
	}
}

