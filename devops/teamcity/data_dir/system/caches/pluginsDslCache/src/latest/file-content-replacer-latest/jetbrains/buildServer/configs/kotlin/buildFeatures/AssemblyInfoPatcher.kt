package jetbrains.buildServer.configs.kotlin.buildFeatures

import jetbrains.buildServer.configs.kotlin.*

/**
 * A [build feature](https://www.jetbrains.com/help/teamcity/?AssemblyInfo+Patcher) which updates the AssemblyVersion,
 * AssemblyFileVersion and AssemblyInformationalVersion attributes in AssemblyInfo files under Properties folders.
 * No additional attributes will be added, make sure you have all necessary attributes in the source code.
 * Changed source files are reverted at the end of a build.
 * 
 * @see assemblyInfoPatcher
 */
open class AssemblyInfoPatcher() : BuildFeature() {

    init {
        type = "JetBrains.AssemblyInfo"
    }

    constructor(init: AssemblyInfoPatcher.() -> Unit): this() {
        init()
    }

    /**
     * Assembly version format to update AssemblyVersion attribute.
     */
    var assemblyFormat by stringParameter("assembly-format")

    /**
     * Assembly file version format to update AssemblyFileVersion attribute.
     * Leave blank to use same version as specified in assembly version.
     */
    var fileFormat by stringParameter("file-format")

    /**
     * Assembly informational version format to update AssemblyInformationalVersion attribute.
     * Leave blank to leave attribute unchanged.
     */
    var infoFormat by stringParameter("info-format")

    /**
     * If true, AssemblyInfoPatcher will attempt to patch GlobalAssemblyInfo files.
     */
    var patchGlobalAssemblyInfo by booleanParameter("patch-global-assembly-info")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [build feature](https://www.jetbrains.com/help/teamcity/?AssemblyInfo+Patcher) which updates the AssemblyVersion,
 * AssemblyFileVersion and AssemblyInformationalVersion attributes in AssemblyInfo files under Properties folders.
 * No additional attributes will be added, make sure you have all necessary attributes in the source code.
 * Changed source files are reverted at the end of a build.
 * @see AssemblyInfoPatchers
 */
fun BuildFeatures.assemblyInfoPatcher(init: AssemblyInfoPatcher.() -> Unit): AssemblyInfoPatcher {
    val result = AssemblyInfoPatcher(init)
    feature(result)
    return result
}
