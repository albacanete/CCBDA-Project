package jetbrains.buildServer.configs.kotlin.buildFeatures

import jetbrains.buildServer.configs.kotlin.*

/**
 * A [build feature](https://www.jetbrains.com/help/teamcity/?Pull+Requests) that introduces GitHub pull requests integration
 * 
 * @see pullRequests
 */
open class PullRequests() : BuildFeature() {

    init {
        type = "pullRequests"
    }

    constructor(init: PullRequests.() -> Unit): this() {
        init()
    }

    /**
     * Id of the VCS root to extract pull request information from.
     * Set to an empty string to extract pull request information from the first VCS root attached to a build configuration.
     */
    var vcsRootExtId by stringParameter("vcsRootId")

    var provider by compoundParameter<Provider>("providerType")

    sealed class Provider(value: String? = null): CompoundParam<Provider>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Github() : Provider("github") {

            /**
             * GitHub/GHE server URL
             */
            var serverUrl by stringParameter()

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("authenticationType")

            sealed class AuthType(value: String? = null): CompoundParam<AuthType>(value) {
                abstract fun validate(consumer: ErrorConsumer)

                class VcsRoot() : AuthType("vcsRoot") {

                    override fun validate(consumer: ErrorConsumer) {
                    }
                }

                class Token() : AuthType("token") {

                    /**
                     * Access token to use
                     */
                    var token by stringParameter("secure:accessToken")

                    override fun validate(consumer: ErrorConsumer) {
                        if (token == null && !hasParam("secure:accessToken")) {
                            consumer.consumePropertyError("provider.authType.token", "mandatory 'provider.authType.token' property is not specified")
                        }
                    }
                }
            }

            /**
             * Use VCS root credentials
             */
            fun vcsRoot() = AuthType.VcsRoot()

            /**
             * Authentication using access token
             */
            fun token(init: AuthType.Token.() -> Unit = {}) : AuthType.Token {
                val result = AuthType.Token()
                result.init()
                return result
            }

            /**
             * Filter by source branch
             */
            var filterSourceBranch by stringParameter()

            /**
             * Filter by target branch
             */
            var filterTargetBranch by stringParameter()

            /**
             * Filter by the role of pull request contributors
             */
            var filterAuthorRole by enumParameter<GitHubRoleFilter>()

            override fun validate(consumer: ErrorConsumer) {
                authType?.validate(consumer)
            }
        }

        class Gitlab() : Provider("gitlab") {

            /**
             * GitLab server URL
             */
            var serverUrl by stringParameter()

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("authenticationType")

            sealed class AuthType(value: String? = null): CompoundParam<AuthType>(value) {
                abstract fun validate(consumer: ErrorConsumer)

                class Token() : AuthType("token") {

                    /**
                     * Access token to use
                     */
                    var token by stringParameter("secure:accessToken")

                    override fun validate(consumer: ErrorConsumer) {
                        if (token == null && !hasParam("secure:accessToken")) {
                            consumer.consumePropertyError("provider.authType.token", "mandatory 'provider.authType.token' property is not specified")
                        }
                    }
                }
            }

            /**
             * Authentication using access token
             */
            fun token(init: AuthType.Token.() -> Unit = {}) : AuthType.Token {
                val result = AuthType.Token()
                result.init()
                return result
            }

            /**
             * Filter by source branch
             */
            var filterSourceBranch by stringParameter()

            /**
             * Filter by target branch
             */
            var filterTargetBranch by stringParameter()

            override fun validate(consumer: ErrorConsumer) {
                authType?.validate(consumer)
            }
        }

        class BitbucketServer() : Provider("bitbucketServer") {

            /**
             * GitHub/GHE server URL
             */
            var serverUrl by stringParameter()

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("authenticationType")

            sealed class AuthType(value: String? = null): CompoundParam<AuthType>(value) {
                abstract fun validate(consumer: ErrorConsumer)

                class VcsRoot() : AuthType("vcsRoot") {

                    override fun validate(consumer: ErrorConsumer) {
                    }
                }

                class Password() : AuthType("password") {

                    /**
                     * Username
                     */
                    var username by stringParameter()

                    /**
                     * Password
                     */
                    var password by stringParameter("secure:password")

                    override fun validate(consumer: ErrorConsumer) {
                        if (username == null && !hasParam("username")) {
                            consumer.consumePropertyError("provider.authType.username", "mandatory 'provider.authType.username' property is not specified")
                        }
                        if (password == null && !hasParam("secure:password")) {
                            consumer.consumePropertyError("provider.authType.password", "mandatory 'provider.authType.password' property is not specified")
                        }
                    }
                }

                class Token() : AuthType("token") {

                    /**
                     * Access token to use
                     */
                    var token by stringParameter("secure:accessToken")

                    override fun validate(consumer: ErrorConsumer) {
                        if (token == null && !hasParam("secure:accessToken")) {
                            consumer.consumePropertyError("provider.authType.token", "mandatory 'provider.authType.token' property is not specified")
                        }
                    }
                }
            }

            /**
             * Use VCS root credentials
             */
            fun vcsRoot() = AuthType.VcsRoot()

            /**
             * Username and password
             */
            fun password(init: AuthType.Password.() -> Unit = {}) : AuthType.Password {
                val result = AuthType.Password()
                result.init()
                return result
            }

            /**
             * Authentication using access token
             */
            fun token(init: AuthType.Token.() -> Unit = {}) : AuthType.Token {
                val result = AuthType.Token()
                result.init()
                return result
            }

            /**
             * Filter by source branch
             */
            var filterSourceBranch by stringParameter()

            /**
             * Filter by target branch
             */
            var filterTargetBranch by stringParameter()

            override fun validate(consumer: ErrorConsumer) {
                authType?.validate(consumer)
            }
        }

        class BitbucketCloud() : Provider("bitbucketCloud") {

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("authenticationType")

            sealed class AuthType(value: String? = null): CompoundParam<AuthType>(value) {
                abstract fun validate(consumer: ErrorConsumer)

                class VcsRoot() : AuthType("vcsRoot") {

                    override fun validate(consumer: ErrorConsumer) {
                    }
                }

                class Password() : AuthType("password") {

                    /**
                     * Username
                     */
                    var username by stringParameter()

                    /**
                     * Password
                     */
                    var password by stringParameter("secure:password")

                    override fun validate(consumer: ErrorConsumer) {
                        if (username == null && !hasParam("username")) {
                            consumer.consumePropertyError("provider.authType.username", "mandatory 'provider.authType.username' property is not specified")
                        }
                        if (password == null && !hasParam("secure:password")) {
                            consumer.consumePropertyError("provider.authType.password", "mandatory 'provider.authType.password' property is not specified")
                        }
                    }
                }
            }

            /**
             * Use VCS root credentials
             */
            fun vcsRoot() = AuthType.VcsRoot()

            /**
             * Username and password
             */
            fun password(init: AuthType.Password.() -> Unit = {}) : AuthType.Password {
                val result = AuthType.Password()
                result.init()
                return result
            }

            /**
             * Filter by target branch
             */
            var filterTargetBranch by stringParameter()

            override fun validate(consumer: ErrorConsumer) {
                authType?.validate(consumer)
            }
        }

        class AzureDevOps() : Provider("azureDevOps") {

            /**
             * Azure DevOps project page URL
             */
            var projectUrl by stringParameter()

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("authenticationType")

            sealed class AuthType(value: String? = null): CompoundParam<AuthType>(value) {
                abstract fun validate(consumer: ErrorConsumer)

                class Token() : AuthType("token") {

                    /**
                     * Access token to use
                     */
                    var token by stringParameter("secure:accessToken")

                    override fun validate(consumer: ErrorConsumer) {
                        if (token == null && !hasParam("secure:accessToken")) {
                            consumer.consumePropertyError("provider.authType.token", "mandatory 'provider.authType.token' property is not specified")
                        }
                    }
                }
            }

            /**
             * Authentication using access token
             */
            fun token(init: AuthType.Token.() -> Unit = {}) : AuthType.Token {
                val result = AuthType.Token()
                result.init()
                return result
            }

            /**
             * Filter by source branch
             */
            var filterSourceBranch by stringParameter()

            /**
             * Filter by target branch
             */
            var filterTargetBranch by stringParameter()

            override fun validate(consumer: ErrorConsumer) {
                authType?.validate(consumer)
            }
        }

        class JetbrainsSpace() : Provider("jetbrainsSpace") {

            /**
             * Filter by target branch
             */
            var filterTargetBranch by stringParameter()

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
                            consumer.consumePropertyError("provider.authType.connectionId", "mandatory 'provider.authType.connectionId' property is not specified")
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

            override fun validate(consumer: ErrorConsumer) {
                authType?.validate(consumer)
            }
        }
    }

    /**
     * GitHub or GitHub Enterprise
     */
    fun github(init: Provider.Github.() -> Unit = {}) : Provider.Github {
        val result = Provider.Github()
        result.init()
        return result
    }

    /**
     * GitLab.com or GitLab CE/EE
     */
    fun gitlab(init: Provider.Gitlab.() -> Unit = {}) : Provider.Gitlab {
        val result = Provider.Gitlab()
        result.init()
        return result
    }

    /**
     * Bitbucket Server
     */
    fun bitbucketServer(init: Provider.BitbucketServer.() -> Unit = {}) : Provider.BitbucketServer {
        val result = Provider.BitbucketServer()
        result.init()
        return result
    }

    /**
     * Bitbucket Cloud
     */
    fun bitbucketCloud(init: Provider.BitbucketCloud.() -> Unit = {}) : Provider.BitbucketCloud {
        val result = Provider.BitbucketCloud()
        result.init()
        return result
    }

    /**
     * Azure DevOps Services/Server
     */
    fun azureDevOps(init: Provider.AzureDevOps.() -> Unit = {}) : Provider.AzureDevOps {
        val result = Provider.AzureDevOps()
        result.init()
        return result
    }

    /**
     * JetBrains Space
     */
    fun jetbrainsSpace(init: Provider.JetbrainsSpace.() -> Unit = {}) : Provider.JetbrainsSpace {
        val result = Provider.JetbrainsSpace()
        result.init()
        return result
    }

    /**
     * Pull request contributor role filter options
     */
    enum class GitHubRoleFilter {
        MEMBER,
        MEMBER_OR_COLLABORATOR,
        EVERYBODY;

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (provider == null && !hasParam("providerType")) {
            consumer.consumePropertyError("provider", "mandatory 'provider' property is not specified")
        }
        provider?.validate(consumer)
    }
}


/**
 * Enables [pull requests integration](https://www.jetbrains.com/help/teamcity/?Pull+Requests)
 * @see PullRequests
 */
fun BuildFeatures.pullRequests(init: PullRequests.() -> Unit): PullRequests {
    val result = PullRequests(init)
    feature(result)
    return result
}
