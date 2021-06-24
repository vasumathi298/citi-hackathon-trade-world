let id = localStorage.getItem("tradeid");
var style = document.createElement('style');
style.innerHTML =
	'button {' +
		'color: white;' +
		'background-color: #008CBA;' +
	'}';
var ref = document.querySelector('script');
ref.parentNode.insertBefore(style, ref);

$(document).ready(function(){
    showDetails();
});

function showDetails() {
	let id = localStorage.getItem("tradeid");
	$.ajax({
        url: "trades/"+id,
        type: 'get',
        dataType: 'JSON',
        success: function(response){
            document.getElementById("myTable").rows[1].cells[1].innerHTML = response.ticker
            document.getElementById("myTable").rows[2].cells[1].innerHTML = response.created
            document.getElementById("myTable").rows[3].cells[1].innerHTML = response.type
            document.getElementById("myTable").rows[4].cells[1].innerHTML = response.quantity
            document.getElementById("myTable").rows[5].cells[1].innerHTML = response.price
            document.getElementById("myTable").rows[6].cells[1].innerHTML = response.state
           }   
        
    });
}
