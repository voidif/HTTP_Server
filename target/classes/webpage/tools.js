"use strict";

//this js control tool bar behavior
var tools = {
    // run after full load
    init: function(pre) {
        //save pre HTML element
        this.pre = pre;
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
                tools.rate.innerHTML = obj.USD_CNY;

                //update time
                var myDate = new Date();
                tools.time.innerHTML = myDate.toLocaleTimeString();
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
                tools.pre.insertAdjacentHTML('afterend', text);;
                //init variables
                tools.time = document.getElementById("time");
                //rate text(p)
                tools.rate = document.getElementById("rate");
                
                //bind getRate button
                document.getElementById("getRate").addEventListener("click", function() {
                    tools.getRate();
                }, false);
                tools.getRate();
            }
        }
        xmlhttp.open("GET","/tools/rate.html", true);
        xmlhttp.send();
    }
}