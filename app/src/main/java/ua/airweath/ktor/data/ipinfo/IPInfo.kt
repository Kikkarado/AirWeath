package ua.airweath.ktor.data.ipinfo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IPInfo(
    //https://ipapi.co/$ipAddress/json
    @SerialName("ip")
    val ip: String? = null,
    @SerialName("network")
    val network: String? = null,
    @SerialName("version")
    val version: String? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("region")
    val region: String? = null,
    @SerialName("region_code")
    val regionCode: String? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("country_name")
    val countryName: String? = null,
    @SerialName("country_code")
    val countryCode: String? = null,
    @SerialName("country_code_iso3")
    val countryCodeIso3: String? = null,
    @SerialName("country_capital")
    val countryCapital: String? = null,
    @SerialName("country_tld")
    val countryTld: String? = null,
    @SerialName("continent_code")
    val continentCode: String? = null,
    @SerialName("in_eu")
    val inEu: Boolean? = null,
    @SerialName("postal")
    val postal: String? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("utc_offset")
    val utcOffset: String? = null,
    @SerialName("country_calling_code")
    val countryCallingCode: String? = null,
    @SerialName("currency")
    val currency: String? = null,
    @SerialName("currency_name")
    val currencyName: String? = null,
    @SerialName("languages")
    val languages: String? = null,
    @SerialName("country_area")
    val countryArea: Float? = null,
    @SerialName("country_population")
    val countryPopulation: Int? = null,
    @SerialName("asn")
    val asn: String? = null,
    @SerialName("org")
    val org: String? = null,

    //https://ipinfo.io/$ipAddress/json
    @SerialName("hostname")
    val hostname: String? = null,
    @SerialName("anycast")
    val anycast: Boolean? = null,
    @SerialName("loc")
    val loc: String? = null,
    @SerialName("readme")
    val readme: String? = null,
)
