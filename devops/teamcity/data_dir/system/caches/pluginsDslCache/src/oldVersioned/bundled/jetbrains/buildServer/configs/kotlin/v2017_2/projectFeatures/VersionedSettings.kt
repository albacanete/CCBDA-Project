package jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * Project feature enabling [versioned settings](https://www.jetbrains.com/help/teamcity/?Storing+Project+Settings+in+Version+Control) in the project.
 * 
 * @see versionedSettings
 */
open class VersionedSettings() : ProjectFeature() {

    init {
        type = "versionedSettings"
    }

    constructor(init: VersionedSettings.() -> Unit): this() {
        init()
    }

    /**
     * Versioned settings mode to use
     * @see Mode
     */
    var mode by enumParameter<Mode>("enabled", mapping = Mode.mapping)

    /**
     * Specifies what settings to use [for builds](https://www.jetbrains.com/help/teamcity/?Storing+Project+Settings+in+Version+Control#StoringProjectSettingsinVersionControl-DefiningSettingstoApplytoBuilds)
     * @see BuildSettingsMode
     */
    var buildSettingsMode by enumParameter<BuildSettingsMode>("buildSettings", mapping = BuildSettingsMode.mapping)

    /**
     * Id of the VCS root where project settings are stored
     */
    var rootExtId by stringParameter("rootId")

    /**
     * Whether [settings changes](https://www.jetbrains.com/help/teamcity/?Storing+Project+Settings+in+Version+Control#StoringProjectSettingsinVersionControl-DisplayingChanges)
     * should be shown in builds when a settings VCS root is not attached to a build configuration.
     */
    var showChanges by booleanParameter()

    /**
     * Project settings [format name](https://www.jetbrains.com/help/teamcity/?Storing+Project+Settings+in+Version+Control#StoringProjectSettingsinVersionControl-settingsFormatSettingsFormat).
     * @see Format
     */
    var settingsFormat by enumParameter<Format>("format", mapping = Format.mapping)

    /**
     * Specifies how secure parameters should be [stored](https://www.jetbrains.com/help/teamcity/?Storing+Project+Settings+in+Version+Control#StoringProjectSettingsinVersionControl-StoringSecureSettings).
     * When set to `true`, parameters are stored outside of version control.
     * When set to `false`, parameters are scrambled and stored in version control.
     */
    var storeSecureParamsOutsideOfVcs by booleanParameter("credentialsStorageType", trueValue = "credentialsJSON", falseValue = "")

    /**
     * If set to true then the project/build configuration settings can be modified via the user interface or REST API and changes will be checked into the settings repository.
     * If set to false then the project settings can be modified only via the settings repository.
     */
    var allowEditingOfProjectSettings by booleanParameter("twoWaySynchronization")

    /**
     * Specifies what settings to use [for builds](https://www.jetbrains.com/help/teamcity/?Storing+Project+Settings+in+Version+Control#StoringProjectSettingsinVersionControl-DefiningSettingstoApplytoBuilds)
     */
    enum class BuildSettingsMode {
        /**
         * Builds use current project settings from the TeamCity server. Settings changes in branches, history and personal builds are ignored.
         * Users cannot run a build with custom project settings.
         */
        USE_CURRENT_SETTINGS,
        /**
         * Builds use the latest project settings from the TeamCity server. Users can run a build with older project settings via the custom build dialog.
         */
        PREFER_CURRENT_SETTINGS,
        /**
         * Builds load settings from the versioned settings revision calculated for the build. Users can change configuration settings in personal builds
         * from IDE or can run a build with project settings current on the TeamCity server via the custom build dialog.
         */
        PREFER_SETTINGS_FROM_VCS;

        companion object {
            val mapping = mapOf<BuildSettingsMode, String>(USE_CURRENT_SETTINGS to "ALWAYS_USE_CURRENT", PREFER_CURRENT_SETTINGS to "PREFER_CURRENT", PREFER_SETTINGS_FROM_VCS to "PREFER_VCS")
        }

    }
    /**
     * Versioned settings format
     */
    enum class Format {
        /**
         * Versioned settings in XML format
         */
        XML,
        /**
         * Versioned settings in Kotlin format
         */
        KOTLIN;

        companion object {
            val mapping = mapOf<Format, String>(XML to "xml", KOTLIN to "kotlin")
        }

    }
    /**
     * Versioned settings mode
     */
    enum class Mode {
        /**
         * Versioned settings are enabled in the project and all subprojects which use the SAME_AS_PARENT mode transitively
         */
        ENABLED,
        /**
         * Versioned settings are disabled in the project and all subprojects which use the SAME_AS_PARENT mode transitively
         */
        DISABLED,
        /**
         * Versioned settings are configured to use the same settings as in the parent project
         */
        SAME_AS_PARENT;

        companion object {
            val mapping = mapOf<Mode, String>(ENABLED to "true", DISABLED to "false", SAME_AS_PARENT to "")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Enables [versioned settings](https://www.jetbrains.com/help/teamcity/?Storing+Project+Settings+in+Version+Control) in the project
 * @see VersionedSettings
 */
fun ProjectFeatures.versionedSettings(init: VersionedSettings.() -> Unit): VersionedSettings {
    val result = VersionedSettings(init)
    feature(result)
    return result
}
