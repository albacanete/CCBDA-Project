package jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Build feature that allows to request manual approve before build starts
 * 
 * @see approval
 */
open class Approval() : BuildFeature() {

    init {
        type = "approval-feature"
    }

    constructor(init: Approval.() -> Unit): this() {
        init()
    }

    /**
     * Set of newline-separated approval rules; supported rules syntax: "user:username", "group:groupKey:requiredApprovalsCount"
     */
    var approvalRules by stringParameter("rules")

    /**
     * Amount of time (in seconds) before the build is cancelled, defaults to 6 hours
     */
    var timeout by intParameter()

    /**
     * If started by user with sufficient permissions, mark build as approved by user
     */
    var manualRunsApproved by booleanParameter("manualStartIsApproval", trueValue = "true", falseValue = "")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (approvalRules == null && !hasParam("rules")) {
            consumer.consumePropertyError("approvalRules", "mandatory 'approvalRules' property is not specified")
        }
    }
}


/**
 * Make build require manual approval before it will be assigned to an agent
 */
fun BuildFeatures.approval(init: Approval.() -> Unit): Approval {
    val result = Approval(init)
    feature(result)
    return result
}
