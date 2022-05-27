package jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * Project feature enabling integration with GitLab issue tracker
 * 
 * @see gitlabIssues
 */
open class GitLabIssueTracker() : ProjectFeature() {

    init {
        type = "IssueTracker"
        param("type", "GitLabIssues")
        param("secure:accessToken", "")
    }

    constructor(init: GitLabIssueTracker.() -> Unit): this() {
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

    sealed class AuthType(value: String? = null): CompoundParam<AuthType>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Anonymous() : AuthType("anonymous") {

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class AccessToken() : AuthType("accesstoken") {

            var accessToken by stringParameter("secure:accessToken")

            override fun validate(consumer: ErrorConsumer) {
                if (accessToken == null && !hasParam("secure:accessToken")) {
                    consumer.consumePropertyError("authType.accessToken", "mandatory 'authType.accessToken' property is not specified")
                }
            }
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("name")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (repositoryURL == null && !hasParam("repository")) {
            consumer.consumePropertyError("repositoryURL", "mandatory 'repositoryURL' property is not specified")
        }
        authType?.validate(consumer)
    }
}


/**
 * Adds a project features enabling integration with GitLab issue tracker
 */
fun ProjectFeatures.gitlabIssues(init: GitLabIssueTracker.() -> Unit): GitLabIssueTracker {
    val result = GitLabIssueTracker(init)
    feature(result)
    return result
}
