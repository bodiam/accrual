package generator

enum class SecurityType(val desc: String) {
    Treasury("U.S. Treasury Bonds"),
    Corporate("Corporate Notes"),
    FederalAgency_GSE("Federal Agency Bonds: Government-Sponsored Enterprises"),
    FederalAgency_CMO("Federal Agency Bonds: Collateralized Mortgage Obligations "),
    ABS("Asset-Backed Securities"),
    NegotiableCD("Negotiable Certificates of Deposit"),
    CommercialPaper("Commercial Paper"),
    Supranatioanls("Supranational Bonds"),
    Municipal("Municipal Bonds"),
}