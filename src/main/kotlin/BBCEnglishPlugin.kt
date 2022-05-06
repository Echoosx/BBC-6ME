package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.AbstractJvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.echoosx.mirai.plugin.data.SubscribeData
import org.echoosx.mirai.plugin.util.CheckUpdate
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import java.util.*

object BBCEnglishPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "org.echoosx.mirai.plugin.BBC-6ME",
        name = "BBC-6ME",
        version = "1.0.0"
    ) {
        author("Echoosx")
    }
) {
    val bbcPerm = this.registerPermission("bbc","BBC 6 Minutes English")

    override fun onEnable() {
        logger.info { "BBC-6ME Plugin loaded" }

        BBCEnglishConfig.reload()
        SubscribeData.reload()

        val cronSchedule = "0 0 * ? * THU"
        val timeZone = "Europe/London"
        val scheduler = StdSchedulerFactory.getDefaultScheduler()

        val jobDetail = JobBuilder.newJob(CheckUpdate::class.java)
            .withIdentity("checkUpdate","BBC-6ME")
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("subscribe","BBC-6ME")
            .withSchedule(
                CronScheduleBuilder.cronSchedule(cronSchedule)
                    .inTimeZone(TimeZone.getTimeZone(timeZone))
            )
            .startNow()
            .build()

        scheduler.scheduleJob(jobDetail,trigger)
        scheduler.start()
    }

    // 权限构造方法
    private fun AbstractJvmPlugin.registerPermission(name: String, description: String): Permission {
        return PermissionService.INSTANCE.register(permissionId(name), description, parentPermission)
    }
}
