package jetbrains.buildServer.configs.kotlin.v2019_2.triggers

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [trigger](https://www.jetbrains.com/help/teamcity/?Configuring+Maven+Triggers#ConfiguringMavenTriggers-MavenSnapshotDependencyTrigger)
 * running builds when there is a modification of the snapshot dependency content in the remote repository.
 * 
 * @see mavenSnapshot
 */
open class MavenSnapshotDependencyTrigger() : Trigger() {

    init {
        type = "mavenSnapshotDependencyTrigger"
    }

    constructor(init: MavenSnapshotDependencyTrigger.() -> Unit): this() {
        init()
    }

    /**
     * Do not trigger a build if currently running builds can produce this artifact
     */
    var skipIfRunning by booleanParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [trigger](https://www.jetbrains.com/help/teamcity/?Configuring+Maven+Triggers#ConfiguringMavenTriggers-MavenSnapshotDependencyTrigger)
 * running builds when there is a modification of the snapshot dependency content in the remote repository.
 * @see MavenSnapshotDependencyTrigger
 */
fun Triggers.mavenSnapshot(init: MavenSnapshotDependencyTrigger.() -> Unit): MavenSnapshotDependencyTrigger {
    val result = MavenSnapshotDependencyTrigger(init)
    trigger(result)
    return result
}
