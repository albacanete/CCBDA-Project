package jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * [Docker Support feature](https://www.jetbrains.com/help/teamcity/?Docker+Support)
 * tracks pushed images and adds a dedicated tab with information about them.
 * 
 * @see dockerSupport
 */
open class DockerSupportFeature() : BuildFeature() {

    init {
        type = "DockerSupport"
    }

    constructor(init: DockerSupportFeature.() -> Unit): this() {
        init()
    }

    /**
     * On server clean-up, delete pushed docker images from registry.
     */
    var cleanupPushedImages by booleanParameter("cleanupPushed", trueValue = "true", falseValue = "")

    /**
     * Log in to the Docker registry with the given ID before the build.
     */
    var loginToRegistry by compoundParameter<LoginToRegistry>("loginCheckbox")

    sealed class LoginToRegistry(value: String? = null): CompoundParam<LoginToRegistry>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class On() : LoginToRegistry("on") {

            /**
             * Docker registry ID.
             * @see DockerRegistryConnection
             */
            var dockerRegistryId by stringParameter("login2registry")

            override fun validate(consumer: ErrorConsumer) {
                if (dockerRegistryId == null && !hasParam("login2registry")) {
                    consumer.consumePropertyError("loginToRegistry.dockerRegistryId", "mandatory 'loginToRegistry.dockerRegistryId' property is not specified")
                }
            }
        }
    }

    fun on(init: LoginToRegistry.On.() -> Unit = {}) : LoginToRegistry.On {
        val result = LoginToRegistry.On()
        result.init()
        return result
    }

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        loginToRegistry?.validate(consumer)
    }
}


/**
 * @see DockerSupportFeature
 */
fun BuildFeatures.dockerSupport(init: DockerSupportFeature.() -> Unit): DockerSupportFeature {
    val result = DockerSupportFeature(init)
    feature(result)
    return result
}
