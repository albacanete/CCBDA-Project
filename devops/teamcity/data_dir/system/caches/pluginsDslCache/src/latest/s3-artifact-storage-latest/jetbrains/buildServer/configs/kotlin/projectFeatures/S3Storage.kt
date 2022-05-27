package jetbrains.buildServer.configs.kotlin.projectFeatures

import jetbrains.buildServer.configs.kotlin.*

/**
 * Amazon S3 Artifact Storage
 * 
 * @see s3Storage
 */
open class S3Storage() : ProjectFeature() {

    init {
        type = "storage_settings"
        param("storage.type", "S3_storage")
        param("storage.s3.bucket.name.wasProvidedAsString", "true")
    }

    constructor(init: S3Storage.() -> Unit): this() {
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

    sealed class AwsEnvironment(value: String? = null): CompoundParam<AwsEnvironment>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Default() : AwsEnvironment("") {

            /**
             * AWS region
             */
            var awsRegionName by stringParameter("aws.region.name")

            override fun validate(consumer: ErrorConsumer) {
            }
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

            override fun validate(consumer: ErrorConsumer) {
                if (endpoint == null && !hasParam("aws.service.endpoint")) {
                    consumer.consumePropertyError("awsEnvironment.endpoint", "mandatory 'awsEnvironment.endpoint' property is not specified")
                }
            }
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

    sealed class Credentials(value: String? = null): CompoundParam<Credentials>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class AccessKeys() : Credentials("aws.access.keys") {

            override fun validate(consumer: ErrorConsumer) {
            }
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

            override fun validate(consumer: ErrorConsumer) {
                if (iamRoleARN == null && !hasParam("aws.iam.role.arn")) {
                    consumer.consumePropertyError("credentials.iamRoleARN", "mandatory 'credentials.iamRoleARN' property is not specified")
                }
            }
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

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (bucketName == null && !hasParam("storage.s3.bucket.name")) {
            consumer.consumePropertyError("bucketName", "mandatory 'bucketName' property is not specified")
        }
        awsEnvironment?.validate(consumer)
        credentials?.validate(consumer)
    }
}


/**
 * Adds a Amazon S3 Artifact Storage project feature
 * @see S3Storage
 */
fun ProjectFeatures.s3Storage(init: S3Storage.() -> Unit): S3Storage {
    val result = S3Storage(init)
    feature(result)
    return result
}
