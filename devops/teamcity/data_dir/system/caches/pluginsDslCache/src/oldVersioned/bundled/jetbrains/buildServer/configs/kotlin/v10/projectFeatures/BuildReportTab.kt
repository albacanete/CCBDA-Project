package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining a custom tab to be shown for all builds of the current project
 * 
 * @see buildReportTab
 */
open class BuildReportTab : ProjectFeature {
    constructor(init: BuildReportTab.() -> Unit = {}, base: BuildReportTab? = null): super(base = base as ProjectFeature?) {
        type = "ReportTab"
        param("type", "BuildReportTab")
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

}


/**
 * Creates a custom tab for every build of the current project
 * @see BuildReportTab
 */
fun ProjectFeatures.buildReportTab(base: BuildReportTab? = null, init: BuildReportTab.() -> Unit = {}) {
    feature(BuildReportTab(init, base))
}
