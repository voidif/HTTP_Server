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
        //tools(right side bar) window
        this.toolsView = document.getElementById("toolsView");
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
        this.goAbout.addEventListener("click", function() {
            view.switchView(2);
        }, false);

        //load maindisplayer and tools
        this.switchView(0);
    },

    //jump to about page

    //jump to index page
    jumpToIndex: function() {
        net.get("/index/content.html", function(xmlhttp) {
            //update index value
            var html = xmlhttp.responseText;
            view.setMaindisplay(html);
        });
        // var xmlhttp = new XMLHttpRequest();
        // xmlhttp.onreadystatechange = function(){
        //     if (xmlhttp.readyState == 4){
        //         //update index value
        //         var html = xmlhttp.responseText;
        //         view.setMaindisplay(html);
        //     }
        // }
        // xmlhttp.open("GET","/index/content.html",true);
        // xmlhttp.send();
    },



    //get Blog list and jump to blog page
    jumpToBlog: function() {
        net.get("/json?id=blog", function(xmlhttp) {
            //update page dispaly
            var text = xmlhttp.responseText;
            var blogJSON = JSON.parse(text);
            blogs.displayBlogList(blogJSON);
        });

        // var xmlhttp = new XMLHttpRequest();
        // xmlhttp.onreadystatechange = function(){
        //     if (xmlhttp.readyState == 4){
        //         //update page dispaly
        //         var text = xmlhttp.responseText;
        //         var blogJSON = JSON.parse(text);
        //         blogs.displayBlogList(blogJSON);
        //     }
        // }
        // xmlhttp.open("GET","/json?id=blog",true);
        // xmlhttp.send();
    },

    jumpToAbout: function() {
        alert("Not done yet!");
        this.switchView(0);
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
            //load tools view
            tool.init();
        }
        if(this.flag != newFlag) {
            this.flag = newFlag;
            this.setNaviBarHighlight(this.flag);
            switch (this.flag) {
                case 0: this.jumpToIndex();
                break;
                case 1: this.jumpToBlog();
                break;
                case 2: this.jumpToAbout();
                break;
            }
            
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
        this.toolsView = tools;

        this.container.innerHTML = "";
        this.container.appendChild(maindispaly);
        this.container.appendChild(tools);

        //set reload flag variable
        this.reload = 0;
    },

    //set the maindisplay content
    setMaindisplay: function(html) {
        this.maindispaly.innerHTML = html;
    },

    //append the maindisplay child
    appendMaindisplay: function(element) {
        this.maindispaly.appendChild(element);
    },


    //set the container content
    setContainer: function(html) {
        this.container.innerHTML = html;
    },

    //append the container child
    appendContainer: function(element) {
        this.container.appendChild(element);
    },

    //set the ToolsView content
    setToolsView: function(html) {
        this.toolsView.innerHTML = html;
    },

    //append the ToolsView child
    appendToolsView: function(element) {
        this.toolsView.appendChild(element);
    }

}