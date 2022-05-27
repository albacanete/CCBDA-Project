package jetbrains.buildServer.configs.kotlin.v2018_1.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Project feature defining an OAuth connection settings for GitHub Enterprise server
 * 
 * @see gheConnection
 */
open class GHEConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "GHE")
        param("defaultTokenScope", "public_repo,repo,repo:status,write:repo_hook")
    }

    constructor(init: GHEConnection.() -> Unit): this() {
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * URL to GitHub Enterprise server
     */
    var serverUrl by stringParameter("gitHubUrl")

    /**
     * OAuth connection client id
     */
    var clientId by stringParameter()

    /**
     * OAuth connection client secret
     */
    var clientSecret by stringParameter("secure:clientSecret")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("displayName")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (clientId == null && !hasParam("clientId")) {
            consumer.consumePropertyError("clientId", "mandatory 'clientId' property is not specified")
        }
        if (clientSecret == null && !hasParam("secure:clientSecret")) {
            consumer.consumePropertyError("clientSecret", "mandatory 'clientSecret' property is not specified")
        }
    }
}


/**
 * Creates a GitHub OAuth connection in the current project
 * @see GitHubConnection
 */
fun ProjectFeatures.gheConnection(init: GHEConnection.() -> Unit): GHEConnection {
    val result = GHEConnection(init)
    feature(result)
    return result
}
