package jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * A [dotnet restore step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET CLI command
 * 
 * @see dotnetRestore
 */
open class DotnetRestoreStep() : BuildStep() {

    init {
        type = "dotnet"
        param("command", "restore")
    }

    constructor(init: DotnetRestoreStep.() -> Unit): this() {
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
     * Specifies NuGet package sources to use during the restore.
     */
    var sources by stringParameter("nuget.packageSources")

    /**
     * Target runtime to restore packages.
     */
    var runtime by stringParameter()

    /**
     * The directory to restore packages.
     */
    var packagesDir by stringParameter("nuget.packagesDir")

    /**
     * The NuGet configuration file to use.
     */
    var configFile by stringParameter("nuget.configFile")

    /**
     * Enter additional command line parameters for dotnet restore.
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
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [dotnet restore step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET CLI command
 * @see DotnetRestoreStep
 */
fun BuildSteps.dotnetRestore(init: DotnetRestoreStep.() -> Unit): DotnetRestoreStep {
    val result = DotnetRestoreStep(init)
    step(result)
    return result
}
