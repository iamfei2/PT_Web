## 介绍

GalaxyBit 是一款基于若依框架(分离版)的PT系统.

## 说明

很久之前就一直想做一套PT系统,这次终于如愿了..

## 主要功能介绍

1. 发布种子
2. 积分制(采用了国内常用的魔力值计算公式[点此处查看介绍](https://www.sohu.com/a/342448586_100173319))
3. 邀请注册
4. 种子兑换
5. 种子标签

## 下一个版本功能

1. 左侧菜单栏移至顶部
2. ~~评论留言~~
3. 资源举报
4. 资源详情页面中增加贡献用户列表
5. 资源列表增加缓存信息

## 更新记录

### 2021-11-18

1. 反作弊功能
2. 封禁申诉功能
3. 样式调整
4. 种子详情增加贡献用户列表

### 2021-11-04

> 复活了复活了!

1. 调整了界面样式
2. 制作了种子详情页面（去掉弹窗）
3. 增加种子标签功能
4. 修改图片及文件资源路径

## 未完成功能

1. ~~反作弊~~
2. ~~对辅种的统计~~
3. 积分与流量值的转换
4. 评论留言
5. ~~资源举报~~
6. 种子促销活动
7. ~~种子下载进度数据独立~~
8. ~~种子详情中显示贡献用户列表~~

## 后续更新

不定期更新

## 部署步骤

1. 查找 **galaxy-bit.com** 并替换成自己的地址
2. 打包成 jar 运行
3. 将前端放到nginx中(配置可以参考以下内容)

```
server {
        listen       80;
        server_name  localhost;

		location / {
            root   /var/www/html/dist;
			try_files $uri $uri/ /index.html;
            index  index.html index.htm;
        }
		
		location /prod-api/{
			proxy_set_header Host $http_host;
			proxy_set_header X-Real-IP $remote_addr;
			proxy_set_header REMOTE-HOST $remote_addr;
			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
			proxy_pass http://localhost:8080/;
		}

		location /profile/ {
		    proxy_pass http://localhost:8080/profile/; 
		}

		location /announce{
			proxy_set_header Host $http_host;
			proxy_set_header X-Real-IP $remote_addr;
			proxy_set_header REMOTE-HOST $remote_addr;
			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
			proxy_pass http://localhost:8080/announce;
		}

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

```

## 其他说明

有一些优化!有一些缓存!

## 截图
![资源详情](screenshots/1.png)

![个人中心](screenshots/2.png)

![资源列表](screenshots/3.jpeg)

![积分明细](screenshots/4.jpeg)

![邀请码](screenshots/5.jpeg)

![参数配置](screenshots/6.png)