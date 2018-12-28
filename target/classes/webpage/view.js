"use strict";

//this js control display behavior
var view = {
    
    // run after full load
    init: function() {
        //flag = 0(index), 1(blogs), 2(About)
        this.flag = 0;
        //maindisplay window(div)
        this.maindispaly = document.getElementById("maindisplay");
        //navi bar buttons
        this.goIndex = document.getElementById("goIndex");
        this.goBlog = document.getElementById("goBlog");
        this.goAbout = document.getElementById("goAbout");

        //bind navi bar buttons
        this.goIndex.addEventListener("click", function() {
            view.jumpToIndex();
        }, false);
        this.goBlog.addEventListener("click", function() {
            view.jumpToBlog();
        }, false);
    },

    //jump to about page

    //jump to index page
    jumpToIndex: function() {
        //if already in index page, return
        if (this.flag == 0) {
            return;
        }
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                //update index value
                var html = xmlhttp.responseText;
                view.maindispaly.innerHTML = html;
                view.flag = 0;
                view.setNaviBarHighlight(0);
            }
        }
        xmlhttp.open("GET","/index/content.html",true);
        xmlhttp.send();
    },

    //jump to blogs page
    jumpToBlog: function () {
        //if already in blogs page, return
        if (this.flag == 1) {
            return;
        }
        this.getBlogList();
        this.flag = 1;
    },

    //Display blogs page according to blogs JSON object
    displayBlog: function (blogJSON) {
        //clear current child element
        this.maindispaly.innerHTML = "";

        var blogsDiv = document.createElement("div");
        blogsDiv.setAttribute("class", "list-group");
        this.maindispaly.appendChild(blogsDiv);

        var blogs = blogJSON.blogs;
        for (var i = 0, len = blogs.length; i < len; i++) { 
            var blogA = document.createElement("a");
            blogA.setAttribute("blogurl", blogs[i].url);
            blogA.setAttribute("class", "list-group-item");
            //add title click event
            blogA.addEventListener("click", function() {
                view.readBlog(blogA);
            }, false);

            var blogTitle = document.createElement("h4");
            blogTitle.setAttribute("class", "list-group-item-heading");
            blogTitle.innerHTML = blogs[i].title;

            var blogAbstract = document.createElement("p");
            blogAbstract.setAttribute("class", "list-group-item-text");
            blogAbstract.innerHTML = "Test";

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
        this.setNaviBarHighlight(1);
    },


    //get Blog list
    getBlogList: function() {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                //update page dispaly
                var text = xmlhttp.responseText;
                var blogJSON = JSON.parse(text);
                view.displayBlog(blogJSON);
            }
        }
        xmlhttp.open("GET","/json?id=blog",true);
        xmlhttp.send();
    },

    //Set navi bar highlight
    setNaviBarHighlight: function(index) {
        switch (index) {
            case 0: {
                this.goIndex.setAttribute("class", "active");
                this.goBlog.setAttribute("class", "inactive");
                this.goAbout.setAttribute("class", "inactive");
                break;
            }
            case 1: {
                this.goIndex.setAttribute("class", "inactive");
                this.goBlog.setAttribute("class", "active");
                this.goAbout.setAttribute("class", "inactive");
                break;
            }
            case 2: {
                this.goIndex.setAttribute("class", "inactive");
                this.goBlog.setAttribute("class", "inactive");
                this.goAbout.setAttribute("class", "active");
                break;
            }
        }
    },

    //Blog Title Click Event
    readBlog: function(html) {
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
                var text = xmlhttp.responseText;
                view.maindispaly.innerHTML = marked(text);
                view.flag = -1;
            }
        }
        var url = html.getAttribute("blogurl");
        xmlhttp.open("GET", url, true);
        xmlhttp.send();

    }
}