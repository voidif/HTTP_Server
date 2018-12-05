"use strict";
// run after full load
function init() {
    document.getElementById("getRate").addEventListener("click", function() {
        getRate();
    }, false);
    
    var time = document.getElementById("time");
    var rate = document.getElementById("rate");
    time.innerHTML = new Date().toLocaleTimeString();
    getRate();
}

//get Rate function
function getRate() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function(){
        if (xmlhttp.readyState==4){
            //update rate
            var text = xmlhttp.responseText;
            var obj = JSON.parse(text);
            rate.innerHTML = obj.USD_CNY;

            //update time
            var myDate = new Date();
            time.innerHTML = myDate.toLocaleTimeString();
        }
    }
    xmlhttp.open("GET","?method=rate",true);
    xmlhttp.send();
}
