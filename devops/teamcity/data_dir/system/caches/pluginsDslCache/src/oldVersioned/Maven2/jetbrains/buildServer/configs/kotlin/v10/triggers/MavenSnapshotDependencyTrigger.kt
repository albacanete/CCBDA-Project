package jetbrains.buildServer.configs.kotlin.v10.triggers

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [trigger](https://www.jetbrains.com/help/teamcity/?Configuring+Maven+Triggers#ConfiguringMavenTriggers-MavenSnapshotDependencyTrigger)
 * running builds when there is a modification of the snapshot dependency content in the remote repository.
 * 
 * @see mavenSnapshot
 */
open class MavenSnapshotDependencyTrigger : Trigger {
    constructor(init: MavenSnapshotDependencyTrigger.() -> Unit = {}, base: MavenSnapshotDependencyTrigger? = null): super(base = base as Trigger?) {
        type = "mavenSnapshotDependencyTrigger"
        init()
    }

    /**
     * Do not trigger a build if currently running builds can produce this artifact
     */
    var skipIfRunning by booleanParameter()

}


/**
 * Adds a [trigger](https://www.jetbrains.com/help/teamcity/?Configuring+Maven+Triggers#ConfiguringMavenTriggers-MavenSnapshotDependencyTrigger)
 * running builds when there is a modification of the snapshot dependency content in the remote repository.
 * @see MavenSnapshotDependencyTrigger
 */
fun Triggers.mavenSnapshot(base: MavenSnapshotDependencyTrigger? = null, init: MavenSnapshotDependencyTrigger.() -> Unit = {}) {
    trigger(MavenSnapshotDependencyTrigger(init, base))
}
