package jetbrains.buildServer.configs.kotlin.v10.triggers

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Perforce Shelve Trigger queues a build when a shelved changelist is created/updated in
 * the VCS Root of the build configuration.
 * 
 * @see perforceShelveTrigger
 */
open class PerforceShelveTrigger : Trigger {
    constructor(init: PerforceShelveTrigger.() -> Unit = {}, base: PerforceShelveTrigger? = null): super(base = base as Trigger?) {
        type = "perforceShelveTrigger"
        init()
    }

    /**
     * The keyword which should be present in Perforce shelved changelist description to trigger the build.
     * Default keyword is `#teamcity`
     */
    var keyword by stringParameter("clDescriptionKeyword")

}


/**
 * Adds [Perforce Build Trigger](https://www.jetbrains.com/help/teamcity/?Perforce+Shelve+Trigger) to build configuration or template
 * @see PerforceShelveTrigger
 */
fun Triggers.perforceShelveTrigger(base: PerforceShelveTrigger? = null, init: PerforceShelveTrigger.() -> Unit = {}) {
    trigger(PerforceShelveTrigger(init, base))
}
