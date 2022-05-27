package jetbrains.buildServer.configs.kotlin.v2017_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * A build feature enabling integration with Jira Cloud via its Build and Deployment APIs
 * 
 * @see jiraCloudIntegration
 */
open class JiraCloudIntegration() : BuildFeature() {

    init {
        type = "jiraCloud"
    }

    constructor(init: JiraCloudIntegration.() -> Unit): this() {
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
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (issueTrackerConnectionId == null && !hasParam("issueTrackerConnectionId")) {
            consumer.consumePropertyError("issueTrackerConnectionId", "mandatory 'issueTrackerConnectionId' property is not specified")
        }
    }
}


/**
 * Provides integration with Jira Cloud via its Build and Deployment APIs
 */
fun BuildFeatures.jiraCloudIntegration(init: JiraCloudIntegration.() -> Unit): JiraCloudIntegration {
    val result = JiraCloudIntegration(init)
    feature(result)
    return result
}
