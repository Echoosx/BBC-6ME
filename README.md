# BBC-6ME

> 基于 [Mirai Console](https://github.com/mamoe/mirai-console) 的 `BBC 6分钟英语听力学习` 订阅

[![Release](https://img.shields.io/github/v/release/Echoosx/BBC-6ME)](https://github.com/Echoosx/BBC-6ME/releases)
## 功能
订阅 [BBC 6分钟英语听力学习](https://www.bbc.co.uk/learningenglish/english/features/6-minute-english) ，在官网更新后发送消息提醒

## 权限
权限ID：`org.echoosx.mirai.plugin.bbc-6me:bbc`

拥有此权限的群组和个人会接收更新提醒

## 配置
### config.yml
- `host`：代理IP，默认127.0.0.1
- `port`：代理端口号，默认7890
- `timeout`：会话超时界限，默认30s

注：由于GFW限制，此插件功能必须通过代理完成

## 安装
- 从 [Releases](https://github.com/Echoosx/BBC-6ME/releases) 下载`jar`包，将其放入工作目录下`plugins`文件夹
- 如果没有`plugins`文件夹，先运行 [Mirai Console](https://github.com/mamoe/mirai-console) ，会自动生成

