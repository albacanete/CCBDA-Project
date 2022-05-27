package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature enabling integration with [YouTrack](https://www.jetbrains.com/help/teamcity/?YouTrack) issue tracker
 * 
 * @see youtrack
 */
open class YouTrackIssueTracker : ProjectFeature {
    constructor(init: YouTrackIssueTracker.() -> Unit = {}, base: YouTrackIssueTracker? = null): super(base = base as ProjectFeature?) {
        type = "IssueTracker"
        param("type", "youtrack")
        param("username", "")
        param("secure:password", "")
        param("secure:accessToken", "")
        init()
    }

    /**
     * Issue tracker integration display name
     */
    var displayName by stringParameter("name")

    /**
     * YouTrack server URL
     */
    var host by stringParameter()

    /**
     * A username for YouTrack connection
     */
    var userName by stringParameter("username")

    /**
     * A password for YouTrack connection
     */
    var password by stringParameter("secure:password")

    /**
     * A space-separated list of YouTrack Project IDs
     */
    var projectExtIds by stringParameter("idPrefix")

    /**
     * Permanent token to access YouTrack
     */
    var accessToken by stringParameter("secure:accessToken")

    /**
     * Automatically populate YouTrack Project IDs
     */
    var useAutomaticIds by booleanParameter("autoSync", trueValue = "true", falseValue = "")

    /**
     * Authentication type for YouTrack.
     * Token authentication is used by default.
     */
    var authType by enumParameter<AuthType>(mapping = AuthType.mapping)

    enum class AuthType {
        TOKEN,
        USERNAME_AND_PASSWORD;

        companion object {
            val mapping = mapOf<AuthType, String>(TOKEN to "accesstoken", USERNAME_AND_PASSWORD to "loginpassword")
        }

    }
}


/**
 * Enables integration with [YouTrack](https://www.jetbrains.com/help/teamcity/?YouTrack) issue tracker
 * @see YouTrackIssueTracker
 */
fun ProjectFeatures.youtrack(base: YouTrackIssueTracker? = null, init: YouTrackIssueTracker.() -> Unit = {}) {
    feature(YouTrackIssueTracker(init, base))
}
