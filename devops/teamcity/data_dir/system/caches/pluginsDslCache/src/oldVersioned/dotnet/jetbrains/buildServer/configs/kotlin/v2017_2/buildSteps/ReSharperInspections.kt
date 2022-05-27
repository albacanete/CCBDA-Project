package jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * An [Inspections (ReSharper) build step](https://www.jetbrains.com/help/teamcity/?inspections-resharper)
 * for gathering JetBrains ReSharper inspection results.
 * 
 * @see reSharperInspections
 */
open class ReSharperInspections() : BuildStep() {

    init {
        type = "dotnet-tools-inspectcode"
    }

    constructor(init: ReSharperInspections.() -> Unit): this() {
        init()
    }

    /**
     * The path to the .sln file created by Microsoft Visual Studio 2005 or later.
     * The specified path should be relative to the checkout directory.
     */
    var solutionPath by stringParameter("dotnet-tools-inspectcode.solution")

    /**
     * Project name wildcards to analyze only a part of the solution.
     * Leave blank to analyze the whole solution. Separate wildcards with new lines.
     */
    var projectFilter by stringParameter("dotnet-tools-inspectcode.project.filter")

    var targetDotNetFramework_2_0 by booleanParameter("TargetDotNetFramework_2.0", trueValue = "true", falseValue = "")

    var targetDotNetFramework_3_0 by booleanParameter("TargetDotNetFramework_3.0", trueValue = "true", falseValue = "")

    var targetDotNetFramework_3_5 by booleanParameter("TargetDotNetFramework_3.5", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_0 by booleanParameter("TargetDotNetFramework_4.0", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_5 by booleanParameter("TargetDotNetFramework_4.5", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_5_1 by booleanParameter("TargetDotNetFramework_4.5.1", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_5_2 by booleanParameter("TargetDotNetFramework_4.5.2", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_6 by booleanParameter("TargetDotNetFramework_4.6", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_6_1 by booleanParameter("TargetDotNetFramework_4.6.1", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_6_2 by booleanParameter("TargetDotNetFramework_4.6.2", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_7 by booleanParameter("TargetDotNetFramework_4.7", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_7_1 by booleanParameter("TargetDotNetFramework_4.7.1", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_7_2 by booleanParameter("TargetDotNetFramework_4.7.2", trueValue = "true", falseValue = "")

    var targetDotNetFramework_4_8 by booleanParameter("TargetDotNetFramework_4.8", trueValue = "true", falseValue = "")

    /**
     * A custom path to R# CLT Home Directory. Paths relative to the checkout directory are supported.
     * The value can reference to JetBrains ReSharper Command Line Tool specified via Administration | Tools.
     */
    var cltPath by stringParameter("jetbrains.resharper-clt.clt-path")

    /**
     * Select the platform bitness of the InspectCode tool.
     * To find code issues in C++ projects, use the x86 platform.
     * The cross-platform inspections are also supported in ReSharper 2020.2.1 or later.
     */
    var cltPlatform by enumParameter<Platform>("jetbrains.resharper-clt.platform", mapping = Platform.mapping)

    /**
     * Newline-delimited list of ReSharper plugins required for InspectCode in the following format:
     * - Download %pluginId%/%version%
     * - File %filePath%
     * - Folder %folderPath%
     * To download plugins add the https://resharper-plugins.jetbrains.com/api/v2/ NuGet package source.
     */
    var cltPlugins by stringParameter("jetbrains.resharper-clt.plugins")

    /**
     * The path to the file containing ReSharper settings created with JetBrains ReSharper 6.1 or later.
     * The specified path should be relative to the checkout directory.
     * If specified, this settings layer has the top priority, so it overrides ReSharper build-in settings.
     * By default, build-in ReSharper settings layers are applied.
     */
    var customSettingsProfilePath by stringParameter("dotnet-tools-inspectcodeCustomSettingsProfile")

    /**
     * Check this option to include debug messages in the build log and
     * publish the file with additional logs (dotnet-tools-inspectcode.log) as a hidden artifact.
     */
    var debugOutput by booleanParameter("dotnet-tools-inspectcode.debug", trueValue = "true", falseValue = "")

    /**
     * Specify newline-separated command line parameters to add to calling inspectCode.exe.
     */
    var customCmdArgs by stringParameter("dotnet-tools-inspectcode.customCmdArgs")

    /**
     * Specifies which Docker image platform will be used to run this build step.
     */
    var dockerImagePlatform by enumParameter<ImagePlatform>("plugin.docker.imagePlatform", mapping = ImagePlatform.mapping)

    /**
     * If enabled, "docker pull [image][dockerImage]" will be run before docker run.
     */
    var dockerPull by booleanParameter("plugin.docker.pull.enabled", trueValue = "true", falseValue = "")

    /**
     * Specifies which Docker image to use for running this build step. I.e. the build step will be run inside specified docker image, using 'docker run' wrapper.
     */
    var dockerImage by stringParameter("plugin.docker.imageId")

    /**
     * Additional docker run command arguments
     */
    var dockerRunParameters by stringParameter("plugin.docker.run.parameters")

    enum class Platform {
        X86,
        X64,
        CROSS_PLATFORM;

        companion object {
            val mapping = mapOf<Platform, String>(X86 to "x86", X64 to "x64", CROSS_PLATFORM to "Cross-platform")
        }

    }
    /**
     * Docker image platforms
     */
    enum class ImagePlatform {
        Any,
        Linux,
        Windows;

        companion object {
            val mapping = mapOf<ImagePlatform, String>(Any to "", Linux to "linux", Windows to "windows")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (solutionPath == null && !hasParam("dotnet-tools-inspectcode.solution")) {
            consumer.consumePropertyError("solutionPath", "mandatory 'solutionPath' property is not specified")
        }
    }
}


/**
 * Adds an [Inspections (ReSharper) build step](https://www.jetbrains.com/help/teamcity/?inspections-resharper)
 * for gathering JetBrains ReSharper inspection results.
 * @see ReSharperInspections
 */
fun BuildSteps.reSharperInspections(init: ReSharperInspections.() -> Unit): ReSharperInspections {
    val result = ReSharperInspections(init)
    step(result)
    return result
}
