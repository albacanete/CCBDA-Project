package jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Command+Line) running a script with the specified content
 * 
 * @see script
 */
open class ScriptBuildStep() : BuildStep() {

    init {
        type = "simpleRunner"
        param("use.custom.script", "true")
    }

    constructor(init: ScriptBuildStep.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * Content of the script to run
     */
    var scriptContent by stringParameter("script.content")

    /**
     * Log stderr output as errors in the build log
     */
    var formatStderrAsError by booleanParameter("log.stderr.as.errors", trueValue = "true", falseValue = "")

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
        if (scriptContent == null && !hasParam("script.content")) {
            consumer.consumePropertyError("scriptContent", "mandatory 'scriptContent' property is not specified")
        }
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Command+Line) running a script with the specified content
 * @see ScriptBuildStep
 */
fun BuildSteps.script(init: ScriptBuildStep.() -> Unit): ScriptBuildStep {
    val result = ScriptBuildStep(init)
    step(result)
    return result
}
