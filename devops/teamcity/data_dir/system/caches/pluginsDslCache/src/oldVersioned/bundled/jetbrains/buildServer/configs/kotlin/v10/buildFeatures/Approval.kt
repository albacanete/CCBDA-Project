package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Build feature that allows to request manual approve before build starts
 * 
 * @see approval
 */
open class Approval : BuildFeature {
    constructor(init: Approval.() -> Unit = {}, base: Approval? = null): super(base = base as BuildFeature?) {
        type = "approval-feature"
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

}


/**
 * Make build require manual approval before it will be assigned to an agent
 */
fun BuildFeatures.approval(base: Approval? = null, init: Approval.() -> Unit = {}) {
    feature(Approval(init, base))
}
