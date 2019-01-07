"use strict";

//This class control blog content and logic
var blog = {

    //Blog Title Click Event
    //view changed
    readBlog: function(event) {

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                //Parse markdown to HTML then update blog dispaly
                marked.setOptions({
                    renderer: new marked.Renderer(),
                    gfm: true,
                    tables: true,
                    escaped : true,
                    breaks: false,
                    pedantic: false,
                    sanitize: false,
                    smartLists: true,
                    smartypants: false,
                    highlight: function (code) {
                      return hljs.highlightAuto(code).value;
                    }
                  });
                var url = xmlhttp.responseURL;
                blog.text = xmlhttp.responseText;

                //edit button
                var buttonString = "<button class=\"btn btn-default\" id=\"edit\">Edit</button>";

                view.setMaindisplay(buttonString + marked(blog.text));
                // view.maindispaly.innerHTML = marked(text);
                // view.maindispaly.insertAdjacentHTML("afterbegin", 
                //     );
                document.getElementById("edit").addEventListener("click", function() {
                    //view changed
                    edit.init(view.container, blog.getBlogFileName(url), blog.text);
                }, false);
                view.flag = -1;
            }
        }
        var url = event.target.getAttribute("blogurl");
        // console.log(url);
        xmlhttp.open("GET", url, true);
        xmlhttp.send();
    },

    //support function
    getBlogFileName: function(url) {
        var words = url.split('/');
        return words[words.length - 1];
    },

        //Display blogs page according to blogs JSON object
    displayBlog: function (blogJSON) {


        var blogsDiv = document.createElement("div");
        blogsDiv.setAttribute("class", "list-group");

        view.setMaindisplay("");
        view.appendMaindisplay(blogsDiv);

        //add new blog button
        var addBlogA = document.createElement("a");
        addBlogA.setAttribute("class", "list-group-item");
        //add new blog click event
        addBlogA.addEventListener("click", function() {
            //view changed
            edit.init(view.container, "");
        }, false);
        var addBlog = document.createElement("h4");
        addBlog.setAttribute("class", "list-group-item-heading");
        addBlog.innerHTML = "new blog";

        addBlogA.appendChild(addBlog);
        blogsDiv.appendChild(addBlogA);

        //load blogs 
        var blogs = blogJSON.blogs;
        for (var i = 0, len = blogs.length; i < len; i++) { 
            var blogA = document.createElement("a");
            blogA.setAttribute("blogurl", blogs[i].url);
            blogA.setAttribute("class", "list-group-item");
            //add title click event
            blogA.addEventListener("click", function() {
                blog.readBlog(event);
            }, false);

            var blogTitle = document.createElement("h4");
            blogTitle.setAttribute("blogurl", blogs[i].url);
            blogTitle.setAttribute("class", "list-group-item-heading");
            blogTitle.innerHTML = blogs[i].title;

            var blogAbstract = document.createElement("p");
            blogAbstract.setAttribute("blogurl", blogs[i].url);
            blogAbstract.setAttribute("class", "list-group-item-text");
            blogAbstract.innerHTML = blogs[i].abstract;

            blogA.appendChild(blogTitle);
            blogA.appendChild(blogAbstract);
            blogsDiv.appendChild(blogA);

            //example HTML
            /*
            <div class="list-group">
                <a href="1.html" class="list-group-item">
                    <h4 class="list-group-item-heading">
                        PDF文件迷思
                    </h4>
                    <p class="list-group-item-text">
                        一个奇怪的问题， 从服务器上下载的pdf乱码， 传输方式如下
                    </p>
                </a>
                <a href="#" class="list-group-item">
                    <h4 class="list-group-item-heading">
                        还没有Blog!
                    </h4>
                    <p class="list-group-item-text">
                        还没有简介！
                    </p>
                </a>
            </div>
            */
        }

    }
}