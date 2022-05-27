package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * This connection is used for some Perforce adminstration tasks on the TeamCity server.
 * See
 * [Docker Support build feature](https://www.jetbrains.com/help/teamcity/?P4AdminAccessConnection).
 * 
 * @see perforceAdminAccess
 */
open class PerforceAdminConnection : ProjectFeature {
    constructor(init: PerforceAdminConnection.() -> Unit = {}, base: PerforceAdminConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "PerforceAdmin")
        init()
    }

    /**
     * Perforce administrator connection display name
     */
    var name by stringParameter("displayName")

    /**
     * A Perforce server address in the "host:port" format
     */
    var port by stringParameter()

    /**
     * A username for Perforce connection
     */
    var userName by stringParameter("user")

    /**
     * A password for Perforce connection
     */
    var password by stringParameter("secure:passwd")

}


/**
 * @see PerforceAdminConnection
 */
fun ProjectFeatures.perforceAdminAccess(base: PerforceAdminConnection? = null, init: PerforceAdminConnection.() -> Unit = {}) {
    feature(PerforceAdminConnection(init, base))
}
