import kotlinx.serialization.Serializable

@Serializable
data class Sim(
    var entityId: String? = null,
    var parentSubscriberId: String? = null,
    var childSubscriberId: String? = null,
    var hasChild: Boolean = false,
    var iccid: String? = null,
    var displayedCustomerAccountName: String? = null,
    var customerAccountId: String,
    var customerAccountName: String? = null,
    var parentCustomerAccountId: String? = null,
    var parentCustomerAccountName: String? = null,
    var childCustomerAccountId: String? = null,
    var childCustomerAccountName: String? = null,
    var lcpId: String? = null,
    var status: String? = null,
    var fromParentSubscriberOffer: MutableList<SubscriberOffer> = mutableListOf(),
    var toChildSubscriberOffer: MutableList<SubscriberOffer> = mutableListOf(),
    var imsiMsisdnPairs: MutableList<ImsiAndMsisdn> = mutableListOf(),
    var lastActiveMsisdn: String? = null,
    var lastActiveImsi: String? = null,
    var imei: Long? = null,
    var countryIso: String? = null,
    var countryName: String? = null,
    var mcc: String? = null,
    var ci: Long? = null,
    var enb: Long? = null,
    var mnc: String? = null,
    var tac: Int? = null,
    var lac: Int? = null,
    var sac: Int? = null,
    var rac: Int? = null,
    var fromParentDataUsageNumber: Double? = null,
    var fromParentSmsUsageNumber: Long? = null,
    var toChildDataUsageNumber: Double? = null,
    var toChildSmsUsageNumber: Long? = null,
    var alias: String? = null,
    var ipAddress: String? = null,
    var locationLat: Double? = null,
    var locationLng: Double? = null,
    var registered: Boolean = false,
    var registeredTime: String? = null,
    var dataSession: Boolean = false,
    var dataSessionTime: String? = null,
    var dataSessionClosureTime: String? = null,
    var operatorIdentifier: String? = null,
    var radioAccessType: String? = null,
    var systemLabels: MutableList<String>? = null,
    var externalParameters: MutableList<ExternalParameter> = mutableListOf(),
    var labels: MutableList<String> = mutableListOf(),
    val dailyDataUsageMb: Double? = null,
    val dailySmsUsage: Double? = null,
//    val dailyUsageEffectiveDateTo: Timestamp? = null,
    val weeklyDataUsageMb: Double? = null,
    val weeklySmsUsage: Double? = null,
//    val weeklyUsageEffectiveDateTo: Timestamp? = null,
    val monthlyDataUsageMb: Double? = null,
    val monthlySmsUsage: Double? = null,
//    val monthlyUsageEffectiveDateTo: Timestamp? = null,
)

@Serializable
data class ExternalParameter(
    var id: String? = null,
    var instanceId: String? = null, // It is unique id
    var name: String? = null,
    var value: String? = null
)

@Serializable
data class ImsiAndMsisdn(
    var imsi: String? = null,
    var msisdn: String? = null
)

@Serializable
data class SubscriberOffer(
    var id: String? = null,
    var productOfferingId: String? = null,
    var type: String? = null,
    var name: String? = null,

    var periodicCharge: String? = null,
    var periodicChargeCurrency: String? = null,

    var bundleOfferPeriodicBalanceId: String? = null,
    var nestedOfferDataId: String? = null,
    var nestedOfferAllowanceDataBalanceId: String? = null,
    var nestedOfferSmsId: String? = null,
    var nestedOfferAllowanceSmsBalanceId: String? = null,

    var priority: Int? = null,
    var position: Int = 0,

    var moneyBarTotal: Double? = null,
    var moneyBarUsage: Double? = null,
    var moneyBarCurrency: String? = null,
    var moneyTopUpped: Double? = null,

    var dataBarTotal: Double? = null,
    var dataBarUsage: Double? = null,
    var dataBarUnit: String? = null,
    var dataTopUpped: Double? = null,
    var dataUsageUpdate: String? = null,


    var smsBarTotal: Double? = null,
    var smsBarUsage: String? = null,
    var smsTopUpped: String? = null,
    var smsUsageUpdate: String? = null,


    // ===== Data from product offering =====
    var catalogPrice: Double? = null,
    var catalogPriceCurrency: String? = null,
    var dataAllowance: Double? = null, //could be RATE plan's limit
    var dataAllowanceUnit: String? = null,
    var smsAllowance: String? = null,
    var moneyAllowance: Double? = null,
    var moneyAllowanceCurrency: String? = null,
    var offerRenewalInterval: String? = null, // recurringType
    var isActive: Boolean = true,
//    var nextChargeDate: Timestamp? = null,
//    var effectiveDateTo: Timestamp? = null,
    var attachedDate: String? = null
)