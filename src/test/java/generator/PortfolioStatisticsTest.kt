package generator

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class PortfolioStatisticsTest {
	val testBonds = createTestBonds()
	lateinit var pStats: PortfolioStatistics

	@BeforeEach
	fun setUp() {
		pStats = PortfolioStatistics(createTestBonds())
	}

	@Test
	fun getBondsBySecurityType() {
		for (testBond in testBonds) {
			val bondFound = pStats.bondsBySecurityType[testBond.securityType]!!.find { bond -> bond == testBond }
			assertNotNull(bondFound)
		}
	}

	@Test
	fun getBondsByMaturityRange() {
		for (testBond in testBonds) {
			val bondFound = pStats.bondsByMaturityRange[testBond.maturityRange]!!.find { bond -> bond == testBond }
			assertNotNull(bondFound)
		}
	}

	@Test
	fun getBondsBySpRating() {
		for (testBond in testBonds) {
			val bondFound = pStats.bondsBySpRating[testBond.spRating]!!.find { bond -> bond == testBond }
			assertNotNull(bondFound)
		}
	}

	@Test
	fun getAllocationBySecurityType() {
		val securityTypes: MutableSet<SecurityType> = HashSet(testBonds.map { it.securityType })
		securityTypes.map { type ->
			val expected = testBonds.asSequence()
				 .filter { it.securityType == type }
				 .sumByDouble { it.marketValue / pStats.marketValue }

			assertEquals(expected, pStats.allocationBySecurityType[type])
		}

		val total = pStats.allocationBySecurityType.values.sumByDouble { it }
		assertEquals(1.0, round(total, 2))
	}


	@Test
	fun getAllocationByMaturityRange() {
		val maturityRanges: MutableSet<MaturityRange> = HashSet(testBonds.map { it.maturityRange })
		maturityRanges.map { range ->
			val expected = testBonds.asSequence()
				 .filter { it.maturityRange == range }
				 .sumByDouble { it.marketValue / pStats.marketValue }
			assertEquals(expected, pStats.allocationByMaturityRange[range])
		}
		val total = pStats.allocationByMaturityRange.values.sumByDouble { it }
		assertEquals(1.0, round(total, 2))
	}

	@Test
	fun getAllocationsBySpRating() {
		val spRatings: MutableSet<SpRating> = HashSet(testBonds.map { it.spRating })
		spRatings.map { rating ->
			val expected = testBonds.asSequence()
				 .filter { it.spRating == rating }
				 .sumByDouble { it.marketValue / pStats.marketValue }
			assertEquals(expected, pStats.allocationsBySpRating[rating])
		}
		val total = pStats.allocationsBySpRating.values.sumByDouble { it }
		assertEquals(1.0, round(total, 2))
	}

}