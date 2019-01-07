"use strict";

//this js control tool bar behavior
var tool = {
    // run after full load
    init: function() {
        //save pre HTML element
        this.tools = document.getElementById("tools");
        //load Rate tool
        this.loadRate();
    },

    //get Rate function
    getRate: function() {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                //update rate
                var text = xmlhttp.responseText;
                var obj = JSON.parse(text);
                tool.rate.innerHTML = obj.USD_CNY;

                //update time
                var myDate = new Date();
                tool.time.innerHTML = myDate.toLocaleTimeString();
            }
        }
        xmlhttp.open("GET","/json?id=rate",true);
        xmlhttp.send();
    },

    //load exchange rate tool
    loadRate: function() {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                //update rate
                var text = xmlhttp.responseText;
                
                //Add rate tools
                tools.innerHTML = text;
                //init variables
                tool.time = document.getElementById("time");
                //rate text(p)
                tool.rate = document.getElementById("rate");
                
                //bind getRate button
                document.getElementById("getRate").addEventListener("click", function() {
                    tool.getRate();
                }, false);
                tool.getRate();
            }
        }
        xmlhttp.open("GET","/tools/rate.html", true);
        xmlhttp.send();
    }
}