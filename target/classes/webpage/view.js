"use strict";

//this js control display behavior
var view = {
    
    // run after full load
    init: function() {
        //flag = 0(index), 1(blogs), 2(About)
        this.flag = -1;
        //reload = 0(no need to reload tools), 1(need reload tools)
        this.reload = 1;
        //maindisplay window(div)
        this.maindispaly = document.getElementById("maindisplay");
        //container window(div)
        this.container = document.getElementById("container");
        //navi bar buttons
        this.goIndex = document.getElementById("goIndex");
        this.goBlog = document.getElementById("goBlog");
        this.goAbout = document.getElementById("goAbout");

        //bind navi bar buttons
        this.goIndex.addEventListener("click", function() {
            view.switchView(0);
        }, false);
        this.goBlog.addEventListener("click", function() {
            view.switchView(1);
        }, false);

        //load maindisplayer and tools
        this.switchView(0);
    },

    //jump to about page

    //jump to index page
    jumpToIndex: function() {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                //update index value
                var html = xmlhttp.responseText;
                view.maindispaly.innerHTML = html;
            }
        }
        xmlhttp.open("GET","/index/content.html",true);
        xmlhttp.send();
    },


    //Display blogs page according to blogs JSON object
    displayBlog: function (blogJSON) {
        //clear current child element
        this.maindispaly.innerHTML = "";

        var blogsDiv = document.createElement("div");
        blogsDiv.setAttribute("class", "list-group");
        this.maindispaly.appendChild(blogsDiv);

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
                view.readBlog(event);
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
    },


    //get Blog list and jump to blog page
    jumpToBlog: function() {
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
                var text = xmlhttp.responseText;
                view.maindispaly.innerHTML = marked(text);
                view.maindispaly.insertAdjacentHTML("afterbegin", 
                    "<button class=\"btn btn-default\" id=\"edit\">Edit</button>");
                document.getElementById("edit").addEventListener("click", function() {
                    //view changed
                    edit.init(view.container, view.getBlogFileName(url), text);
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
    getBlogFileName: function() {
        //TODO
        return "test";
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



    //changeView based on flag and reload
    switchView: function(newFlag) {
        if (this.reload == 1) {
            this.reloadView();
        }
        if(this.flag != newFlag) {
            this.flag = newFlag;
            switch (this.flag) {
                case 0: this.jumpToIndex();
                break;
                case 1: this.jumpToBlog();
                break;
                case 2: this.jumpToAbout();
                break;
            }
            this.setNaviBarHighlight(this.flag);
        }
        
    },

    //reload container view, add maindisplayer div and tools div
    reloadView: function() {
        //maindisplayer
        var maindispaly = document.createElement("div");
        maindispaly.setAttribute("class", "col-md-8");
        maindispaly.setAttribute("id", "maindisplay");

        var tools = document.createElement("div");
        tools.setAttribute("class", "col-md-4");
        tools.setAttribute("id", "tools");
        // <div class="col-md-8" id="maindisplay"></div>
        // <div class="col-md-4"></div>
        
        this.maindispaly = maindispaly;
        this.tools = tools;

        this.container.innerHTML = "";
        this.container.appendChild(maindispaly);
        this.container.appendChild(tools);

        //set reload flag variable
        this.reload = 0;

        //load tools view
        tool.init();
    }
}