package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Amazon S3 Artifact Storage
 * 
 * @see s3Storage
 */
open class S3Storage : ProjectFeature {
    constructor(init: S3Storage.() -> Unit = {}, base: S3Storage? = null): super(base = base as ProjectFeature?) {
        type = "storage_settings"
        param("storage.type", "S3_storage")
        param("storage.s3.bucket.name.wasProvidedAsString", "true")
        init()
    }

    /**
     * Storage name
     */
    var storageName by stringParameter("storage.name")

    /**
     * Bucket name
     */
    var bucketName by stringParameter("storage.s3.bucket.name")

    /**
     * Bucket path prefix
     */
    var bucketPrefix by stringParameter("storage.s3.bucket.prefix")

    /**
     * Whether to use Pre-Signed URLs to upload
     */
    var enablePresignedURLUpload by booleanParameter("storage.s3.upload.presignedUrl.enabled", trueValue = "true", falseValue = "")

    /**
     * Whether to force Virtual Host Addressing
     */
    var forceVirtualHostAddressing by booleanParameter("storage.s3.forceVirtualHostAddressing")

    /**
     * Initiates multipart upload for files larger than the specified value.
     * Minimum value is 5MB. Allowed suffixes: KB, MB, GB, TB.
     * Leave empty to use the default value.
     */
    var multipartThreshold by stringParameter("storage.s3.upload.multipart_threshold")

    /**
     * Specify the maximum allowed part size. Minimum value is 5MB.
     * Allowed suffixes: KB, MB, GB, TB. Leave empty to use the default value.
     */
    var multipartChunksize by stringParameter("storage.s3.upload.multipart_chunksize")

    /**
     * Whether to use CloudFront for artifact transport
     */
    var cloudFrontEnabled by booleanParameter("storage.s3.cloudfront.enabled", trueValue = "true", falseValue = "")

    /**
     * Name of CloudFront distribution for uploads
     */
    var cloudFrontUploadDistribution by stringParameter("storage.s3.cloudfront.upload.distribution")

    /**
     * Name of CloudFront distribution for downloads
     */
    var cloudFrontDownloadDistribution by stringParameter("storage.s3.cloudfront.download.distribution")

    /**
     * Id of Public Key in CloudFront that has access to selected distribution
     */
    var cloudFrontPublicKeyId by stringParameter("storage.s3.cloudfront.publicKeyId")

    /**
     * Private key that corresponds to chosen public key
     */
    var cloudFrontPrivateKey by stringParameter("secure:storage.s3.cloudfront.privateKey")

    /**
     * Use default credential provider chain
     */
    var useDefaultCredentialProviderChain by booleanParameter("aws.use.default.credential.provider.chain")

    /**
     * AWS account secret access key
     */
    var accessKey by stringParameter("secure:aws.secret.access.key")

    var awsEnvironment by compoundParameter<AwsEnvironment>("aws.environment")

    sealed class AwsEnvironment(value: String? = null): CompoundParam(value) {
        class Default() : AwsEnvironment("") {

            /**
             * AWS region
             */
            var awsRegionName by stringParameter("aws.region.name")

        }

        class Custom() : AwsEnvironment("custom") {

            /**
             * Endpoint URL
             */
            var endpoint by stringParameter("aws.service.endpoint")

            /**
             * AWS region
             */
            var awsRegionName by stringParameter("aws.region.name")

        }
    }

    fun default(init: AwsEnvironment.Default.() -> Unit = {}) : AwsEnvironment.Default {
        val result = AwsEnvironment.Default()
        result.init()
        return result
    }

    fun custom(init: AwsEnvironment.Custom.() -> Unit = {}) : AwsEnvironment.Custom {
        val result = AwsEnvironment.Custom()
        result.init()
        return result
    }

    var credentials by compoundParameter<Credentials>("aws.credentials.type")

    sealed class Credentials(value: String? = null): CompoundParam(value) {
        class AccessKeys() : Credentials("aws.access.keys") {

        }

        class Temporary() : Credentials("aws.temp.credentials") {

            /**
             * Pre-configured IAM role with necessary permissions
             */
            var iamRoleARN by stringParameter("aws.iam.role.arn")

            /**
             * External ID is strongly recommended to be used in role trust relationship condition
             */
            var externalID by stringParameter("aws.external.id")

        }
    }

    /**
     * Use pre-configured AWS account access keys
     */
    fun accessKeys() = Credentials.AccessKeys()

    /**
     * Get temporary access keys via AWS STS
     */
    fun temporary(init: Credentials.Temporary.() -> Unit = {}) : Credentials.Temporary {
        val result = Credentials.Temporary()
        result.init()
        return result
    }

    /**
     * AWS account access key ID
     */
    var accessKeyID by stringParameter("aws.access.key.id")

}


/**
 * Adds a Amazon S3 Artifact Storage project feature
 * @see S3Storage
 */
fun ProjectFeatures.s3Storage(base: S3Storage? = null, init: S3Storage.() -> Unit = {}) {
    feature(S3Storage(init, base))
}
