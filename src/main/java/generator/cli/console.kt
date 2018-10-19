package generator.cli

import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestWordMin
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import generator.Bonds
import generator.createTestBonds
import generator.toPercent
import generator.withCommas

class TablePrinter {
	val at = AsciiTable()

	fun addHeaders(title: String, headers: Array<String>) {
		/* setup title */
		if (title.isNotEmpty()) {
			at.addRule()
			val nulls = arrayOfNulls<String>(headers.size)
			nulls[nulls.size - 1] = title
			at.addRow(*nulls).setTextAlignment(TextAlignment.CENTER)
			at.addRule()
		}

		at.addRow(*headers)
			 .setTextAlignment(TextAlignment.CENTER)
		at.addRule()
	}

	fun formatTable(at: AsciiTable) {
		at.setPaddingLeft(1)
		at.setPaddingRight(1)
		at.renderer.cwc = CWC_LongestWordMin(8)
	}

	fun render() {
		formatTable(at)
		println(at.render())
	}

	fun addBonds_Detailed(title: String, bonds: Bonds) {
		addHeaders(title, arrayOf("CUSIP", "Par", "Coupon", "Settle\nDate", "Maturity\nDate",
			 "Original\nCost", "YTM At Cost", "Amortized\nCost", "Market\nValue", "S&P Rating", "Moody's Rating"))
		for (b in bonds) {
			at.addRow(
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
			at.addRule()
		}
	}
}

fun main(args: Array<String>) {
	val bonds = createTestBonds()
	val tp = TablePrinter()
	tp.addBonds_Detailed("Portfolio Bonds", bonds)


	tp.render()

}