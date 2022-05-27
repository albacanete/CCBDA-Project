package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A build feature enabling integration with Jira Cloud via its Build and Deployment APIs
 * 
 * @see jiraCloudIntegration
 */
open class JiraCloudIntegration : BuildFeature {
    constructor(init: JiraCloudIntegration.() -> Unit = {}, base: JiraCloudIntegration? = null): super(base = base as BuildFeature?) {
        type = "jiraCloud"
        init()
    }

    /**
     * Jira Issue Tracker integration ID
     */
    var issueTrackerConnectionId by stringParameter()

    var isDeploymentConfiguration by booleanParameter("deployment", trueValue = "true", falseValue = "")

    /**
     * Deployment environment type
     * @see EnvironmentType
     */
    var deploymentEnvironmentType by enumParameter<EnvironmentType>("environmentType", mapping = EnvironmentType.mapping)

    /**
     * Deployment environment name
     */
    var deploymentEnvironmentName by stringParameter("environmentName")

    /**
     * Jira Cloud supported environment types
     */
    enum class EnvironmentType {
        PRODUCTION,
        STAGING,
        TESTING,
        DEVELOPMENT,
        UNMAPPED;

        companion object {
            val mapping = mapOf<EnvironmentType, String>(PRODUCTION to "production", STAGING to "staging", TESTING to "testing", DEVELOPMENT to "development", UNMAPPED to "unmapped")
        }

    }
}


/**
 * Provides integration with Jira Cloud via its Build and Deployment APIs
 */
fun BuildFeatures.jiraCloudIntegration(base: JiraCloudIntegration? = null, init: JiraCloudIntegration.() -> Unit = {}) {
    feature(JiraCloudIntegration(init, base))
}
