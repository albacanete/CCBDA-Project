package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * [Docker Support feature](https://www.jetbrains.com/help/teamcity/?Docker+Support)
 * tracks pushed images and adds a dedicated tab with information about them.
 * 
 * @see dockerSupport
 */
open class DockerSupportFeature : BuildFeature {
    constructor(init: DockerSupportFeature.() -> Unit = {}, base: DockerSupportFeature? = null): super(base = base as BuildFeature?) {
        type = "DockerSupport"
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

    sealed class LoginToRegistry(value: String? = null): CompoundParam(value) {
        class On() : LoginToRegistry("on") {

            /**
             * Docker registry ID.
             * @see DockerRegistryConnection
             */
            var dockerRegistryId by stringParameter("login2registry")

        }
    }

    fun on(init: LoginToRegistry.On.() -> Unit = {}) : LoginToRegistry.On {
        val result = LoginToRegistry.On()
        result.init()
        return result
    }

}


/**
 * @see DockerSupportFeature
 */
fun BuildFeatures.dockerSupport(base: DockerSupportFeature? = null, init: DockerSupportFeature.() -> Unit = {}) {
    feature(DockerSupportFeature(init, base))
}
