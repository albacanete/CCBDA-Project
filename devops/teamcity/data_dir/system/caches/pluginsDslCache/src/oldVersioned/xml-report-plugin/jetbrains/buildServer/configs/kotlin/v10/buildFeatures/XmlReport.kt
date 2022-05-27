package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build feature](https://www.jetbrains.com/help/teamcity/xml-report-processing.html) parses
 * xml report produced by external tools and present them as build results
 * 
 * @see xmlReport
 */
open class XmlReport : BuildFeature {
    constructor(init: XmlReport.() -> Unit = {}, base: XmlReport? = null): super(base = base as BuildFeature?) {
        type = "xml-report-plugin"
        init()
    }

    /**
     * A report type
     * @see XmlReportType
     */
    var reportType by enumParameter<XmlReportType>("xmlReportParsing.reportType", mapping = XmlReportType.mapping)

    /**
     * Newline- or comma-separated set of rules specifying where to find reports in the form of +|-:path.
     * Ant-style wildcards are supported.
     */
    var rules by stringParameter("xmlReportParsing.reportDirs")

    /**
     * Enables verbose output to the build log
     */
    var verbose by booleanParameter("xmlReportParsing.verboseOutput", trueValue = "true", falseValue = "")

    /**
     * A path to FindBugs installation on the agent if the XmlReportType.FINDBUGS reportType
     */
    var findBugsHome by stringParameter("xmlReportParsing.findBugs.home")

    /**
     * Xml report type
     */
    enum class XmlReportType {
        JUNIT,
        TESTNG,
        CTEST,
        CHECKSTYLE,
        FINDBUGS,
        GOOGLE_TEST,
        JSLINT,
        MSTEST,
        NUNIT,
        PMD,
        PMD_CPD,
        SUREFIRE,
        TRX,
        VSTEST;

        companion object {
            val mapping = mapOf<XmlReportType, String>(JUNIT to "junit", TESTNG to "testng", CTEST to "ctest", CHECKSTYLE to "checkstyle", FINDBUGS to "findBugs", GOOGLE_TEST to "gtest", JSLINT to "jslint", MSTEST to "mstest", NUNIT to "nunit", PMD to "pmd", PMD_CPD to "pmdCpd", SUREFIRE to "surefire", TRX to "trx", VSTEST to "vstest")
        }

    }
}


/**
 * Adds a [build feature](https://www.jetbrains.com/help/teamcity/xml-report-processing.html) parses
 * xml report produced by external tools and present them as build results
 * @see XmlReport
 */
fun BuildFeatures.xmlReport(base: XmlReport? = null, init: XmlReport.() -> Unit = {}) {
    feature(XmlReport(init, base))
}
