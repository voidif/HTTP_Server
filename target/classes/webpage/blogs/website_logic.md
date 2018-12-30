#个人网站单页面应用逻辑设计

```html
<div id="container"/>
	<div id="maindisplayer"/>
	<div id="tools"/>
```

以及flag标志位
```
0:index
1:Blog
2:About
```
另设reload标志位
```
0:只需要reload maindisplayer的内容
1:需要reload整个container内容
```

模式介绍 (flag, reload)
```
1. 主页模式(index) (0, 0)
2. Blog列表浏览模式 (1, 0)
3. Blog单篇文章浏览模式 (-1, 0)
4. Blog编辑模式 (-1, 1)
5. About模式(TODO) (0, 0)
```
转换规则：
```
1. 当页面开始加载或导航栏index键被点击, 进入模式1。
2. 当导航栏Blog被点击，进入模式2。
3. 当从模式2点击任意文章，进入模式3
4. 从模式2或者3点击new按钮，进入模式4.
5. 当导航栏About被点击，进入模式5.
```