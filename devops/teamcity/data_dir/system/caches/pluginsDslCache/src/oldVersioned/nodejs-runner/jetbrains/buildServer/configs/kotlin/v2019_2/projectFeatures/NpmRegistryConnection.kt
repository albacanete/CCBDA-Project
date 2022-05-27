package jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * Connection to npm registries
 * 
 * @see npmRegistry
 */
open class NpmRegistryConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "NpmRegistry")
    }

    constructor(init: NpmRegistryConnection.() -> Unit): this() {
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * @see NpmRegistryConnection
 */
fun ProjectFeatures.npmRegistry(init: NpmRegistryConnection.() -> Unit): NpmRegistryConnection {
    val result = NpmRegistryConnection(init)
    feature(result)
    return result
}
