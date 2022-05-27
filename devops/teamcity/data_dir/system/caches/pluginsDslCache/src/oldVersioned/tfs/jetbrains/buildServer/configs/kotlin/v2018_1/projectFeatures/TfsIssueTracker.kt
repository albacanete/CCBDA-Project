package jetbrains.buildServer.configs.kotlin.v2018_1.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Project feature enabling integration with [Team Foundation Work Items](https://www.jetbrains.com/help/teamcity/?Team+Foundation+Work+Items) as an issue tracker
 * 
 * @see tfsIssueTracker
 */
open class TfsIssueTracker() : ProjectFeature() {

    init {
        type = "IssueTracker"
        param("type", "tfs")
    }

    constructor(init: TfsIssueTracker.() -> Unit): this() {
        init()
    }

    /**
     * Issue tracker integration display name
     */
    var displayName by stringParameter("name")

    /**
     * URL format:
     * * Azure DevOps Server: <public URL>/<collection>/<project>
     * * Azure DevOps Sevices: https://dev.azure.com/<organization>/<project> or https://<organization>.visualstudio.com/<project>
     * * TFS before 2018: http[s]://<host>[:<port>]/tfs/<collection>/<project>
     */
    var host by stringParameter()

    /**
     * A username for TFS connection
     */
    var userName by stringParameter("username")

    /**
     * A password for TFS connection
     */
    var password by stringParameter("secure:password")

    /**
     * A [java regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) to find the work item id
     * in a commit message
     */
    var pattern by stringParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Enables integration with [Team Foundation Work Items](https://www.jetbrains.com/help/teamcity/?Team+Foundation+Work+Items)
 */
fun ProjectFeatures.tfsIssueTracker(init: TfsIssueTracker.() -> Unit): TfsIssueTracker {
    val result = TfsIssueTracker(init)
    feature(result)
    return result
}
