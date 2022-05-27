package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [dotnet publish step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET CLI command
 * 
 * @see dotnetPublish
 */
open class DotnetPublishStep : BuildStep {
    constructor(init: DotnetPublishStep.() -> Unit = {}, base: DotnetPublishStep? = null): super(base = base as BuildStep?) {
        type = "dotnet"
        param("command", "publish")
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
     * Target framework to publish for.
     */
    var framework by stringParameter()

    /**
     * Target configuration to publish for.
     */
    var configuration by stringParameter()

    /**
     * Target runtime to publish for.
     */
    var runtime by stringParameter()

    /**
     * The directory where to publish the app.
     */
    var outputDir by stringParameter()

    /**
     * Do not build the project before testing
     */
    var skipBuild by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Defines the value for the $(VersionSuffix) property in the project.
     */
    var versionSuffix by stringParameter()

    /**
     * Enter additional command line parameters for dotnet publish.
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
}


/**
 * Adds a [dotnet publish step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET CLI command
 * @see DotnetPublishStep
 */
fun BuildSteps.dotnetPublish(base: DotnetPublishStep? = null, init: DotnetPublishStep.() -> Unit = {}) {
    step(DotnetPublishStep(init, base))
}
