package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * This connection is used in
 * [Docker Support build feature](https://www.jetbrains.com/help/teamcity/?Docker+Support).
 * @see DockerSupportFeature
 * 
 * @see dockerRegistry
 */
open class DockerRegistryConnection : ProjectFeature {
    constructor(init: DockerRegistryConnection.() -> Unit = {}, base: DockerRegistryConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "Docker")
        init()
    }

    /**
     * Docker registry connection display name
     */
    var name by stringParameter("displayName")

    /**
     * Docker registry URL, like 'https://docker.io'
     */
    var url by stringParameter("repositoryUrl")

    var userName by stringParameter()

    var password by stringParameter("secure:userPass")

}


/**
 * @see DockerRegistryConnection
 */
fun ProjectFeatures.dockerRegistry(base: DockerRegistryConnection? = null, init: DockerRegistryConnection.() -> Unit = {}) {
    feature(DockerRegistryConnection(init, base))
}
