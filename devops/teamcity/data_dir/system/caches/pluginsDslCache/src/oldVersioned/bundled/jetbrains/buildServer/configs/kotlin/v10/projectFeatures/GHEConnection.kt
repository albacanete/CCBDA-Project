package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for GitHub Enterprise server
 * 
 * @see gheConnection
 */
open class GHEConnection : ProjectFeature {
    constructor(init: GHEConnection.() -> Unit = {}, base: GHEConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "GHE")
        param("defaultTokenScope", "public_repo,repo,repo:status,write:repo_hook")
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

}


/**
 * Creates a GitHub OAuth connection in the current project
 * @see GitHubConnection
 */
fun ProjectFeatures.gheConnection(base: GHEConnection? = null, init: GHEConnection.() -> Unit = {}) {
    feature(GHEConnection(init, base))
}
