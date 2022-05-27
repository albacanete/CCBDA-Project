package jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * Project feature defining an OAuth connection settings for JetBrains Space
 * 
 * @see spaceConnection
 */
open class JetBrainsSpaceConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "JetBrains Space")
    }

    constructor(init: JetBrainsSpaceConnection.() -> Unit): this() {
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("displayName")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (serverUrl == null && !hasParam("spaceServerUrl")) {
            consumer.consumePropertyError("serverUrl", "mandatory 'serverUrl' property is not specified")
        }
        if (clientId == null && !hasParam("spaceClientId")) {
            consumer.consumePropertyError("clientId", "mandatory 'clientId' property is not specified")
        }
        if (clientSecret == null && !hasParam("secure:spaceClientSecret")) {
            consumer.consumePropertyError("clientSecret", "mandatory 'clientSecret' property is not specified")
        }
    }
}


/**
 * Creates a JetBrains Space OAuth connection in the current project
 * @see JetBrainsSpaceConnection
 */
fun ProjectFeatures.spaceConnection(init: JetBrainsSpaceConnection.() -> Unit): JetBrainsSpaceConnection {
    val result = JetBrainsSpaceConnection(init)
    feature(result)
    return result
}
