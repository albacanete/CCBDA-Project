package jetbrains.buildServer.configs.kotlin.v2019_2.triggers

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * Triggers the build if the previous build failed after a specified time delay
 * 
 * @see retryBuild
 */
open class RetryBuildTrigger() : Trigger() {

    init {
        type = "retryBuildTrigger"
    }

    constructor(init: RetryBuildTrigger.() -> Unit): this() {
        init()
    }

    /**
     * Seconds to wait before adding build to queue
     */
    var delaySeconds by intParameter("enqueueTimeout")

    /**
     * Number of attempts to retry the build
     */
    var attempts by intParameter("retryAttempts")

    /**
     * Move triggered build to the queue top
     */
    var moveToTheQueueTop by booleanParameter()

    /**
     * Whether to trigger a new build with the same revisions or not
     */
    var retryWithTheSameRevisions by booleanParameter("reRunBuildWithTheSameRevisions", trueValue = "true", falseValue = "")

    /**
     * [Branch filter](https://www.jetbrains.com/help/teamcity/?Configuring+Schedule+Triggers#ConfiguringScheduleTriggers-BranchFilterbranchFilter) specifies
     * the set of branches where Retry Build Trigger should attempt to retry builds
     */
    var branchFilter by stringParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds Retry Build Trigger
 */
fun Triggers.retryBuild(init: RetryBuildTrigger.() -> Unit): RetryBuildTrigger {
    val result = RetryBuildTrigger(init)
    trigger(result)
    return result
}
