package org.echoosx.mirai.plugin.data

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object SubscribeData:AutoSavePluginData("subscribe") {
    @ValueDescription("latest")
    var latest:String by value()
}