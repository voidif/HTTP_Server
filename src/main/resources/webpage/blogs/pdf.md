# PDF文件迷思
一个奇怪的问题， 从服务器上下载的pdf乱码， 传输方式如下

	String htmlPath = this.getClass().getResource(url).getPath();
	File file = new File(htmlPath)
	FileInputStream fis = new FileInputStream(file);
	byte[] data = new byte[(int) file.length()];
	fis.read(data);
其中对头部的操作如下

	StringBuilder head = new StringBuilder("HTTP/1.1 200 OK" + "\r\n");
经过尝试后，加入下列语句后正常

	head.append("Content-Type: application/pdf; charset=utf-8" + "\r\n");
估计原因为，当未指定Content-Type头部栏时，浏览器有不同的编码。

参考：[字符编码笔记：ASCII，Unicode 和 UTF-8](http://www.ruanyifeng.com/blog/2007/10/ascii_unicode_and_utf-8.html)