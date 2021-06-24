$(document).ready(function(){
    
});
function signup()
{
    let user = { "username":$("#username").val(),"password":$("#password").val() };
	$.ajax({
        url: '/users/signup/',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(user),
        datatype: 'json',
        success: function(response){
        	$("#username").val('');
            $("#password").val('');
            $("#signup").prop('disabled', true);
            //alert(response._id);
            window.location.assign("login.html");
        }
    });
}