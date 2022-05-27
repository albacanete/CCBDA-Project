package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature enabling integration with GitLab issue tracker
 * 
 * @see gitlabIssues
 */
open class GitLabIssueTracker : ProjectFeature {
    constructor(init: GitLabIssueTracker.() -> Unit = {}, base: GitLabIssueTracker? = null): super(base = base as ProjectFeature?) {
        type = "IssueTracker"
        param("type", "GitLabIssues")
        param("secure:accessToken", "")
        init()
    }

    /**
     * Issue tracker integration display name.
     */
    var displayName by stringParameter("name")

    /**
     * GitLab server URL.
     */
    var repositoryURL by stringParameter("repository")

    var authType by compoundParameter<AuthType>()

    sealed class AuthType(value: String? = null): CompoundParam(value) {
        class Anonymous() : AuthType("anonymous") {

        }

        class AccessToken() : AuthType("accesstoken") {

            var accessToken by stringParameter("secure:accessToken")

        }
    }

    fun anonymous() = AuthType.Anonymous()

    fun accessToken(init: AuthType.AccessToken.() -> Unit = {}) : AuthType.AccessToken {
        val result = AuthType.AccessToken()
        result.init()
        return result
    }

    /**
     * Issues ID pattern. Use regex syntax, e.g. '#(\d+)'.
     */
    var issuesPattern by stringParameter("pattern")

}


/**
 * Adds a project features enabling integration with GitLab issue tracker
 */
fun ProjectFeatures.gitlabIssues(base: GitLabIssueTracker? = null, init: GitLabIssueTracker.() -> Unit = {}) {
    feature(GitLabIssueTracker(init, base))
}
