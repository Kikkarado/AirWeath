package ua.airweath.enums

sealed class USAqiRanges(val range: IntRange) {

    /**0 .. 50*/
    object Good: USAqiRanges(0 .. 50)

    /**51 .. 100*/
    object Moderate: USAqiRanges(51 .. 100)

    /**101 .. 150*/
    object UnhealthyForSensitiveGroups: USAqiRanges(101 .. 150)

    /**151 .. 200*/
    object Unhealthy: USAqiRanges(151 .. 200)

    /**201 .. 300*/
    object VeryUnhealthy: USAqiRanges(201 .. 300)

    /**301 .. Int.MAX_VALUE*/
    object Hazardous: USAqiRanges(301 .. Int.MAX_VALUE)

}

sealed class USAqiRangesO3(val range: IntRange) {

    /**0 .. 50*/
    object Good: USAqiRangesO3(0 until 55)

    /**51 .. 100*/
    object Moderate: USAqiRangesO3(55 until 70)

    /**101 .. 150*/
    object UnhealthyForSensitiveGroups: USAqiRangesO3(70 until 85)

    /**151 .. 200*/
    object Unhealthy: USAqiRangesO3(85 until 105)

    /**201 .. 300*/
    object VeryUnhealthy: USAqiRangesO3(105 until 200)

    /**301 .. Int.MAX_VALUE*/
    object Hazardous: USAqiRangesO3(200 until 405)

}

sealed class USAqiRangesPM25(val range: IntRange) {

    /**0 .. 50*/
    object Good: USAqiRangesPM25(0 until 12)

    /**51 .. 100*/
    object Moderate: USAqiRangesPM25(12 until 35)

    /**101 .. 150*/
    object UnhealthyForSensitiveGroups: USAqiRangesPM25(35 until 55)

    /**151 .. 200*/
    object Unhealthy: USAqiRangesPM25(55 until 150)

    /**201 .. 300*/
    object VeryUnhealthy: USAqiRangesPM25(150 until 250)

    /**301 .. Int.MAX_VALUE*/
    object Hazardous: USAqiRangesPM25(250 until 500)

}

sealed class USAqiRangesPM10(val range: IntRange) {

    /**0 .. 50*/
    object Good: USAqiRangesPM10(0 until 55)

    /**51 .. 100*/
    object Moderate: USAqiRangesPM10(55 until 155)

    /**101 .. 150*/
    object UnhealthyForSensitiveGroups: USAqiRangesPM10(155 until 255)

    /**151 .. 200*/
    object Unhealthy: USAqiRangesPM10(255 until 355)

    /**201 .. 300*/
    object VeryUnhealthy: USAqiRangesPM10(355 until 425)

    /**301 .. Int.MAX_VALUE*/
    object Hazardous: USAqiRangesPM10(425 until 605)

}

sealed class USAqiRangesCO(val range: IntRange) {

    /**0 .. 50*/
    object Good: USAqiRangesCO(0 until 5)

    /**51 .. 100*/
    object Moderate: USAqiRangesCO(5 until 10)

    /**101 .. 150*/
    object UnhealthyForSensitiveGroups: USAqiRangesCO(10 until 13)

    /**151 .. 200*/
    object Unhealthy: USAqiRangesCO(13 until 16)

    /**201 .. 300*/
    object VeryUnhealthy: USAqiRangesCO(16 until 31)

    /**301 .. Int.MAX_VALUE*/
    object Hazardous: USAqiRangesCO(31 until 50)

}

sealed class USAqiRangesSO2(val range: IntRange) {

    /**0 .. 50*/
    object Good: USAqiRangesSO2(0 until 35)

    /**51 .. 100*/
    object Moderate: USAqiRangesSO2(35 until 75)

    /**101 .. 150*/
    object UnhealthyForSensitiveGroups: USAqiRangesSO2(75 until 185)

    /**151 .. 200*/
    object Unhealthy: USAqiRangesSO2(185 until 305)

    /**201 .. 300*/
    object VeryUnhealthy: USAqiRangesSO2(305 until 605)

    /**301 .. Int.MAX_VALUE*/
    object Hazardous: USAqiRangesSO2(605 until 1005)

}

sealed class USAqiRangesNO2(val range: IntRange) {

    /**0 .. 50*/
    object Good: USAqiRangesNO2(0 until 54)

    /**51 .. 100*/
    object Moderate: USAqiRangesNO2(54 until 100)

    /**101 .. 150*/
    object UnhealthyForSensitiveGroups: USAqiRangesNO2(100 until 360)

    /**151 .. 200*/
    object Unhealthy: USAqiRangesNO2(360 until 650)

    /**201 .. 300*/
    object VeryUnhealthy: USAqiRangesNO2(650 until 1250)

    /**301 .. Int.MAX_VALUE*/
    object Hazardous: USAqiRangesNO2(1250 until 2050)

}

sealed class EUAqiRanges(val range: IntRange) {

    /**0 until  20*/
    object Good: EUAqiRanges(0 until  20)

    /**20 until 40*/
    object Fair: EUAqiRanges(20 until 40)

    /**40 until 60*/
    object Moderate: EUAqiRanges(40 until 60)

    /**80 until 100*/
    object Poor: EUAqiRanges(60 until 80)

    /**80 until 100*/
    object VeryPoor: EUAqiRanges(80 until 100)

    /**101 until Int.MAX_VALUE*/
    object ExtremelyPoor: EUAqiRanges(101 until Int.MAX_VALUE)

}

sealed class EUAqiRangesPM25(val range: IntRange) {

    /**0 until  20*/
    object Good: EUAqiRangesPM25(0 until  10)

    /**20 until 40*/
    object Fair: EUAqiRangesPM25(10 until 20)

    /**40 until 60*/
    object Moderate: EUAqiRangesPM25(20 until 25)

    /**80 until 100*/
    object Poor: EUAqiRangesPM25(25 until 50)

    /**80 until 100*/
    object VeryPoor: EUAqiRangesPM25(50 until 75)

    /**101 until Int.MAX_VALUE*/
    object ExtremelyPoor: EUAqiRangesPM25(75 until 800)

}

sealed class EUAqiRangesPM10(val range: IntRange) {

    /**0 until  20*/
    object Good: EUAqiRangesPM10(0 until  20)

    /**20 until 40*/
    object Fair: EUAqiRangesPM10(20 until 40)

    /**40 until 60*/
    object Moderate: EUAqiRangesPM10(40 until 50)

    /**80 until 100*/
    object Poor: EUAqiRangesPM10(50 until 100)

    /**80 until 100*/
    object VeryPoor: EUAqiRangesPM10(100 until 150)

    /**101 until Int.MAX_VALUE*/
    object ExtremelyPoor: EUAqiRangesPM10(150 until 1200)

}

sealed class EUAqiRangesNO2(val range: IntRange) {

    /**0 until  20*/
    object Good: EUAqiRangesNO2(0 until  40)

    /**20 until 40*/
    object Fair: EUAqiRangesNO2(40 until 90)

    /**40 until 60*/
    object Moderate: EUAqiRangesNO2(90 until 120)

    /**80 until 100*/
    object Poor: EUAqiRangesNO2(120 until 230)

    /**80 until 100*/
    object VeryPoor: EUAqiRangesNO2(230 until 340)

    /**101 until Int.MAX_VALUE*/
    object ExtremelyPoor: EUAqiRangesNO2(340 until 1000)

}

sealed class EUAqiRangesO3(val range: IntRange) {

    /**0 until  20*/
    object Good: EUAqiRangesO3(0 until  50)

    /**20 until 40*/
    object Fair: EUAqiRangesO3(50 until 100)

    /**40 until 60*/
    object Moderate: EUAqiRangesO3(100 until 130)

    /**80 until 100*/
    object Poor: EUAqiRangesO3(130 until 240)

    /**80 until 100*/
    object VeryPoor: EUAqiRangesO3(240 until 380)

    /**101 until Int.MAX_VALUE*/
    object ExtremelyPoor: EUAqiRangesO3(380 until 800)

}

sealed class EUAqiRangesSO2(val range: IntRange) {

    /**0 until  20*/
    object Good: EUAqiRangesSO2(0 until  100)

    /**20 until 40*/
    object Fair: EUAqiRangesSO2(100 until 200)

    /**40 until 60*/
    object Moderate: EUAqiRangesSO2(200 until 350)

    /**80 until 100*/
    object Poor: EUAqiRangesSO2(350 until 500)

    /**80 until 100*/
    object VeryPoor: EUAqiRangesSO2(500 until 750)

    /**101 until Int.MAX_VALUE*/
    object ExtremelyPoor: EUAqiRangesSO2(750 until 1520)

}