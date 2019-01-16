"use strict";

var edit = {
    init: function(container, title, abstract, fileName, textareaValue) {
        view.reload = 1;
        view.flag = -1;
        //file name that will storage in the server
        this.fileName = fileName;
        //save container
        this.container = container;
        //editor default value
        this.textareaValue = textareaValue;
        //title
        this.title = title;
        //abstract
        this.abstract = abstract;
        //get edit html
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                //update index value
                var html = xmlhttp.responseText;
                edit.dispaly(html);
            }
        }
        xmlhttp.open("GET","/editormd/edit.html",true);
        xmlhttp.send();
    },

    dispaly: function(html) {
        this.container.innerHTML = html;
        //save button
        this.saveButton = document.getElementById("save");
        //title text
        this.titleText = document.getElementById("title");
        this.titleText.value = this.title;
        //abstract text
        this.abstractText = document.getElementById("abstract");
        this.abstractText.value = this.abstract;
        //textarea text
        this.textarea = document.getElementById("textarea");
        //set default value
        if(this.textareaValue != undefined) {
            this.textarea.innerHTML = this.textareaValue;
        }

        //bind click event
        this.saveButton.addEventListener("click", function() {
            edit.save();
        }, false);

        //create editor
        this.editor = editormd("editor", {
            width   : "100%",
            height  : 800,
            syncScrolling : "single",
            path    : 'editormd/lib/'
        });

        //change language
        editormd.loadScript("editormd/languages/en", function() {
            edit.editor.lang = editormd.defaults.lang;
                        
            // 只重建涉及语言包的部分，如工具栏、弹出对话框等
            //edit.editor.recreate();
            // 整个编辑器重建，预览HTML会重新生成，出现闪动
            //testEditor = editormd("test-editormd", {
                //width: "90%",
                //height: 640,
                //path : '../lib/'
            //});
        
        // lang = value;
        // console.log(lang, value, editormd.defaults.lang);                        
        });
    },

    save: function() {
        //Title and abstract can not be empty
        if(this.titleText.value == "" || this.abstractText.value == "") {
            alert("Title and abstract can not be empty!!!");
            return;
        }
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                //update page dispaly
                var text = xmlhttp.responseText;
                var json = JSON.parse(text);
                edit.fileName = json.file;
                var msg = json.message;
                alert(msg);
                //this.fileName = text.file;
            }
        }

        //get post message
        var msg = {
            title: this.titleText.value,
            abstract: this.abstractText.value,
            content: this.editor.getMarkdown(),
            file: this.fileName
        }
        var msgText = JSON.stringify(msg);
        xmlhttp.open("POST","/", true);
        xmlhttp.send(msgText);

        // marked.setOptions({
        //     renderer: new marked.Renderer(),
        //     gfm: true,
        //     tables: true,
        //     escaped : true,
        //     breaks: false,
        //     pedantic: false,
        //     sanitize: false,
        //     smartLists: true,
        //     smartypants: false,
        //     highlight: function (code) {
        //       return hljs.highlightAuto(code).value;
        //     }
        //   });

        // //Test
        // var text = this.editor.getMarkdown();
        // this.testBlock.innerHTML = marked(text);
    }
}
