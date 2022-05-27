package jetbrains.buildServer.configs.kotlin.buildSteps

import jetbrains.buildServer.configs.kotlin.*

/**
 * A [.NET msbuild step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET MSBuild
 * 
 * @see dotnetMsBuild
 */
open class DotnetMsBuildStep() : BuildStep() {

    init {
        type = "dotnet"
        param("command", "msbuild")
    }

    constructor(init: DotnetMsBuildStep.() -> Unit): this() {
        init()
    }

    /**
     * Specify paths to projects and solutions. Wildcards are supported.
     */
    var projects by stringParameter("paths")

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for
     * script,
     * specify it if it is different from the [checkout
     * directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * MSBuild version to use
     * @see MSBuildVersion
     */
    var version by enumParameter<MSBuildVersion>("msbuild.version", mapping = MSBuildVersion.mapping)

    /**
     * Specify the list of build targets.
     */
    var targets by stringParameter()

    /**
     * Target configuration to build for.
     */
    var configuration by stringParameter()

    /**
     * Target runtime to build for.
     */
    var runtime by stringParameter()

    /**
     * Enter additional command line parameters for .NET MSBuild.
     */
    var args by stringParameter()

    /**
     * Specify logging verbosity
     * @see Verbosity
     */
    var logging by enumParameter<Verbosity>("verbosity")

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
     * MSBuild version
     */
    enum class MSBuildVersion {
        /**
         * Cross-platform MSBuild
         */
        CrossPlatform,
        /**
         * MSBuild 2022
         */
        V17,
        /**
         * MSBuild 2019
         */
        V16,
        /**
         * MSBuild 2017
         */
        V15,
        /**
         * MSBuild 2015
         */
        V14,
        /**
         * MSBuild 2013
         */
        V12,
        /**
         * MSBuild 4
         */
        V4;

        companion object {
            val mapping = mapOf<MSBuildVersion, String>(CrossPlatform to "MSBuild_CrossPlatform", V17 to "MSBuild_17_Windows", V16 to "MSBuild_16_Windows", V15 to "MSBuild_15_Windows", V14 to "MSBuild_14_Windows", V12 to "MSBuild_12_Windows", V4 to "MSBuild_4_Windows")
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
 * Adds a [.NET msbuild step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET MSBuild
 * @see DotnetMsBuildStep
 */
fun BuildSteps.dotnetMsBuild(init: DotnetMsBuildStep.() -> Unit): DotnetMsBuildStep {
    val result = DotnetMsBuildStep(init)
    step(result)
    return result
}
