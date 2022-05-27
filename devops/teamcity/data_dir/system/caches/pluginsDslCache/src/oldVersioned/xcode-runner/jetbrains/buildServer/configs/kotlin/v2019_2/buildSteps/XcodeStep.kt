package jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * An [Xcode build step](https://www.jetbrains.com/help/teamcity/?Xcode+Project) for running Xcode projects.
 * 
 * @see xcode
 */
open class XcodeStep() : BuildStep() {

    init {
        type = "Xcode"
    }

    constructor(init: XcodeStep.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for the executable,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * The path to a (.xcodeproj) project file or a (.xcworkspace) workspace file,
     * should be relative to the checkout directory.
     * For Xcode 3 build, only the path to the project file is supported.
     */
    var projectPath by stringParameter("project")

    /**
     * The path to Xcode on the agent. The build will be run using this Xcode.
     */
    var xcodePath by stringParameter()

    /**
     * Select either a target-based (for project) or scheme-based (for project and workspace) build.
     * Depending on the selection, the settings will vary.
     */
    var buildType by compoundParameter<BuildType>("xcode")

    sealed class BuildType(value: String? = null): CompoundParam<BuildType>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class TargetBased() : BuildType("3") {

            /**
             * Xcode target to execute.
             */
            var target by stringParameter()

            /**
             * Xcode configuration.
             */
            var configuration by stringParameter()

            /**
             * Use predefined platforms "macosx" (Mac OS X), "iphoneos" (iOS), "iphonesimulator" (Simulator - iOS)
             * or any other platform (if it is provided by the agent) to build your project on.
             */
            var platform by stringParameter()

            /**
             * An SDK to build your project with.
             * Set to the SDKs available on your agents for the platform selected.
             */
            var sdk by stringParameter()

            /**
             * An architecture to build your project with.
             * Seto to the architectures available on your agents for the platform selected.
             */
            var architecture by stringParameter("arch")

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class SchemeBased() : BuildType("4") {

            var scheme by stringParameter()

            var outputDirectory by compoundParameter<OutputDirectory>("useCustomBuildOutputDir")

            sealed class OutputDirectory(value: String? = null): CompoundParam<OutputDirectory>(value) {
                class Default() : OutputDirectory("") {

                }

                class Custom() : OutputDirectory("true") {

                    var path by stringParameter("customBuildOutputDir")

                }
            }

            fun default() = OutputDirectory.Default()

            fun custom(init: OutputDirectory.Custom.() -> Unit = {}) : OutputDirectory.Custom {
                val result = OutputDirectory.Custom()
                result.init()
                return result
            }

            override fun validate(consumer: ErrorConsumer) {
                if (scheme == null && !hasParam("scheme")) {
                    consumer.consumePropertyError("buildType.scheme", "mandatory 'buildType.scheme' property is not specified")
                }
            }
        }
    }

    fun targetBased(init: BuildType.TargetBased.() -> Unit = {}) : BuildType.TargetBased {
        val result = BuildType.TargetBased()
        result.init()
        return result
    }

    fun schemeBased(init: BuildType.SchemeBased.() -> Unit = {}) : BuildType.SchemeBased {
        val result = BuildType.SchemeBased()
        result.init()
        return result
    }

    /**
     * Xcode build action(s). The default actions are clean and build.
     * A space-separated list of the following actions is supported: clean, build, test, archive, installsrc, install;
     * the order of actions will be preserved during execution.
     * It is not recommended changing this field unless you are sure you want to change the number or order of actions.
     * If your project is built under Xcode 5+, setting the runTests option
     * automatically adds the test build action to the list (unless the option is already explicitly specified in the current field).
     */
    var buildActions by stringParameter()

    /**
     * Whether to run tests after your project is built.
     */
    var runTests by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Other command line parameters to be passed to the xcodebuild utility.
     */
    var additionalCommandLineParameters by stringParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (projectPath == null && !hasParam("project")) {
            consumer.consumePropertyError("projectPath", "mandatory 'projectPath' property is not specified")
        }
        buildType?.validate(consumer)
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Xcode+Project) for running Xcode projects.
 * @see XcodeStep
 */
fun BuildSteps.xcode(init: XcodeStep.() -> Unit): XcodeStep {
    val result = XcodeStep(init)
    step(result)
    return result
}
