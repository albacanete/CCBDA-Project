package jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?PowerShell) running PowerShell script
 * 
 * @see powerShell
 */
open class PowerShellStep() : BuildStep() {

    init {
        type = "jetbrains_powershell"
    }

    constructor(init: PowerShellStep.() -> Unit): this() {
        init()
    }

    @Deprecated("This property allows to specify a limited number of versions, please use minRequiredVersion instead.")
    var minVersion by enumParameter<Version>("jetbrains_powershell_minVersion", mapping = Version.mapping)

    /**
     * A minimum required PowerShell version installed on a build agent
     */
    var minRequiredVersion by stringParameter("jetbrains_powershell_minVersion")

    /**
     * A required platform bitness
     * @see Platform
     */
    var platform by enumParameter<Platform>("jetbrains_powershell_bitness")

    /**
     * PowerShell edition to use
     * @see Edition
     */
    var edition by enumParameter<Edition>("jetbrains_powershell_edition", mapping = Edition.mapping)

    /**
     * Specifies how the error output is handled. If set to true any output to stderr is handled as an error.
     * By default any output to stderr is handled as a warning.
     */
    var formatStderrAsError by booleanParameter("jetbrains_powershell_errorToError", trueValue = "true", falseValue = "")

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for ant script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * PowerShell script execution mode
     */
    var scriptMode by compoundParameter<ScriptMode>("jetbrains_powershell_script_mode")

    sealed class ScriptMode(value: String? = null): CompoundParam<ScriptMode>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class File() : ScriptMode("FILE") {

            /**
             * A path to the script to run
             */
            var path by stringParameter("jetbrains_powershell_script_file")

            override fun validate(consumer: ErrorConsumer) {
                if (path == null && !hasParam("jetbrains_powershell_script_file")) {
                    consumer.consumePropertyError("scriptMode.path", "mandatory 'scriptMode.path' property is not specified")
                }
            }
        }

        class Script() : ScriptMode("CODE") {

            /**
             * A PowerShell script content
             */
            var content by stringParameter("jetbrains_powershell_script_code")

            override fun validate(consumer: ErrorConsumer) {
                if (content == null && !hasParam("jetbrains_powershell_script_code")) {
                    consumer.consumePropertyError("scriptMode.content", "mandatory 'scriptMode.content' property is not specified")
                }
            }
        }
    }

    /**
     * Run a PowerShell script at the given path
     */
    fun file(init: ScriptMode.File.() -> Unit = {}) : ScriptMode.File {
        val result = ScriptMode.File()
        result.init()
        return result
    }

    /**
     * Run a PowerShell script with the given content
     */
    fun script(init: ScriptMode.Script.() -> Unit = {}) : ScriptMode.Script {
        val result = ScriptMode.Script()
        result.init()
        return result
    }

    /**
     * PowerShell execution mode
     * @see ExecutionMode
     */
    var scriptExecMode by enumParameter<ExecutionMode>("jetbrains_powershell_execution")

    /**
     * Whether the script should be executed with the -NoProfile added
     */
    var noProfile by booleanParameter("jetbrains_powershell_noprofile", trueValue = "true", falseValue = "")

    /**
     * Additional arguments to be passed to PowerShell
     */
    var args by stringParameter("jetbrains_powershell_additionalArguments")

    /**
     * Build parameters to be passed as arguments into the PowerShell script
     */
    var scriptArgs by stringParameter("jetbrains_powershell_scriptArguments")

    /**
     * A required platform bitness
     */
    enum class Platform {
        /**
         * Require a 32-bit platform
         */
        x86,
        /**
         * Require a 64-bit platform
         */
        x64;

    }
    enum class Version {
        Any,
        v1_0,
        v2_0,
        v3_0,
        v4_0,
        v5_0;

        companion object {
            val mapping = mapOf<Version, String>(Any to "", v1_0 to "1.0", v2_0 to "2.0", v3_0 to "3.0", v4_0 to "4.0", v5_0 to "5.0")
        }

    }
    /**
     * PowerShell script execution mode
     */
    enum class ExecutionMode {
        /**
         * Run script using the -Command argument
         */
        STDIN,
        /**
         * Run script with the -ExecutionPolicy ByPass argument to enable arbitrary scripts execution
         */
        PS1;

    }
    /**
     * A PowerShell edition
     */
    enum class Edition {
        /**
         * Use any edition
         */
        Any,
        /**
         * Open-source edition based on .Net Core, cross-platform, 64-bit only
         */
        Core,
        /**
         * Closed-source edition bundled with Windows, available only on Windows platforms.
         */
        Desktop;

        companion object {
            val mapping = mapOf<Edition, String>(Any to "", Core to "Core", Desktop to "Desktop")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (scriptMode == null && !hasParam("jetbrains_powershell_script_mode")) {
            consumer.consumePropertyError("scriptMode", "mandatory 'scriptMode' property is not specified")
        }
        scriptMode?.validate(consumer)
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?PowerShell) running PowerShell script
 * @see PowerShellStep
 */
fun BuildSteps.powerShell(init: PowerShellStep.() -> Unit): PowerShellStep {
    val result = PowerShellStep(init)
    step(result)
    return result
}
