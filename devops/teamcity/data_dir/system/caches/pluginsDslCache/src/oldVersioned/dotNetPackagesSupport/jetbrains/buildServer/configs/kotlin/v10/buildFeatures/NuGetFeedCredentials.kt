package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build feature](https://confluence.jetbrains.com/display/TCDL/NuGet+Feed+Credentials) to provide feed credentials.
 * 
 * @see nuGetFeedCredentials
 */
open class NuGetFeedCredentials : BuildFeature {
    constructor(init: NuGetFeedCredentials.() -> Unit = {}, base: NuGetFeedCredentials? = null): super(base = base as BuildFeature?) {
        type = "jb.nuget.auth"
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

}


/**
 * Provides [NuGet feed credentials](https://confluence.jetbrains.com/display/TCDL/NuGet+Feed+Credentials) for feed
 * @see NuGetFeedCredentials
 */
fun BuildFeatures.nuGetFeedCredentials(base: NuGetFeedCredentials? = null, init: NuGetFeedCredentials.() -> Unit = {}) {
    feature(NuGetFeedCredentials(init, base))
}
