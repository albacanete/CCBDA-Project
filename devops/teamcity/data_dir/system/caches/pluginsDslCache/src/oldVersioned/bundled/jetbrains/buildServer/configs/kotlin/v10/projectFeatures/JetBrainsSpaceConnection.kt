package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for JetBrains Space
 * 
 * @see spaceConnection
 */
open class JetBrainsSpaceConnection : ProjectFeature {
    constructor(init: JetBrainsSpaceConnection.() -> Unit = {}, base: JetBrainsSpaceConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "JetBrains Space")
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * JetBrains Space server URL
     */
    var serverUrl by stringParameter("spaceServerUrl")

    /**
     * JetBrains Space OAuth connection client ID
     */
    var clientId by stringParameter("spaceClientId")

    /**
     * JetBrains Space OAuth connection client secret
     */
    var clientSecret by stringParameter("secure:spaceClientSecret")

}


/**
 * Creates a JetBrains Space OAuth connection in the current project
 * @see JetBrainsSpaceConnection
 */
fun ProjectFeatures.spaceConnection(base: JetBrainsSpaceConnection? = null, init: JetBrainsSpaceConnection.() -> Unit = {}) {
    feature(JetBrainsSpaceConnection(init, base))
}
