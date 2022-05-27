package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for Slack
 * 
 * @see slackConnection
 */
open class SlackConnection : ProjectFeature {
    constructor(init: SlackConnection.() -> Unit = {}, base: SlackConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "slackConnection")
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * Bot token, xoxb-***
     */
    var botToken by stringParameter("secure:token")

    /**
     * Client ID
     */
    var clientId by stringParameter()

    /**
     * Client secret
     */
    var clientSecret by stringParameter("secure:clientSecret")

}


/**
 * Creates a Slack connection in the current project
 * @see SlackConnection
 */
fun ProjectFeatures.slackConnection(base: SlackConnection? = null, init: SlackConnection.() -> Unit = {}) {
    feature(SlackConnection(init, base))
}
