package generator.cli

import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestWordMin
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import generator.Bonds
import generator.createTestBonds
import generator.toPercent
import generator.withCommas

class TablePrinter {
	val table = AsciiTable()

	init {
		table.addRule()
	}

	fun addHeaders(title: String, headers: Array<String>) {
		/* setup title */
		addTitle(title, headers.size)

		// add value columns
		table.addRow(*headers)
			 .setTextAlignment(TextAlignment.CENTER)
		table.addRule()
	}

	fun addTitle(title: String, numOfColumns: Int) {
		/* setup title */
		val nulls = arrayOfNulls<String>(numOfColumns)
		nulls[nulls.size - 1] = title
		table.addRow(*nulls)
			 .setTextAlignment(TextAlignment.CENTER)
		table.addRule()
	}


	private fun formatTable(at: AsciiTable) {
		at.setPaddingLeft(1)
		at.setPaddingRight(1)
		at.renderer.cwc = CWC_LongestWordMin(8)
	}

	fun render() {
		formatTable(table)
		println(table.render())
	}

	fun addKeyValue(key: String, value: Any) {
		table.addRow(key, value)
		table.addRule()
	}

	fun addBondsInDetail(title: String, bonds: Bonds) {
		addHeaders(title, arrayOf("CUSIP", "Par", "Coupon", "Settle\nDate", "Maturity Date", "Original Cost", "YTM At Cost", "Amortized Cost", "Market Value", "S&P Rating", "Moody's Rating"))

		for (b in bonds) {
			table.addRow(
//				 b.description,
				 b.cusip,
				 b.par.withCommas(),
				 b.coupon.toPercent(),
				 b.settleDate,
				 b.maturityDate,
				 b.originalCost.withCommas(),
				 b.yieldAtCost.toPercent(),
				 b.amortizedCost.withCommas(),
				 b.marketValue.withCommas(),
				 b.spRating.rating,
				 b.moodysRating.rating
			)
			table.addRule()
		}
	}
}

fun main(args: Array<String>) {
	val bonds = createTestBonds()
	val tp = TablePrinter()
	tp.addBondsInDetail("Portfolio Bonds", bonds)


	tp.render()

}