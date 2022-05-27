package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature enabling integration with [Bugzilla](https://www.jetbrains.com/help/teamcity/?Bugzilla) issue tracker
 * 
 * @see bugzilla
 */
open class BugzillaIssueTracker : ProjectFeature {
    constructor(init: BugzillaIssueTracker.() -> Unit = {}, base: BugzillaIssueTracker? = null): super(base = base as ProjectFeature?) {
        type = "IssueTracker"
        param("type", "bugzilla")
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

}


/**
 * Enables integration with [Bugzilla](https://www.jetbrains.com/help/teamcity/?Bugzilla) issue tracker
 * @see BugzillaIssueTracker
 */
fun ProjectFeatures.bugzilla(base: BugzillaIssueTracker? = null, init: BugzillaIssueTracker.() -> Unit = {}) {
    feature(BugzillaIssueTracker(init, base))
}
