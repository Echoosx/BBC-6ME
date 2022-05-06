package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object BBCEnglishConfig:ReadOnlyPluginConfig("config") {
    @ValueDescription("代理host")
    val host:String by value("127.0.0.1")

    @ValueDescription("代理端口")
    val port:Int by value(7890)

    @ValueDescription("请求过期时间(s)")
    val timeout:Int by value(30)
}