Cache
==========================
读取顺序
-----------
1. 读取顺序 -> LocalCache -> OuterCache -> DB
2. 数据更新
 * 从数据库中读取最新数据，依次更新 LocalCache -> OuterCache ，发送广播清除某个缓存信息
 * 接收到广播（手工清除缓存 & 一级缓存自动失效），从 LocalCache 中清除指定的缓存信息

