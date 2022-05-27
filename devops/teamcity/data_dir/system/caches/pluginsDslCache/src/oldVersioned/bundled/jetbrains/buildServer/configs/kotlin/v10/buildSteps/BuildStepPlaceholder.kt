package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Base class for a [build step placeholder](https://www.jetbrains.com/help/teamcity/?Build+Configuration+Template#BuildConfigurationTemplate-BuildStepPlaceholders).
 * Build step placeholder is used in a build configuration template to indicate where the build steps of a configuration
 * will go when it is attached to the template.
 * 
 * @see placeholder
 */
open class BuildStepPlaceholder : BuildStep {
    constructor(init: BuildStepPlaceholder.() -> Unit = {}, base: BuildStepPlaceholder? = null): super(base = base as BuildStep?) {
        type = "_placeholder_"
        init()
    }

}


/**
 * Inserts a [build step placeholder](https://www.jetbrains.com/help/teamcity/?Build+Configuration+Template#BuildConfigurationTemplate-BuildStepPlaceholders).
 * @see BuildStepPaceholder
 */
fun BuildSteps.placeholder(base: BuildStepPlaceholder? = null, init: BuildStepPlaceholder.() -> Unit = {}) {
    step(BuildStepPlaceholder(init, base))
}
