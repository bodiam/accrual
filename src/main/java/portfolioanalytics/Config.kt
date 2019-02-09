package portfolioanalytics

import java.time.format.DateTimeFormatter

val workingDir: String = System.getProperty("user.dir")

const val APP_NAME = "Accrual — a Bond Analytics Tool"
val TEST_DATA_DEFAULT_FILEPATH = "$workingDir/src/test/test-data/input/sample-data.csv"
val DATE_FORMAT = "M/d/yy"
val dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
