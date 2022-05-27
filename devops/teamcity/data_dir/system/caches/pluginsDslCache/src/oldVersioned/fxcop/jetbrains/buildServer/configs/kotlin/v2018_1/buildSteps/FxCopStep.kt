package jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * An [FxCop build step](https://www.jetbrains.com/help/teamcity/?FxCop) is intended for inspecting .NET assemblies
 * and reporting possible design, localization, performance, and security improvements.
 * 
 * @see fxCop
 */
open class FxCopStep() : BuildStep() {

    init {
        type = "FxCop"
    }

    constructor(init: FxCopStep.() -> Unit): this() {
        init()
    }

    /**
     * When a build agent is started, it detects automatically whether FxCop is installed.
     * If FxCop is detected, TeamCity defines the %system.FxCopRoot% agent system property.
     * You can also use a custom installation of FxCop or the use FxCop checked in your version control.
     */
    var fxCopInstallation by compoundParameter<FxCopInstallation>("fxcop.detection_mode")

    sealed class FxCopInstallation(value: String? = null): CompoundParam<FxCopInstallation>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Auto() : FxCopInstallation("auto") {

            /**
             * The FxCop version required by the build; the agent requirement will be created.
             * To use any version auto-detected on the agent side, set to 'ANY_DETECTED'.
             */
            var version by enumParameter<FxCopVersion>("fxcop.version", mapping = FxCopVersion.mapping)

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class Manual() : FxCopInstallation("manual") {

            /**
             * The path to the FxCop installation root on the agent machine or
             * the path to an FxCop executable relative to the Build Checkout Directory.
             */
            var installationRoot by stringParameter("fxcop.root")

            override fun validate(consumer: ErrorConsumer) {
                if (installationRoot == null && !hasParam("fxcop.root")) {
                    consumer.consumePropertyError("fxCopInstallation.installationRoot", "mandatory 'fxCopInstallation.installationRoot' property is not specified")
                }
            }
        }
    }

    /**
     * Select to use the FxCop installation on an agent.
     */
    fun auto(init: FxCopInstallation.Auto.() -> Unit = {}) : FxCopInstallation.Auto {
        val result = FxCopInstallation.Auto()
        result.init()
        return result
    }

    /**
     * Select to use a custom installation of FxCop (not the autodetected one),
     * or if you do not have FxCop installed on the build agent
     * (for example, you can place the FxCop tool in your source control, and check it out with the build sources).
     */
    fun manual(init: FxCopInstallation.Manual.() -> Unit = {}) : FxCopInstallation.Manual {
        val result = FxCopInstallation.Manual()
        result.init()
        return result
    }

    var inspectionSource by compoundParameter<InspectionSource>("fxcop.what")

    sealed class InspectionSource(value: String? = null): CompoundParam<InspectionSource>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Assemblies() : InspectionSource("files") {

            /**
             * Enter the paths to the assemblies to be inspected (use Ant-like wildcards to select files by a mask).
             * FxCop will use default settings to inspect them.
             * The paths should be relative to the Build Checkout Directory and separated by spaces.
             * Note that there is a limitation to the maximum number of assemblies
             * that can be specified here due to command-line string limitation.
             */
            var files by stringParameter("fxcop.files")

            /**
             * Exclude wildcards to refine the included assemblies list.
             * Assembly file names relative to the checkout root separated by spaces.
             * Ant-like wildcards are supported. Example: bin*.dll
             */
            var exclude by stringParameter("fxcop.files_exclude")

            override fun validate(consumer: ErrorConsumer) {
                if (files == null && !hasParam("fxcop.files")) {
                    consumer.consumePropertyError("inspectionSource.files", "mandatory 'inspectionSource.files' property is not specified")
                }
            }
        }

        class Project() : InspectionSource("project") {

            /**
             * The FxCop project file name relative to the checkout root.
             */
            var projectFile by stringParameter("fxcop.project")

            override fun validate(consumer: ErrorConsumer) {
                if (projectFile == null && !hasParam("fxcop.project")) {
                    consumer.consumePropertyError("inspectionSource.projectFile", "mandatory 'inspectionSource.projectFile' property is not specified")
                }
            }
        }
    }

    fun assemblies(init: InspectionSource.Assemblies.() -> Unit = {}) : InspectionSource.Assemblies {
        val result = InspectionSource.Assemblies()
        result.init()
        return result
    }

    fun project(init: InspectionSource.Project.() -> Unit = {}) : InspectionSource.Project {
        val result = InspectionSource.Project()
        result.init()
        return result
    }

    /**
     * Search the assemblies referenced by targets in Global Assembly Cache.
     */
    var searchInGAC by booleanParameter("fxcop.search_in_gac", trueValue = "true", falseValue = "")

    /**
     * Search the assemblies referenced by targets in the specified directories separated by spaces.
     * Sets /d: options for FxCopCmd
     */
    var searchInDirs by stringParameter("fxcop.search_in_dirs")

    /**
     * Sets /ignoregeneratedcode for FxCopCmd (note: it's supported since FxCop 1.36).
     * Speeds up inspection.
     */
    var ignoreGeneratedCode by booleanParameter("fxcop.ignore_generated_code", trueValue = "true", falseValue = "")

    /**
     * The path to the XSLT transformation file relative to the Build Checkout Directory or absolute on the agent machine.
     * You can use the path to the detected FxCop on the target machine (i.e. %system.FxCopRoot%/Xml/FxCopReport.xsl).
     * When the Report XSLT file option is set, the build runner will apply an XSLT transform to FxCop XML output and
     * display the resulting HTML in a new FxCop tab on the build results page.
     */
    var reportXSLTFile by stringParameter("fxcop.report_xslt")

    /**
     * Additional options for calling FxCopCmd executable.
     * All entered options will be added to the beginning of the command line parameters.
     */
    var additionalOptions by stringParameter("fxcop.addon_options")

    /**
     * Fails build on analysis errors from FxCop such as:
     * ANALYSIS_ERROR, ASSEMBLY_LOAD_ERROR, ASSEMBLY_REFERENCES_ERROR,
     * PROJECT_LOAD_ERROR, RULE_LIBRARY_LOAD_ERROR, UNKNOWN_ERROR, OUTPUT_ERROR
     */
    var failOnAnalysisError by booleanParameter("fxcop.fail_on_analysis_error", trueValue = "true", falseValue = "")

    enum class FxCopVersion {
        ANY_DETECTED,
        v1_35,
        v9_0,
        v10_0,
        v12_0,
        v14_0,
        v15_0,
        v16_0;

        companion object {
            val mapping = mapOf<FxCopVersion, String>(ANY_DETECTED to "not_specified", v1_35 to "1.35", v9_0 to "9.0", v10_0 to "10.0", v12_0 to "12.0", v14_0 to "14.0", v15_0 to "15.0", v16_0 to "16.0")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        fxCopInstallation?.validate(consumer)
        inspectionSource?.validate(consumer)
    }
}


/**
 * Add an [FxCop build step](https://www.jetbrains.com/help/teamcity/?FxCop).
 * @see FxCopStep
 */
fun BuildSteps.fxCop(init: FxCopStep.() -> Unit): FxCopStep {
    val result = FxCopStep(init)
    step(result)
    return result
}
