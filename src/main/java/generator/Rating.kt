package generator

import java.util.*


interface Rating {
	val rating: String
	val agency: RatingAgency
	val grade: Grade
	val term: Term
}

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

enum class SpRating(
	 override val rating: String,
	 override val grade: Grade,
	 override val term: Term
) : Rating {
	//long term ratings
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
	//short term ratings
	A_1_Plus("A-1+", Grade.Prime, Term.ShortTerm),
	A_1("A-1", Grade.VeryHigh, Term.ShortTerm),
	A_2("A-2", Grade.High, Term.ShortTerm),
	A_3("A-3", Grade.Good, Term.ShortTerm),
	NotRated()
	;

	companion object {
		private val ratingMap = HashMap<String, SpRating>(values().size, 1f)

		init {
			ratingMap.forEach { (_, value) ->
				ratingMap[value.rating] = value
			}
		}

		fun of(name: String): SpRating {
			return ratingMap[name] ?: throw IllegalArgumentException("Invalid rating name: $name")
		}
	}

	override fun toString(): String {
		return rating
	}

	//for NotRated type
	constructor()

	override val agency = RatingAgency.SP
}

enum class MoodysRating(
	 override val rating: String,
	 override val grade: Grade,
	 override val term: Term
) : Rating {
	//long term ratings
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
	//short term ratings
	P_1("P-1", Grade.Prime, Term.ShortTerm),
	P_2("A-1", Grade.High, Term.ShortTerm),
	P_3("A-2", Grade.Good, Term.ShortTerm),
	;

	override val agency = RatingAgency.Moodys
}

enum class Term {
	ShortTerm,
	LongTerm;
}