package jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * Project feature defining an OAuth connection settings for GitHub.com
 * 
 * @see githubConnection
 */
open class GitHubConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "GitHub")
        param("defaultTokenScope", "public_repo,repo,repo:status,write:repo_hook")
        param("gitHubUrl", "https://github.com/")
    }

    constructor(init: GitHubConnection.() -> Unit): this() {
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

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
fun ProjectFeatures.githubConnection(init: GitHubConnection.() -> Unit): GitHubConnection {
    val result = GitHubConnection(init)
    feature(result)
    return result
}
