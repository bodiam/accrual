package portfolioanalytics

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import portfolioanalytics.bonds.MaturityRange
import portfolioanalytics.bonds.SecurityType
import portfolioanalytics.bonds.SpRating
import portfolioanalytics.bonds.createSampleBonds
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class PortfolioTest {
	private val testBonds = createSampleBonds()
	private lateinit var portfolio: Portfolio

	@BeforeEach
	fun setUp() {
		portfolio = Portfolio(createSampleBonds())
	}

	@Test
	fun getBondsBySecurityType() {
		for (testBond in testBonds) {
			val bondFound = portfolio.bondsBySecurityType[testBond.securityType]!!.find { bond -> bond == testBond }
			assertNotNull(bondFound)
		}
	}

	@Test
	fun getBondsByMaturityRange() {
		for (testBond in testBonds) {
			val bondFound = portfolio.bondsByMaturityRange[testBond.maturityRange]!!.find { bond -> bond == testBond }
			assertNotNull(bondFound)
		}
	}

	@Test
	fun getBondsBySpRating() {
		for (testBond in testBonds) {
			val bondFound = portfolio.bondsBySpRating[testBond.spRating]!!.find { bond -> bond == testBond }
			assertNotNull(bondFound)
		}
	}

	@Test
	fun getPercentagesBySecurityType() {
		val securityTypes: MutableSet<SecurityType> = HashSet(testBonds.map { it.securityType })
		securityTypes.map { type ->
			val expected = testBonds.asSequence()
				 .filter { it.securityType == type }
				 .sumByDouble { it.marketValue / portfolio.marketValue }

			assertEquals(expected, portfolio.percentagesBySecurityType[type])
		}

		val total = portfolio.percentagesBySecurityType.values.sumByDouble { it }
		assertEquals(1.0, round(total, 2))
	}


	@Test
	fun getPercentagesByMaturityRange() {
		val maturityRanges: MutableSet<MaturityRange> = HashSet(testBonds.map { it.maturityRange })
		maturityRanges.map { range ->
			val expected = testBonds.asSequence()
				 .filter { it.maturityRange == range }
				 .sumByDouble { it.marketValue / portfolio.marketValue }
			assertEquals(expected, portfolio.percentagesByMaturityRange[range])
		}
		val total = portfolio.percentagesByMaturityRange.values.sumByDouble { it }
		assertEquals(1.0, round(total, 2))
	}

	@Test
	fun getPercentagesBySpRating() {
		val spRatings: HashSet<SpRating?> = HashSet(testBonds.map { it.spRating })
		spRatings.map { rating ->
			val expected = testBonds.asSequence()
				 .filter { it.spRating == rating }
				 .sumByDouble { it.marketValue / portfolio.marketValue }
			assertEquals(expected, portfolio.percentagesBySpRating[rating])
		}
		val total = portfolio.percentagesBySpRating.values.sumByDouble { it }
		assertEquals(1.0, round(total, 2))
	}

}