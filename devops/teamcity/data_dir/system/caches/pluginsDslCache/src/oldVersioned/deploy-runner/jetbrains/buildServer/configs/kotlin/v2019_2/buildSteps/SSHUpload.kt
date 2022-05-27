package jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * An SSH Upload build step.
 * 
 * @see sshUpload
 */
open class SSHUpload() : BuildStep() {

    init {
        type = "ssh-deploy-runner"
    }

    constructor(init: SSHUpload.() -> Unit): this() {
        init()
    }

    /**
     * An SSH transfer protocol to use.
     */
    var transportProtocol by enumParameter<TransportProtocol>("jetbrains.buildServer.deployer.ssh.transport", mapping = TransportProtocol.mapping)

    /**
     * Newline- or comma-separated paths to files/directories to be deployed.
     * Ant-style wildcards like dir/**/*.zip and target directories like
     * *.zip => winFiles,unix/distro.tgz => linuxFiles,
     * where winFiles and linuxFiles are target directories, are supported.
     */
    var sourcePath by stringParameter("jetbrains.buildServer.deployer.sourcePath")

    /**
     * Optional. Default value: 22.
     */
    var port by intParameter("jetbrains.buildServer.sshexec.port")

    /**
     * Target url in form {hostname|ip_address}[:path/to/target/folder].
     */
    var targetUrl by stringParameter("jetbrains.buildServer.deployer.targetUrl")

    /**
     * An SSH authentication method.
     */
    var authMethod by compoundParameter<AuthMethod>("jetbrains.buildServer.sshexec.authMethod")

    sealed class AuthMethod(value: String? = null): CompoundParam<AuthMethod>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class UploadedKey() : AuthMethod("UPLOADED_KEY") {

            var username by stringParameter("jetbrains.buildServer.deployer.username")

            var passphrase by stringParameter("secure:jetbrains.buildServer.deployer.password")

            /**
             * The name of the key uploaded to the project.
             */
            var key by stringParameter("teamcitySshKey")

            override fun validate(consumer: ErrorConsumer) {
                if (username == null && !hasParam("jetbrains.buildServer.deployer.username")) {
                    consumer.consumePropertyError("authMethod.username", "mandatory 'authMethod.username' property is not specified")
                }
                if (key == null && !hasParam("teamcitySshKey")) {
                    consumer.consumePropertyError("authMethod.key", "mandatory 'authMethod.key' property is not specified")
                }
            }
        }

        class DefaultPrivateKey() : AuthMethod("DEFAULT_KEY") {

            /**
             * Username (optional, used with "id_rsa" key).
             */
            var username by stringParameter("jetbrains.buildServer.deployer.username")

            var passphrase by stringParameter("secure:jetbrains.buildServer.deployer.password")

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class CustomPrivateKey() : AuthMethod("CUSTOM_KEY") {

            /**
             * Path to key file (optional).
             */
            var keyFile by stringParameter("jetbrains.buildServer.sshexec.keyFile")

            var username by stringParameter("jetbrains.buildServer.deployer.username")

            var passphrase by stringParameter("secure:jetbrains.buildServer.deployer.password")

            override fun validate(consumer: ErrorConsumer) {
                if (username == null && !hasParam("jetbrains.buildServer.deployer.username")) {
                    consumer.consumePropertyError("authMethod.username", "mandatory 'authMethod.username' property is not specified")
                }
            }
        }

        class Password() : AuthMethod("PWD") {

            var username by stringParameter("jetbrains.buildServer.deployer.username")

            var password by stringParameter("secure:jetbrains.buildServer.deployer.password")

            override fun validate(consumer: ErrorConsumer) {
                if (username == null && !hasParam("jetbrains.buildServer.deployer.username")) {
                    consumer.consumePropertyError("authMethod.username", "mandatory 'authMethod.username' property is not specified")
                }
            }
        }

        class SshAgent() : AuthMethod("SSH_AGENT") {

            var username by stringParameter("jetbrains.buildServer.deployer.username")

            override fun validate(consumer: ErrorConsumer) {
                if (username == null && !hasParam("jetbrains.buildServer.deployer.username")) {
                    consumer.consumePropertyError("authMethod.username", "mandatory 'authMethod.username' property is not specified")
                }
            }
        }
    }

    /**
     * Uses the key(s) uploaded to the project.
     */
    fun uploadedKey(init: AuthMethod.UploadedKey.() -> Unit = {}) : AuthMethod.UploadedKey {
        val result = AuthMethod.UploadedKey()
        result.init()
        return result
    }

    /**
     * Will try to perform private key authentication using the ~/.ssh/config settings.
     * If no settings file exists, will try to use the ~/.ssh/rsa_pub public key file.
     */
    fun defaultPrivateKey(init: AuthMethod.DefaultPrivateKey.() -> Unit = {}) : AuthMethod.DefaultPrivateKey {
        val result = AuthMethod.DefaultPrivateKey()
        result.init()
        return result
    }

    /**
     * Will try to perform private key authentication
     * using the given public key file with given passphrase.
     */
    fun customPrivateKey(init: AuthMethod.CustomPrivateKey.() -> Unit = {}) : AuthMethod.CustomPrivateKey {
        val result = AuthMethod.CustomPrivateKey()
        result.init()
        return result
    }

    /**
     * Simple password authentication.
     */
    fun password(init: AuthMethod.Password.() -> Unit = {}) : AuthMethod.Password {
        val result = AuthMethod.Password()
        result.init()
        return result
    }

    /**
     * Use ssh-agent for authentication, the SSH-Agent build feature must be enabled.
     */
    fun sshAgent(init: AuthMethod.SshAgent.() -> Unit = {}) : AuthMethod.SshAgent {
        val result = AuthMethod.SshAgent()
        result.init()
        return result
    }

    enum class TransportProtocol {
        SFTP,
        SCP;

        companion object {
            val mapping = mapOf<TransportProtocol, String>(SFTP to "jetbrains.buildServer.deployer.ssh.transport.sftp", SCP to "jetbrains.buildServer.deployer.ssh.transport.scp")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (transportProtocol == null && !hasParam("jetbrains.buildServer.deployer.ssh.transport")) {
            consumer.consumePropertyError("transportProtocol", "mandatory 'transportProtocol' property is not specified")
        }
        if (sourcePath == null && !hasParam("jetbrains.buildServer.deployer.sourcePath")) {
            consumer.consumePropertyError("sourcePath", "mandatory 'sourcePath' property is not specified")
        }
        if (targetUrl == null && !hasParam("jetbrains.buildServer.deployer.targetUrl")) {
            consumer.consumePropertyError("targetUrl", "mandatory 'targetUrl' property is not specified")
        }
        if (authMethod == null && !hasParam("jetbrains.buildServer.sshexec.authMethod")) {
            consumer.consumePropertyError("authMethod", "mandatory 'authMethod' property is not specified")
        }
        authMethod?.validate(consumer)
    }
}


/**
 * Adds an SSH Upload build step.
 * @see SSHUpload
 */
fun BuildSteps.sshUpload(init: SSHUpload.() -> Unit): SSHUpload {
    val result = SSHUpload(init)
    step(result)
    return result
}
