package jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A Container Deployer build step.
 * 
 * @see containerDeployer
 */
open class ContainerDeployer() : BuildStep() {

    init {
        type = "cargo-deploy-runner"
    }

    constructor(init: ContainerDeployer.() -> Unit): this() {
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
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (targetUrl == null && !hasParam("jetbrains.buildServer.deployer.targetUrl")) {
            consumer.consumePropertyError("targetUrl", "mandatory 'targetUrl' property is not specified")
        }
        if (containerType == null && !hasParam("jetbrains.buildServer.deployer.container.type")) {
            consumer.consumePropertyError("containerType", "mandatory 'containerType' property is not specified")
        }
        if (username == null && !hasParam("jetbrains.buildServer.deployer.username")) {
            consumer.consumePropertyError("username", "mandatory 'username' property is not specified")
        }
        if (password == null && !hasParam("secure:jetbrains.buildServer.deployer.password")) {
            consumer.consumePropertyError("password", "mandatory 'password' property is not specified")
        }
        if (sourcePath == null && !hasParam("jetbrains.buildServer.deployer.sourcePath")) {
            consumer.consumePropertyError("sourcePath", "mandatory 'sourcePath' property is not specified")
        }
    }
}


/**
 * Adds a Container Deployer build step.
 * @see ContainerDeployer
 */
fun BuildSteps.containerDeployer(init: ContainerDeployer.() -> Unit): ContainerDeployer {
    val result = ContainerDeployer(init)
    step(result)
    return result
}
