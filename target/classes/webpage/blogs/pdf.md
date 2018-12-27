<h1 class="page-header">PDF文件迷思</h1> 
                    <p>一个奇怪的问题， 从服务器上下载的pdf乱码， 传输方式如下</p>
                    <pre class="pre-scrollable">
String htmlPath = this.getClass().getResource(url).getPath();
File file = new File(htmlPath);
FileInputStream fis = new FileInputStream(file);
byte[] data = new byte[(int) file.length()];
fis.read(data);  
                    </pre>
                    <p>其中对头部的操作如下</p>
                    <pre>
StringBuilder head = new StringBuilder("HTTP/1.1 200 OK" + "\r\n");
                    </pre>
                    <p>经过尝试后，加入下列语句后正常</p>
                    <pre>
head.append("Content-Type: application/pdf; charset=utf-8" + "\r\n");
                    </pre>
                    <p>估计原因为，当未指定Content-Type头部栏时，浏览器有不同的编码。</p>

                    <p>参考：<a href="http://www.ruanyifeng.com/blog/2007/10/ascii_unicode_and_utf-8.html">字符编码笔记：ASCII，Unicode 和 UTF-8</a></p>