package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A build step running a C# script with from a specified file
 * 
 * @see csharpFile
 */
open class CSharpScriptFileBuildStep : BuildStep {
    constructor(init: CSharpScriptFileBuildStep.() -> Unit = {}, base: CSharpScriptFileBuildStep? = null): super(base = base as BuildStep?) {
        type = "csharpScript"
        param("scriptType", "file")
        param("csharpToolPath", "%teamcity.tool.TeamCity.csi.DEFAULT%")
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for the script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * A path to a file with .main.kts extension
     */
    var path by stringParameter("scriptFile")

    /**
     * Space-separated list of additional arguments for C# script
     */
    var arguments by stringParameter("scriptArgs")

    /**
     * Space-separated list of NuGet package source (URL, UNC/folder path)
     */
    var sources by stringParameter("nuget.packageSources")

    /**
     * C# tool path
     */
    var tool by stringParameter("csharpToolPath")

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
}


/**
 * Adds a build step running a C# script from a specified file
 * @see CSharpScriptFileBuildStep
 */
fun BuildSteps.csharpFile(base: CSharpScriptFileBuildStep? = null, init: CSharpScriptFileBuildStep.() -> Unit = {}) {
    step(CSharpScriptFileBuildStep(init, base))
}