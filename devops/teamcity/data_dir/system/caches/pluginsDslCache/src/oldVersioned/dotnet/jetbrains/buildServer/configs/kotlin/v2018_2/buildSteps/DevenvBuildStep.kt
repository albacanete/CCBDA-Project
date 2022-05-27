package jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * A [Visual Studio IDE step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run Visual Studio IDE command
 * 
 * @see dotnetDevenv
 */
open class DevenvBuildStep() : BuildStep() {

    init {
        type = "dotnet"
        param("command", "devenv")
    }

    constructor(init: DevenvBuildStep.() -> Unit): this() {
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
     * Visual Studio version to use
     * @see VSVersion
     */
    var version by enumParameter<VSVersion>("vs.version", mapping = VSVersion.mapping)

    /**
     * Visual Studio IDE command to execute
     * @see VSAction
     */
    var action by enumParameter<VSAction>("vs.action", mapping = VSAction.mapping)

    /**
     * Specifies the project configuration to build or deploy.
     */
    var configuration by stringParameter()

    /**
     * Enter additional command line parameters for .NET MSBuild.
     */
    var args by stringParameter()

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
     * Visual Studio version
     */
    enum class VSVersion {
        /**
         * Visual Studio 2019
         */
        Any,
        /**
         * Visual Studio 2022
         */
        V17,
        /**
         * Visual Studio 2019
         */
        V16,
        /**
         * Visual Studio 2017
         */
        V15,
        /**
         * Visual Studio 2015
         */
        V14,
        /**
         * Visual Studio 2013
         */
        V12,
        /**
         * Visual Studio 2012
         */
        V11,
        /**
         * Visual Studio 2010
         */
        V10;

        companion object {
            val mapping = mapOf<VSVersion, String>(Any to "VisualStudio_Windows", V17 to "VisualStudio_17_Windows", V16 to "VisualStudio_16_Windows", V15 to "VisualStudio_15_Windows", V14 to "VisualStudio_14_Windows", V12 to "VisualStudio_12_Windows", V11 to "VisualStudio_11_Windows", V10 to "VisualStudio_10_Windows")
        }

    }
    /**
     * MSBuild action
     */
    enum class VSAction {
        /**
         * Deletes any files created by the build command, without affecting source files
         */
        Clean,
        /**
         * Cleans and then builds the specified solution or project according to the configuration of the specified solution
         */
        Rebuild,
        /**
         * Builds the specified solution or project according to the configuration of the specified solution
         */
        Build,
        /**
         * Builds the solution, along with files necessary for deployment, according to the solution's configuration
         */
        Deploy;

        companion object {
            val mapping = mapOf<VSAction, String>(Clean to "clean", Rebuild to "rebuild", Build to "build", Deploy to "deploy")
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
        if (action == null && !hasParam("vs.action")) {
            consumer.consumePropertyError("action", "mandatory 'action' property is not specified")
        }
    }
}


/**
 * Adds a [Visual Studio IDE step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run Visual Studio IDE command
 * @see DevenvBuildStep
 */
fun BuildSteps.dotnetDevenv(init: DevenvBuildStep.() -> Unit): DevenvBuildStep {
    val result = DevenvBuildStep(init)
    step(result)
    return result
}
