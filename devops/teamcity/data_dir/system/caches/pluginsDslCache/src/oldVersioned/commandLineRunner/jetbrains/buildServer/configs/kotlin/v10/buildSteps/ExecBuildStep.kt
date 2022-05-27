package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Command+Line) running the specified executable with given arguments
 * 
 * @see exec
 */
open class ExecBuildStep : BuildStep {
    constructor(init: ExecBuildStep.() -> Unit = {}, base: ExecBuildStep? = null): super(base = base as BuildStep?) {
        type = "simpleRunner"
        param("use.custom.script", "")
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for the executable,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * A path to executable
     */
    var path by stringParameter("command.executable")

    /**
     * A space-separated arguments list for the executable
     */
    var arguments by stringParameter("command.parameters")

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
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Command+Line) running the specified executable with given arguments
 * @see ExecBuildStep
 */
fun BuildSteps.exec(base: ExecBuildStep? = null, init: ExecBuildStep.() -> Unit = {}) {
    step(ExecBuildStep(init, base))
}
