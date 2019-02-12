package portfolioanalytics

import java.time.format.DateTimeFormatter

// Name of the app shown when the CLI starts
const val APP_NAME = "Accrual — a Bond Analytics Tool"

val workingDir: String = System.getProperty("user.dir")
// default file path for CSV file containing bond data
val DEFAULT_DATA_FILEPATH = "$workingDir/src/test/test-data/input/sample-data.csv"

// Date formatter for printing dates
val dateFormatter = DateTimeFormatter.ofPattern("M/d/yy")

