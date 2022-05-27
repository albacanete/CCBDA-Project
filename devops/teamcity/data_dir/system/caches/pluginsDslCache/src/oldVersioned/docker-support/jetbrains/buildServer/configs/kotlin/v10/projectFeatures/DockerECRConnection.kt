package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * This connection is used in
 * [Docker Support build feature](https://www.jetbrains.com/help/teamcity/?Docker+Support).
 * @see DockerSupportFeature
 * 
 * @see dockerECRRegistry
 */
open class DockerECRConnection : ProjectFeature {
    constructor(init: DockerECRConnection.() -> Unit = {}, base: DockerECRConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "AmazonDocker")
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

    sealed class EcrType(value: String? = null): CompoundParam(value) {
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

    sealed class CredentialsProvider(value: String? = null): CompoundParam(value) {
        class DefaultCredentialsProvider() : CredentialsProvider("true") {

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

    sealed class CredentialsType(value: String? = null): CompoundParam(value) {
        class AccessKeys() : CredentialsType("aws.access.keys") {

        }

        class TempCredentials() : CredentialsType("aws.temp.credentials") {

            var iamRoleArn by stringParameter("aws.iam.role.arn")

            var externalId by stringParameter("aws.external.id")

        }
    }

    fun accessKeys() = CredentialsType.AccessKeys()

    fun tempCredentials(init: CredentialsType.TempCredentials.() -> Unit = {}) : CredentialsType.TempCredentials {
        val result = CredentialsType.TempCredentials()
        result.init()
        return result
    }

}


/**
 * @see DockerECRConnection
 */
fun ProjectFeatures.dockerECRRegistry(base: DockerECRConnection? = null, init: DockerECRConnection.() -> Unit = {}) {
    feature(DockerECRConnection(init, base))
}
