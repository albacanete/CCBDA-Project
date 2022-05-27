package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature enabling integration with [JIRA](https://www.jetbrains.com/help/teamcity/?JIRA) issue tracker
 * 
 * @see jira
 */
open class JiraIssueTracker : ProjectFeature {
    constructor(init: JiraIssueTracker.() -> Unit = {}, base: JiraIssueTracker? = null): super(base = base as ProjectFeature?) {
        type = "IssueTracker"
        param("type", "jira")
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

}


/**
 * Enables integration with [JIRA](https://www.jetbrains.com/help/teamcity/?JIRA) issue tracker
 */
fun ProjectFeatures.jira(base: JiraIssueTracker? = null, init: JiraIssueTracker.() -> Unit = {}) {
    feature(JiraIssueTracker(init, base))
}
