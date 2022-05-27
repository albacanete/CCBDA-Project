package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [dotnet nuget push step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET CLI command
 * 
 * @see dotnetNugetPush
 */
open class DotnetNugetPushStep : BuildStep {
    constructor(init: DotnetNugetPushStep.() -> Unit = {}, base: DotnetNugetPushStep? = null): super(base = base as BuildStep?) {
        type = "dotnet"
        param("command", "nuget-push")
        init()
    }

    /**
     * Specify paths to nuget packages. Wildcards are supported.
     */
    var packages by stringParameter("paths")

    /**
     * Specify the server URL. To use a TeamCity NuGet feed, specify the URL from the NuGet feed project settings page.
     */
    var serverUrl by stringParameter("nuget.packageSource")

    /**
     * Specify the API key to access the NuGet packages feed.
     * For the built-in TeamCity NuGet server use %teamcity.nuget.feed.api.key%.
     */
    var apiKey by stringParameter("secure:nuget.apiKey")

    /**
     * Do not publish nuget symbol packages
     */
    var noSymbols by booleanParameter("nuget.noSymbols", trueValue = "true", falseValue = "")

    /**
     * Enter additional command line parameters for dotnet nuget push.
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
 * Adds a [dotnet nuget push step](https://github.com/JetBrains/teamcity-dotnet-plugin) to run .NET CLI command
 * @see DotnetNugetPushStep
 */
fun BuildSteps.dotnetNugetPush(base: DotnetNugetPushStep? = null, init: DotnetNugetPushStep.() -> Unit = {}) {
    step(DotnetNugetPushStep(init, base))
}
