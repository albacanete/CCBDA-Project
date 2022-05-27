package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for GitHub.com
 * 
 * @see githubConnection
 */
open class GitHubConnection : ProjectFeature {
    constructor(init: GitHubConnection.() -> Unit = {}, base: GitHubConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "GitHub")
        param("defaultTokenScope", "public_repo,repo,repo:status,write:repo_hook")
        param("gitHubUrl", "https://github.com/")
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

}


/**
 * Creates a GitHub OAuth connection in the current project
 * @see GitHubConnection
 */
fun ProjectFeatures.githubConnection(base: GitHubConnection? = null, init: GitHubConnection.() -> Unit = {}) {
    feature(GitHubConnection(init, base))
}
