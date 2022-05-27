package jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [build feature](https://www.jetbrains.com/help/teamcity/?Build+Files+Cleaner+(Swabra)) cleaning files and processes created during a build
 * 
 * @see swabra
 */
open class Swabra() : BuildFeature() {

    init {
        type = "swabra"
    }

    constructor(init: Swabra.() -> Unit): this() {
        init()
    }

    /**
     * A files clean-up mode to use
     * @see FilesCleanup
     */
    var filesCleanup by enumParameter<FilesCleanup>("swabra.enabled", mapping = FilesCleanup.mapping)

    /**
     * Force [clean checkout](https://www.jetbrains.com/help/teamcity/?Clean+Checkout) if cannot restore clean directory state
     */
    var forceCleanCheckout by booleanParameter("swabra.strict", trueValue = "true", falseValue = "")

    /**
     * Whether to inspect the checkout directory for processes locking files in this directory, and what to do with such processes
     * @see LockingProcessPolicy
     */
    var lockingProcesses by enumParameter<LockingProcessPolicy>("swabra.processes", mapping = LockingProcessPolicy.mapping)

    /**
     * Enables detailed logging to build log
     */
    var verbose by booleanParameter("swabra.verbose", trueValue = "true", falseValue = "")

    /**
     * Paths to monitor
     */
    var paths by stringParameter("swabra.rules")

    /**
     * Files clean-up mode
     */
    enum class FilesCleanup {
        /**
         * Don't cleanup files
         */
        DISABLED,
        /**
         * Cleanup files before build start
         */
        BEFORE_BUILD,
        /**
         * Cleanup files after build finish
         */
        AFTER_BUILD;

        companion object {
            val mapping = mapOf<FilesCleanup, String>(DISABLED to "", BEFORE_BUILD to "swabra.before.build", AFTER_BUILD to "swabra.after.build")
        }

    }
    /**
     * Specifies how to handle processes locking some files
     */
    enum class LockingProcessPolicy {
        /**
         * Don't detect locking processes
         */
        DISABLED,
        /**
         * Report detected locking processes
         */
        REPORT,
        /**
         * Kill detected locking processes
         */
        KILL;

        companion object {
            val mapping = mapOf<LockingProcessPolicy, String>(DISABLED to "", REPORT to "report", KILL to "kill")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [build feature](https://www.jetbrains.com/help/teamcity/?Build+Files+Cleaner+(Swabra)) cleaning files and processes created during a build
 * @see Swabra
 */
fun BuildFeatures.swabra(init: Swabra.() -> Unit): Swabra {
    val result = Swabra(init)
    feature(result)
    return result
}
