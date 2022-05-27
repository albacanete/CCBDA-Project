package jetbrains.buildServer.configs.kotlin.buildFeatures

import jetbrains.buildServer.configs.kotlin.*

/**
 * Build feature for notification rule
 * 
 * @see notifications
 */
open class Notifications() : BuildFeature() {

    init {
        type = "notifications"
    }

    constructor(init: Notifications.() -> Unit): this() {
        init()
    }

    /**
     * Notifier that will send notifications
     */
    @Deprecated("Use notifierSettings property instead")
    var notifier by stringParameter()

    /**
     * Notifier that will send notifications
     */
    var notifierSettings by compoundParameter<NotifierSettings>("notifier")

    sealed class NotifierSettings(value: String? = null): CompoundParam<NotifierSettings>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class EmailNotifier() : NotifierSettings("email") {

            /**
             * Email to send notifications to
             */
            var email by stringParameter()

            override fun validate(consumer: ErrorConsumer) {
                if (email == null && !hasParam("email")) {
                    consumer.consumePropertyError("notifierSettings.email", "mandatory 'notifierSettings.email' property is not specified")
                }
            }
        }

        class SlackNotifier() : NotifierSettings("jbSlackNotifier") {

            /**
             * Slack connection id
             */
            var connection by stringParameter("plugin:notificator:jbSlackNotifier:connection")

            /**
             * User or channel to send notifications to
             */
            var sendTo by stringParameter("plugin:notificator:jbSlackNotifier:channel")

            var messageFormat by compoundParameter<MessageFormat>("plugin:notificator:jbSlackNotifier:messageFormat")

            sealed class MessageFormat(value: String? = null): CompoundParam<MessageFormat>(value) {
                class SimpleMessageFormat() : MessageFormat("simple") {

                }

                class VerboseMessageFormat() : MessageFormat("verbose") {

                    /**
                     * Add branch name to notification message
                     */
                    var addBranch by booleanParameter("plugin:notificator:jbSlackNotifier:addBranch")

                    /**
                     * Add changes, committers and commit date to notifications
                     */
                    var addChanges by booleanParameter("plugin:notificator:jbSlackNotifier:addChanges")

                    /**
                     * Add status text to notifications
                     */
                    var addStatusText by booleanParameter("plugin:notificator:jbSlackNotifier:addBuildStatus")

                    /**
                     * Maximum number of changes to include in notifications
                     */
                    var maximumNumberOfChanges by intParameter("plugin:notificator:jbSlackNotifier:maximumNumberOfChanges")

                }
            }

            fun simpleMessageFormat() = MessageFormat.SimpleMessageFormat()

            fun verboseMessageFormat(init: MessageFormat.VerboseMessageFormat.() -> Unit = {}) : MessageFormat.VerboseMessageFormat {
                val result = MessageFormat.VerboseMessageFormat()
                result.init()
                return result
            }

            override fun validate(consumer: ErrorConsumer) {
            }
        }
    }

    /**
     * Send notifications via email
     */
    fun emailNotifier(init: NotifierSettings.EmailNotifier.() -> Unit = {}) : NotifierSettings.EmailNotifier {
        val result = NotifierSettings.EmailNotifier()
        result.init()
        return result
    }

    /**
     * Send notifications to Slack
     */
    fun slackNotifier(init: NotifierSettings.SlackNotifier.() -> Unit = {}) : NotifierSettings.SlackNotifier {
        val result = NotifierSettings.SlackNotifier()
        result.init()
        return result
    }

    /**
     * Branch filter
     */
    var branchFilter by stringParameter()

    /**
     * Send notification when build started
     */
    var buildStarted by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Send notification if build fails to start
     */
    var buildFailedToStart by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Send notification if build failed
     */
    var buildFailed by booleanParameter("buildFinishedFailure", trueValue = "true", falseValue = "")

    /**
     * Send notification if build failed for the first time after success
     */
    var firstFailureAfterSuccess by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Keep notifying until build is complete (even without my changes)
     */
    var notifyUntilBuildIsComplete by booleanParameter("buildFinishedNewFailure", trueValue = "true", falseValue = "")

    /**
     * Only notify on new build problem or new failed test
     */
    @Deprecated("DSL property is named incorrectly. Use 'newBuildProblemOccurred' instead")
    var newBuildProblemOccured by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Only notify on new build problem or new failed test
     */
    var newBuildProblemOccurred by booleanParameter("newBuildProblemOccured", trueValue = "true", falseValue = "")

    /**
     * Send notification if build finished successfully
     */
    var buildFinishedSuccessfully by booleanParameter("buildFinishedSuccess", trueValue = "true", falseValue = "")

    /**
     * Send notification if build is successfull for the first time after failure
     */
    var firstSuccessAfterFailure by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Notify when the first build error occurs
     */
    var firstBuildErrorOccurs by booleanParameter("buildFailing", trueValue = "true", falseValue = "")

    /**
     * Build is probably hanging
     */
    var buildProbablyHanging by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Queued build requires approval
     */
    var queuedBuildRequiresApproval by booleanParameter(trueValue = "true", falseValue = "")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (notifierSettings == null && !hasParam("notifier")) {
            consumer.consumePropertyError("notifierSettings", "mandatory 'notifierSettings' property is not specified")
        }
        notifierSettings?.validate(consumer)
    }
}


/**
 * Add notification rule
 */
fun BuildFeatures.notifications(init: Notifications.() -> Unit): Notifications {
    val result = Notifications(init)
    feature(result)
    return result
}
