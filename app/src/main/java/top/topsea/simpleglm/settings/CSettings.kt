package top.topsea.simpleglm.settings

import androidx.compose.runtime.Composable

class CSettings(name: String, enabled: Boolean = true) {
    var commentName: String
    var executeStage: Int = 0
    var enabled: Boolean = true
    lateinit var settingsUI: @Composable () -> Unit

    constructor(
        settingsUI: @Composable () -> Unit,
        executeStage: Int,
        name: String,
        enabled: Boolean = true
    ): this(name, enabled){
        this.executeStage = executeStage
        this.settingsUI = settingsUI
        this.enabled = enabled
    }

    init {
        commentName = name
    }

}