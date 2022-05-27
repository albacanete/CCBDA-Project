package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature enabling [TeamCity NuGet feed](https://confluence.jetbrains.com/display/TCDL/NuGet)
 * 
 * @see nuGetFeed
 */
open class NuGetFeed : ProjectFeature {
    constructor(init: NuGetFeed.() -> Unit = {}, base: NuGetFeed? = null): super(base = base as ProjectFeature?) {
        type = "PackageRepository"
        param("type", "nuget")
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

}


/**
 * Enables [TeamCity NuGet feed](https://confluence.jetbrains.com/display/TCDL/NuGet)
 */
fun ProjectFeatures.nuGetFeed(base: NuGetFeed? = null, init: NuGetFeed.() -> Unit = {}) {
    feature(NuGetFeed(init, base))
}
