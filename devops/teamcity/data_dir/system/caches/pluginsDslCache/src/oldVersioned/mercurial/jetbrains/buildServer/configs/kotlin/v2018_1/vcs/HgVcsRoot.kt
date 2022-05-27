package jetbrains.buildServer.configs.kotlin.v2018_1.vcs

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Mercurial [VCS root](https://confluence.jetbrains.com/display/TCDL/Mercurial)
 */
open class HgVcsRoot() : VcsRoot() {

    init {
        type = "mercurial"
    }

    constructor(init: HgVcsRoot.() -> Unit): this() {
        init()
    }

    /**
     * Mercurial repository URL
     */
    var url by stringParameter("repositoryPath")

    /**
     * Default branch name
     */
    var branch by stringParameter("branchName")

    /**
     * [Branch specification](https://confluence.jetbrains.com/display/TCDL/Working+with+Feature+Branches#WorkingwithFeatureBranches-branchSpec)
     * to use in VCS root
     */
    var branchSpec by stringParameter("teamcity:branchSpec")

    /**
     * Allows to use tags in branch specification. By default, tags are ignored.
     */
    var useTagsAsBranches by booleanParameter()

    /**
     * Whether TeamCity should detect changes in submodules
     */
    var detectSubrepoChanges by booleanParameter()

    /**
     * A custom username for tags. Format: User Name <email>
     */
    var userForTags by stringParameter("tagUsername")

    /**
     * [Path](https://confluence.jetbrains.com/display/TCDL/Mercurial#Mercurial-hgDetection) to hg executable
     */
    var hgPath by stringParameter("hgCommandPath")

    /**
     * Mercurial configuration options to be applied to the repository in the
     * standard [format](https://www.selenic.com/mercurial/hgrc.5.html)
     */
    var customHgConfig by stringParameter()

    /**
     * A username for mercurial connection
     */
    var userName by stringParameter("username")

    /**
     * A password for mercurial connection
     */
    var password by stringParameter("secure:password")

    /**
     * Specifies whether TeamCity should run the [hg purge](https://www.mercurial-scm.org/wiki/PurgeExtension) command and what files should be purged
     * @see PurgePolicy
     */
    var purgePolicy by enumParameter<PurgePolicy>()

    /**
     * When set to true, TeamCity creates a local agent mirror first (under agent's system/mercurial directory) and then clones to the
     * build [checkout directory](https://confluence.jetbrains.com/display/TCDL/Build+Checkout+Directory) from this local mirror.
     * This option speeds up clean checkout, because only the build working directory is cleaned.
     * Also, if a single root is used in several build configurations, a clone will be faster.
     */
    var useMirrors by booleanParameter("useSharedMirrors")

    /**
     * Defines [hg purge](https://www.mercurial-scm.org/wiki/PurgeExtension) settings
     */
    enum class PurgePolicy {
        /**
         * Don't run the "hg purge"
         */
        DONT_RUN,
        /**
         * Purge unknown files and empty directories
         */
        PURGE_UNKNOWN,
        /**
         * Purge unknown files, empty directories, and ignored files. Runs "hg purge --all".
         */
        PURGE_ALL;

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (url == null && !hasParam("repositoryPath")) {
            consumer.consumePropertyError("url", "mandatory 'url' property is not specified")
        }
    }
}


