package generator.cli

import generatoar.SecurityType
import generator.SpRating
import generator.TradedBond
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter


const val CSV_FILEPATH = "/users/vince/Downloads/sample-portfolio-data.csv"

fun parseBonds(file: String): List<TradedBond> {
	val bonds = ArrayList<TradedBond>()
	try {
		val reader = Files.newBufferedReader(Paths.get(file))
		val csvParser = CSVParser(reader, CSVFormat.DEFAULT
			 .withFirstRecordAsHeader()
			 .withIgnoreHeaderCase()
			 .withTrim()

		)
		for (row in csvParser) {
			if (row.get(0).isBlank()) break

			val bond = TradedBond(
				 currentDate = row.get("current date").toLocalDate(),
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
				 spRating = SpRating.getRating(row.get("s&p rating")),
				 // ignore these properties for now
				 description = null,
				 tradeDate = null,
				 settleDate = null,
				 moodysRating = null
			)
			bonds.add(bond)

			print(row, bond)
		}
	} catch (e: IOException) {
		println(e.message)
		System.exit(0)
	}
	return bonds
}

private fun print(row: CSVRecord, bond: TradedBond) {
	println("Record No - " + row.getRecordNumber())
	println("---------------")
	println(bond)
	println("---------------\n")
}

private fun String.toLocalDate(): LocalDate {
	val formatter = DateTimeFormatter.ofPattern("M/d/yy")
	return LocalDate.parse(this, formatter)
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







