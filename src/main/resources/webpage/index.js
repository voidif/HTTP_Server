"use strict";
//global variables
//flag = 0(index), 1(blogs), 2(About)
var flag = 0;
//rate text(p)
var rate;
var time;
//maindisplay window(div)
var maindispaly;
//navi bar buttons
var goIndex;
var goBlog;
var goAbout;

// run after full load
function init() {
    //init variables
    time = document.getElementById("time");
    rate = document.getElementById("rate");
    maindispaly = document.getElementById("maindisplay");
    goIndex = document.getElementById("goIndex");
    goBlog = document.getElementById("goBlog");
    goAbout = document.getElementById("goAbout");

    time.innerHTML = new Date().toLocaleTimeString();
    getRate();


    //bind getRate button
    document.getElementById("getRate").addEventListener("click", function() {
        getRate();
    }, false);
    
    //bind navi bar buttons
    goIndex.addEventListener("click", function() {
        jumpToIndex();
    }, false);
    goBlog.addEventListener("click", function() {
        jumpToBlog();
    }, false);
    

}

//jump to about page

//jump to index page
function jumpToIndex() {
    //if already in index page, return
    if (flag == 0) {
        return;
    }
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState == 4){
            //update index value
            var html = xmlhttp.responseText;
            maindispaly.innerHTML = html;
            flag = 0;
            setNaviBarHighlight(0);
        }
    }
    xmlhttp.open("GET","/index/content.html",true);
    xmlhttp.send();
}

//jump to blogs page
function jumpToBlog() {
    //if already in blogs page, return
    if (flag == 1) {
        return;
    }
    getBlogList();
    flag = 1;
}

//Display blogs page according to blogs JSON object
function displayBlog(blogJSON) {
    //clear current child element
    maindispaly.innerHTML = "";

    var blogsDiv = document.createElement("div");
    blogsDiv.setAttribute("class", "list-group");
    maindispaly.appendChild(blogsDiv);

    var blogs = blogJSON.blogs;
    for (var i = 0, len = blogs.length; i < len; i++) { 
        var blogA = document.createElement("a");
        blogA.setAttribute("blogurl", blogs[i].url);
        blogA.setAttribute("class", "list-group-item");
        //add title click event
        blogA.addEventListener("click", function() {
            readBlog(blogA);
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
    setNaviBarHighlight(1);
}


//get Rate function
function getRate() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState == 4){
            //update rate
            var text = xmlhttp.responseText;
            var obj = JSON.parse(text);
            rate.innerHTML = obj.USD_CNY;

            //update time
            var myDate = new Date();
            time.innerHTML = myDate.toLocaleTimeString();
        }
    }
    xmlhttp.open("GET","/json?id=rate",true);
    xmlhttp.send();
}

//get Blog list
function getBlogList() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState == 4){
            //update page dispaly
            var text = xmlhttp.responseText;
            var blogJSON = JSON.parse(text);
            displayBlog(blogJSON);
        }
    }
    xmlhttp.open("GET","/json?id=blog",true);
    xmlhttp.send();
}

//Set navi bar highlight
function setNaviBarHighlight(index) {
    switch (index) {
        case 0: {
            goIndex.setAttribute("class", "active");
            goBlog.setAttribute("class", "inactive");
            goAbout.setAttribute("class", "inactive");
            break;
        }
        case 1: {
            goIndex.setAttribute("class", "inactive");
            goBlog.setAttribute("class", "active");
            goAbout.setAttribute("class", "inactive");
            break;
        }
        case 2: {
            goIndex.setAttribute("class", "inactive");
            goBlog.setAttribute("class", "inactive");
            goAbout.setAttribute("class", "active");
            break;
        }
    }
}

//Blog Title Click Event
function readBlog(html) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState == 4){
            //Parse markdown to HTML then update blog dispaly
            var text = xmlhttp.responseText;
            var converter = new showdown.Converter()
            maindispaly.innerHTML = converter.makeHtml(text);
            flag = -1;
        }
    }
    var url = html.getAttribute("blogurl");
    xmlhttp.open("GET", url, true);
    xmlhttp.send();

}