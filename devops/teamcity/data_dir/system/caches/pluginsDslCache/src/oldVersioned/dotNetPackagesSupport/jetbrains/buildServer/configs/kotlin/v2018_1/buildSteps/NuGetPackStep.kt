package jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * A [NuGet pack step](https://confluence.jetbrains.com/display/TCDL/NuGet+Pack) to run nuget pack command
 * 
 * @see nuGetPack
 */
open class NuGetPackStep() : BuildStep() {

    init {
        type = "jb.nuget.pack"
    }

    constructor(init: NuGetPackStep.() -> Unit): this() {
        init()
    }

    /**
     * Specify path to NuGet.exe.
     */
    var toolPath by stringParameter("nuget.path")

    /**
     * Specify paths to .nuspec files and/or to project files.
     */
    var paths by stringParameter("nuget.pack.specFile")

    /**
     * Use the project file (if exists, i.e. .csproj or .vbproj) for every matched .nuspec file.
     */
    var preferProjectPaths by booleanParameter("nuget.pack.prefer.project", trueValue = "true", falseValue = "")

    /**
     * Overrides the version number from the .nuspec file.
     */
    var version by stringParameter("nuget.pack.version")

    /**
     * The -BaseDirectory parameter value. Leave blank to use the build checkout directory
     */
    var baseDir by compoundParameter<BaseDir>("nuget.pack.project.dir")

    sealed class BaseDir(value: String? = null): CompoundParam<BaseDir>(value) {
        class CustomPath() : BaseDir("explicit") {

            /**
             * Specify the path for -BaseDirectory parameter.
             */
            var path by stringParameter("nuget.pack.base.directory")

        }

        class ProjectPath() : BaseDir("project") {

        }
    }

    fun customPath(init: BaseDir.CustomPath.() -> Unit = {}) : BaseDir.CustomPath {
        val result = BaseDir.CustomPath()
        result.init()
        return result
    }

    /**
     * Use project/.nuspec directory.
     */
    fun projectPath() = BaseDir.ProjectPath()

    /**
     * The path to the output directory for generated NuGet packages.
     */
    var outputDir by stringParameter("nuget.pack.output.directory")

    /**
     * Clean output directory
     */
    var cleanOutputDir by booleanParameter("nuget.pack.output.clean", trueValue = "true", falseValue = "")

    /**
     * Publish created packages to build artifacts
     */
    var publishPackages by booleanParameter("nuget.pack.as.artifact", trueValue = "true", falseValue = "")

    /**
     * Exclude files when creating a package.
     */
    var excludePaths by stringParameter("nuget.pack.excludes")

    /**
     * A semicolon or a newline-separated list of package creation properties.
     */
    var properties by stringParameter("nuget.pack.properties")

    /**
     * Create tool package.
     */
    var toolPackage by booleanParameter("nuget.pack.pack.mode.tool", trueValue = "true", falseValue = "")

    /**
     * Include sources and symbols.
     */
    var includeSymbols by booleanParameter("nuget.pack.include.sources", trueValue = "true", falseValue = "")

    /**
     * Enter additional parameters to use when calling nuget pack command.
     */
    var args by stringParameter("nuget.pack.commandline")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [NuGet pack step](https://confluence.jetbrains.com/display/TCDL/NuGet+Pack) to run nuget pack command
 * @see NuGetPackStep
 */
fun BuildSteps.nuGetPack(init: NuGetPackStep.() -> Unit): NuGetPackStep {
    val result = NuGetPackStep(init)
    step(result)
    return result
}
