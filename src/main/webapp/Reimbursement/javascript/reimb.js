var ha = document.getElementById(newTicket);

ha.onsubmit = function (event){
	event.preventDefault();
	console.log("AAA I'm Here");
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
	console.log("BBB I'm Here");
