package jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * Project feature defining an OAuth connection settings for GitLab.com
 * 
 * @see gitlabConnection
 */
open class GitLabConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "GitLabCom")
    }

    constructor(init: GitLabConnection.() -> Unit): this() {
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * GitLab OAuth connection application ID
     */
    var applicationId by stringParameter("clientId")

    /**
     * GitLab OAuth connection client secret
     */
    var clientSecret by stringParameter("secure:clientSecret")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("displayName")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (applicationId == null && !hasParam("clientId")) {
            consumer.consumePropertyError("applicationId", "mandatory 'applicationId' property is not specified")
        }
        if (clientSecret == null && !hasParam("secure:clientSecret")) {
            consumer.consumePropertyError("clientSecret", "mandatory 'clientSecret' property is not specified")
        }
    }
}


/**
 * Creates a GitLab.com OAuth connection in the current project
 * @see GitLabConnection
 */
fun ProjectFeatures.gitlabConnection(init: GitLabConnection.() -> Unit): GitLabConnection {
    val result = GitLabConnection(init)
    feature(result)
    return result
}
