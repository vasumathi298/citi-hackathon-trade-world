$(document).ready(function(){
//    document.getElementById("loginForm").addEventListener("submit", submitHandler);
//      loginnow();
});

function loginnow()
{
    let user = { "username":$("#username").val(),"password":$("#password").val() };
	$.ajax({
        url: '/users/login/',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(user),
        datatype: 'json',
        success: function(response){
        	$("#username").text("");
            $("#password").text("");
            $('#login').prop('disabled', true);
            localStorage.setItem("userid",response._id);
            window.location.replace("homepage.html");
        },
        error: function(response) {
        	alert("Username/password incorrect");
        }
    });
}


async function submitHandler(event) {
                alert("triggered handler");
                // stops the form submitting itself we want to send the data to a REST API
                event.preventDefault();
                let form = event.target;
                let user = {}; 
                user.username = form.username.value;
                user.password = form.password.value;
                // done with a promise using await / async syntax
                let response = await loginUser(user);
                alert(response.username);
                //localStorage.setItem("userid",response._id);
                window.location.href="viewTrades.html";
                return false;
            }

async function loginUser(user) {
    let response = await fetch("/users/login", {
        method: "POST",
        headers :{
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    });
}



