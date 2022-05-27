package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build feature](https://confluence.jetbrains.com/display/TCDL/Performance+Monitor) allows you to get the statistics on the CPU, disk and memory usage during a build run on a build agent.
 * 
 * @see perfmon
 */
open class Perfmon : BuildFeature {
    constructor(init: Perfmon.() -> Unit = {}, base: Perfmon? = null): super(base = base as BuildFeature?) {
        type = "perfmon"
        init()
    }

}


/**
 * Adds a [build feature](https://confluence.jetbrains.com/display/TCDL/Performance+Monitor) build feature
 * @see Perfmon
 */
fun BuildFeatures.perfmon(base: Perfmon? = null, init: Perfmon.() -> Unit = {}) {
    feature(Perfmon(init, base))
}
