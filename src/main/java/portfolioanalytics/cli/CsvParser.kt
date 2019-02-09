package portfolioanalytics.cli

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import portfolioanalytics.Bonds
import portfolioanalytics.bonds.MoodysRating
import portfolioanalytics.bonds.SecurityType
import portfolioanalytics.bonds.SpRating
import portfolioanalytics.bonds.TradedBond
import portfolioanalytics.dateFormatter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate


class CsvParser(
	 private val filepath: String
) {
	private lateinit var bonds: Bonds

	fun getBonds(): Bonds {
		try {
			bonds = parseBondsFromFile()
		} catch (e: IOException) {
			println(e.message)
			System.exit(0)
		}
		return bonds
	}

	private fun parseBondsFromFile(): Bonds {
		val bonds = ArrayList<TradedBond>()
		val reader = Files.newBufferedReader(Paths.get(filepath))
		val csvParser = CSVParser(reader, CSVFormat.DEFAULT
			 .withFirstRecordAsHeader()
			 .withIgnoreHeaderCase()
			 .withTrim()
		)
		for (row in csvParser) {
			if (row.get(0).isBlank()) break

			bonds.add(
				 createTradedBond(row)
			)
		}
		return bonds
	}


	private fun createTradedBond(row: CSVRecord): TradedBond {
		/* nullable values */
		val tradeDate = row.getIfExists("trade date")
		val settleDate = row.getIfExists("settle date")
		val description = row.getIfExists("description")
		val spRating = row.getIfExists("s&P rating")
		val moodysRating = row.getIfExists("moodys rating")

		return TradedBond(
			 date = row.get("current date").toLocalDate(),
			 maturityDate = row.get("maturity date").toLocalDate(),
			 securityType = row.get("security type").toSecurityType(),
			 cusip = row.get("cusip"),
			 par = row.get("par").toDouble(),
			 coupon = row.get("coupon rate").toDouble() / 100,
			 originalCost = row.get("original cost").toDouble(),
			 amortizedCost = row.get("amortized cost").toDouble(),
			 marketValue = row.get("market value").toDouble(),
			 accruedInterest = row.get("accrued interest").toDouble(),
			 yieldAtCost = row.get("yield at cost").toDouble() / 100,
			 spRating = if (isNotEmpty(spRating)) SpRating.getRating(spRating) else null,
			 moodysRating = if (isNotEmpty(moodysRating)) MoodysRating.getRating(moodysRating) else null,
			 description = description,
			 tradeDate = if (isNotEmpty(tradeDate)) tradeDate.toLocalDate() else null,
			 settleDate = if (isNotEmpty(settleDate)) settleDate.toLocalDate() else null
		)
	}

	private fun CSVRecord.getIfExists(columnName: String): String {
		return try {
			this.get(columnName)
		} catch (e: IllegalArgumentException) {
			""
		}
	}

	private fun isNotEmpty(str: String): Boolean {
		return !str.isEmpty()
	}

	private fun printRecord(row: CSVRecord, bond: TradedBond) {
		println("Record No - " + row.getRecordNumber())
		println("---------------")
		println(bond)
		println("---------------\n")
	}

	private fun String.toLocalDate(): LocalDate {
		return LocalDate.parse(this, dateFormatter)
	}

	private fun String.toSecurityType(): SecurityType {
		return when (this.toLowerCase()) {
			"u.s. treasury" -> SecurityType.Treasury
			"corporate" -> SecurityType.Corporate
			"federal agency gse" -> SecurityType.FederalAgency_GSE
			"federal agency cmo" -> SecurityType.FederalAgency_CMO
			"asset backed securities" -> SecurityType.ABS
			"negotiable cds" -> SecurityType.NegotiableCD
			"supranationals" -> SecurityType.Supranationals
			"municipal" -> SecurityType.Municipal
			else -> throw NoSuchElementException("No security type of $this found")
		}
	}


}

