package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A Container Deployer build step.
 * 
 * @see containerDeployer
 */
open class ContainerDeployer : BuildStep {
    constructor(init: ContainerDeployer.() -> Unit = {}, base: ContainerDeployer? = null): super(base = base as BuildStep?) {
        type = "cargo-deploy-runner"
        init()
    }

    /**
     * Target container info. Use format: {hostname|IP}[:port].
     */
    var targetUrl by stringParameter("jetbrains.buildServer.deployer.targetUrl")

    /**
     * Default "Manager" web app must be deployed to target Tomcat.
     * User must have role "manager-script".
     */
    var containerType by enumParameter<ContainerType>("jetbrains.buildServer.deployer.container.type", mapping = ContainerType.mapping)

    /**
     * Use HTTPS protocol.
     */
    var useHTTPS by booleanParameter("jetbrains.buildServer.deployer.cargo.https", trueValue = "true", falseValue = "")

    var username by stringParameter("jetbrains.buildServer.deployer.username")

    var password by stringParameter("secure:jetbrains.buildServer.deployer.password")

    /**
     * Path to WAR archive to deploy.
     */
    var sourcePath by stringParameter("jetbrains.buildServer.deployer.sourcePath")

    enum class ContainerType {
        TOMCAT_8_X,
        TOMCAT_7_X,
        TOMCAT_6_X,
        TOMCAT_5_X;

        companion object {
            val mapping = mapOf<ContainerType, String>(TOMCAT_8_X to "tomcat8x", TOMCAT_7_X to "tomcat7x", TOMCAT_6_X to "tomcat6x", TOMCAT_5_X to "tomcat5x")
        }

    }
}


/**
 * Adds a Container Deployer build step.
 * @see ContainerDeployer
 */
fun BuildSteps.containerDeployer(base: ContainerDeployer? = null, init: ContainerDeployer.() -> Unit = {}) {
    step(ContainerDeployer(init, base))
}
