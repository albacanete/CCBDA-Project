package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?NAnt) running NAnt scripts
 * 
 * @see nant
 */
open class NAntStep : BuildStep {
    constructor(init: NAntStep.() -> Unit = {}, base: NAntStep? = null): super(base = base as BuildStep?) {
        type = "NAnt"
        init()
    }

    /**
     * Specifies what Nant script will be executed
     */
    var mode by compoundParameter<Mode>("use-custom-build-file")

    sealed class Mode(value: String? = null): CompoundParam(value) {
        class NantFile() : Mode("") {

            /**
             * A path to NAnt script
             */
            var path by stringParameter("build-file-path")

        }

        class NantScript() : Mode("true") {

            /**
             * NAnt script content
             */
            var content by stringParameter("build-file")

        }
    }

    /**
     * Executes the NAnt script at the given path
     */
    fun nantFile(init: Mode.NantFile.() -> Unit = {}) : Mode.NantFile {
        val result = Mode.NantFile()
        result.init()
        return result
    }

    /**
     * Executes the NAnt script with the given content
     */
    fun nantScript(init: Mode.NantScript.() -> Unit = {}) : Mode.NantScript {
        val result = Mode.NantScript()
        result.init()
        return result
    }

    /**
     * A path to NAnt home directory
     */
    var nantHome by stringParameter("NAntHome")

    /**
     * Space-separated list of NAnt targets to execute
     */
    var targets by stringParameter("target-list")

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for ant script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * A required target framework (a shortcut for -t: option of NAnt.exe).
     * @see TargetFramework
     */
    var targetFramework by enumParameter<TargetFramework>("targetframework", mapping = TargetFramework.mapping)

    /**
     * Space-separated list of additional arguments for NAnt.exe.
     */
    var args by stringParameter("runnerArgs")

    /**
     * Whether TeamCity should run recently failed tests first to reduce test feedback
     */
    var reduceTestFeedback by booleanParameter("teamcity.tests.runRiskGroupTestsFirst", trueValue = "recentlyFailed", falseValue = "")

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

    /**
     * Target Framework version
     */
    enum class TargetFramework {
        UNSPECIFIED,
        NET_1_0,
        NET_1_1,
        NET_2_0,
        NET_3_5,
        NET_4_0,
        MONO_1_0,
        MONO_2_0,
        MONO_3_5,
        NETCF_1_0,
        NETCF_2_0,
        SSCLI_1_0,
        SILVERLIGHT_2_0,
        MOONLIGHT_2_0;

        companion object {
            val mapping = mapOf<TargetFramework, String>(UNSPECIFIED to "unspecified", NET_1_0 to "net-1.0", NET_1_1 to "net-1.1", NET_2_0 to "net-2.0", NET_3_5 to "net-3.5", NET_4_0 to "net-4.0", MONO_1_0 to "mono-1.0", MONO_2_0 to "mono-2.0", MONO_3_5 to "mono-3.5", NETCF_1_0 to "netcf-1.0", NETCF_2_0 to "netcf-2.0", SSCLI_1_0 to "sscli-1.0", SILVERLIGHT_2_0 to "silverlight-2.0", MOONLIGHT_2_0 to "moonlight-2.0")
        }

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
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?NAnt) running NAnt scripts
 * @see NAntStep
 */
fun BuildSteps.nant(base: NAntStep? = null, init: NAntStep.() -> Unit = {}) {
    step(NAntStep(init, base))
}
