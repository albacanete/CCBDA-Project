package jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * Project feature defining a custom tab to be shown on a project level
 * 
 * @see projectReportTab
 */
open class ProjectReportTab() : ProjectFeature() {

    init {
        type = "ReportTab"
        param("type", "ProjectReportTab")
    }

    constructor(init: ProjectReportTab.() -> Unit): this() {
        init()
    }

    /**
     * Report tab title
     */
    var title by stringParameter()

    /**
     * Relative path to an artifact within build artifacts which should be used as a start page (eg, path to index.html)
     */
    var startPage by stringParameter()

    /**
     * Id of a build configuration where to search for a build with report tab artifacts
     */
    var buildType by stringParameter("buildTypeId")

    /**
     * The rule to select the build with report tab artifacts
     * @see SourceBuildRule
     */
    var sourceBuildRule by enumParameter<SourceBuildRule>("revisionRuleName", mapping = SourceBuildRule.mapping)

    /**
     * A tag for the build when BuildRule.TAG rule is used
     * @see SourceBuildRule
     */
    var sourceBuildTag by stringParameter("revisionRuleBuildTag")

    /**
     * A build number for the build when BuildRule.BUILD_NUMBER rule is used
     * @see SourceBuildRule
     */
    var sourceBuildNumber by stringParameter("revisionRuleBuildNumber")

    /**
     * A branch filter to limit the set of builds by their branches.
     */
    var sourceBuildBranchFilter by stringParameter("revisionRuleBranchFilter")

    /**
     * Specifies build where to search for report tab
     */
    enum class SourceBuildRule {
        /**
         * Use artifacts of the last finished build
         */
        LAST_FINISHED,
        /**
         * Use artifacts of the last successful build
         */
        LAST_SUCCESSFUL,
        /**
         * Use artifacts of the last pinned build
         */
        LAST_PINNED,
        /**
         * Use artifacts of the last build with the specified tag
         */
        TAG,
        /**
         * Use artifacts of the last build with the specified build number
         */
        BUILD_NUMBER;

        companion object {
            val mapping = mapOf<SourceBuildRule, String>(LAST_FINISHED to "lastFinished", LAST_SUCCESSFUL to "lastSuccessful", LAST_PINNED to "lastPinned", TAG to "buildTag", BUILD_NUMBER to "buildNumber")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (title == null && !hasParam("title")) {
            consumer.consumePropertyError("title", "mandatory 'title' property is not specified")
        }
    }
}


/**
 * Creates a custom tab on a project report with content based on artifacts of some build under this project
 * @see ProjectReportTab
 */
fun ProjectFeatures.projectReportTab(init: ProjectReportTab.() -> Unit): ProjectReportTab {
    val result = ProjectReportTab(init)
    feature(result)
    return result
}
