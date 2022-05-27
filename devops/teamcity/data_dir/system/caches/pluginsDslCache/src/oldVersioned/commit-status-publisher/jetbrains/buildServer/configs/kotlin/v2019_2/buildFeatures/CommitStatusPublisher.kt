package jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [build feature](https://www.jetbrains.com/help/teamcity/?Commit+Status+Publisher) publishing
 * status to external system
 * 
 * @see commitStatusPublisher
 */
open class CommitStatusPublisher() : BuildFeature() {

    init {
        type = "commit-status-publisher"
    }

    constructor(init: CommitStatusPublisher.() -> Unit): this() {
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

    sealed class Publisher(value: String? = null): CompoundParam<Publisher>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class BitbucketCloud() : Publisher("bitbucketCloudPublisher") {

            /**
             * A username for Bitbucket Cloud connection
             */
            var userName by stringParameter("bitbucketUsername")

            /**
             * A password for Bitbucket Cloud connection
             */
            var password by stringParameter("secure:bitbucketPassword")

            override fun validate(consumer: ErrorConsumer) {
                if (userName == null && !hasParam("bitbucketUsername")) {
                    consumer.consumePropertyError("publisher.userName", "mandatory 'publisher.userName' property is not specified")
                }
                if (password == null && !hasParam("secure:bitbucketPassword")) {
                    consumer.consumePropertyError("publisher.password", "mandatory 'publisher.password' property is not specified")
                }
            }
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

            override fun validate(consumer: ErrorConsumer) {
                if (url == null && !hasParam("stashBaseUrl")) {
                    consumer.consumePropertyError("publisher.url", "mandatory 'publisher.url' property is not specified")
                }
                if (userName == null && !hasParam("stashUsername")) {
                    consumer.consumePropertyError("publisher.userName", "mandatory 'publisher.userName' property is not specified")
                }
                if (password == null && !hasParam("secure:stashPassword")) {
                    consumer.consumePropertyError("publisher.password", "mandatory 'publisher.password' property is not specified")
                }
            }
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

            override fun validate(consumer: ErrorConsumer) {
                if (server == null && !hasParam("gerritServer")) {
                    consumer.consumePropertyError("publisher.server", "mandatory 'publisher.server' property is not specified")
                }
                if (gerritProject == null && !hasParam("gerritProject")) {
                    consumer.consumePropertyError("publisher.gerritProject", "mandatory 'publisher.gerritProject' property is not specified")
                }
                if (failureVote == null && !hasParam("failureVote")) {
                    consumer.consumePropertyError("publisher.failureVote", "mandatory 'publisher.failureVote' property is not specified")
                }
                if (successVote == null && !hasParam("successVote")) {
                    consumer.consumePropertyError("publisher.successVote", "mandatory 'publisher.successVote' property is not specified")
                }
                if (userName == null && !hasParam("gerritUsername")) {
                    consumer.consumePropertyError("publisher.userName", "mandatory 'publisher.userName' property is not specified")
                }
            }
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

            sealed class AuthType(value: String? = null): CompoundParam<AuthType>(value) {
                abstract fun validate(consumer: ErrorConsumer)

                class PersonalToken() : AuthType("token") {

                    /**
                     * Personal token to use
                     */
                    var token by stringParameter("secure:github_access_token")

                    override fun validate(consumer: ErrorConsumer) {
                        if (token == null && !hasParam("secure:github_access_token")) {
                            consumer.consumePropertyError("publisher.authType.token", "mandatory 'publisher.authType.token' property is not specified")
                        }
                    }
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

                    override fun validate(consumer: ErrorConsumer) {
                        if (userName == null && !hasParam("github_username")) {
                            consumer.consumePropertyError("publisher.authType.userName", "mandatory 'publisher.authType.userName' property is not specified")
                        }
                        if (password == null && !hasParam("secure:github_password")) {
                            consumer.consumePropertyError("publisher.authType.password", "mandatory 'publisher.authType.password' property is not specified")
                        }
                    }
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

            override fun validate(consumer: ErrorConsumer) {
                if (githubUrl == null && !hasParam("github_host")) {
                    consumer.consumePropertyError("publisher.githubUrl", "mandatory 'publisher.githubUrl' property is not specified")
                }
                authType?.validate(consumer)
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

            override fun validate(consumer: ErrorConsumer) {
                if (gitlabApiUrl == null && !hasParam("gitlabApiUrl")) {
                    consumer.consumePropertyError("publisher.gitlabApiUrl", "mandatory 'publisher.gitlabApiUrl' property is not specified")
                }
                if (accessToken == null && !hasParam("secure:gitlabAccessToken")) {
                    consumer.consumePropertyError("publisher.accessToken", "mandatory 'publisher.accessToken' property is not specified")
                }
            }
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

            override fun validate(consumer: ErrorConsumer) {
                if (serverUrl == null && !hasParam("upsourceServerUrl")) {
                    consumer.consumePropertyError("publisher.serverUrl", "mandatory 'publisher.serverUrl' property is not specified")
                }
                if (projectId == null && !hasParam("upsourceProjectId")) {
                    consumer.consumePropertyError("publisher.projectId", "mandatory 'publisher.projectId' property is not specified")
                }
                if (userName == null && !hasParam("upsourceUsername")) {
                    consumer.consumePropertyError("publisher.userName", "mandatory 'publisher.userName' property is not specified")
                }
                if (password == null && !hasParam("secure:upsourcePassword")) {
                    consumer.consumePropertyError("publisher.password", "mandatory 'publisher.password' property is not specified")
                }
            }
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

            override fun validate(consumer: ErrorConsumer) {
                if (authType == null && !hasParam("tfsAuthType")) {
                    consumer.consumePropertyError("publisher.authType", "mandatory 'publisher.authType' property is not specified")
                }
                if (accessToken == null && !hasParam("secure:accessToken")) {
                    consumer.consumePropertyError("publisher.accessToken", "mandatory 'publisher.accessToken' property is not specified")
                }
            }
        }

        class Space() : Publisher("spaceStatusPublisher") {

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("spaceCredentialsType")

            sealed class AuthType(value: String? = null): CompoundParam<AuthType>(value) {
                abstract fun validate(consumer: ErrorConsumer)

                class Connection() : AuthType("spaceCredentialsConnection") {

                    /**
                     * JetBrains Space Connection project feature ID
                     */
                    var connectionId by stringParameter("spaceConnectionId")

                    override fun validate(consumer: ErrorConsumer) {
                        if (connectionId == null && !hasParam("spaceConnectionId")) {
                            consumer.consumePropertyError("publisher.authType.connectionId", "mandatory 'publisher.authType.connectionId' property is not specified")
                        }
                    }
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

            override fun validate(consumer: ErrorConsumer) {
                authType?.validate(consumer)
            }
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

            override fun validate(consumer: ErrorConsumer) {
                if (serverUrl == null && !hasParam("swarmUrl")) {
                    consumer.consumePropertyError("publisher.serverUrl", "mandatory 'publisher.serverUrl' property is not specified")
                }
                if (username == null && !hasParam("swarmUser")) {
                    consumer.consumePropertyError("publisher.username", "mandatory 'publisher.username' property is not specified")
                }
                if (token == null && !hasParam("secure:swarmPassword")) {
                    consumer.consumePropertyError("publisher.token", "mandatory 'publisher.token' property is not specified")
                }
            }
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (publisher == null && !hasParam("publisherId")) {
            consumer.consumePropertyError("publisher", "mandatory 'publisher' property is not specified")
        }
        publisher?.validate(consumer)
    }
}


/**
 * Enables [status publishing](https://www.jetbrains.com/help/teamcity/?Commit+Status+Publisher) to external system
 * @see CommitStatusPublisher
 */
fun BuildFeatures.commitStatusPublisher(init: CommitStatusPublisher.() -> Unit): CommitStatusPublisher {
    val result = CommitStatusPublisher(init)
    feature(result)
    return result
}
