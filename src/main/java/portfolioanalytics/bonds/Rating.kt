package portfolioanalytics.bonds

import java.util.*

/**
 * A rating represents a grade given to a bond for its credit quality. The rating system helps investors
 * determine a bond's credit risk, the bond issuer's ability to pay back principal and interest on a bond.
 * @author Vincent Xiao
 */
interface Rating {
	val rating: String
	val agency: RatingAgency
	val grade: Grade?
	val term: Term?
}

/**
 * A term is the time horizon in which a bond will mature. Generally, tradedBonds maturing in one year or less are
 * considered key-term
 */
enum class Term {
	ShortTerm,
	LongTerm;
}

/**
 * A grade is a category that a rating is associated with
 * For example, a bond is considered Prime if its credit quality is rated 'AAA' by S&P
 */
enum class Grade constructor(val desc: String, val investmentGrade: Boolean?) {
	Prime("Highest", true),
	VeryHigh("Very High", true),
	High("High", true),
	Good("Good", true),
	/*
	Could add more options here, like: Speculative, High Risk...
	*/
	Default("In Default", false),
}

private fun <T : Rating> createRatingMap(creditRatings: Array<T>): HashMap<String, T> {
	val ratingMap = HashMap<String, T>()
	for (creditRating in creditRatings) ratingMap[creditRating.rating] = creditRating

	return ratingMap
}

fun <T : Rating> Map<String, T>.of(name: String): T {
	return this[name] ?: throw IllegalArgumentException("Invalid rating name: $name")
}

/**
 * A company that assesses the credit risk of tradedBonds and their issuers
 * The major credit rating agencies include Standard & Poor's, Moody's, and Fitch
 */
enum class RatingAgency(val desc: String) {
	StandardAndPoors("Standard & Poors"),
	Moodys("Moody's");
}

/**
 * Ratings by Standard & Poor's
 */
enum class SpRating(
	 override val rating: String,
	 override val grade: Grade?,
	 override val term: Term?
) : Rating {
	//name term ratings
	AAA("AAA", Grade.Prime, Term.LongTerm),
	AA_Plus("AA+", Grade.VeryHigh, Term.LongTerm),
	AA("AA", Grade.VeryHigh, Term.LongTerm),
	AA_Minus("AA-", Grade.VeryHigh, Term.LongTerm),
	A_Plus("A+", Grade.High, Term.LongTerm),
	A("A", Grade.High, Term.LongTerm),
	A_Minus("A-", Grade.High, Term.LongTerm),
	BBB_Plus("BBB+", Grade.Good, Term.LongTerm),
	BBB("BBB", Grade.Good, Term.LongTerm),
	BBB_Minus("BBB-", Grade.Good, Term.LongTerm),
	//key term ratings
	A_1_Plus("A-1+", Grade.Prime, Term.ShortTerm),
	A_1("A-1", Grade.VeryHigh, Term.ShortTerm),
	A_2("A-2", Grade.High, Term.ShortTerm),
	A_3("A-3", Grade.Good, Term.ShortTerm),
	NotRated("NR", null, null)
	;

	override val agency = RatingAgency.StandardAndPoors

	companion object {
		private val ratingMap = createRatingMap(values())

		fun getRating(rating: String): SpRating? {
			return ratingMap.of(rating)
		}
	}

	override fun toString(): String {
		return rating
	}

}

/**
 * Ratings by Moody's
 */
enum class MoodysRating(
	 override val rating: String,
	 override val grade: Grade?,
	 override val term: Term?
) : Rating {
	//name term ratings
	Aaa("Aaa", Grade.Prime, Term.LongTerm),
	Aa1("Aa1", Grade.VeryHigh, Term.LongTerm),
	Aa2("Aa2", Grade.VeryHigh, Term.LongTerm),
	Aa3("Aa3", Grade.VeryHigh, Term.LongTerm),
	A1("A1", Grade.High, Term.LongTerm),
	A2("A2", Grade.High, Term.LongTerm),
	A3("A3", Grade.High, Term.LongTerm),
	Baa1("Baa1", Grade.Good, Term.LongTerm),
	Baa2("Baa2", Grade.Good, Term.LongTerm),
	Baa3("Baa3", Grade.Good, Term.LongTerm),
	//key term ratings
	P_1("P-1", Grade.Prime, Term.ShortTerm),
	P_2("A-1", Grade.High, Term.ShortTerm),
	P_3("A-2", Grade.Good, Term.ShortTerm),

	NotRated("NR", null, null)
	;

	override val agency = RatingAgency.Moodys

	companion object {
		private val ratingMap = createRatingMap(values())

		fun getRating(name: String): MoodysRating? {
			return ratingMap.of(name)
		}
	}

}

