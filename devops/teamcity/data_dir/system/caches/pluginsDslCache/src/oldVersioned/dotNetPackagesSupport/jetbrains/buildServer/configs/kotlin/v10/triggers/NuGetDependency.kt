package jetbrains.buildServer.configs.kotlin.v10.triggers

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * [The NuGet Dependency Trigger](https://www.jetbrains.com/help/teamcity/?NuGet+Dependency+Trigger)
 * allows starting a new build if a NuGet packages update is detected in the NuGet repository.
 * Note that if a custom NuGet executable is used, it must be explicitly allowed on this server.
 * 
 * @see nuGetDependency
 */
open class NuGetDependency : Trigger {
    constructor(init: NuGetDependency.() -> Unit = {}, base: NuGetDependency? = null): super(base = base as Trigger?) {
        type = "nuget.simple"
        init()
    }

    /**
     * A custom path to NuGet.exe. Absolute paths are supported.
     * It can reference to Nuget.exe Tool installed via Administration | Tools.
     */
    var nugetPath by stringParameter("nuget.exe")

    /**
     * The NuGet packages feed URL to monitor packages changes.
     * Leave blank to use default NuGet feed.
     */
    var feedURL by stringParameter("nuget.source")

    /**
     * A username to access NuGet feed, leave blank if no authentication is required.
     */
    var username by stringParameter("nuget.username")

    /**
     * A password to access NuGet feed, leave blank if no authentication is required.
     */
    var password by stringParameter("secure:nuget.password")

    /**
     * A Package Id to check for updates.
     */
    var packageId by stringParameter("nuget.package")

    /**
     * Specify package version to check. Leave empty to check for latest version.
     */
    var packageVersion by stringParameter("nuget.version")

    /**
     * Trigger build if pre-release package version is detected.
     */
    var includePrerelease by booleanParameter("nuget.include.prerelease", trueValue = "true", falseValue = "")

}


/**
 * Adds [The NuGet Dependency Trigger](https://www.jetbrains.com/help/teamcity/?NuGet+Dependency+Trigger)
 */
fun Triggers.nuGetDependency(base: NuGetDependency? = null, init: NuGetDependency.() -> Unit = {}) {
    trigger(NuGetDependency(init, base))
}
