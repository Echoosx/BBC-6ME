package org.echoosx.mirai.plugin.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.permission.PermissionService.Companion.testPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import org.echoosx.mirai.plugin.BBCEnglishConfig.host
import org.echoosx.mirai.plugin.BBCEnglishConfig.port
import org.echoosx.mirai.plugin.BBCEnglishConfig.timeout
import org.echoosx.mirai.plugin.BBCEnglishPlugin
import org.echoosx.mirai.plugin.BBCEnglishPlugin.bbcPerm
import org.echoosx.mirai.plugin.data.SubscribeData.latest
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import java.io.InputStream
import java.net.InetSocketAddress
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Article(title:String,link:String,desc:String,img:String) {
    var title:String = ""
    var link:String = ""
    var desc:String = ""
    var img:String = ""

    init {
        this.title = title
        this.link = link
        this.desc = desc
        this.img = img
    }
}

internal class CheckUpdate: Job {
    @Throws(JobExecutionException::class)
    override fun execute(jobExecutionContext: JobExecutionContext?) {
        // 当前时间
        val date = Date()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd:HH:mm:ss")
        val dateStr = dateFormat.format(date)
        // 工作内容
        BBCEnglishPlugin.logger.info("6ME更新检查，执行时间：$dateStr")

        Bot.instances.filter {
            it.isOnline
        }.forEach{
            val article = getLatest()
            if(article.title != latest){
                it.sendSubscribe(article)
                latest = article.title
            }
        }
    }

    private fun getLatest():Article{
        val proxy = java.net.Proxy(java.net.Proxy.Type.HTTP, InetSocketAddress(host, port))
        val document: Document = Jsoup.connect("https://www.bbc.co.uk/learningenglish/english/features/6-minute-english")
            .proxy(proxy)
            .timeout(timeout)
            .get()

        val latest = document.select("div.widget-bbcle-coursecontentlist-featured")

        val title = latest.select("div.text>h2>a").text()
        val link = "https://www.bbc.co.uk" + latest.select("div.text>h2>a").attr("href")
        val description = latest.select("div.text>div.details>p").text()
        val thumbnail = latest.select("div.img").select("img").attr("src")

        return Article(title,link,description,thumbnail)
    }
}

internal fun downloadThumbnail(url:String): InputStream{
    val proxy = java.net.Proxy(java.net.Proxy.Type.HTTP, InetSocketAddress(host, port))
    val response = Jsoup.connect(url).proxy(proxy).ignoreContentType(true).timeout(timeout).execute()
    return response.bodyStream()
}

internal fun Bot.sendSubscribe(article:Article) = BBCEnglishPlugin.launch {
    val resource = downloadThumbnail(article.img).toExternalResource()
    groups.filter { bbcPerm.testPermission(it.permitteeId) }.forEach{ group->
        val message = buildMessageChain {
            appendLine("BBC 6 Minutes English")
            appendLine("")
            appendLine("Topic: ${article.title}")
            appendLine(article.desc)
            append(resource.use { it.uploadAsImage(group) })
            append(article.link)
        }
        group.sendMessage(message)
    }
    withContext(Dispatchers.IO) {
        resource.close()
    }
}