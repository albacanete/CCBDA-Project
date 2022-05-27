package jetbrains.buildServer.configs.kotlin.v10.vcs

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Git [VCS root](https://www.jetbrains.com/help/teamcity/?Git)
 */
open class GitVcsRoot : VcsRoot {
    constructor(init: GitVcsRoot.() -> Unit = {}, base: GitVcsRoot? = null): super(base = base as VcsRoot?) {
        type = "jetbrains.git"
        init()
    }

    /**
     * Repository url
     */
    var url by stringParameter()

    /**
     * Custom repository push url. If not specified then the url specified in the url property is used for push operations.
     * @see url
     */
    var pushUrl by stringParameter("push_url")

    /**
     * The default branch name
     */
    var branch by stringParameter()

    /**
     * [Branch specification](https://www.jetbrains.com/help/teamcity/?Working+with+Feature+Branches#WorkingwithFeatureBranches-branchSpec)
     * to use in VCS root
     */
    var branchSpec by stringParameter("teamcity:branchSpec")

    /**
     * When enabled tags matched by branch specification will be shown in UI as regular branches.
     * By default false.
     */
    var useTagsAsBranches by booleanParameter("reportTagRevisions", trueValue = "true", falseValue = "")

    /**
     * Defines how TeamCity retrieves [VCS username](https://www.jetbrains.com/help/teamcity/?Managing+Users+and+User+Groups#ManagingUsersandUserGroups-vcsUsername)
     * from git commit.
     * @see UserNameStyle
     */
    var userNameStyle by enumParameter<UserNameStyle>("usernameStyle")

    /**
     * Whether VCS root should include changes in submodules and check their sources for build.
     * By default submodules are checked out.
     * @see CheckoutSubmodules
     */
    var checkoutSubmodules by enumParameter<CheckoutSubmodules>("submoduleCheckout", mapping = CheckoutSubmodules.mapping)

    /**
     * A username for tag/merge operations in this VCS root.
     * Format: Username <email>
     */
    var userForTags by stringParameter()

    /**
     * Whether TeamCity should convert line-endings of all text files to CRLF during server-side checkout.
     * By default false.
     */
    var serverSideAutoCRLF by booleanParameter("serverSideAutoCrlf", trueValue = "true", falseValue = "")

    /**
     * Custom path to git executable on the build agent machine
     */
    var agentGitPath by stringParameter()

    /**
     * Specifies when the "git clean" command should be executed in case of agent-side checkout
     * @see AgentCleanPolicy
     */
    var agentCleanPolicy by enumParameter<AgentCleanPolicy>()

    /**
     * Specifies which files should be removed when "git clean" command is executed during agent-side checkout.
     * @see AgentCleanFilesPolicy
     */
    var agentCleanFilesPolicy by enumParameter<AgentCleanFilesPolicy>()

    /**
     * When this option is enabled, TeamCity creates a separate clone of the repository on each agent
     * and uses it in the checkout directory via git alternates. This make agent-side checkout faster.
     * By default true.
     */
    @Deprecated("Use checkoutPolicy parameter instead. Corresponding values are NO_MIRRORS for false and USE_MIRRORS for true (default value, can be ommitted).")
    var useMirrors by booleanParameter("useAlternates", trueValue = "true", falseValue = "")

    /**
     * Policy for checking out sources on agent
     */
    var checkoutPolicy by enumParameter<AgentCheckoutPolicy>("useAlternates")

    /**
     * VCS Root authentication method
     */
    var authMethod by compoundParameter<AuthMethod>()

    sealed class AuthMethod(value: String? = null): CompoundParam(value) {
        class Anonymous() : AuthMethod("ANONYMOUS") {

        }

        class Password() : AuthMethod("PASSWORD") {

            /**
             * Username to use, overwrites the username in the url
             */
            var userName by stringParameter("username")

            /**
             * Password to use
             */
            var password by stringParameter("secure:password")

        }

        class UploadedKey() : AuthMethod("TEAMCITY_SSH_KEY") {

            /**
             * Username to use, overwrites the username in the url
             */
            var userName by stringParameter("username")

            /**
             * Name of the uploaded [SSH key](https://www.jetbrains.com/help/teamcity/?SSH+Keys+Management) to use
             */
            var uploadedKey by stringParameter("teamcitySshKey")

            /**
             * Passphrase for the uploaded [SSH key](https://www.jetbrains.com/help/teamcity/?SSH+Keys+Management).
             * Leave it empty if the key is not encrypted.
             */
            var passphrase by stringParameter("secure:passphrase")

        }

        class DefaultPrivateKey() : AuthMethod("PRIVATE_KEY_DEFAULT") {

            /**
             * Username to use, overwrites the username in the url
             */
            var userName by stringParameter("username")

        }

        class CustomPrivateKey() : AuthMethod("PRIVATE_KEY_FILE") {

            /**
             * Username to use, overwrites the username in the url
             */
            var userName by stringParameter("username")

            /**
             * Path to the SSH key on TeamCity server machine
             */
            var customKeyPath by stringParameter("privateKeyPath")

            /**
             * Passphrase for the key. Leave it empty if the key is not encrypted.
             */
            var passphrase by stringParameter("secure:passphrase")

        }
    }

    /**
     * Anonymous repository access
     */
    fun anonymous() = AuthMethod.Anonymous()

    /**
     * Password authentication
     */
    fun password(init: AuthMethod.Password.() -> Unit = {}) : AuthMethod.Password {
        val result = AuthMethod.Password()
        result.init()
        return result
    }

    /**
     * Uploaded [SSH key](https://www.jetbrains.com/help/teamcity/?SSH+Keys+Management) with the specified name.
     */
    fun uploadedKey(init: AuthMethod.UploadedKey.() -> Unit = {}) : AuthMethod.UploadedKey {
        val result = AuthMethod.UploadedKey()
        result.init()
        return result
    }

    /**
     * Default SSH key found on the machine.
     * If you use an agent-side checkout, then this key should also be available on the build agent machines.
     * Often it is easier to use the uploaded SSH key.
     * @see uploadedKey
     */
    fun defaultPrivateKey(init: AuthMethod.DefaultPrivateKey.() -> Unit = {}) : AuthMethod.DefaultPrivateKey {
        val result = AuthMethod.DefaultPrivateKey()
        result.init()
        return result
    }

    /**
     * SSH key on the specified path. Supported only for server-side checkout.
     * Switch to uploaded SSH key if you want to use an agent-side checkout.
     * @see uploadedKey
     */
    fun customPrivateKey(init: AuthMethod.CustomPrivateKey.() -> Unit = {}) : AuthMethod.CustomPrivateKey {
        val result = AuthMethod.CustomPrivateKey()
        result.init()
        return result
    }

    /**
     * Policy for checking out sources on agent
     */
    enum class AgentCheckoutPolicy {
        /**
         * Uses shallow clone for short-lived agents and mirrors for regular long-lived agents.
         */
        AUTO,
        /**
         * Creates repository mirror on the agent machine and shares it between different builds with the same fetch URL. Most optimal approach for large repositories and long-lived agents.
         */
        USE_MIRRORS,
        /**
         * Performs checkout right into the checkout directory without creating a mirror. Less optimal in terms of disk usage than mirrors.
         */
        NO_MIRRORS,
        /**
         * Uses git shallow clone to checkout build revision (--depth 1). Ideal for short-lived agents.
         */
        SHALLOW_CLONE;

    }
    /**
     * Submodules checkout mode
     */
    enum class CheckoutSubmodules {
        /**
         * Checkout submodules and show submodule changes in UI
         */
        SUBMODULES_CHECKOUT,
        /**
         * Don't checkout submodules and don't show changes from submodules in UI
         */
        IGNORE;

        companion object {
            val mapping = mapOf<CheckoutSubmodules, String>(SUBMODULES_CHECKOUT to "CHECKOUT", IGNORE to "IGNORE")
        }

    }
    /**
     * Specifies when the "git clean" command should be executed in case of agent-side checkout
     */
    enum class AgentCleanPolicy {
        /**
         * Don't run the "git clean" command
         */
        NEVER,
        /**
         * Run the "git clean" command before each build
         */
        ALWAYS,
        /**
         * Run the "git clean" command if the branch in build is different comparing to the branch in the previous build on same agent
         */
        ON_BRANCH_CHANGE;

    }
    /**
     * Defines how TeamCity retrieves [VCS username](https://www.jetbrains.com/help/teamcity/?Managing+Users+and+User+Groups#ManagingUsersandUserGroups-vcsUsername)
     * from git commit.
     * When the git config contains the following
     * ```
     * [user]
     * name = Joe Coder
     * email = joe.coder@acme.com
     * ```
     * then the git username in commit is `Joe Coder <joe.coder@acme.com>`.
     * Different options specify which part of the git commit username is used in TeamCity.
     */
    enum class UserNameStyle {
        /**
         * Use the name part, for full name `Joe Coder <joe.coder@acme.com>` it will be `Joe Coder`
         */
        NAME,
        /**
         * Use part of the email before the @ sign, for full name `Joe Coder <joe.coder@acme.com>` it will be `joe.coder`
         */
        USERID,
        /**
         * Use the email part, for full name `Joe Coder <joe.coder@acme.com>` it will be `joe.coder@acme.com`
         */
        EMAIL,
        /**
         * Use full commit username, i.e. `Joe Coder <joe.coder@acme.com>`
         */
        FULL;

    }
    /**
     * Specifies flags for the "git clean" command during agent-side checkout and defines which files will be removed.
     */
    enum class AgentCleanFilesPolicy {
        /**
         * Will run "git clean -dfX"
         */
        IGNORED_ONLY,
        /**
         * Will run "git clean -df"
         */
        NON_IGNORED_ONLY,
        /**
         * Will run "git clean -dfx"
         */
        ALL_UNTRACKED;

    }
}


