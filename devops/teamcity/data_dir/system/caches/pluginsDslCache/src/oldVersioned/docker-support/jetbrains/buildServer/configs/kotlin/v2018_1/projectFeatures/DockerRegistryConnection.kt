package jetbrains.buildServer.configs.kotlin.v2018_1.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * This connection is used in
 * [Docker Support build feature](https://www.jetbrains.com/help/teamcity/?Docker+Support).
 * @see DockerSupportFeature
 * 
 * @see dockerRegistry
 */
open class DockerRegistryConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "Docker")
    }

    constructor(init: DockerRegistryConnection.() -> Unit): this() {
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * @see DockerRegistryConnection
 */
fun ProjectFeatures.dockerRegistry(init: DockerRegistryConnection.() -> Unit): DockerRegistryConnection {
    val result = DockerRegistryConnection(init)
    feature(result)
    return result
}
