package jetbrains.buildServer.configs.kotlin.projectFeatures

import jetbrains.buildServer.configs.kotlin.*

/**
 * Cloud Integration project feature
 * 
 * @see cloudIntegration
 */
open class CloudIntegration() : ProjectFeature() {

    init {
        type = "CloudIntegration"
    }

    constructor(init: CloudIntegration.() -> Unit): this() {
        init()
    }

    /**
     * Whether to enable cloud integrations in this project.
     */
    var enabled by booleanParameter()

    /**
     * Whether to enable cloud integrations in subprojects.
     */
    var subprojectsEnabled by booleanParameter("SubprojectsEnabled")

    /**
     * Whether to allow override cloud integrations.
     */
    var allowOverride by booleanParameter("AllowOverride")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a Cloud Integration project feature
 * @see CloudIntegration
 */
fun ProjectFeatures.cloudIntegration(init: CloudIntegration.() -> Unit): CloudIntegration {
    val result = CloudIntegration(init)
    feature(result)
    return result
}
