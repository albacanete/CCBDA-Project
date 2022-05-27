package jetbrains.buildServer.configs.kotlin.v2018_1.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Project feature defining an OAuth connection settings for Bitbucket Cloud
 * 
 * @see bitbucketCloudConnection
 */
open class BitbucketCloudConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "BitBucketCloud")
    }

    constructor(init: BitbucketCloudConnection.() -> Unit): this() {
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("displayName")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (key == null && !hasParam("clientId")) {
            consumer.consumePropertyError("key", "mandatory 'key' property is not specified")
        }
        if (clientSecret == null && !hasParam("secure:clientSecret")) {
            consumer.consumePropertyError("clientSecret", "mandatory 'clientSecret' property is not specified")
        }
    }
}


/**
 * Creates a Bitbucket Cloud OAuth connection in the current project
 * @see BitbucketCloudConnection
 */
fun ProjectFeatures.bitbucketCloudConnection(init: BitbucketCloudConnection.() -> Unit): BitbucketCloudConnection {
    val result = BitbucketCloudConnection(init)
    feature(result)
    return result
}
