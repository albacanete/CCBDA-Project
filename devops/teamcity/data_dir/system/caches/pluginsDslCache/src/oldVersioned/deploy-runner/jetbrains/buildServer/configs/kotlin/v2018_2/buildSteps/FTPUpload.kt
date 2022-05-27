package jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * An FTP Upload build step.
 * 
 * @see ftpUpload
 */
open class FTPUpload() : BuildStep() {

    init {
        type = "ftp-deploy-runner"
    }

    constructor(init: FTPUpload.() -> Unit): this() {
        init()
    }

    /**
     * An FTP server (use a hostname or IP address) and a remote directory (relative to the FTP user's home).
     */
    var targetUrl by stringParameter("jetbrains.buildServer.deployer.targetUrl")

    /**
     * Choose between an insecure and a secure connection (FTPS, FTPES).
     */
    var securityMode by enumParameter<SecurityMode>("jetbrains.buildServer.deployer.ftp.securityMode", mapping = SecurityMode.mapping)

    /**
     * Choose a data protection scheme if FTPS or FTPES is used.
     */
    var dataChannelProtection by enumParameter<DataChannelProtectionMode>("jetbrains.buildServer.deployer.ftp.dataChannelProtection", mapping = DataChannelProtectionMode.mapping)

    /**
     * Authentication method.
     */
    var authMethod by compoundParameter<AuthMethod>("jetbrains.buildServer.deployer.ftp.authMethod")

    sealed class AuthMethod(value: String? = null): CompoundParam<AuthMethod>(value) {
        class Anonymous() : AuthMethod("ANONYMOUS") {

        }

        class UsernameAndPassword() : AuthMethod("USER_PWD") {

            var username by stringParameter("jetbrains.buildServer.deployer.username")

            var password by stringParameter("secure:jetbrains.buildServer.deployer.password")

        }
    }

    fun anonymous() = AuthMethod.Anonymous()

    fun usernameAndPassword(init: AuthMethod.UsernameAndPassword.() -> Unit = {}) : AuthMethod.UsernameAndPassword {
        val result = AuthMethod.UsernameAndPassword()
        result.init()
        return result
    }

    /**
     * FTP Mode.
     */
    var ftpMode by enumParameter<FTPMode>("jetbrains.buildServer.deployer.ftp.ftpMode")

    /**
     * Optional. An FTP transfer mode to force.
     */
    var transferMode by enumParameter<TransferMode>("jetbrains.buildServer.deployer.ftp.transferMethod")

    /**
     * Newline- or comma-separated paths to files/directories to be deployed.
     * Ant-style wildcards like dir/**/*.zip and target directories like
     * *.zip => winFiles,unix/distro.tgz => linuxFiles,
     * where winFiles and linuxFiles are target directories, are supported.
     */
    var sourcePath by stringParameter("jetbrains.buildServer.deployer.sourcePath")

    enum class DataChannelProtectionMode {
        DISABLE,
        CLEAR,
        SAFE,
        CONFIDENTIAL,
        PRIVATE;

        companion object {
            val mapping = mapOf<DataChannelProtectionMode, String>(DISABLE to "D", CLEAR to "C", SAFE to "S", CONFIDENTIAL to "E", PRIVATE to "P")
        }

    }
    enum class FTPMode {
        ACTIVE,
        PASSIVE;

    }
    enum class SecurityMode {
        NONE,
        FTPS,
        FTPES;

        companion object {
            val mapping = mapOf<SecurityMode, String>(NONE to "0", FTPS to "1", FTPES to "2")
        }

    }
    enum class TransferMode {
        AUTO,
        BINARY,
        ASCII;

    }
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
 * Adds an FTP Upload build step.
 * @see FTPUpload
 */
fun BuildSteps.ftpUpload(init: FTPUpload.() -> Unit): FTPUpload {
    val result = FTPUpload(init)
    step(result)
    return result
}
