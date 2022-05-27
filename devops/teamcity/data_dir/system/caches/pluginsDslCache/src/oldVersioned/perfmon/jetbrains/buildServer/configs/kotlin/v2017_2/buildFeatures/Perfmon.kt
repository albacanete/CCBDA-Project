package jetbrains.buildServer.configs.kotlin.v2017_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * A [build feature](https://confluence.jetbrains.com/display/TCDL/Performance+Monitor) allows you to get the statistics on the CPU, disk and memory usage during a build run on a build agent.
 * 
 * @see perfmon
 */
open class Perfmon() : BuildFeature() {

    init {
        type = "perfmon"
    }

    constructor(init: Perfmon.() -> Unit): this() {
        init()
    }

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [build feature](https://confluence.jetbrains.com/display/TCDL/Performance+Monitor) build feature
 * @see Perfmon
 */
fun BuildFeatures.perfmon(init: Perfmon.() -> Unit): Perfmon {
    val result = Perfmon(init)
    feature(result)
    return result
}
