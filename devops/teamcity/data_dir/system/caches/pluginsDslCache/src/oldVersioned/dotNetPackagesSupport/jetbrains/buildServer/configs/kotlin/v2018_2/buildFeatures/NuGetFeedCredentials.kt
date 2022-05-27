package jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * A [build feature](https://confluence.jetbrains.com/display/TCDL/NuGet+Feed+Credentials) to provide feed credentials.
 * 
 * @see nuGetFeedCredentials
 */
open class NuGetFeedCredentials() : BuildFeature() {

    init {
        type = "jb.nuget.auth"
    }

    constructor(init: NuGetFeedCredentials.() -> Unit): this() {
        init()
    }

    /**
     * Specify a feed URL which credentials will be used in the build.
     */
    var feedUrl by stringParameter("nuget.auth.feed")

    /**
     * Specify username for the feed.
     */
    var username by stringParameter("nuget.auth.username")

    /**
     * Specify password for the feed.
     */
    var password by stringParameter("secure:nuget.auth.password")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Provides [NuGet feed credentials](https://confluence.jetbrains.com/display/TCDL/NuGet+Feed+Credentials) for feed
 * @see NuGetFeedCredentials
 */
fun BuildFeatures.nuGetFeedCredentials(init: NuGetFeedCredentials.() -> Unit): NuGetFeedCredentials {
    val result = NuGetFeedCredentials(init)
    feature(result)
    return result
}
