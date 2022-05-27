package jetbrains.buildServer.configs.kotlin.v2018_1.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Project feature for Azure DevOps OAuth connection settings
 * 
 * @see azureDevOpsOAuthConnection
 */
open class AzureDevOpsOAuthConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "AzureDevOps")
    }

    constructor(init: AzureDevOpsOAuthConnection.() -> Unit): this() {
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * Azure DevOps server URL, for example:
     * https://app.vssps.visualstudio.com
     */
    var azureDevOpsUrl by stringParameter()

    /**
     * Azure DevOps Application ID
     */
    var applicationId by stringParameter()

    /**
     * Client Secret
     */
    var clientSecret by stringParameter("secure:clientSecret")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("displayName")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (azureDevOpsUrl == null && !hasParam("azureDevOpsUrl")) {
            consumer.consumePropertyError("azureDevOpsUrl", "mandatory 'azureDevOpsUrl' property is not specified")
        }
        if (applicationId == null && !hasParam("applicationId")) {
            consumer.consumePropertyError("applicationId", "mandatory 'applicationId' property is not specified")
        }
        if (clientSecret == null && !hasParam("secure:clientSecret")) {
            consumer.consumePropertyError("clientSecret", "mandatory 'clientSecret' property is not specified")
        }
    }
}


/**
 * Creates an Azure DevOps OAuth connection in the current project
 * @see AzureDevOpsOAuthConnection
 */
fun ProjectFeatures.azureDevOpsOAuthConnection(init: AzureDevOpsOAuthConnection.() -> Unit): AzureDevOpsOAuthConnection {
    val result = AzureDevOpsOAuthConnection(init)
    feature(result)
    return result
}
