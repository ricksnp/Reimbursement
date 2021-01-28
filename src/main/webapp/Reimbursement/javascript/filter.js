


makeEmployee.onsubmit = function (event){
	event.preventDefault();
	if(!/^\d+\.\d+/.test(amount.value)){
		alert("Amount must be a number in $0.00 format!");
		return;
	}
	
	if(!/\S+/.test(reason.value)){
		alert("Please enter a description!");
		return;
	}
	
  	const url = "http://localhost:8080/Reimbursement/NewReimbursement?"+
  		`username=${username.value}&password=${password.value}&amount=${amount.value}&`+
  			`desc=${reason.value}&author=${author.value}&type=${type.value}`;
  			
  	console.log(url);
  	fetch(url , {method: 'POST'})
    .then((response) => {
    console.log(response)
    response.text().then(function (text) {
    employeeTable.innerHTML = text;
	});
    
    }
   )
}


const filterButton = document.getElementById("filterButton");
filterButton.onclick = function(event) {
	const remIdList = document.getElementById("remIdList");
	const remIds = remIdList.value.split(" ");
	const showPending = document.getElementById("showPending").checked;
	const showRejected = document.getElementById("showRejected").checked;
	const showAccepted = document.getElementById("showAccepted").checked;

	for (remId of remIds) {
		console.log(remId);
		const select = document.getElementById("type" + remId);
		if (select) {
			const row = document.getElementById("filterRow" + remId);
			if ((showPending && select.value == 1) ||(showRejected && select.value == 2) ||(showAccepted && select.value == 3)){
				row.style.visibility = "visible";
			}

			else {
				row.style.visibility = "hidden";
			}
		}
	}
}
