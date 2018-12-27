"use strict";
//global variables
//flag = 0(index), 1(blogs), 2(About)
var flag = 0;
//rate text(p)
var rate;
var time;
//maindisplay window(div)
var maindispaly;


// run after full load
function init() {
    //init variables
    time = document.getElementById("time");
    rate = document.getElementById("rate");
    maindispaly = document.getElementById("maindisplay");


    time.innerHTML = new Date().toLocaleTimeString();
    getRate();


    //bind getRate button
    document.getElementById("getRate").addEventListener("click", function() {
        getRate();
    }, false);
    
    //bind blogs navi bar button
    document.getElementById("goBlog").addEventListener("click", function() {
        jumpToBlog();
    }, false);
    

}



//jump to index page

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
        blogA.setAttribute("href", blogs[i].url);
        blogA.setAttribute("class", "list-group-item");

        var blogTitle = document.createElement("h4");
        blogTitle.setAttribute("class", "list-group-item-heading");
        blogTitle.appendChild(document.createTextNode(blogs[i].title));

        var blogAbstract = document.createElement("p");
        blogAbstract.setAttribute("class", "list-group-item-text");
        blogAbstract.appendChild(document.createTextNode("Test"));

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

//jump to about page

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