package jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * A [build step](https://confluence.jetbrains.com/pages/viewpage.action?pageId=74844955) running Visual Studio solution.
 * 
 * @see visualStudio
 */
open class VisualStudioStep() : BuildStep() {

    init {
        type = "VS.Solution"
    }

    constructor(init: VisualStudioStep.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for ant script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * A path to the solution to be built relative to the build [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory)
     */
    var path by stringParameter("build-file-path")

    /**
     * Visual Studio version to use
     * @see VisualStudioVersion
     */
    var version by enumParameter<VisualStudioVersion>("vs.version")

    /**
     * A required platform bitness
     * @see Platform
     */
    var runPlatform by enumParameter<Platform>("run-platform")

    /**
     * MSBuild version to use
     * @see MSBuildVersion
     */
    var msBuildVersion by enumParameter<MSBuildVersion>("msbuild_version", mapping = MSBuildVersion.mapping)

    /**
     * A version of tools to be used to compile (equivalent to the /toolsversion: commandline argument).
     * @see MSBuildToolsVersion
     */
    var msBuildToolsVersion by enumParameter<MSBuildToolsVersion>("toolsVersion", mapping = MSBuildToolsVersion.mapping)

    /**
     * Specify list of MSBuild targets to execute.
     */
    var targets by stringParameter()

    /**
     * Specify solution configuration for the build.
     */
    var configuration by stringParameter("msbuild.prop.Configuration")

    /**
     * Specify solution platform for the build.
     */
    var platform by stringParameter("msbuild.prop.Platform")

    /**
     * Additional command line parameters for MSBuild
     */
    var args by stringParameter("runnerArgs")

    /**
     * Microsoft Visual Studio version
     */
    enum class VisualStudioVersion {
        /**
         * Microsoft Visual Studio 2019
         */
        vs2019,
        /**
         * Microsoft Visual Studio 2017
         */
        vs2017,
        /**
         * Microsoft Visual Studio 2015
         */
        vs2015,
        /**
         * Microsoft Visual Studio 2013
         */
        vs2013,
        /**
         * Microsoft Visual Studio 2012
         */
        vs2012,
        /**
         * Microsoft Visual Studio 2010
         */
        vs2010,
        /**
         * Microsoft Visual Studio 2008
         */
        vs2008,
        /**
         * Microsoft Visual Studio 2005
         */
        vs2005;

    }
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
    /**
     * MSBuild version
     */
    enum class MSBuildVersion {
        /**
         * Microsoft Build Tools 2019
         */
        V16_0,
        /**
         * Microsoft Build Tools 2017
         */
        V15_0,
        /**
         * Microsoft Build Tools 2015
         */
        V14_0,
        /**
         * Microsoft Build Tools 2013
         */
        V12_0,
        /**
         * Microsoft .NET Framework 4.5
         */
        V4_5,
        /**
         * Microsoft .NET Framework 4.0
         */
        V4_0,
        /**
         * Microsoft .NET Framework 3.5
         */
        V3_5,
        /**
         * Microsoft .NET Framework 2.0
         */
        V2_0,
        /**
         * Mono xbuild 4.5
         */
        MONO_v4_5,
        /**
         * Mono xbuild 4.0
         */
        MONO_v4_0,
        /**
         * Mono xbuild 3.5
         */
        MONO_v3_5,
        /**
         * Mono xbuild 2.0
         */
        MONO_v2_0;

        companion object {
            val mapping = mapOf<MSBuildVersion, String>(V16_0 to "16.0", V15_0 to "15.0", V14_0 to "14.0", V12_0 to "12.0", V4_5 to "4.5", V4_0 to "4.0", V3_5 to "3.5", V2_0 to "2.0", MONO_v4_5 to "mono_4.5", MONO_v4_0 to "mono_4.0", MONO_v3_5 to "mono_3.5", MONO_v2_0 to "mono")
        }

    }
    /**
     * MSBuild ToolsVersion
     */
    enum class MSBuildToolsVersion {
        V16_0,
        V15_0,
        V14_0,
        V12_0,
        V4_0,
        V3_5,
        V2_0,
        NONE;

        companion object {
            val mapping = mapOf<MSBuildToolsVersion, String>(V16_0 to "Current", V15_0 to "15.0", V14_0 to "14.0", V12_0 to "12.0", V4_0 to "4.0", V3_5 to "3.5", V2_0 to "2.0", NONE to "none")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (path == null && !hasParam("build-file-path")) {
            consumer.consumePropertyError("path", "mandatory 'path' property is not specified")
        }
    }
}


/**
 * Adds a [build step](https://confluence.jetbrains.com/pages/viewpage.action?pageId=74844955) running Visual Studio solution
 * @see VisualStudioStep
 */
fun BuildSteps.visualStudio(init: VisualStudioStep.() -> Unit): VisualStudioStep {
    val result = VisualStudioStep(init)
    step(result)
    return result
}
