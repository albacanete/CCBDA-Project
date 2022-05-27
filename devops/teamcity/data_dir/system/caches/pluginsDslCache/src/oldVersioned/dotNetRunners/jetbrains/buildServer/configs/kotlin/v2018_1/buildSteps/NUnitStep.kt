package jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?NUnit) running NUnit tests
 * 
 * @see nunit
 */
open class NUnitStep() : BuildStep() {

    init {
        type = "NUnit"
        param("nunit_enabled", "checked")
        param("dotNetTestRunner.Type", "NUnit")
    }

    constructor(init: NUnitStep.() -> Unit): this() {
        init()
    }

    /**
     * NUnit version to use
     * @see NUnitVersion
     */
    var nunitVersion by enumParameter<NUnitVersion>("nunit_version", mapping = NUnitVersion.mapping)

    /**
     * A path to NUnit console tool including the file name
     */
    var nunitPath by stringParameter("nunit_path")

    /**
     * A desired execution mode on a x64 machine
     * @see Platform
     */
    var platform by enumParameter<Platform>("nunit_platform")

    /**
     * A desired .NET Framework version
     * @see RuntimeVersion
     */
    var runtimeVersion by enumParameter<RuntimeVersion>("nunit_environment", mapping = RuntimeVersion.mapping)

    /**
     * Comma- or newline-separated list of .NET assemblies where the NUnit tests are specified
     * relative to the checkout directory. Wildcards are supported.
     */
    var includeTests by stringParameter("nunit_include")

    /**
     * Comma- or newline-separated list of .NET assemblies which should be excluded
     * from the list of found assemblies to test.
     */
    var excludeTests by stringParameter("nunit_exclude")

    /**
     * Comma- or newline-separated list of NUnit categories.
     * [Category expressions](https://www.jetbrains.com/help/teamcity/?TeamCity+NUnit+Test+Launcher#TeamCityNUnitTestLauncher-CategoryExpression) are supported as well.
     */
    var includeCategories by stringParameter("nunit_categery_include")

    /**
     * Comma- or newline-separated list of NUnit categories which should be excluded.
     * [Category expressions](https://www.jetbrains.com/help/teamcity/?TeamCity+NUnit+Test+Launcher#TeamCityNUnitTestLauncher-CategoryExpression) are supported as well.
     */
    var excludeCategories by stringParameter("nunit_categery_exclude")

    /**
     * Whether TeamCity should run recently failed tests first to reduce test feedback
     */
    var reduceTestFeedback by booleanParameter("teamcity.tests.runRiskGroupTestsFirst", trueValue = "recentlyFailed", falseValue = "")

    /**
     * Whether TeamCity should run each assembly in a separate process. Works for NUnit versions prior to 3.0
     */
    var runProcessPerAssembly by booleanParameter("nunit_process_per_process", trueValue = "true", falseValue = "")

    /**
     * Specifies coverage tool to use
     */
    var coverage by compoundParameter<Coverage>("dotNetCoverage.tool")

    sealed class Coverage(value: String? = null): CompoundParam<Coverage>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Dotcover() : Coverage("dotcover") {

            /**
             * Specify the path to dotCover CLT.
             */
            var toolPath by stringParameter("dotNetCoverage.dotCover.home.path")

            /**
             * Specify a new-line separated list of filters for code coverage.
             */
            var assemblyFilters by stringParameter("dotNetCoverage.dotCover.filters")

            /**
             * Specify a new-line separated list of attribute filters for code coverage.
             * Supported only with dotCover 2.0 or later.
             */
            var attributeFilters by stringParameter("dotNetCoverage.dotCover.attributeFilters")

            /**
             * Enter additional new-line sepatated command line parameters for dotCover.
             */
            var args by stringParameter("dotNetCoverage.dotCover.customCmd")

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class Partcover() : Coverage("partcover") {

            /**
             * Path to coverage tool
             */
            var toolPath by stringParameter("dotNetCoverage.PartCover.executable")

            /**
             * Write additional coverage tool specific arguments.
             */
            var args by stringParameter("dotNetCoverage.PartCover.arguments")

            /**
             * Specify the .NET runtime version.
             */
            var platformVersion by enumParameter<PlatformVersion>("dotNetCoverage.PartCover.platformVersion", mapping = PlatformVersion.mapping)

            /**
             * Specify the .NET runtime bitness.
             */
            var platformBitness by enumParameter<PlatformBitness>("dotNetCoverage.PartCover.platformBitness", mapping = PlatformBitness.mapping)

            /**
             * PartCover include patterns.
             */
            var includePatterns by stringParameter("dotNetCoverage.PartCover.includes")

            /**
             * PartCover exclude patterns.
             */
            var excludePatterns by stringParameter("dotNetCoverage.PartCover.excludes")

            /**
             * Write xslt transformation rules one per line in format: file.xslt=>generatedFileName.html,
             * where file.xslt path should be relative to checkout directory. Note that default xslt files
             * bundled with PartCover 2.3 are broken and you need to write your own xslt files to be able to generate reports.
             */
            var xsltTransformations by stringParameter("dotNetCoverage.partCover.xslts")

            /**
             * Specify the assemblies registration.
             */
            var registration by enumParameter<Registration>("dotNetCoverage.PartCover.Reg")

            override fun validate(consumer: ErrorConsumer) {
                if (toolPath == null && !hasParam("dotNetCoverage.PartCover.executable")) {
                    consumer.consumePropertyError("coverage.toolPath", "mandatory 'coverage.toolPath' property is not specified")
                }
            }
        }

        class Ncover() : Coverage("ncover") {

            /**
             * Path to coverage tool
             */
            var toolPath by stringParameter("dotNetCoverage.NCover.executable")

            /**
             * Write additional coverage tool specific arguments.
             */
            var args by stringParameter("dotNetCoverage.NCover.arguments")

            /**
             * Path to explorer coverage tool
             */
            var explorerToolPath by stringParameter("dotNetCoverage.NCover.explorer.executable")

            /**
             * Write additional explorer coverage tool specific arguments.
             */
            var explorerArgs by stringParameter("dotNetCoverage.NCover.explorer.args")

            /**
             * Specify the .NET runtime version.
             */
            var platformVersion by enumParameter<PlatformVersion>("dotNetCoverage.NCover.platformVersion", mapping = PlatformVersion.mapping)

            /**
             * Specify the .NET runtime bitness.
             */
            var platformBitness by enumParameter<PlatformBitness>("dotNetCoverage.NCover.platformBitness", mapping = PlatformBitness.mapping)

            /**
             * Write assembly names separated with new line. Leave blank for all assembly names
             * (without paths and ".dll" or ".exe" extensions) to profile. Equivalent to //a NCover.Console option.
             */
            var assemblies by stringParameter("dotNetCoverage.NCover.Assemblies")

            /**
             * List of attributes making classes or methods to be excluded from coverage.
             * Equivalent to //ea NCover.Console option.
             */
            var excludeAttributes by stringParameter("dotNetCoverage.NCover.ExcludeAttributes")

            var reportType by enumParameter<ReportType>("dotNetCoverage.NCover.HTMLReport.File.Type", mapping = ReportType.mapping)

            var reportOrder by enumParameter<ReportOrder>("dotNetCoverage.NCover.HTMLReport.File.Sort", mapping = ReportOrder.mapping)

            override fun validate(consumer: ErrorConsumer) {
                if (toolPath == null && !hasParam("dotNetCoverage.NCover.executable")) {
                    consumer.consumePropertyError("coverage.toolPath", "mandatory 'coverage.toolPath' property is not specified")
                }
                if (explorerToolPath == null && !hasParam("dotNetCoverage.NCover.explorer.executable")) {
                    consumer.consumePropertyError("coverage.explorerToolPath", "mandatory 'coverage.explorerToolPath' property is not specified")
                }
            }
        }

        class Ncover3() : Coverage("ncover3") {

            /**
             * Path to coverage tool
             */
            var toolPath by stringParameter("dotNetCoverage.NCover3.tool")

            /**
             * Write additional coverage tool specific arguments.
             */
            var args by stringParameter("dotNetCoverage.NCover3.args")

            /**
             * Write additional NCover.Reporting tool arguments. Use {teamcity.report.path} as path to report folder in the reporting commandline arguments.
             * Try "//or FullCoverageReport:Html:{teamcity.report.path}" to create full coverage report.
             */
            var reportArgs by stringParameter("dotNetCoverage.NCover3.reporter.executable.args")

            /**
             * Specify the .NET runtime version.
             */
            var platformVersion by enumParameter<PlatformVersion>("dotNetCoverage.NCover3.platformVersion", mapping = PlatformVersion.mapping)

            /**
             * Specify the .NET runtime bitness.
             */
            var platformBitness by enumParameter<PlatformBitness>("dotNetCoverage.NCover3.platformBitness", mapping = PlatformBitness.mapping)

            /**
             * Write the name of the index file (i.e. fullcoveragereport.html) in generated HTML report
             */
            var reportFile by stringParameter("dotNetCoverage.index")

            override fun validate(consumer: ErrorConsumer) {
                if (toolPath == null && !hasParam("dotNetCoverage.NCover3.tool")) {
                    consumer.consumePropertyError("coverage.toolPath", "mandatory 'coverage.toolPath' property is not specified")
                }
            }
        }
    }

    fun dotcover(init: Coverage.Dotcover.() -> Unit = {}) : Coverage.Dotcover {
        val result = Coverage.Dotcover()
        result.init()
        return result
    }

    fun partcover(init: Coverage.Partcover.() -> Unit = {}) : Coverage.Partcover {
        val result = Coverage.Partcover()
        result.init()
        return result
    }

    fun ncover(init: Coverage.Ncover.() -> Unit = {}) : Coverage.Ncover {
        val result = Coverage.Ncover()
        result.init()
        return result
    }

    fun ncover3(init: Coverage.Ncover3.() -> Unit = {}) : Coverage.Ncover3 {
        val result = Coverage.Ncover3()
        result.init()
        return result
    }

    /**
     * NUnit version
     */
    enum class NUnitVersion {
        NUnit_3,
        NUnit_2_6_4,
        NUnit_2_6_3,
        NUnit_2_6_2,
        NUnit_2_6_1,
        NUnit_2_6_0,
        NUnit_2_5_10,
        NUnit_2_5_9,
        NUnit_2_5_8,
        NUnit_2_5_7,
        NUnit_2_5_6,
        NUnit_2_5_5,
        NUnit_2_5_4,
        NUnit_2_5_3,
        NUnit_2_5_2,
        NUnit_2_5_0,
        NUnit_2_4_8,
        NUnit_2_4_7,
        NUnit_2_4_6,
        NUnit_2_4_1,
        NUnit_2_2_10;

        companion object {
            val mapping = mapOf<NUnitVersion, String>(NUnit_3 to "NUnit-3.0.0", NUnit_2_6_4 to "NUnit-2.6.4", NUnit_2_6_3 to "NUnit-2.6.3", NUnit_2_6_2 to "NUnit-2.6.2", NUnit_2_6_1 to "NUnit-2.6.1", NUnit_2_6_0 to "NUnit-2.6.0", NUnit_2_5_10 to "NUnit-2.5.10", NUnit_2_5_9 to "NUnit-2.5.9", NUnit_2_5_8 to "NUnit-2.5.8", NUnit_2_5_7 to "NUnit-2.5.7", NUnit_2_5_6 to "NUnit-2.5.6", NUnit_2_5_5 to "NUnit-2.5.5", NUnit_2_5_4 to "NUnit-2.5.4", NUnit_2_5_3 to "NUnit-2.5.3", NUnit_2_5_2 to "NUnit-2.5.2", NUnit_2_5_0 to "NUnit-2.5.0", NUnit_2_4_8 to "NUnit-2.4.8", NUnit_2_4_7 to "NUnit-2.4.7", NUnit_2_4_6 to "NUnit-2.4.6", NUnit_2_4_1 to "NUnit-2.4.1", NUnit_2_2_10 to "NUnit-2.2.10")
        }

    }
    enum class Platform {
        MSIL,
        x86,
        x64;

    }
    enum class RuntimeVersion {
        AUTO,
        v2_0,
        v4_0;

        companion object {
            val mapping = mapOf<RuntimeVersion, String>(AUTO to "auto", v2_0 to "v2.0", v4_0 to "v4.0")
        }

    }
    enum class PlatformVersion {
        v2_0,
        v4_0,
        auto;

        companion object {
            val mapping = mapOf<PlatformVersion, String>(v2_0 to "v2.0", v4_0 to "v4.0", auto to "NATIVE")
        }

    }
    enum class PlatformBitness {
        x86,
        x64,
        auto;

        companion object {
            val mapping = mapOf<PlatformBitness, String>(x86 to "x86", x64 to "x64", auto to "NATIVE")
        }

    }
    enum class Registration {
        selected;

    }
    enum class ReportType {
        moduleSummary,
        namespaceSummary,
        moduleNamespaceSummary,
        moduleClassSummary,
        moduleClassFunctionSummary;

        companion object {
            val mapping = mapOf<ReportType, String>(moduleSummary to "1", namespaceSummary to "2", moduleNamespaceSummary to "3", moduleClassSummary to "4", moduleClassFunctionSummary to "5")
        }

    }
    enum class ReportOrder {
        itemName,
        classLine,
        coveragePercentageAscending,
        coveragePercentageDescending,
        unvisitedSequencePointsAscending,
        unvisitedSequencePointsDescending,
        visitCountAscending,
        visitCountDescending,
        functionCoverageAscending,
        functionCoverageDescending;

        companion object {
            val mapping = mapOf<ReportOrder, String>(itemName to "0", classLine to "1", coveragePercentageAscending to "2", coveragePercentageDescending to "3", unvisitedSequencePointsAscending to "4", unvisitedSequencePointsDescending to "5", visitCountAscending to "6", visitCountDescending to "7", functionCoverageAscending to "8", functionCoverageDescending to "9")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (includeTests == null && !hasParam("nunit_include")) {
            consumer.consumePropertyError("includeTests", "mandatory 'includeTests' property is not specified")
        }
        coverage?.validate(consumer)
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?NUnit) running NUnit tests
 * @see NUnitStep
 */
fun BuildSteps.nunit(init: NUnitStep.() -> Unit): NUnitStep {
    val result = NUnitStep(init)
    step(result)
    return result
}
