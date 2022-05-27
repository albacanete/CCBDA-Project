package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Build feature enabling [automatic merge](https://www.jetbrains.com/help/teamcity/?Automatic+Merge)
 * in build configuration or template.
 * 
 * @see merge
 */
open class AutoMerge : BuildFeature {
    constructor(init: AutoMerge.() -> Unit = {}, base: AutoMerge? = null): super(base = base as BuildFeature?) {
        type = "AutoMergeFeature"
        init()
    }

    /**
     * A filter for [logical](https://www.jetbrains.com/help/teamcity/?Working+with+Feature+Branches#WorkingwithFeatureBranches-Logicalbranchname)
     * names of the branches whose build's sources will be merged.
     */
    var branchFilter by stringParameter("teamcity.automerge.srcBranchFilter")

    /**
     * A [logical](https://www.jetbrains.com/help/teamcity/?Working+with+Feature+Branches#WorkingwithFeatureBranches-Logicalbranchname)
     * name of the destination branch the sources will be merged to.
     * The branch must be present in a repository and included into the [branch specification](https://www.jetbrains.com/help/teamcity/?Working+with+Feature+Branches#WorkingwithFeatureBranches-Configuringbranches).
     */
    var destinationBranch by stringParameter("teamcity.automerge.dstBranch")

    /**
     * A merge commit message
     */
    var commitMessage by stringParameter("teamcity.automerge.message")

    /**
     * Specifies settings for merge commit
     * @see MergePolicy
     */
    var mergePolicy by enumParameter<MergePolicy>("teamcity.merge.policy", mapping = MergePolicy.mapping)

    /**
     * A condition which should be satisfied for merge to happen
     * @see MergeCondition
     */
    var mergeCondition by stringParameter("teamcity.automerge.buildStatusCondition")

    /**
     * Run policy of auto merge
     * @see RunPolicy
     */
    var runPolicy by enumParameter<RunPolicy>("teamcity.automerge.run.policy", mapping = RunPolicy.mapping)

    /**
     * Settings for merge commit
     */
    enum class MergePolicy {
        /**
         * Use fast-forward merge if possible
         */
        FAST_FORWARD,
        /**
         * Always create a merge commit even when fast-forward merge is possible
         */
        ALWAYS_MERGE;

        companion object {
            val mapping = mapOf<MergePolicy, String>(FAST_FORWARD to "fastForward", ALWAYS_MERGE to "alwaysCreateMergeCommit")
        }

    }
    /**
     * A condition which should be satisfied for merge to happen
     */
    enum class MergeCondition {
        /**
         * Merge sources in successful builds only
         */
        SUCCESSFUL_BUILD,
        /**
         * Merge sources in build if there no new test failures
         */
        NO_NEW_FAILED_TESTS;

        companion object {
            val mapping = mapOf<MergeCondition, String>(SUCCESSFUL_BUILD to "successful", NO_NEW_FAILED_TESTS to "noNewTests")
        }

    }
    /**
     * Policy for running merge feature
     */
    enum class RunPolicy {
        /**
         * Run merge before build finish
         * It will affect duration of build but status will be not changed after build finish
         */
        BEFORE_BUILD_FINISH,
        /**
         * Run merge after build finish.
         * Build will be marked as finished when merge is not performed. If merge fails the status of the build can be changed
         */
        AFTER_BUILD_FINISH;

        companion object {
            val mapping = mapOf<RunPolicy, String>(BEFORE_BUILD_FINISH to "runBeforeBuildFinish", AFTER_BUILD_FINISH to "runAfterBuildFinish")
        }

    }
}


/**
 * Enables [automatic merge](https://www.jetbrains.com/help/teamcity/?Automatic+Merge) in the
 * build configuration or template
 * @see AutoMerge
 */
fun BuildFeatures.merge(base: AutoMerge? = null, init: AutoMerge.() -> Unit = {}) {
    feature(AutoMerge(init, base))
}
