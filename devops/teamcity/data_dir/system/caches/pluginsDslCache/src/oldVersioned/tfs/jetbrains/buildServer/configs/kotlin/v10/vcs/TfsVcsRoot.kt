package jetbrains.buildServer.configs.kotlin.v10.vcs

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * TFS [VCS root](https://www.jetbrains.com/help/teamcity/?Team+Foundation+Server)
 */
open class TfsVcsRoot : VcsRoot {
    constructor(init: TfsVcsRoot.() -> Unit = {}, base: TfsVcsRoot? = null): super(base = base as VcsRoot?) {
        type = "tfs"
        init()
    }

    /**
     * URL format:
     * * Azure DevOps Server: <public URL>/<collection>
     * * Azure DevOps Sevices: https://dev.azure.com/<organization> or https://<organization>.visualstudio.com
     * * TFS before 2018: http[s]://<host>[:<port>]/tfs/<collection>
     */
    var url by stringParameter("tfs-url")

    /**
     * TFS path to checkout. Format: $/path.
     */
    var root by stringParameter("tfs-root")

    /**
     * A username for TFS connection
     */
    var userName by stringParameter("tfs-username")

    /**
     * A password for TFS connection
     */
    var password by stringParameter("secure:tfs-password")

    /**
     * When set to true, TeamCity will call TFS to update workspace rewriting all files
     */
    var forceOverwrite by booleanParameter("tfs-force-get", trueValue = "true", falseValue = "")

}


