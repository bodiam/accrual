# Accrual
Accrual is a bond portfolio analytics tool written in Kotlin.

Features:
- calculates portfolio statistics (totals, weighted-average yield and maturity)
- bond distributions by security type, maturity, and credit ratings
- detailed bond listings by security type, maturity, and credit ratings 
- parses CSV files containing bond data 
- shell CLI with ASCII table output
 
## Setup Locally
### Requirements
- Java 8 or later
- Maven

### Installation
```shell
 git clone https://github.com/xiao-vincent/accrual.git
 cd accrual 
```
### Running the Application
Use your IDE's build/run tool or use Kotlin's [command-line compiler](https://kotlinlang.org/docs/tutorials/command-line.html) to run [App.kt](./src/main/java/portfolioanalytics/App.kt)

## Usage
Running the app presents an interative console:
```shell
Choose from the following menu to view portfolio analytics and bond data:

  Main Menu
  ---------
  [1] Portfolio Stats
  [2] Distributions
    [a] Sector
    [b] Maturity
    [c] Credit
  [3] Detailed Bond Listings
    [a] Sector
    [b] Maturity
    [c] Credit

>> 1
┌─────────────────────────────────┐
│      Portfolio Statistics       │
│          As of 12/31/18         │
├────────────────┬────────────────┤
│ Par            │ 172,175,288.50 │
├────────────────┼────────────────┤
│ Market Value   │ 172,463,172.88 │
├────────────────┼────────────────┤
│ Amortized Cost │ 173,172,162.34 │
├────────────────┼────────────────┤
│ Original Cost  │ 175,780,852.49 │
├────────────────┼────────────────┤
│ Accrued        │ 750,033.51     │
│ Interest       │                │
├────────────────┼────────────────┤
│ Yield At Cost  │ 1.55%          │
├────────────────┼────────────────┤
│ Average        │ 2.81 Years     │
│ Maturity       │                │
└────────────────┴────────────────┘

>> 2c
┌─────────────────────────────────┐
│       Credit Distribution       │
│    Ratings by Standard & Poors  │
├────────────────┬────────────────┤
│ AAA            │ 6.9%           │
├────────────────┼────────────────┤
│ AA+            │ 59.3%          │
├────────────────┼────────────────┤
│ AA             │ 0.2%           │
├────────────────┼────────────────┤
│ AA-            │ 5.7%           │
├────────────────┼────────────────┤
│ A+             │ 13.3%          │
├────────────────┼────────────────┤
│ A              │ 5.8%           │
├────────────────┼────────────────┤
│ A-             │ 3.9%           │
├────────────────┼────────────────┤
│ BBB+           │ 1.5%           │
├────────────────┼────────────────┤
│ A-1+           │ 2.0%           │
├────────────────┼────────────────┤
│ NR             │ 1.5%           │
└────────────────┴────────────────┘
```

