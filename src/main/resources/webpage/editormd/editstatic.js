"use strict";

var edit = {
    init: function() {
        //save button
        this.saveButton = document.getElementById("save");
        //test
        this.testBlock = document.getElementById("testblock");

        this.editor = editormd("editor", {
            width   : "100%",
            height  : 800,
            syncScrolling : "single",
            path    : "lib/"
        });

        editormd.loadScript("languages/en", function() {
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


        this.saveButton.addEventListener("click", function() {
            edit.save();
        }, false);
    },

    save: function() {
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

        //Test
        var text = this.editor.getMarkdown();
        this.testBlock.innerHTML = marked(text);
    }
}