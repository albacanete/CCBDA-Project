package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [NuGet publish step](https://confluence.jetbrains.com/display/TCDL/NuGet+Publish) to run nuget push command
 * 
 * @see nuGetPublish
 */
open class NuGetPublishStep : BuildStep {
    constructor(init: NuGetPublishStep.() -> Unit = {}, base: NuGetPublishStep? = null): super(base = base as BuildStep?) {
        type = "jb.nuget.publish"
        init()
    }

    /**
     * Specify path to NuGet.exe.
     */
    var toolPath by stringParameter("nuget.path")

    /**
     * A newline-separated list of NuGet package files (.nupkg) to push to the NuGet feed.
     */
    var packages by stringParameter("nuget.publish.files")

    /**
     * Specify the NuGet packages feed URL to push packages to.
     */
    var serverUrl by stringParameter("nuget.publish.source")

    /**
     * Specify the API key to access a NuGet packages feed.
     */
    var apiKey by stringParameter("secure:nuget.api.key")

    /**
     * Enter additional parameters to use when calling nuget push command.
     */
    var args by stringParameter("nuget.push.commandline")

}


/**
 * Adds a [NuGet publish step](https://confluence.jetbrains.com/display/TCDL/NuGet+Publish) to run nuget push command
 * @see NuGetPublishStep
 */
fun BuildSteps.nuGetPublish(base: NuGetPublishStep? = null, init: NuGetPublishStep.() -> Unit = {}) {
    step(NuGetPublishStep(init, base))
}
