let id = localStorage.getItem("tradeid");
var style = document.createElement('style');
style.innerHTML =
	'button {' +
		'color: white;' +
		'background-color: #008CBA;' +
	'}';
var ref = document.querySelector('script');
ref.parentNode.insertBefore(style, ref);

function editTrade() {
	let id = localStorage.getItem("tradeid");
	let trade = { "quantity":parseInt($("#quantity").val()) };
	$.ajax({
        url: "trades/"+id,
        type: 'put',
        contentType: 'application/json',
        data: JSON.stringify(trade),
        datatype: 'json',
        success: function(response){
        	alert(id);
            $("#quantity").val("");
            $('#edit').prop('disabled', true);
        }
    });
}
