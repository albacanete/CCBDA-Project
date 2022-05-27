package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Active storage
 * 
 * @see activeStorage
 */
open class ActiveStorage : ProjectFeature {
    constructor(init: ActiveStorage.() -> Unit = {}, base: ActiveStorage? = null): super(base = base as ProjectFeature?) {
        type = "active_storage"
        init()
    }

    /**
     * Active storage project feature external ID.
     * Set to "DefaultStorage" to use TeamCity built-in storage.
     */
    var activeStorageID by stringParameter("active.storage.feature.id")

}


/**
 * Adds an Active Storage project feature
 * @see ActiveStorage
 */
fun ProjectFeatures.activeStorage(base: ActiveStorage? = null, init: ActiveStorage.() -> Unit = {}) {
    feature(ActiveStorage(init, base))
}
