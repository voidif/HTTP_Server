// this js file deal all network issues, for example ajax, post delimiter
"use strict";
var net = {
    POST_DELIMITER: "\r\n\r\n\r\n",

    //get function, delimiter is \r\n\r\n (HTTP default)
    get: function(url, recallfunction) {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                recallfunction(xmlhttp);
            }
        }
        xmlhttp.open("GET", url, true);
        xmlhttp.send();
    }, 

    post: function(url, msg, recallfunction) {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState == 4){
                recallfunction(xmlhttp);
            }
        }
        var msgText = msg + POST_DELIMITER;
        xmlhttp.open("POST", url, true);
        xmlhttp.send(msgText);
    }
}