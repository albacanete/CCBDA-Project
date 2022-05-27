package jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [NuGet publish step](https://confluence.jetbrains.com/display/TCDL/NuGet+Publish) to run nuget push command
 * 
 * @see nuGetPublish
 */
open class NuGetPublishStep() : BuildStep() {

    init {
        type = "jb.nuget.publish"
    }

    constructor(init: NuGetPublishStep.() -> Unit): this() {
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (serverUrl == null && !hasParam("nuget.publish.source")) {
            consumer.consumePropertyError("serverUrl", "mandatory 'serverUrl' property is not specified")
        }
        if (apiKey == null && !hasParam("secure:nuget.api.key")) {
            consumer.consumePropertyError("apiKey", "mandatory 'apiKey' property is not specified")
        }
    }
}


/**
 * Adds a [NuGet publish step](https://confluence.jetbrains.com/display/TCDL/NuGet+Publish) to run nuget push command
 * @see NuGetPublishStep
 */
fun BuildSteps.nuGetPublish(init: NuGetPublishStep.() -> Unit): NuGetPublishStep {
    val result = NuGetPublishStep(init)
    step(result)
    return result
}
