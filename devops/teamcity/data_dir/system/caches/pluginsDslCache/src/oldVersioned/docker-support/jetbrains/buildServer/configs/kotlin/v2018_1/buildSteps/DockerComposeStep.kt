package jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Docker+Compose) for docker-compose step.
 * 
 * @see dockerCompose
 */
open class DockerComposeStep() : BuildStep() {

    init {
        type = "DockerCompose"
    }

    constructor(init: DockerComposeStep.() -> Unit): this() {
        init()
    }

    /**
     * TeamCity will start docker-compose services from the file in this step, and will shut down the services at the end of the build.
     */
    var file by stringParameter("dockerCompose.file")

    /**
     * If enabled, "docker-compose pull" will be run before "docker-compose up" command.
     */
    var forcePull by booleanParameter("dockerCompose.pull", trueValue = "true", falseValue = "")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * @see DockerComposeStep
 */
fun BuildSteps.dockerCompose(init: DockerComposeStep.() -> Unit): DockerComposeStep {
    val result = DockerComposeStep(init)
    step(result)
    return result
}
