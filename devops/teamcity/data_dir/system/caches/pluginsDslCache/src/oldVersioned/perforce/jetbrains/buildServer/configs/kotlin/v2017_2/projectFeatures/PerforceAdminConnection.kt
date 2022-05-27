package jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * This connection is used for some Perforce adminstration tasks on the TeamCity server.
 * See
 * [Docker Support build feature](https://www.jetbrains.com/help/teamcity/?P4AdminAccessConnection).
 * 
 * @see perforceAdminAccess
 */
open class PerforceAdminConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "PerforceAdmin")
    }

    constructor(init: PerforceAdminConnection.() -> Unit): this() {
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * @see PerforceAdminConnection
 */
fun ProjectFeatures.perforceAdminAccess(init: PerforceAdminConnection.() -> Unit): PerforceAdminConnection {
    val result = PerforceAdminConnection(init)
    feature(result)
    return result
}
