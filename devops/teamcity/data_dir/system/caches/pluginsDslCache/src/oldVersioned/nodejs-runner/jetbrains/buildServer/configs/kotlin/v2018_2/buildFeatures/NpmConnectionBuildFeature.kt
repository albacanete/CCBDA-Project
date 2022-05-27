package jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * Adds a connection to npm registry
 * 
 * @see npmRegistry
 */
open class NpmConnectionBuildFeature() : BuildFeature() {

    init {
        type = "NpmRegistryConnection"
    }

    constructor(init: NpmConnectionBuildFeature.() -> Unit): this() {
        init()
    }

    /**
     * Log in to the npm registry with the given ID before the build.
     */
    var connectionId by stringParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (connectionId == null && !hasParam("connectionId")) {
            consumer.consumePropertyError("connectionId", "mandatory 'connectionId' property is not specified")
        }
    }
}


/**
 * @see NpmConnectionBuildFeature
 */
fun BuildFeatures.npmRegistry(init: NpmConnectionBuildFeature.() -> Unit): NpmConnectionBuildFeature {
    val result = NpmConnectionBuildFeature(init)
    feature(result)
    return result
}
