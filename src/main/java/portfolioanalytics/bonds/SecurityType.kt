package portfolioanalytics.bonds

enum class SecurityType(val desc: String) {
	Treasury("U.S. Treasury"),
	Corporate("Corporate Notes"),
	FederalAgency_GSE("Federal Agency: GSE"),
	FederalAgency_CMO("Federal Agency: CMO"),
	ABS("Asset-Backed Securities"),
	NegotiableCD("Negotiable Certificates of Deposit"),
	CommercialPaper("Commercial Paper"),
	Supranationals("Supranationals"),
	Municipal("Municipal"), ;

	override fun toString(): String {
		return desc
	}


}