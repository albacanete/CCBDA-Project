package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for GitLab CE/EE
 * 
 * @see gitlabEEConnection
 */
open class GitLabEEConnection : ProjectFeature {
    constructor(init: GitLabEEConnection.() -> Unit = {}, base: GitLabEEConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "GitLabCEorEE")
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * GitLab server URL
     */
    var serverUrl by stringParameter("gitLabUrl")

    /**
     * GitLab OAuth connection application ID
     */
    var applicationId by stringParameter("clientId")

    /**
     * GitLab OAuth connection client secret
     */
    var clientSecret by stringParameter("secure:clientSecret")

}


/**
 * Creates a GitLab CE/EE OAuth connection in the current project
 * @see GitLabConnection
 */
fun ProjectFeatures.gitlabEEConnection(base: GitLabEEConnection? = null, init: GitLabEEConnection.() -> Unit = {}) {
    feature(GitLabEEConnection(init, base))
}
