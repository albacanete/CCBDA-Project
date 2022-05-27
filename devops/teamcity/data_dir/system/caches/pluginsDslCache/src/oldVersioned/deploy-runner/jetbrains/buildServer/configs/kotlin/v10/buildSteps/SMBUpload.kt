package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * An SMB Upload build step.
 * 
 * @see smbUpload
 */
open class SMBUpload : BuildStep {
    constructor(init: SMBUpload.() -> Unit = {}, base: SMBUpload? = null): super(base = base as BuildStep?) {
        type = "smb-deploy-runner"
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

}


/**
 * Adds an SMB Upload build step.
 * @see SMBUpload
 */
fun BuildSteps.smbUpload(base: SMBUpload? = null, init: SMBUpload.() -> Unit = {}) {
    step(SMBUpload(init, base))
}
