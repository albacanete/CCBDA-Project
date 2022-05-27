package jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * An SMB Upload build step.
 * 
 * @see smbUpload
 */
open class SMBUpload() : BuildStep() {

    init {
        type = "smb-deploy-runner"
    }

    constructor(init: SMBUpload.() -> Unit): this() {
        init()
    }

    /**
     * Target path in form \\host\share[\subdir].
     */
    var targetUrl by stringParameter("jetbrains.buildServer.deployer.targetUrl")

    /**
     * The DNS only name resolution allows switching JCIFS to "DNS-only" mode.
     * May fix performance or out of memory exceptions.
     */
    var dnsOnly by booleanParameter("jetbrains.buildServer.deployer.smb.dns_only", trueValue = "true", falseValue = "")

    var username by stringParameter("jetbrains.buildServer.deployer.username")

    var password by stringParameter("secure:jetbrains.buildServer.deployer.password")

    /**
     * Newline- or comma-separated paths to files/directories to be deployed.
     * Ant-style wildcards like dir/**/*.zip and target directories like
     * *.zip => winFiles,unix/distro.tgz => linuxFiles,
     * where winFiles and linuxFiles are target directories, are supported.
     */
    var sourcePath by stringParameter("jetbrains.buildServer.deployer.sourcePath")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (targetUrl == null && !hasParam("jetbrains.buildServer.deployer.targetUrl")) {
            consumer.consumePropertyError("targetUrl", "mandatory 'targetUrl' property is not specified")
        }
        if (sourcePath == null && !hasParam("jetbrains.buildServer.deployer.sourcePath")) {
            consumer.consumePropertyError("sourcePath", "mandatory 'sourcePath' property is not specified")
        }
    }
}


/**
 * Adds an SMB Upload build step.
 * @see SMBUpload
 */
fun BuildSteps.smbUpload(init: SMBUpload.() -> Unit): SMBUpload {
    val result = SMBUpload(init)
    step(result)
    return result
}
