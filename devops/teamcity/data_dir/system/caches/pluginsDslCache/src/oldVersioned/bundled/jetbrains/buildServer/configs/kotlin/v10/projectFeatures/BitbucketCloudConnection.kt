package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for Bitbucket Cloud
 * 
 * @see bitbucketCloudConnection
 */
open class BitbucketCloudConnection : ProjectFeature {
    constructor(init: BitbucketCloudConnection.() -> Unit = {}, base: BitbucketCloudConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "BitBucketCloud")
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * Bitbucket OAuth connection key
     */
    var key by stringParameter("clientId")

    /**
     * Bitbucket OAuth connection client secret
     */
    var clientSecret by stringParameter("secure:clientSecret")

}


/**
 * Creates a Bitbucket Cloud OAuth connection in the current project
 * @see BitbucketCloudConnection
 */
fun ProjectFeatures.bitbucketCloudConnection(base: BitbucketCloudConnection? = null, init: BitbucketCloudConnection.() -> Unit = {}) {
    feature(BitbucketCloudConnection(init, base))
}
