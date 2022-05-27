package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for GitLab.com
 * 
 * @see gitlabConnection
 */
open class GitLabConnection : ProjectFeature {
    constructor(init: GitLabConnection.() -> Unit = {}, base: GitLabConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "GitLabCom")
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

}


/**
 * Creates a GitLab.com OAuth connection in the current project
 * @see GitLabConnection
 */
fun ProjectFeatures.gitlabConnection(base: GitLabConnection? = null, init: GitLabConnection.() -> Unit = {}) {
    feature(GitLabConnection(init, base))
}
