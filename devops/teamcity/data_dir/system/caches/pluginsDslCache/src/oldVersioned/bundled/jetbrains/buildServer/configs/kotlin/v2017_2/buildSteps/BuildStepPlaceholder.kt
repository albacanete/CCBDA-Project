package jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * Base class for a [build step placeholder](https://www.jetbrains.com/help/teamcity/?Build+Configuration+Template#BuildConfigurationTemplate-BuildStepPlaceholders).
 * Build step placeholder is used in a build configuration template to indicate where the build steps of a configuration
 * will go when it is attached to the template.
 * 
 * @see placeholder
 */
open class BuildStepPlaceholder() : BuildStep() {

    init {
        type = "_placeholder_"
    }

    constructor(init: BuildStepPlaceholder.() -> Unit): this() {
        init()
    }

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Inserts a [build step placeholder](https://www.jetbrains.com/help/teamcity/?Build+Configuration+Template#BuildConfigurationTemplate-BuildStepPlaceholders).
 * @see BuildStepPaceholder
 */
fun BuildSteps.placeholder(init: BuildStepPlaceholder.() -> Unit): BuildStepPlaceholder {
    val result = BuildStepPlaceholder(init)
    step(result)
    return result
}
