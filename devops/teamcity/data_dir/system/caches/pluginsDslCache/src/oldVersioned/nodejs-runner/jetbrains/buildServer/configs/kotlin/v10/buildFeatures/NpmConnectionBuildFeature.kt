package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Adds a connection to npm registry
 * 
 * @see npmRegistry
 */
open class NpmConnectionBuildFeature : BuildFeature {
    constructor(init: NpmConnectionBuildFeature.() -> Unit = {}, base: NpmConnectionBuildFeature? = null): super(base = base as BuildFeature?) {
        type = "NpmRegistryConnection"
        init()
    }

    /**
     * Log in to the npm registry with the given ID before the build.
     */
    var connectionId by stringParameter()

}


/**
 * @see NpmConnectionBuildFeature
 */
fun BuildFeatures.npmRegistry(base: NpmConnectionBuildFeature? = null, init: NpmConnectionBuildFeature.() -> Unit = {}) {
    feature(NpmConnectionBuildFeature(init, base))
}
