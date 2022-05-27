package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Docker+Compose) for docker-compose step.
 * 
 * @see dockerCompose
 */
open class DockerComposeStep : BuildStep {
    constructor(init: DockerComposeStep.() -> Unit = {}, base: DockerComposeStep? = null): super(base = base as BuildStep?) {
        type = "DockerCompose"
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

}


/**
 * @see DockerComposeStep
 */
fun BuildSteps.dockerCompose(base: DockerComposeStep? = null, init: DockerComposeStep.() -> Unit = {}) {
    step(DockerComposeStep(init, base))
}
