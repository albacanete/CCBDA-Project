package jetbrains.buildServer.configs.kotlin.projectFeatures

import jetbrains.buildServer.configs.kotlin.*

/**
 * This connection is used in
 * [Docker Support build feature](https://www.jetbrains.com/help/teamcity/?Docker+Support).
 * @see DockerSupportFeature
 * 
 * @see dockerECRRegistry
 */
open class DockerECRConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "AmazonDocker")
    }

    constructor(init: DockerECRConnection.() -> Unit): this() {
        init()
    }

    /**
     * ECR connection display name
     */
    var displayName by stringParameter()

    /**
     * ECR type (defaults to ECR Private)
     */
    var ecrType by compoundParameter<EcrType>()

    sealed class EcrType(value: String? = null): CompoundParam<EcrType>(value) {
        class EcrPrivate() : EcrType("ECRPrivate") {

        }

        class EcrPublic() : EcrType("ECRPublic") {

        }
    }

    /**
     * ECR Private (default)
     */
    fun ecrPrivate() = EcrType.EcrPrivate()

    /**
     * ECR Public
     */
    fun ecrPublic() = EcrType.EcrPublic()

    /**
     * Registry ID (equals to Amazon Account ID) - a 12-digit number, such as 123456789012
     */
    var registryId by stringParameter()

    var credentialsProvider by compoundParameter<CredentialsProvider>("aws.use.default.credential.provider.chain")

    sealed class CredentialsProvider(value: String? = null): CompoundParam<CredentialsProvider>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class DefaultCredentialsProvider() : CredentialsProvider("true") {

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class AccessKey() : CredentialsProvider("") {

            /**
             * AWS Access Key ID
             */
            var accessKeyId by stringParameter("aws.access.key.id")

            /**
             * AWS Secret Access Key
             */
            var secretAccessKey by stringParameter("secure:aws.secret.access.key")

            override fun validate(consumer: ErrorConsumer) {
                if (accessKeyId == null && !hasParam("aws.access.key.id")) {
                    consumer.consumePropertyError("credentialsProvider.accessKeyId", "mandatory 'credentialsProvider.accessKeyId' property is not specified")
                }
                if (secretAccessKey == null && !hasParam("secure:aws.secret.access.key")) {
                    consumer.consumePropertyError("credentialsProvider.secretAccessKey", "mandatory 'credentialsProvider.secretAccessKey' property is not specified")
                }
            }
        }
    }

    /**
     * Use Default Credentials Provider Chain
     */
    fun defaultCredentialsProvider() = CredentialsProvider.DefaultCredentialsProvider()

    fun accessKey(init: CredentialsProvider.AccessKey.() -> Unit = {}) : CredentialsProvider.AccessKey {
        val result = CredentialsProvider.AccessKey()
        result.init()
        return result
    }

    /**
     * AWS region code. For example, us-east-1
     */
    var regionCode by stringParameter("aws.region.name")

    /**
     * AWS Credentials Type
     */
    var credentialsType by compoundParameter<CredentialsType>("aws.credentials.type")

    sealed class CredentialsType(value: String? = null): CompoundParam<CredentialsType>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class AccessKeys() : CredentialsType("aws.access.keys") {

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class TempCredentials() : CredentialsType("aws.temp.credentials") {

            var iamRoleArn by stringParameter("aws.iam.role.arn")

            var externalId by stringParameter("aws.external.id")

            override fun validate(consumer: ErrorConsumer) {
                if (iamRoleArn == null && !hasParam("aws.iam.role.arn")) {
                    consumer.consumePropertyError("credentialsType.iamRoleArn", "mandatory 'credentialsType.iamRoleArn' property is not specified")
                }
            }
        }
    }

    fun accessKeys() = CredentialsType.AccessKeys()

    fun tempCredentials(init: CredentialsType.TempCredentials.() -> Unit = {}) : CredentialsType.TempCredentials {
        val result = CredentialsType.TempCredentials()
        result.init()
        return result
    }

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (displayName == null && !hasParam("displayName")) {
            consumer.consumePropertyError("displayName", "mandatory 'displayName' property is not specified")
        }
        if (registryId == null && !hasParam("registryId")) {
            consumer.consumePropertyError("registryId", "mandatory 'registryId' property is not specified")
        }
        if (credentialsProvider == null && !hasParam("aws.use.default.credential.provider.chain")) {
            consumer.consumePropertyError("credentialsProvider", "mandatory 'credentialsProvider' property is not specified")
        }
        credentialsProvider?.validate(consumer)
        if (regionCode == null && !hasParam("aws.region.name")) {
            consumer.consumePropertyError("regionCode", "mandatory 'regionCode' property is not specified")
        }
        if (credentialsType == null && !hasParam("aws.credentials.type")) {
            consumer.consumePropertyError("credentialsType", "mandatory 'credentialsType' property is not specified")
        }
        credentialsType?.validate(consumer)
    }
}


/**
 * @see DockerECRConnection
 */
fun ProjectFeatures.dockerECRRegistry(init: DockerECRConnection.() -> Unit): DockerECRConnection {
    val result = DockerECRConnection(init)
    feature(result)
    return result
}
