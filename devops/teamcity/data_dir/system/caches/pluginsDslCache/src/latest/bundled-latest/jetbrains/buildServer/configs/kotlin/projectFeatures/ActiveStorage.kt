package jetbrains.buildServer.configs.kotlin.projectFeatures

import jetbrains.buildServer.configs.kotlin.*

/**
 * Active storage
 * 
 * @see activeStorage
 */
open class ActiveStorage() : ProjectFeature() {

    init {
        type = "active_storage"
    }

    constructor(init: ActiveStorage.() -> Unit): this() {
        init()
    }

    /**
     * Active storage project feature external ID.
     * Set to "DefaultStorage" to use TeamCity built-in storage.
     */
    var activeStorageID by stringParameter("active.storage.feature.id")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds an Active Storage project feature
 * @see ActiveStorage
 */
fun ProjectFeatures.activeStorage(init: ActiveStorage.() -> Unit): ActiveStorage {
    val result = ActiveStorage(init)
    feature(result)
    return result
}
