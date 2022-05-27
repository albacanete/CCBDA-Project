package jetbrains.buildServer.configs.kotlin.v2019_2.triggers

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * Base class for [Schedule Build Triggers](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers).
 * Schedule trigger runs builds by specified schedule.
 * 
 * @see schedule
 */
open class ScheduleTrigger() : Trigger() {

    init {
        type = "schedulingTrigger"
    }

    constructor(init: ScheduleTrigger.() -> Unit): this() {
        init()
    }

    /**
     * Specifies when the build should be triggered.
     * Use [daily], [weekly], or [cron] methods to specify it.
     */
    var schedulingPolicy by compoundParameter<SchedulingPolicy>()

    sealed class SchedulingPolicy(value: String? = null): CompoundParam<SchedulingPolicy>(value) {
        class Daily() : SchedulingPolicy("daily") {

            var hour by intParameter()

            var minute by intParameter()

            var timezone by stringParameter()

        }

        class Weekly() : SchedulingPolicy("weekly") {

            var dayOfWeek by enumParameter<DAY>()

            var hour by intParameter()

            var minute by intParameter()

            var timezone by stringParameter()

        }

        class Cron() : SchedulingPolicy("cron") {

            var seconds by stringParameter("cronExpression_sec")

            var minutes by stringParameter("cronExpression_min")

            var hours by stringParameter("cronExpression_hour")

            var dayOfMonth by stringParameter("cronExpression_dm")

            var dayOfWeek by stringParameter("cronExpression_dw")

            var month by stringParameter("cronExpression_month")

            var year by stringParameter("cronExpression_year")

            var timezone by stringParameter()

        }
    }

    /**
     * Build is triggered [daily](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers#ConfiguringScheduleTriggers-DateandTime)
     * on the specified hour and minuted in the given timezone
     */
    fun daily(init: SchedulingPolicy.Daily.() -> Unit = {}) : SchedulingPolicy.Daily {
        val result = SchedulingPolicy.Daily()
        result.init()
        return result
    }

    /**
     * Build is triggered [weekly](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers#ConfiguringScheduleTriggers-DateandTime)
     * on the specified day on the specified hour and minuted in the given timezone
     */
    fun weekly(init: SchedulingPolicy.Weekly.() -> Unit = {}) : SchedulingPolicy.Weekly {
        val result = SchedulingPolicy.Weekly()
        result.init()
        return result
    }

    /**
     * Build is triggered by time in the [cron expression](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers#ConfiguringScheduleTriggers-Examples)
     */
    fun cron(init: SchedulingPolicy.Cron.() -> Unit = {}) : SchedulingPolicy.Cron {
        val result = SchedulingPolicy.Cron()
        result.init()
        return result
    }

    /**
     * [Branch filter](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers#ConfiguringScheduleTriggers-BranchFilterbranchFilter) specifies
     * set of branches where build should be triggered
     */
    var branchFilter by stringParameter()

    /**
     * Specifies [trigger rules](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers#ConfiguringScheduleTriggers-VCSTriggerRules)
     */
    var triggerRules by stringParameter()

    /**
     * Specifies whether build should be triggered only when the specified [watched build changes](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers#ConfiguringScheduleTriggers-WatchedBuild).
     * Use [always], or [onWatchedBuildChange] methods to specify it.
     */
    var triggerBuild by compoundParameter<TriggerBuild>("triggerBuildIfWatchedBuildChanges")

    sealed class TriggerBuild(value: String? = null): CompoundParam<TriggerBuild>(value) {
        class Always() : TriggerBuild("") {

        }

        class OnWatchedBuildChange() : TriggerBuild("true") {

            /**
             * Id of the build configuration where build is watched
             */
            var buildType by stringParameter("revisionRuleDependsOn")

            /**
             * The rule to select the watched build
             * @see WatchedBuildRule
             */
            var watchedBuildRule by enumParameter<WatchedBuildRule>("revisionRule", mapping = WatchedBuildRule.mapping)

            /**
             * A tag for the watched build when WatchedBuildRule.TAG rule is used
             * @see WatchedBuildRule
             */
            var watchedBuildTag by stringParameter("revistionRuleBuildTag")

            /**
             * A branch filter to limit the set of watched builds by their branches.
             */
            var watchedBuildBranchFilter by stringParameter("revisionRuleBuildBranch")

            /**
             * Whether the trigger should [promote](https://www.jetbrains.com/help/teamcity/?Triggering+a+Custom+Build#TriggeringaCustomBuild-PromotingBuild) the watched build
             */
            var promoteWatchedBuild by booleanParameter(trueValue = "true", falseValue = "")

        }
    }

    /**
     * Don't watch any build, trigger build using the specified schedule
     */
    fun always() = TriggerBuild.Always()

    /**
     * Trigger build only when the watched build changes
     */
    fun onWatchedBuildChange(init: TriggerBuild.OnWatchedBuildChange.() -> Unit = {}) : TriggerBuild.OnWatchedBuildChange {
        val result = TriggerBuild.OnWatchedBuildChange()
        result.init()
        return result
    }

    /**
     * Whether the trigger should run build only when there are pending changes in the build configuration
     */
    var withPendingChangesOnly by booleanParameter("triggerBuildWithPendingChangesOnly", trueValue = "true", falseValue = "")

    /**
     * When this option is enabled the build will be triggered on all enabled compatible agents
     */
    var triggerBuildOnAllCompatibleAgents by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Whether the trigger should allow [builds optimization](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers#ConfiguringScheduleTriggers-BuildQueueOptimizationSettings)
     * in the build queue. By default optimization is enabled.
     */
    var enableQueueOptimization by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Specifies which build to watch
     */
    enum class WatchedBuildRule {
        /**
         * Watch the last finished build
         */
        LAST_FINISHED,
        /**
         * Watch the last successful build
         */
        LAST_SUCCESSFUL,
        /**
         * Watch the last pinned build
         */
        LAST_PINNED,
        /**
         * Watch the last build with the specified tag
         */
        TAG;

        companion object {
            val mapping = mapOf<WatchedBuildRule, String>(LAST_FINISHED to "lastFinished", LAST_SUCCESSFUL to "lastSuccessful", LAST_PINNED to "lastPinned", TAG to "buildTag")
        }

    }
    enum class DAY {
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday;

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds [Schedule Build Trigger](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers) to build configuration or template
 * @see ScheduleTrigger
 */
fun Triggers.schedule(init: ScheduleTrigger.() -> Unit): ScheduleTrigger {
    val result = ScheduleTrigger(init)
    trigger(result)
    return result
}
