package jetbrains.buildServer.configs.kotlin.v2019_2.vcs

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * Perforce [VCS root](https://www.jetbrains.com/help/teamcity/?Perforce)
 */
open class PerforceVcsRoot() : VcsRoot() {

    init {
        type = "perforce"
    }

    constructor(init: PerforceVcsRoot.() -> Unit): this() {
        init()
    }

    /**
     * A Perforce server address in the "host:port" format
     */
    var port by stringParameter()

    /**
     * A mode of Perforce connection
     */
    var mode by compoundParameter<Mode>("use-client")

    sealed class Mode(value: String? = null): CompoundParam<Mode>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Stream() : Mode("stream") {

            /**
             * A Perforce stream name
             */
            var streamName by stringParameter("stream")

            /**
             * Enables support for branches in Perforce
             */
            var enableFeatureBranches by booleanParameter("use-dag", trueValue = "true", falseValue = "")

            /**
             * [Branch specification](https://www.jetbrains.com/help/teamcity/?Working+with+Feature+Branches#WorkingwithFeatureBranches-branchSpec) to use
             */
            var branchSpec by stringParameter("teamcity:branchSpec")

            override fun validate(consumer: ErrorConsumer) {
                if (streamName == null && !hasParam("stream")) {
                    consumer.consumePropertyError("mode.streamName", "mandatory 'mode.streamName' property is not specified")
                }
            }
        }

        class Client() : Mode("true") {

            /**
             * A Perforce client name to use
             */
            var clientName by stringParameter("client")

            override fun validate(consumer: ErrorConsumer) {
                if (clientName == null && !hasParam("client")) {
                    consumer.consumePropertyError("mode.clientName", "mandatory 'mode.clientName' property is not specified")
                }
            }
        }

        class ClientMapping() : Mode("false") {

            /**
             * A [client mapping](https://www.jetbrains.com/help/teamcity/?Perforce#Perforce-perforceClientMappingOptionDescription) to use
             */
            var mapping by stringParameter("client-mapping")

            override fun validate(consumer: ErrorConsumer) {
                if (mapping == null && !hasParam("client-mapping")) {
                    consumer.consumePropertyError("mode.mapping", "mandatory 'mode.mapping' property is not specified")
                }
            }
        }
    }

    /**
     * Connect to [Perforce stream](https://www.jetbrains.com/help/teamcity/?Perforce#Perforce-perforceStreamOptionDescription) with specified name
     */
    fun stream(init: Mode.Stream.() -> Unit = {}) : Mode.Stream {
        val result = Mode.Stream()
        result.init()
        return result
    }

    /**
     * Connect to to Perforce using the specified [client name](https://www.jetbrains.com/help/teamcity/?Perforce#Perforce-perforceClientOptionDescription)
     */
    fun client(init: Mode.Client.() -> Unit = {}) : Mode.Client {
        val result = Mode.Client()
        result.init()
        return result
    }

    /**
     * Connect to Perforce using the specified [client mapping](https://www.jetbrains.com/help/teamcity/?Perforce#Perforce-perforceClientMappingOptionDescription)
     */
    fun clientMapping(init: Mode.ClientMapping.() -> Unit = {}) : Mode.ClientMapping {
        val result = Mode.ClientMapping()
        result.init()
        return result
    }

    /**
     * A username for Perforce connection
     */
    var userName by stringParameter("user")

    /**
     * A password for Perforce connection
     */
    var password by stringParameter("secure:passwd")

    @Deprecated("Not used anymore")
    var useTicketBasedAuth by booleanParameter("use-login", trueValue = "true", falseValue = "")

    /**
     * [Settings](https://www.jetbrains.com/help/teamcity/?Perforce#Perforce-perforceWorkspaceOptions)
     * for the [p4 client](http://www.perforce.com/perforce/doc.current/manuals/cmdref/p4_client.html#p4_client.form_fields) command.
     */
    var workspaceOptions by stringParameter("workspace-options")

    /**
     * Whether TeamCity should run 'p4 clean' command before the build
     */
    var runP4Clean by booleanParameter("use-p4-clean", trueValue = "true", falseValue = "")

    /**
     * When set, the created Perforce workspace on agent will not be stream-enabled.
     * The option allows using arbitrary checkout rules, but prohibits commits from agent into the stream.
     */
    var nonStreamWorkspace by booleanParameter("no-stream-workspace", trueValue = "true", falseValue = "")

    /**
     * Whether TeamCity should track files on the Perforce server on sync
     */
    var skipHaveListUpdate by stringParameter("use-sync-p")

    /**
     * Additional options for the [p4 sync](http://www.perforce.com/perforce/doc.current/manuals/cmdref/p4_sync.html) command
     */
    var syncOptions by stringParameter("extra-sync-options")

    /**
     * A path to p4 executable
     */
    var p4Path by stringParameter("p4-exe")

    /**
     * [Custom](https://www.jetbrains.com/help/teamcity/?Perforce#Perforce-perforceLabelToCheckout) Perforce revision to checkout
     */
    var checkoutRevision by stringParameter("label-revision")

    /**
     * A charset to use on the client machine
     */
    var charsetName by stringParameter("charset")

    /**
     * Enables support for files with UTF-16 encoding
     */
    var supportUtf16 by booleanParameter("detect-utf-16", trueValue = "true", falseValue = "")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        mode?.validate(consumer)
    }
}


