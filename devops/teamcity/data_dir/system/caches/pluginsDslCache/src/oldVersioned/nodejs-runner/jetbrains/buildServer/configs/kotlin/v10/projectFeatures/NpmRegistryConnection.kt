package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Connection to npm registries
 * 
 * @see npmRegistry
 */
open class NpmRegistryConnection : ProjectFeature {
    constructor(init: NpmRegistryConnection.() -> Unit = {}, base: NpmRegistryConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "NpmRegistry")
        init()
    }

    /**
     * Npm registry connection display name
     */
    var name by stringParameter("displayName")

    /**
     * Npm registry URL, Format: 'http(s)://hostname[:port]'
     */
    var url by stringParameter("npmRegistryHost")

    /**
     * Scope for registry. Leave empty to override default registry.
     */
    var scope by stringParameter("npmRegistryScope")

    /**
     * Token with access to repository
     */
    var token by stringParameter("secure:npmRegistryToken")

}


/**
 * @see NpmRegistryConnection
 */
fun ProjectFeatures.npmRegistry(base: NpmRegistryConnection? = null, init: NpmRegistryConnection.() -> Unit = {}) {
    feature(NpmRegistryConnection(init, base))
}
