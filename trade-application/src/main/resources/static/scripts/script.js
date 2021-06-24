var style = document.createElement('style');
style.innerHTML =
	'button {' +
		'color: white;' +
		'background-color: #008CBA;' +
	'}';
var ref = document.querySelector('script');
ref.parentNode.insertBefore(style, ref);

$(document).ready(function(){
    populateTrades();
});

function populateTrades(){
	$("#trades tbody").html("");
	$.ajax({
        url: '/trades/',
        type: 'get',
        dataType: 'JSON',
        success: function(response){
            let len = response.length;
            for(let i=0; i<len; i++){
                let id = response[i]._id;
                let date = response[i].created; 
                let ticker = response[i].ticker;
                let type = response[i].type;
                let status = response[i].state;
				
                let tr_str = "<tr>" +
                    "<td align='center' >" + id + "</td>" + 
                    "<td align='center'>" + ticker + "</td>" +
                    "<td align='center'>" + date + "</td>" +
                    "<td align='center'>" + type + "</td>" +
                    "<td align='center'>" + status + "</td>" +
                    "<td align='center'><button class='button details'>Details</button></td>" +
                    "<td align='center'><button class='button edit'>Edit</button></td>" + 
                    "<td align='center'><button class='button delete'>Delete</button></td>"
                    "</tr>";
                $("#trades tbody").append(tr_str);
            }
            
            $(document).find('.details').on('click',function(){
    			let trade_id = $(this).parents('tr:first').find('td:eq(0)').text();
    			localStorage.setItem("tradeid",trade_id);
    			window.location.replace("details.html");
			});
			
            $(document).find('.edit').on('click',function(){
    			let trade_id = $(this).parents('tr:first').find('td:eq(0)').text();
    			localStorage.setItem("tradeid",trade_id);
    			window.location.replace("editTrade.html");
    			
			});
			$(document).find('.delete').on('click',function(){
    			let trade_id = $(this).parents('tr:first').find('td:eq(0)').text();
    			let trade = $(this).parents('tr:first').find('td:eq(1)').text();
    			if(confirm("Are you sure you want to delete " + trade)){
    				deleteTrade(trade_id);
    			}
    			
			});
        }
    });
}

function populateInputs(trade_id){
	$.ajax({
        url: '/trades/' + trade_id,
        type: 'get',
        dataType: 'JSON',
        success: function(response){
            $("#id").text(response.hexString);
            $("#title").val(response.title);
            $("#artist").val(response.artist);
            $("#price").val(response.price);
        }
    });
    $('#save').prop('disabled', false);
    $('#add').prop('disabled', true);
}

function deleteTrade(trade_id){
	$.ajax({
        url: '/trades/' + trade_id,
        type: 'delete',
        success: function(response){
        	alert("Trade deleted");
            populateTrades();
        }
    });
}

function addTrade(){
	let trade = { "ticker":$("#ticker").val(),"type":$("#type").val(), "quantity":parseInt($("#quantity").val()) };
	$.ajax({
        url: '/trades/',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(trade),
        datatype: 'json',
        success: function(response){
            $("#ticker").text("");
            $("#type").val("");
            $("#quantity").val("");
            $('#add').prop('disabled', true);
            alert("hi");
            populateTrades();
        }
    });
}
