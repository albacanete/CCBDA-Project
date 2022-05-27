package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Build feature ensuring certain [free disk space](https://www.jetbrains.com/help/teamcity/?Free+Disk+Space)
 * on the agent before the build by deleting files managed by the TeamCity agent.
 * 
 * @see freeDiskSpace
 */
open class FreeDiskSpace : BuildFeature {
    constructor(init: FreeDiskSpace.() -> Unit = {}, base: FreeDiskSpace? = null): super(base = base as BuildFeature?) {
        type = "jetbrains.agent.free.space"
        init()
    }

    /**
     * Amount of the free disk space to ensure. Without a suffix treated as bytes.
     * Supported suffixes are: kb, mb, gb, tb.
     */
    var requiredSpace by stringParameter("free-space-work")

    /**
     * Whether TeamCity should fail a build if sufficient disk space cannot be freed.
     */
    var failBuild by booleanParameter("free-space-fail-start", trueValue = "true", falseValue = "")

}


/**
 * Adds a [free disk space](https://www.jetbrains.com/help/teamcity/?Free+Disk+Space) check
 * to the build configuration or template
 * @see FreeDiskSpace
 */
fun BuildFeatures.freeDiskSpace(base: FreeDiskSpace? = null, init: FreeDiskSpace.() -> Unit = {}) {
    feature(FreeDiskSpace(init, base))
}
