package jetbrains.buildServer.configs.kotlin.v2018_1.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Project feature enabling integration with [JIRA](https://www.jetbrains.com/help/teamcity/?JIRA) issue tracker
 * 
 * @see jira
 */
open class JiraIssueTracker() : ProjectFeature() {

    init {
        type = "IssueTracker"
        param("type", "jira")
    }

    constructor(init: JiraIssueTracker.() -> Unit): this() {
        init()
    }

    /**
     * Issue tracker integration display name
     */
    var displayName by stringParameter("name")

    /**
     * JIRA server URL
     */
    var host by stringParameter()

    /**
     * A username for JIRA connection
     */
    var userName by stringParameter("username")

    /**
     * A password for JIRA connection
     */
    var password by stringParameter("secure:password")

    var projectKeys by stringParameter("idPrefix")

    /**
     * Automatically populate JIRA Project keys
     */
    var useAutomaticKeys by booleanParameter("autoSync", trueValue = "true", falseValue = "")

    /**
     * A Client ID for JIRA Cloud integration via its Build and Deployment APIs
     */
    var cloudClientID by stringParameter("jiraCloudClientId")

    /**
     * A Secret for JIRA Cloud integration via its Build and Deployment APIs
     */
    var cloudSecret by stringParameter("secure:jiraCloudServerSecret")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("name")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (host == null && !hasParam("host")) {
            consumer.consumePropertyError("host", "mandatory 'host' property is not specified")
        }
        if (projectKeys == null && !hasParam("idPrefix")) {
            consumer.consumePropertyError("projectKeys", "mandatory 'projectKeys' property is not specified")
        }
    }
}


/**
 * Enables integration with [JIRA](https://www.jetbrains.com/help/teamcity/?JIRA) issue tracker
 */
fun ProjectFeatures.jira(init: JiraIssueTracker.() -> Unit): JiraIssueTracker {
    val result = JiraIssueTracker(init)
    feature(result)
    return result
}
