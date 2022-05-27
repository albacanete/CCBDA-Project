package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature for Azure Devops or VSTS connection settings
 * 
 * @see azureDevopsConnection
 */
open class AzureDevopsConnection : ProjectFeature {
    constructor(init: AzureDevopsConnection.() -> Unit = {}, base: AzureDevopsConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "tfs")
        param("type", "token")
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

}


/**
 * Creates an Azure Devops/VSTS connection in the current project
 * @see AzureDevopsConnection
 */
fun ProjectFeatures.azureDevopsConnection(base: AzureDevopsConnection? = null, init: AzureDevopsConnection.() -> Unit = {}) {
    feature(AzureDevopsConnection(init, base))
}
