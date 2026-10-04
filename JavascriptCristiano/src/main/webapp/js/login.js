(function () {
    let loginButton = document.getElementById("login_button");
    let loginWarningDiv = document.getElementById("login_warning_msg");
    let callback = function(request) {
		if (request.readyState == XMLHttpRequest.DONE) {
			switch(request.status){
   	            case 200: //Okay, parse and save data to local storage and go to home
                    let user = JSON.parse(request.responseText);
                    localStorage.setItem("id", user.id);
                    localStorage.setItem("name", user.user);
                    localStorage.setItem("professore", user.professore);
                    if (user.professore){
						window.location.href = "professore.html";
						localStorage.setItem("idDocente", user.idDocente);
					}else{
						window.location.href = "studente.html";
						localStorage.setItem("matricola", user.matricola);	
					}                     
                    break;
                case 400: // bad request  (fallthrough)
                case 401: // unauthorized       |
                case 500: // server error       v
                    loginWarningDiv.textContent = request.responseText;
                    loginWarningDiv.style.display = "block"; // Show error div
                    break;
                default: //Error
                    loginWarningDiv.textContent = "Request reported status " + request.status;
                    loginWarningDiv.style.display = "block"; // Show error div
    		}
		}
	};
  
    attachForm(loginButton,loginWarningDiv,"Login",callback);
    
})();