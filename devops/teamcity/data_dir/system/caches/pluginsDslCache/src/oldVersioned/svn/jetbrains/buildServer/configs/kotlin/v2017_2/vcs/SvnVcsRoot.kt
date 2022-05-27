package jetbrains.buildServer.configs.kotlin.v2017_2.vcs

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * A Subversion [VCS root](https://www.jetbrains.com/help/teamcity/?Subversion)
 */
open class SvnVcsRoot() : VcsRoot() {

    init {
        type = "svn"
    }

    constructor(init: SvnVcsRoot.() -> Unit): this() {
        init()
    }

    /**
     * Subversion server URL
     */
    var url by stringParameter()

    /**
     * A username for Subversion connection
     */
    var userName by stringParameter("user")

    /**
     * A password for Subversion connection
     */
    var password by stringParameter("secure:svn-password")

    /**
     * Custom Subversion configuration directory to use
     */
    var configDir by stringParameter("svn-config-directory")

    /**
     * Whether the default Subversion configuration directory should be used
     */
    var useDefaultConfigDir by booleanParameter("svn-use-default-config-directory", trueValue = "true", falseValue = "")

    /**
     * A mode of externals support
     * @see ExternalsMode
     */
    var externalsMode by enumParameter<ExternalsMode>("externals-mode", mapping = ExternalsMode.mapping)

    /**
     * Whether TeamCity should accept non-trusted SSL certificates from Subversion server
     */
    var enableNonTrustedSSL by booleanParameter("enable-unsafe-ssl", trueValue = "true", falseValue = "")

    /**
     * Name of the uploaded [SSH key](https://www.jetbrains.com/help/teamcity/?SSH+Keys+Management) to use for connections via SSH.
     */
    var uploadedKey by stringParameter("teamcitySshKey")

    /**
     * A path to SSH key on the TeamCity server machine to use
     */
    var customSshKey by stringParameter("ssh-key-file")

    /**
     * A passphrase for SSH key if it is encrypted
     */
    var passphrase by stringParameter("secure:ssh-passphrase")

    /**
     * SSH port to use
     */
    var sshPort by intParameter("ssh-port")

    /**
     * Subversion working copy format to use
     * @see WorkingCopyFormat
     */
    var workingCopyFormat by enumParameter<WorkingCopyFormat>("working-copy-format", mapping = WorkingCopyFormat.mapping)

    /**
     * [Labeling patterns](https://www.jetbrains.com/help/teamcity/?VCS+Labeling#VCSLabeling-SubversionLabelingRules) to use
     */
    var labelingRules by stringParameter("labelingPatterns")

    /**
     * A message to use for labeling
     */
    var labelingMessage by stringParameter()

    /**
     * If true, TeamCity will always run svn revert before updating sources
     */
    var enforceRevert by booleanParameter("enforce-revert", trueValue = "true", falseValue = "")

    /**
     * A Subversion working copy format
     */
    enum class WorkingCopyFormat {
        v1_8,
        v1_7,
        v1_6,
        v1_5,
        v1_4;

        companion object {
            val mapping = mapOf<WorkingCopyFormat, String>(v1_8 to "1.8", v1_7 to "1.7", v1_6 to "1.6", v1_5 to "1.5", v1_4 to "1.4")
        }

    }
    /**
     * Subversion externals mode
     */
    enum class ExternalsMode {
        /**
         * Detect changes and checkout sources from externals
         */
        FULL_SUPPORT,
        /**
         * Checkout sources from externals, ignore changes
         */
        CHECKOUT,
        /**
         * Don't detect changes and don't checkout sources from externals
         */
        DISABLED;

        companion object {
            val mapping = mapOf<ExternalsMode, String>(FULL_SUPPORT to "externals-full", CHECKOUT to "externals-checkout", DISABLED to "externals-none")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (url == null && !hasParam("url")) {
            consumer.consumePropertyError("url", "mandatory 'url' property is not specified")
        }
    }
}


