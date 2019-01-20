"use strict";

//This class control blog content and logic
var blog = {

    //Blog Title Click Event
    //view changed
    readBlog: function(event) {
        var index = event.target.getAttribute("index");
        this.blogJSON = blogs.blogListJSON.blogs[index];

        

        // var xmlhttp = new XMLHttpRequest();
        // xmlhttp.onreadystatechange = function(){
        //     if (xmlhttp.readyState == 4){
        //         //Parse markdown to HTML then update blog dispaly
        //         marked.setOptions({
        //             renderer: new marked.Renderer(),
        //             gfm: true,
        //             tables: true,
        //             escaped : true,
        //             breaks: false,
        //             pedantic: false,
        //             sanitize: false,
        //             smartLists: true,
        //             smartypants: false,
        //             highlight: function (code) {
        //               return hljs.highlightAuto(code).value;
        //             }
        //           });
        //         var url = xmlhttp.responseURL;
        //         blog.text = xmlhttp.responseText;

        //         //edit button
        //         var buttonString = "<button class=\"btn btn-default\" id=\"edit\">Edit</button>";

        //         view.setMaindisplay(buttonString + marked(blog.text));
        //         // view.maindispaly.innerHTML = marked(text);
        //         // view.maindispaly.insertAdjacentHTML("afterbegin", 
        //         //     );
        //         document.getElementById("edit").addEventListener("click", function() {
        //             //view changed
        //             edit.init(view.container, blog.blogJSON.title, blog.blogJSON.abstract, 
        //                 blog.getBlogFileName(url), blog.text);
        //         }, false);
        //         view.flag = -1;
        //     }
        // }
        
        var url = this.blogJSON.url;
        // console.log(url);
        xmlhttp.open("GET", url, true);
        xmlhttp.send();

        net.get(url, function(xmlhttp) {
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
                edit.init(view.container, blog.blogJSON.title, blog.blogJSON.abstract, 
                    blog.getBlogFileName(url), blog.text);
            }, false);
            view.flag = -1;
        });
    },

    //support function
    getBlogFileName: function(url) {
        var words = url.split('/');
        return words[words.length - 1];
    }
}


//variables that controls blog list 
var blogs = {

    //Display blogs page according to blogs JSON object
    displayBlogList: function (blogJSON) {
        this.blogListJSON = blogJSON;

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
            edit.init(view.container, "", "", "", "");
        }, false);
        // var addBlog = document.createElement("p");
        // addBlog.setAttribute("class", "list-group-item");
        addBlogA.innerHTML = "new blog";

        // addBlogA.appendChild(addBlog);
        blogsDiv.appendChild(addBlogA);

        //load blogs 
        var blogs = blogJSON.blogs;
        for (var i = 0, len = blogs.length; i < len; i++) { 
            var blogListItem = document.createElement("li");
            blogListItem.setAttribute("class", "list-group-item");


            var blogA = document.createElement("a");
            //blogA.setAttribute("blogurl", blogs[i].url);
            //blogA.setAttribute("class", "list-group-item");
            //add title click event

            var blogTitle = document.createElement("h4");
            blogTitle.setAttribute("index", i);

            blogTitle.innerHTML = blogs[i].title;
            blogTitle.addEventListener("click", function() {
                blog.readBlog(event);
            }, false);

            var blogAbstract = document.createElement("p");
            blogAbstract.innerHTML = blogs[i].abstract;

            blogA.appendChild(blogTitle);

            blogListItem.appendChild(blogA);
            blogListItem.appendChild(blogAbstract);

            blogsDiv.appendChild(blogListItem);

            //example HTML
            /*
            <div class="list-group">
                <li class="list-group-item">
                    <a href="#"><h4>页面标题实例<h4></a>
                    <p>这是一个示例文本。这是一个示例文本。这是一个示例文本。这是一个示例文本。这是一个示例文本。</p>
                </li>
                <li class="list-group-item">
                    <a href="#"><h4>页面标题实例<h4></a>
                    <p>这是一个示例文本。这是一个示例文本。这是一个示例文本。这是一个示例文本。这是一个示例文本。</p>
                </li>
            </div>
            */
        }

    }
}