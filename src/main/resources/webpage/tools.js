"use strict";

//this js control tool bar behavior
var tools = {
    //flag = 0(index), 1(blogs), 2(About)
    flag : 0,
    // run after full load
    init: function() {
        //init variables
        this.time = document.getElementById("time");
        //rate text(p)
        this.rate = document.getElementById("rate");
        
        //bind getRate button
        document.getElementById("getRate").addEventListener("click", function() {
            tools.getRate();
        }, false);

        this.getRate();
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
    }
}