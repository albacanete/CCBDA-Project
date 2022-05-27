package jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Visual+Studio+Tests) running Visual Studio Tests
 * 
 * @see vstest
 */
open class VSTestStep() : BuildStep() {

    init {
        type = "VisualStudioTest"
        param("mstest_enable", "checked")
        param("dotNetTestRunner.Type", "GenericProcess")
        param("vstest_engine", "VSTest")
    }

    constructor(init: VSTestStep.() -> Unit): this() {
        init()
    }

    /**
     * A path to test engine. TeamCity detects test engine installation on the agent,
     * to run the detected engine use the following paths:
     * * VSTest 2019 - "%teamcity.dotnet.vstest.16.0%"
     * * VSTest 2017 - "%teamcity.dotnet.vstest.15.0%"
     * * VSTest 2015 - "%teamcity.dotnet.vstest.14.0%"
     * * VSTest 2013 - "%teamcity.dotnet.vstest.12.0%"
     * * VSTest 2012 - "%teamcity.dotnet.vstest.11.0%"
     * Or specify a custom path to the test engine on a build agent machine.
     */
    var vstestPath by stringParameter("vstest_runner_path")

    /**
     * Newline-separated list of assemblies to be included in test run.
     * [Wildcards](https://www.jetbrains.com/help/teamcity/?Wildcards) are supported.
     */
    var includeTestFileNames by stringParameter("vstest_include")

    /**
     * Newline-separated list of assemblies to be excluded from test run.
     * [Wildcards](https://www.jetbrains.com/help/teamcity/?Wildcards) are supported.
     */
    var excludeTestFileNames by stringParameter("vstest_exclude")

    /**
     * A path to run settings configuration file
     */
    var runSettings by stringParameter("vstest_runsettings")

    var platform by enumParameter<Platform>("vstest_platform")

    /**
     * Target .NET Framework version to be used for test execution
     * @see TargetFramework
     */
    var targetFramework by enumParameter<TargetFramework>("vstest_framework", mapping = TargetFramework.mapping)

    /**
     * Newline-separated list of test names. If empty, all tests will be executed.
     * Cannot be used together with testCaseFilter.
     * @see testCaseFilter
     */
    var testNames by stringParameter("vstest_test_names")

    /**
     * A regular expression selecting tests to run.
     * Cannot be used together with testNames.
     * @see testNames
     */
    var testCaseFilter by stringParameter("vstest_test_case_filter")

    /**
     * Whether TeamCity should run tests in an isolated process
     */
    var runInIsolation by booleanParameter("vstest_in_isolation", trueValue = "true", falseValue = "")

    /**
     * Whether TeamCity should use a [custom test logger](https://www.jetbrains.com/help/teamcity/?Visual+Studio+Tests#VisualStudioTests-Customtestlogger)
     * for real-time reporting.
     */
    var realTimeTestReporting by booleanParameter("vstest_detect_team_city_logger", trueValue = "true", falseValue = "")

    /**
     * Additional parameters to add to the command line for VSTest
     */
    var args by stringParameter("vstest_cmdline")

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
     * Target .NET Framework
     */
    enum class TargetFramework {
        /**
         * Vstest.console will select the target framework automatically
         */
        DEFAULT,
        Framework45,
        Framework40,
        Framework35;

        companion object {
            val mapping = mapOf<TargetFramework, String>(DEFAULT to "default", Framework45 to "Framework45", Framework40 to "Framework40", Framework35 to "Framework35")
        }

    }
    /**
     * Platform bitness
     */
    enum class Platform {
        x86,
        x64;

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
        if (vstestPath == null && !hasParam("vstest_runner_path")) {
            consumer.consumePropertyError("vstestPath", "mandatory 'vstestPath' property is not specified")
        }
        if (includeTestFileNames == null && !hasParam("vstest_include")) {
            consumer.consumePropertyError("includeTestFileNames", "mandatory 'includeTestFileNames' property is not specified")
        }
        coverage?.validate(consumer)
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Visual+Studio+Tests) running Visual Studio Tests
 * @see VisualStudioTest
 */
fun BuildSteps.vstest(init: VSTestStep.() -> Unit): VSTestStep {
    val result = VSTestStep(init)
    step(result)
    return result
}
