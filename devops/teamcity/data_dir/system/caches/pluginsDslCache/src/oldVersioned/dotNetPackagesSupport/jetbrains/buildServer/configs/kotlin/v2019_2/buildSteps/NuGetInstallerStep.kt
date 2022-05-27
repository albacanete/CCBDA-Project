package jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [NuGet installer step](https://confluence.jetbrains.com/display/TCDL/NuGet+Installer) to run nuget restore command
 * 
 * @see nuGetInstaller
 */
open class NuGetInstallerStep() : BuildStep() {

    init {
        type = "jb.nuget.installer"
    }

    constructor(init: NuGetInstallerStep.() -> Unit): this() {
        init()
    }

    /**
     * Specify path to NuGet.exe.
     */
    var toolPath by stringParameter("nuget.path")

    /**
     * Specify the location of a solution or a packages.config file.
     */
    var projects by stringParameter("sln.path")

    /**
     * Select NuGet.exe restore or NuGet.exe install command to restore packages
     */
    var mode by compoundParameter<Mode>("nuget.use.restore")

    sealed class Mode(value: String? = null): CompoundParam<Mode>(value) {
        class Install() : Mode("install") {

            /**
             * Exclude version from package folder names
             */
            var excludeVersion by booleanParameter("nuget.excludeVersion", trueValue = "true", falseValue = "")

        }
    }

    fun install(init: Mode.Install.() -> Unit = {}) : Mode.Install {
        val result = Mode.Install()
        result.init()
        return result
    }

    /**
     * Disable looking up packages from local machine cache.
     */
    var noCache by booleanParameter("nuget.noCache", trueValue = "true", falseValue = "")

    /**
     * Specifies NuGet package sources to use during the restore.
     */
    var sources by stringParameter("nuget.sources")

    /**
     * Enter additional parameters to use when calling nuget pack command.
     */
    var args by stringParameter("nuget.restore.commandline")

    /**
     * Uses the NuGet update command to update all packages under solution
     */
    var updatePackages by compoundParameter<UpdatePackages>("nuget.updatePackages")

    sealed class UpdatePackages(value: String? = null): CompoundParam<UpdatePackages>(value) {
        class UpdateParams() : UpdatePackages("true") {

            /**
             * Exclude version from package folder names
             */
            var excludeVersion by booleanParameter("nuget.excludeVersion", trueValue = "true", falseValue = "")

            /**
             * Select how to update packages: via a call to nuget update SolutionFile.sln or via calls to nuget update packages.config
             * @see UpdateMode
             */
            var mode by enumParameter<UpdateMode>("nuget.updatePackages.mode", mapping = UpdateMode.mapping)

            /**
             * Include pre-release packages.
             */
            var includePreRelease by booleanParameter("nuget.updatePackages.include.prerelease", trueValue = "true", falseValue = "")

            /**
             * Perform safe update.
             */
            var useSafe by booleanParameter("nuget.updatePackages.safe", trueValue = "true", falseValue = "")

            /**
             * Enter additional parameters to use when calling nuget update command.
             */
            var args by stringParameter("nuget.update.commandline")

        }
    }

    fun updateParams(init: UpdatePackages.UpdateParams.() -> Unit = {}) : UpdatePackages.UpdateParams {
        val result = UpdatePackages.UpdateParams()
        result.init()
        return result
    }

    /**
     * Update mode
     */
    enum class UpdateMode {
        SolutionFile,
        PackagesConfig;

        companion object {
            val mapping = mapOf<UpdateMode, String>(SolutionFile to "sln", PackagesConfig to "perConfig")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [NuGet installer step](https://confluence.jetbrains.com/display/TCDL/NuGet+Installer) to run nuget restore command
 * @see NuGetInstallerStep
 */
fun BuildSteps.nuGetInstaller(init: NuGetInstallerStep.() -> Unit): NuGetInstallerStep {
    val result = NuGetInstallerStep(init)
    step(result)
    return result
}
