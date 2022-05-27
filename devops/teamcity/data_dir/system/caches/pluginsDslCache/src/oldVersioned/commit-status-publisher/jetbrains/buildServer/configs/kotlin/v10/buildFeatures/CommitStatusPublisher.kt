package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build feature](https://www.jetbrains.com/help/teamcity/?Commit+Status+Publisher) publishing
 * status to external system
 * 
 * @see commitStatusPublisher
 */
open class CommitStatusPublisher : BuildFeature {
    constructor(init: CommitStatusPublisher.() -> Unit = {}, base: CommitStatusPublisher? = null): super(base = base as BuildFeature?) {
        type = "commit-status-publisher"
        init()
    }

    /**
     * Id of the VCS root for which commits a status should be published.
     * Set to an empty string to publish status for all VCS roots attached to a build configuration.
     */
    var vcsRootExtId by stringParameter("vcsRootId")

    /**
     * Specifies to which system a status should be published
     */
    var publisher by compoundParameter<Publisher>("publisherId")

    sealed class Publisher(value: String? = null): CompoundParam(value) {
        class BitbucketCloud() : Publisher("bitbucketCloudPublisher") {

            /**
             * A username for Bitbucket Cloud connection
             */
            var userName by stringParameter("bitbucketUsername")

            /**
             * A password for Bitbucket Cloud connection
             */
            var password by stringParameter("secure:bitbucketPassword")

        }

        class BitbucketServer() : Publisher("atlassianStashPublisher") {

            /**
             * Bitbucket Server URL
             */
            var url by stringParameter("stashBaseUrl")

            /**
             * A username for Bitbucket Server connection
             */
            var userName by stringParameter("stashUsername")

            /**
             * A password for Bitbucket Server connection
             */
            var password by stringParameter("secure:stashPassword")

        }

        class Gerrit() : Publisher("gerritStatusPublisher") {

            /**
             * Gerrit server in the format: {server}[:{port}]
             */
            var server by stringParameter("gerritServer")

            /**
             * Gerrit project name
             */
            var gerritProject by stringParameter()

            /**
             * Gerrit label to be used, "Verified" is used if the parameter is left blank
             */
            var label by stringParameter()

            /**
             * A failed build vote, e.g. "-1"
             */
            var failureVote by stringParameter()

            /**
             * A successful build vote, e.g. "+1"
             */
            var successVote by stringParameter()

            /**
             * A username for Gerrit connection
             */
            var userName by stringParameter("gerritUsername")

            /**
             * Name of the [uploaded key](https://www.jetbrains.com/help/teamcity/?SSH+Keys+Management) to use for Gerrit connection
             */
            var uploadedKey by stringParameter("teamcitySshKey")

        }

        class Github() : Publisher("githubStatusPublisher") {

            /**
             * GitHub server URL.
             * Use "https://api.github.com" for projects hosted at github.com. For GitHub enterprise use the URL in the
             * following format: http{s}://{host}:{port}/api/v3
             */
            var githubUrl by stringParameter("github_host")

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("github_authentication_type")

            sealed class AuthType(value: String? = null): CompoundParam(value) {
                class PersonalToken() : AuthType("token") {

                    /**
                     * Personal token to use
                     */
                    var token by stringParameter("secure:github_access_token")

                }

                class Password() : AuthType("password") {

                    /**
                     * A username for GitHub connection
                     */
                    var userName by stringParameter("github_username")

                    /**
                     * A password for GitHub connection
                     */
                    var password by stringParameter("secure:github_password")

                }
            }

            /**
             * Authentication using personal token
             */
            fun personalToken(init: AuthType.PersonalToken.() -> Unit = {}) : AuthType.PersonalToken {
                val result = AuthType.PersonalToken()
                result.init()
                return result
            }

            /**
             * Password authentication
             */
            fun password(init: AuthType.Password.() -> Unit = {}) : AuthType.Password {
                val result = AuthType.Password()
                result.init()
                return result
            }

        }

        class Gitlab() : Publisher("gitlabStatusPublisher") {

            /**
             * GitLab URL in the format http{s}://{hostname}:{port}/api/v3
             */
            var gitlabApiUrl by stringParameter()

            /**
             * Access token to use for GitLab connection. Can be found at /profile/account in GitLab.
             */
            var accessToken by stringParameter("secure:gitlabAccessToken")

        }

        class Upsource() : Publisher("upsourcePublisher") {

            /**
             * Upsource server URL
             */
            var serverUrl by stringParameter("upsourceServerUrl")

            /**
             * Upsource project ID
             */
            var projectId by stringParameter("upsourceProjectId")

            /**
             * A username for Upsource connection
             */
            var userName by stringParameter("upsourceUsername")

            /**
             * A password for Upsource connection
             */
            var password by stringParameter("secure:upsourcePassword")

        }

        class Tfs() : Publisher("tfs") {

            /**
             * Server URL for SSH-based VCS roots
             */
            var serverUrl by stringParameter("tfsServerUrl")

            /**
             * Authentication type
             */
            var authType by stringParameter("tfsAuthType")

            /**
             * A Personal Access Token value
             */
            var accessToken by stringParameter("secure:accessToken")

            /**
             * Enables publishing build status for pull requests
             */
            var publishPullRequests by booleanParameter("publish.pull.requests", trueValue = "true", falseValue = "")

        }

        class Space() : Publisher("spaceStatusPublisher") {

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("spaceCredentialsType")

            sealed class AuthType(value: String? = null): CompoundParam(value) {
                class Connection() : AuthType("spaceCredentialsConnection") {

                    /**
                     * JetBrains Space Connection project feature ID
                     */
                    var connectionId by stringParameter("spaceConnectionId")

                }
            }

            /**
             * Authentication using JetBrains Space Connection
             */
            fun connection(init: AuthType.Connection.() -> Unit = {}) : AuthType.Connection {
                val result = AuthType.Connection()
                result.init()
                return result
            }

            /**
             * JetBrains Space Project Key
             */
            var projectKey by stringParameter("spaceProjectKey")

            /**
             * Display name
             */
            var displayName by stringParameter("spaceCommitStatusPublisherDisplayName")

        }

        class Swarm() : Publisher("perforceSwarmPublisher") {

            /**
             * Peforce Helix Swarm Server URL
             */
            var serverUrl by stringParameter("swarmUrl")

            /**
             * Username to access the server.
             */
            var username by stringParameter("swarmUser")

            /**
             * Token or password to access the Perforc Swarm server.
             */
            var token by stringParameter("secure:swarmPassword")

            /**
             * If set, TeamCity will create a test run on the Helix Swarm server and update its status according to
             * the build status in TeamCity.
             */
            var createSwarmTest by booleanParameter(trueValue = "true", falseValue = "")

        }
    }

    /**
     * Publish status to Bitbucket Cloud
     */
    fun bitbucketCloud(init: Publisher.BitbucketCloud.() -> Unit = {}) : Publisher.BitbucketCloud {
        val result = Publisher.BitbucketCloud()
        result.init()
        return result
    }

    /**
     * Publish status to Bitbucket Server
     */
    fun bitbucketServer(init: Publisher.BitbucketServer.() -> Unit = {}) : Publisher.BitbucketServer {
        val result = Publisher.BitbucketServer()
        result.init()
        return result
    }

    /**
     * Publish status to Gerrit Code Review
     */
    fun gerrit(init: Publisher.Gerrit.() -> Unit = {}) : Publisher.Gerrit {
        val result = Publisher.Gerrit()
        result.init()
        return result
    }

    /**
     * Publish status to GitHub
     */
    fun github(init: Publisher.Github.() -> Unit = {}) : Publisher.Github {
        val result = Publisher.Github()
        result.init()
        return result
    }

    /**
     * Publish status to GitLab
     */
    fun gitlab(init: Publisher.Gitlab.() -> Unit = {}) : Publisher.Gitlab {
        val result = Publisher.Gitlab()
        result.init()
        return result
    }

    /**
     * Publish status to Upsource
     */
    fun upsource(init: Publisher.Upsource.() -> Unit = {}) : Publisher.Upsource {
        val result = Publisher.Upsource()
        result.init()
        return result
    }

    /**
     * Publish build status to Azure DevOps / TFS
     */
    fun tfs(init: Publisher.Tfs.() -> Unit = {}) : Publisher.Tfs {
        val result = Publisher.Tfs()
        result.init()
        return result
    }

    /**
     * Publish status to JetBrains Space
     */
    fun space(init: Publisher.Space.() -> Unit = {}) : Publisher.Space {
        val result = Publisher.Space()
        result.init()
        return result
    }

    /**
     * Publish build status to Perforce Helix Swarm
     */
    fun swarm(init: Publisher.Swarm.() -> Unit = {}) : Publisher.Swarm {
        val result = Publisher.Swarm()
        result.init()
        return result
    }

}


/**
 * Enables [status publishing](https://www.jetbrains.com/help/teamcity/?Commit+Status+Publisher) to external system
 * @see CommitStatusPublisher
 */
fun BuildFeatures.commitStatusPublisher(base: CommitStatusPublisher? = null, init: CommitStatusPublisher.() -> Unit = {}) {
    feature(CommitStatusPublisher(init, base))
}
