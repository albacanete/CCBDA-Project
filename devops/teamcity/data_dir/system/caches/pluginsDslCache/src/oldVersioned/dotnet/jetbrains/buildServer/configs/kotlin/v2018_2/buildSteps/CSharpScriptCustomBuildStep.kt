package jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * A build step running a C# script with the specified content
 * 
 * @see csharpScript
 */
open class CSharpScriptCustomBuildStep() : BuildStep() {

    init {
        type = "csharpScript"
        param("scriptType", "customScript")
        param("csharpToolPath", "%teamcity.tool.TeamCity.csi.DEFAULT%")
    }

    constructor(init: CSharpScriptCustomBuildStep.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for the script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * Content of the script to run
     */
    var content by stringParameter("scriptContent")

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
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (content == null && !hasParam("scriptContent")) {
            consumer.consumePropertyError("content", "mandatory 'content' property is not specified")
        }
    }
}


/**
 * Adds a build step running a C# script with the specified content
 * @see CSharpCustomScriptBuildStep
 */
fun BuildSteps.csharpScript(init: CSharpScriptCustomBuildStep.() -> Unit): CSharpScriptCustomBuildStep {
    val result = CSharpScriptCustomBuildStep(init)
    step(result)
    return result
}
