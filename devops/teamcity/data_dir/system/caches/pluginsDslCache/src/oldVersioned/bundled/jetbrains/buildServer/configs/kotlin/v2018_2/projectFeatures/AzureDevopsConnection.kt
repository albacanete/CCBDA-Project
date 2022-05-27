package jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * Project feature for Azure Devops or VSTS connection settings
 * 
 * @see azureDevopsConnection
 */
open class AzureDevopsConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "tfs")
        param("type", "token")
    }

    constructor(init: AzureDevopsConnection.() -> Unit): this() {
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * Azure DevOps or VSTS server URL, for example:
     * https://dev.azure.com/<organization> (Azure Devops) or https://<account>.visualstudio.com (VSTS)
     */
    var serverUrl by stringParameter()

    /**
     * Access token
     */
    var accessToken by stringParameter("secure:accessToken")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("displayName")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (serverUrl == null && !hasParam("serverUrl")) {
            consumer.consumePropertyError("serverUrl", "mandatory 'serverUrl' property is not specified")
        }
        if (accessToken == null && !hasParam("secure:accessToken")) {
            consumer.consumePropertyError("accessToken", "mandatory 'accessToken' property is not specified")
        }
    }
}


/**
 * Creates an Azure Devops/VSTS connection in the current project
 * @see AzureDevopsConnection
 */
fun ProjectFeatures.azureDevopsConnection(init: AzureDevopsConnection.() -> Unit): AzureDevopsConnection {
    val result = AzureDevopsConnection(init)
    feature(result)
    return result
}
