var style = document.createElement('style');
style.innerHTML =
	'button {' +
		'color: white;' +
		'background-color: #008CBA;' +
	'}';
var ref = document.querySelector('script');
ref.parentNode.insertBefore(style, ref);

$(document).ready(function(){
    userid = localStorage.getItem("userid");
    //alert(userid);
    var span = document.getElementsByClassName("close")[0];
			span.onclick = function() {
			  modal.style.display = "none";
			}
    viewTrades();
});

function viewTrades()
{
	userid = localStorage.getItem("userid");
	$("#trades tbody").html("");
	$.ajax({
        url: "/users/"+userid+"/trades",
        type: 'get',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        data: userid,
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
                    "<td align='center'><button class='button advice'>Advice</button></td>"+
                    "<td align='center'><button class='button delete'>Delete</button></td>"+
                    "</tr>";
                $("#trades tbody").append(tr_str);
            }
            $(document).find('.details').on('click',function(){
    			let trade_id = $(this).parents('tr:first').find('td:eq(0)').text();
    			localStorage.setItem("tradeid",trade_id);
    			window.location.replace("detailsnew.html");
			});
			$(document).find('.edit').on('click',function(){
    			let trade_id = $(this).parents('tr:first').find('td:eq(0)').text();
    			localStorage.setItem("tradeid",trade_id);
    			editTrade(trade_id);
    			
			});
			$(document).find('.delete').on('click',function(){
    			let trade_id = $(this).parents('tr:first').find('td:eq(0)').text();
    			let trade = $(this).parents('tr:first').find('td:eq(1)').text();
    			if(confirm("Are you sure you want to delete " + trade)){
    				deleteTrade(trade_id);
    			}
    			
			});
			$(document).find('.advice').on('click',function(){
    			let trade_id = $(this).parents('tr:first').find('td:eq(0)').text();
    			let trade = $(this).parents('tr:first').find('td:eq(1)').text();
    			getAdvice(trade_id);
    			
    			
			});
        }
    });
}

function editTrade(trade_id)
{
	$.ajax({
        url: "/users/"+userid+"/trades/"+trade_id,
        type: 'get',
        success: function(response){
        	let state = response.state;
            if(state == "CREATED")
            	window.location.replace("editnew.html");
            else
            	alert("Cannot edit the trade as it has already been processed!");
           }   
        
    });
	
}

function deleteTrade(trade_id){
	$.ajax({
        url: "/users/"+userid+"/trades/" + trade_id,
        type: 'delete',
        success: function(response){
        	alert("Trade deleted");
            viewTrades();
        }
    });
}

function getAdvice(trade_id)
{
	userid = localStorage.getItem("userid");
	$.ajax({
        url: "/users/"+userid+"/tradeAdvice/"+trade_id,
        type: 'get',
        success: function(response){
            document.getElementById("adviceid").innerHTML = response;
            var modal = document.getElementById("myModal");
            var span = document.getElementsByClassName("close")[0];
			span.onclick = function() {
			  modal.style.display = "none";
			}
            modal.style.display = "block";
          }   
        
    });
}

function addTrade(){
	let userid = localStorage.getItem("userid");
	let trade = { "ticker":$("#ticker").val(),"type":$("#type").val(), "quantity":parseInt($("#quantity").val()) };
	$.ajax({
        url: "/users/"+userid+"/trades",
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(trade),
        datatype: 'json',
        success: function(response){
            $("#ticker").val("");
            $("#type").val("");
            $("#quantity").val("");
            $('#add').prop('disabled', true);
        }
    });
}



