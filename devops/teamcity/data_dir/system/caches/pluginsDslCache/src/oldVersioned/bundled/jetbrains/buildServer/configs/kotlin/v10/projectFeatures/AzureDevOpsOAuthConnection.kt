package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature for Azure DevOps OAuth connection settings
 * 
 * @see azureDevOpsOAuthConnection
 */
open class AzureDevOpsOAuthConnection : ProjectFeature {
    constructor(init: AzureDevOpsOAuthConnection.() -> Unit = {}, base: AzureDevOpsOAuthConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "AzureDevOps")
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

}


/**
 * Creates an Azure DevOps OAuth connection in the current project
 * @see AzureDevOpsOAuthConnection
 */
fun ProjectFeatures.azureDevOpsOAuthConnection(base: AzureDevOpsOAuthConnection? = null, init: AzureDevOpsOAuthConnection.() -> Unit = {}) {
    feature(AzureDevOpsOAuthConnection(init, base))
}
