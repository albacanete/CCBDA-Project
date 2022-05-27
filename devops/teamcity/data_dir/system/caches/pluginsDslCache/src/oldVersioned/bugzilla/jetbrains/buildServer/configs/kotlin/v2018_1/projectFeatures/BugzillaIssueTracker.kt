package jetbrains.buildServer.configs.kotlin.v2018_1.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Project feature enabling integration with [Bugzilla](https://www.jetbrains.com/help/teamcity/?Bugzilla) issue tracker
 * 
 * @see bugzilla
 */
open class BugzillaIssueTracker() : ProjectFeature() {

    init {
        type = "IssueTracker"
        param("type", "bugzilla")
    }

    constructor(init: BugzillaIssueTracker.() -> Unit): this() {
        init()
    }

    /**
     * Issue tracker integration display name
     */
    var displayName by stringParameter("name")

    /**
     * Bugzilla server URL
     */
    var host by stringParameter()

    /**
     * A username for Bugzilla connection
     */
    var userName by stringParameter("username")

    /**
     * A password for Bugzilla connection
     */
    var password by stringParameter("secure:password")

    /**
     * A [java regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) to find the Bugzilla issue ID
     * in a commit message
     */
    var issueIdPattern by stringParameter("pattern")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("name")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (host == null && !hasParam("host")) {
            consumer.consumePropertyError("host", "mandatory 'host' property is not specified")
        }
        if (issueIdPattern == null && !hasParam("pattern")) {
            consumer.consumePropertyError("issueIdPattern", "mandatory 'issueIdPattern' property is not specified")
        }
    }
}


/**
 * Enables integration with [Bugzilla](https://www.jetbrains.com/help/teamcity/?Bugzilla) issue tracker
 * @see BugzillaIssueTracker
 */
fun ProjectFeatures.bugzilla(init: BugzillaIssueTracker.() -> Unit): BugzillaIssueTracker {
    val result = BugzillaIssueTracker(init)
    feature(result)
    return result
}
