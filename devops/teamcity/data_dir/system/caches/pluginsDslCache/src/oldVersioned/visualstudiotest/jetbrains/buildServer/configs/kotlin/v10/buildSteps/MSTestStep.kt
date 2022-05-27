package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Visual+Studio+Tests) running MSTest tests
 * 
 * @see mstest
 */
open class MSTestStep : BuildStep {
    constructor(init: MSTestStep.() -> Unit = {}, base: MSTestStep? = null): super(base = base as BuildStep?) {
        type = "VisualStudioTest"
        param("mstest_enable", "checked")
        param("dotNetTestRunner.Type", "GenericProcess")
        param("vstest_engine", "MSTest")
        init()
    }

    /**
     * A path to test engine. TeamCity detects test engine installation on the agent,
     * to run the detected engine use the following paths:
     * * MSTest 2019 - "%teamcity.dotnet.mstest.16.0%"
     * * MSTest 2017 - "%teamcity.dotnet.mstest.15.0%"
     * * MSTest 2015 - "%teamcity.dotnet.mstest.14.0%"
     * * MSTest 2013 - "%teamcity.dotnet.mstest.12.0%"
     * * MSTest 2012 - "%teamcity.dotnet.mstest.11.0%"
     * * MSTest 2010 - "%teamcity.dotnet.mstest.10.0%"
     * * MSTest 2008 - "%teamcity.dotnet.mstest.9.0%"
     * * MSTest 2005 - "%teamcity.dotnet.mstest.8.0%"
     * Or specify a custom path to the test engine on a build agent machine.
     */
    var mstestPath by stringParameter("vstest_runner_path")

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

    /**
     * A value for the /testmetadata: argument
     */
    var metadata by stringParameter("mstest_metadata")

    /**
     * Newline-separated list of testlist names from metadata to run.
     * Every line will be translated into the [/testlist:](https://msdn.microsoft.com/en-us/library/ms182489(VS.80).aspx#testlist) argument.
     */
    var mstest_testlist by stringParameter()

    /**
     * Newline-separated list of individual tests to run.
     * Each test nae will be translated into the [/test:](https://msdn.microsoft.com/en-us/library/ms182489(VS.80).aspx#test) argument.
     */
    var testsToRun by stringParameter("mstest_test")

    /**
     * When set to true, TeamCity will run the test only if a unique match is found for any specified test name in the testsToRun property
     * @see testsToRun
     */
    var runIfUniqueMatchFound by booleanParameter("mstest_unique", trueValue = "true", falseValue = "")

    /**
     * A value for the [/resultsfile:](https://msdn.microsoft.com/en-us/library/ms182489(VS.80).aspx#resultsfile) command line argument
     */
    var resultsFile by stringParameter("mstest_result")

    /**
     * Additional parameters to add to the command line for MSTest
     */
    var args by stringParameter("vstest_cmdline")

    /**
     * Specifies coverage tool to use
     */
    var coverage by compoundParameter<Coverage>("dotNetCoverage.tool")

    sealed class Coverage(value: String? = null): CompoundParam(value) {
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
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Visual+Studio+Tests) running MSTest tests
 */
fun BuildSteps.mstest(base: MSTestStep? = null, init: MSTestStep.() -> Unit = {}) {
    step(MSTestStep(init, base))
}
