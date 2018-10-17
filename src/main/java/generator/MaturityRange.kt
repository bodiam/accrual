package generator

enum class MaturityRange(private val maturity: Int, val desc: String) {
	SixMonths(182, "Less than 6 Months"),
	OneYear(365, "6 Months to 1 Year"),
	TwoYears(730, "1 to 2 Years"),
	ThreeYears(1095, "2 to 3 Years"),
	FourYears(1460, "3 to 4 Years"),
	FiveYears(1825, "4 to 5 Years"),
	OverFiveYears(Int.MAX_VALUE, "Over 5 Years")
	;

	companion object {
		fun getRange(maturity: Int): MaturityRange {
			var found = SixMonths
			for (range in values())
				if (range.maturity <= maturity)
					found = range
			return found
		}
	}
}


