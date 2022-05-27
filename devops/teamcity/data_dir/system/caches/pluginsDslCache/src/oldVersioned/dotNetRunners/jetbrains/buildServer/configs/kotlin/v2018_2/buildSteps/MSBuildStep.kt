package jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?MSBuild) running MSBuild script
 * 
 * @see msBuild
 */
open class MSBuildStep() : BuildStep() {

    init {
        type = "MSBuild"
    }

    constructor(init: MSBuildStep.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for ant script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * A path to the solution to be built relative to the build [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory)
     */
    var path by stringParameter("build-file-path")

    /**
     * MSBuild version to use
     * @see MSBuildVersion
     */
    var version by enumParameter<MSBuildVersion>("msbuild_version", mapping = MSBuildVersion.mapping)

    /**
     * A version of tools to be used to compile (equivalent to the /toolsversion: commandline argument).
     * @see MSBuildToolsVersion
     */
    var toolsVersion by enumParameter<MSBuildToolsVersion>(mapping = MSBuildToolsVersion.mapping)

    /**
     * A required platform bitness
     * @see Platform
     */
    var platform by enumParameter<Platform>("run-platform")

    /**
     * Space- or comma-separated list of MSBuild targets to execute
     */
    var targets by stringParameter()

    /**
     * Additional command line parameters for MSBuild
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
     * A required platform bitness
     */
    enum class Platform {
        /**
         * Require a 32-bit platform
         */
        x86,
        /**
         * Require a 64-bit platform
         */
        x64;

    }
    /**
     * MSBuild version
     */
    enum class MSBuildVersion {
        /**
         * Microsoft Build Tools 2019
         */
        V16_0,
        /**
         * Microsoft Build Tools 2017
         */
        V15_0,
        /**
         * Microsoft Build Tools 2015
         */
        V14_0,
        /**
         * Microsoft Build Tools 2013
         */
        V12_0,
        /**
         * Microsoft .NET Framework 4.5
         */
        V4_5,
        /**
         * Microsoft .NET Framework 4.0
         */
        V4_0,
        /**
         * Microsoft .NET Framework 3.5
         */
        V3_5,
        /**
         * Microsoft .NET Framework 2.0
         */
        V2_0,
        /**
         * Mono xbuild 4.5
         */
        MONO_v4_5,
        /**
         * Mono xbuild 4.0
         */
        MONO_v4_0,
        /**
         * Mono xbuild 3.5
         */
        MONO_v3_5,
        /**
         * Mono xbuild 2.0
         */
        MONO_v2_0;

        companion object {
            val mapping = mapOf<MSBuildVersion, String>(V16_0 to "16.0", V15_0 to "15.0", V14_0 to "14.0", V12_0 to "12.0", V4_5 to "4.5", V4_0 to "4.0", V3_5 to "3.5", V2_0 to "2.0", MONO_v4_5 to "mono_4.5", MONO_v4_0 to "mono_4.0", MONO_v3_5 to "mono_3.5", MONO_v2_0 to "mono")
        }

    }
    /**
     * MSBuild ToolsVersion
     */
    enum class MSBuildToolsVersion {
        V16_0,
        V15_0,
        V14_0,
        V12_0,
        V4_0,
        V3_5,
        V2_0,
        NONE;

        companion object {
            val mapping = mapOf<MSBuildToolsVersion, String>(V16_0 to "Current", V15_0 to "15.0", V14_0 to "14.0", V12_0 to "12.0", V4_0 to "4.0", V3_5 to "3.5", V2_0 to "2.0", NONE to "none")
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
        if (path == null && !hasParam("build-file-path")) {
            consumer.consumePropertyError("path", "mandatory 'path' property is not specified")
        }
        coverage?.validate(consumer)
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?MSBuild) running MSBuild script
 * @see MSBuildStep
 */
fun BuildSteps.msBuild(init: MSBuildStep.() -> Unit): MSBuildStep {
    val result = MSBuildStep(init)
    step(result)
    return result
}
