package jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * Project feature enabling [TeamCity NuGet feed](https://confluence.jetbrains.com/display/TCDL/NuGet)
 * 
 * @see nuGetFeed
 */
open class NuGetFeed() : ProjectFeature() {

    init {
        type = "PackageRepository"
        param("type", "nuget")
    }

    constructor(init: NuGetFeed.() -> Unit): this() {
        init()
    }

    /**
     * The feed name
     */
    var name by stringParameter()

    /**
     * The feed description
     */
    var description by stringParameter()

    /**
     * Enables indexing NuGet packages into feed produced by builds in this project and all subprojects
     */
    var indexPackages by booleanParameter(trueValue = "true", falseValue = "")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Enables [TeamCity NuGet feed](https://confluence.jetbrains.com/display/TCDL/NuGet)
 */
fun ProjectFeatures.nuGetFeed(init: NuGetFeed.() -> Unit): NuGetFeed {
    val result = NuGetFeed(init)
    feature(result)
    return result
}
