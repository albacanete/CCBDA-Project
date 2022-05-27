package jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * Project feature defining a custom tab to be shown for all builds of the current project
 * 
 * @see buildReportTab
 */
open class BuildReportTab() : ProjectFeature() {

    init {
        type = "ReportTab"
        param("type", "BuildReportTab")
    }

    constructor(init: BuildReportTab.() -> Unit): this() {
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (title == null && !hasParam("title")) {
            consumer.consumePropertyError("title", "mandatory 'title' property is not specified")
        }
    }
}


/**
 * Creates a custom tab for every build of the current project
 * @see BuildReportTab
 */
fun ProjectFeatures.buildReportTab(init: BuildReportTab.() -> Unit): BuildReportTab {
    val result = BuildReportTab(init)
    feature(result)
    return result
}
