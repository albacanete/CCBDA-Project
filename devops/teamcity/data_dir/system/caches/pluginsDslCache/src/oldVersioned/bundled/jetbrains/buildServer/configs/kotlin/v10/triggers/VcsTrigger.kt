package jetbrains.buildServer.configs.kotlin.v10.triggers

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Base class for [VCS triggers](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers).
 * VCS trigger automatically starts a new build each time TeamCity detects new changes.
 * 
 * @see vcs
 */
open class VcsTrigger : Trigger {
    constructor(init: VcsTrigger.() -> Unit = {}, base: VcsTrigger? = null): super(base = base as Trigger?) {
        type = "vcsTrigger"
        init()
    }

    /**
     * [Quiet Period Settings](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers#ConfiguringVCSTriggers-QuietPeriodSettings)
     * @see QuietPeriodMode
     */
    var quietPeriodMode by enumParameter<QuietPeriodMode>()

    /**
     * Custom quiet period for the trigger, to enable it set the quietPeriodMode to QuietPeriodMode.USE_CUSTOM
     * @see quietPeriodMode
     */
    var quietPeriod by intParameter()

    /**
     * [Trigger rules](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers#ConfiguringVCSTriggers-VCSTriggerRules) allow to
     * limit the changes that trigger builds
     */
    var triggerRules by stringParameter()

    /**
     * [Branch filter](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers#ConfiguringVCSTriggers-BranchFilter) specifies
     * changes in which branches the trigger should watch
     */
    var branchFilter by stringParameter()

    /**
     * When enabled, the trigger runs builds [on changes in snapshot dependencies](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers#ConfiguringVCSTriggers-Triggerabuildonchangesinsnapshotdependencies)
     */
    var watchChangesInDependencies by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Whether the trigger should run build on [each check-in in VCS](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers#ConfiguringVCSTriggers-Per-check-inTriggering).
     * @see groupCheckinsByCommitter
     * @see enableQueueOptimization
     */
    var perCheckinTriggering by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Whether the trigger should include check-ins from the same user into a build.
     * Works only when [per-check-in triggering][perCheckinTriggering] is enabled.
     * @see perCheckinTriggering
     */
    var groupCheckinsByCommitter by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Whether the trigger should allow [builds optimization](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers#ConfiguringVCSTriggers-BuildQueueOptimizationSettings)
     * in the build queue. By default optimization is enabled. It is ignored when [per-check-in triggering][perCheckinTriggering] is enabled.
     * @see perCheckinTriggering
     */
    var enableQueueOptimization by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * [Quiet Period Settings](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers#ConfiguringVCSTriggers-QuietPeriodSettings)
     */
    enum class QuietPeriodMode {
        /**
         * Trigger doesn't use quiet period
         */
        DO_NOT_USE,
        /**
         * Trigger uses the default quiet period configured on the server
         */
        USE_DEFAULT,
        /**
         * Trigger uses the custom quiet period specified in the trigger settings
         */
        USE_CUSTOM;

    }
}


/**
 * Adds [VCS trigger](https://www.jetbrains.com/help/teamcity/?Configuring+VCS+Triggers) to the build configuration or template
 * @see VcsTrigger
 */
fun Triggers.vcs(base: VcsTrigger? = null, init: VcsTrigger.() -> Unit = {}) {
    trigger(VcsTrigger(init, base))
}
