package jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [.NET vstest step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET VSTest
 * 
 * @see dotnetVsTest
 */
open class DotnetVsTestStep() : BuildStep() {

    init {
        type = "dotnet"
        param("command", "vstest")
    }

    constructor(init: DotnetVsTestStep.() -> Unit): this() {
        init()
    }

    /**
     * Specify paths to test assemblies. Wildcards are supported.
     */
    var assemblies by stringParameter("paths")

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for
     * script,
     * specify it if it is different from the [checkout
     * directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * VSTest version to use
     * @see VSTestVersion
     */
    var version by enumParameter<VSTestVersion>("vstest.version", mapping = VSTestVersion.mapping)

    /**
     * The filter type used while test run
     */
    var filter by compoundParameter<Filter>("test.filter")

    sealed class Filter(value: String? = null): CompoundParam<Filter>(value) {
        class TestName() : Filter("name") {

            /**
             * Specify the list of test names to run.
             */
            var names by stringParameter("test.names")

        }

        class TestCaseFilter() : Filter("filter") {

            /**
             * Run tests that match the given expression.
             */
            var filter by stringParameter("test.testCaseFilter")

        }
    }

    fun testName(init: Filter.TestName.() -> Unit = {}) : Filter.TestName {
        val result = Filter.TestName()
        result.init()
        return result
    }

    fun testCaseFilter(init: Filter.TestCaseFilter.() -> Unit = {}) : Filter.TestCaseFilter {
        val result = Filter.TestCaseFilter()
        result.init()
        return result
    }

    /**
     * The target .NET Framework version to be used for test execution.
     */
    var framework by stringParameter()

    /**
     * The path to the run settings configuration file.
     */
    var settingsFile by stringParameter("test.settingsFile")

    /**
     * Enter additional command line parameters for dotnet vstest.
     */
    var args by stringParameter()

    /**
     * Specify logging verbosity
     * @see Verbosity
     */
    var logging by enumParameter<Verbosity>("verbosity")

    /**
     * Whether TeamCity should run tests in an isolated process
     */
    var runInIsolation by booleanParameter("vstest.InIsolation", trueValue = "true", falseValue = "")

    /**
     * Whether TeamCity should run tests in a single session
     */
    var singleSession by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Target platform architecture to be used for test execution
     * @see Platform
     */
    var platform by enumParameter<Platform>(mapping = Platform.mapping)

    /**
     * .NET SDK versions separated by semicolon to be required on agents.
     */
    var sdk by stringParameter("required.sdk")

    /**
     * Specifies which Docker image platform will be used to run this build step.
     */
    var dockerImagePlatform by enumParameter<ImagePlatform>("plugin.docker.imagePlatform", mapping = ImagePlatform.mapping)

    /**
     * If enabled, "docker pull [image][dockerImage]" will be run before docker run.
     */
    var dockerPull by booleanParameter("plugin.docker.pull.enabled", trueValue = "true", falseValue = "")

    /**
     * Specifies which Docker image to use for running this build step. I.e. the build step will be run inside specified docker image, using 'docker run' wrapper.
     */
    var dockerImage by stringParameter("plugin.docker.imageId")

    /**
     * Additional docker run command arguments
     */
    var dockerRunParameters by stringParameter("plugin.docker.run.parameters")

    /**
     * Specifies coverage tool to use
     */
    var coverage by compoundParameter<Coverage>("dotNetCoverage.tool")

    sealed class Coverage(value: String? = null): CompoundParam<Coverage>(value) {
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
             * Enter additional new-line separated command line parameters for dotCover.
             */
            var args by stringParameter("dotNetCoverage.dotCover.customCmd")

        }
    }

    fun dotcover(init: Coverage.Dotcover.() -> Unit = {}) : Coverage.Dotcover {
        val result = Coverage.Dotcover()
        result.init()
        return result
    }

    /**
     * Platform bitness
     */
    enum class Platform {
        Auto,
        x86,
        x64,
        ARM;

        companion object {
            val mapping = mapOf<Platform, String>(Auto to "auto", x86 to "x86", x64 to "x64", ARM to "ARM")
        }

    }
    /**
     * Logging verbosity
     */
    enum class Verbosity {
        Quiet,
        Minimal,
        Normal,
        Detailed,
        Diagnostic;

    }
    /**
     * MSBuild version
     */
    enum class VSTestVersion {
        /**
         * Cross-platform MSBuild
         */
        CrossPlatform,
        /**
         * VSTest 2022
         */
        V17,
        /**
         * VSTest 2019
         */
        V16,
        /**
         * VSTest 2017
         */
        V15,
        /**
         * VSTest 2015
         */
        V14,
        /**
         * VSTest 2013
         */
        V12;

        companion object {
            val mapping = mapOf<VSTestVersion, String>(CrossPlatform to "VSTest_CrossPlatform", V17 to "VSTest_17_Windows", V16 to "VSTest_16_Windows", V15 to "VSTest_15_Windows", V14 to "VSTest_14_Windows", V12 to "VSTest_12_Windows")
        }

    }
    /**
     * Docker image platforms
     */
    enum class ImagePlatform {
        Any,
        Linux,
        Windows;

        companion object {
            val mapping = mapOf<ImagePlatform, String>(Any to "", Linux to "linux", Windows to "windows")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [.NET vstest step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET VSTest
 * @see DotnetVsTestStep
 */
fun BuildSteps.dotnetVsTest(init: DotnetVsTestStep.() -> Unit): DotnetVsTestStep {
    val result = DotnetVsTestStep(init)
    step(result)
    return result
}
