package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build feature](https://www.jetbrains.com/help/teamcity/?Pull+Requests) that introduces GitHub pull requests integration
 * 
 * @see pullRequests
 */
open class PullRequests : BuildFeature {
    constructor(init: PullRequests.() -> Unit = {}, base: PullRequests? = null): super(base = base as BuildFeature?) {
        type = "pullRequests"
        init()
    }

    /**
     * Id of the VCS root to extract pull request information from.
     * Set to an empty string to extract pull request information from the first VCS root attached to a build configuration.
     */
    var vcsRootExtId by stringParameter("vcsRootId")

    var provider by compoundParameter<Provider>("providerType")

    sealed class Provider(value: String? = null): CompoundParam(value) {
        class Github() : Provider("github") {

            /**
             * GitHub/GHE server URL
             */
            var serverUrl by stringParameter()

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("authenticationType")

            sealed class AuthType(value: String? = null): CompoundParam(value) {
                class VcsRoot() : AuthType("vcsRoot") {

                }

                class Token() : AuthType("token") {

                    /**
                     * Access token to use
                     */
                    var token by stringParameter("secure:accessToken")

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

            sealed class AuthType(value: String? = null): CompoundParam(value) {
                class Token() : AuthType("token") {

                    /**
                     * Access token to use
                     */
                    var token by stringParameter("secure:accessToken")

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

            sealed class AuthType(value: String? = null): CompoundParam(value) {
                class VcsRoot() : AuthType("vcsRoot") {

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

                }

                class Token() : AuthType("token") {

                    /**
                     * Access token to use
                     */
                    var token by stringParameter("secure:accessToken")

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

        }

        class BitbucketCloud() : Provider("bitbucketCloud") {

            /**
             * Type of authentication
             */
            var authType by compoundParameter<AuthType>("authenticationType")

            sealed class AuthType(value: String? = null): CompoundParam(value) {
                class VcsRoot() : AuthType("vcsRoot") {

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

            sealed class AuthType(value: String? = null): CompoundParam(value) {
                class Token() : AuthType("token") {

                    /**
                     * Access token to use
                     */
                    var token by stringParameter("secure:accessToken")

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
}


/**
 * Enables [pull requests integration](https://www.jetbrains.com/help/teamcity/?Pull+Requests)
 * @see PullRequests
 */
fun BuildFeatures.pullRequests(base: PullRequests? = null, init: PullRequests.() -> Unit = {}) {
    feature(PullRequests(init, base))
}
