( function (){
	// Initialize main controller on window load.
    window.addEventListener("load", () => {
	    var pageOrchestrator = new PageOrchestrator();
	    pageOrchestrator.refresh();
    });
    
    
    /**
	 * Main page controller
	 */
    class PageOrchestrator {   
        constructor() {
			this.userData = new UserData(this);
            this.corsi = new Corsi(this);
            this.appelli = new Appelli(this);
            this.voto = new Voto(this);
            this.current = this.corsi;
        };
        
        refresh() {
			this.corsi.show();
			this.current = this.corsi;
			this.userData.show();
		};
		
		goToCorsi(){
			this.current.hide();
			this.corsi.show();
			this.current = this.corsi;
		}
		goToAppelli(idCorso){
			this.current.hide();
			this.appelli.show(idCorso);
			this.current = this.appelli;
		}
		goToVoto(idCorso,data,matricola){
			this.current.hide();
			this.voto.show(idCorso,data,matricola);
			this.current = this.voto;
		}

    }
    
    let warningDiv = document.getElementById("warning_div"); 
    
    
    class Corsi{
		constructor(pageOrchestrator){
			this.corsiTable = document.getElementById("corsi-table");
			this.corsiDiv = document.getElementById("corsi-div");
			this.pageOrchestrator = pageOrchestrator;
			this.corsiDiv.style.display = "none";
		}
		
		show(){
			makeCall("GET", "GetCorsi", null, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					switch(request.status){
		                case 200: //Okay, save the taxonomy
		                    this.corsi = JSON.parse(request.responseText);
		                    warningDiv.style.display = "none"; // Reset error div
		                    this.update();
		                    break;
		                case 400: // bad request  (fallthrough)
		                case 401: // unauthorized       |
		                case 500: // server error       v
		                    warningDiv.textContent = request.responseText;
		                    warningDiv.style.display = "block"; // Show error div
		                    break;
		                default: //Error
		                    warningDiv.textContent = "Request reported status " + request.status;
		                    warningDiv.style.display = "block"; // Show error div
		    		}
				}
			})
		}
		
		update(){
			document.querySelectorAll(".riga-corso").forEach(riga => riga.remove());			
			this.corsi.forEach(corso => {
				let row = document.createElement("tr");
				row.setAttribute("class", "riga-corso");
				let idCorso = document.createElement("td");
				idCorso.textContent = corso.idCorso;
				let nomeCorso = document.createElement("td");
				nomeCorso.textContent = corso.nomeCorso;
				let idDocente = document.createElement("td");
				idDocente.textContent = corso.idDocente;	
				let appelliButton = document.createElement("td");
				appelliButton.classList.add("btn","btn-small","btn-colored-solid");
				appelliButton.innerText = "appelli";
				
				appelliButton.addEventListener("click", () => {
					this.pageOrchestrator.goToAppelli(corso.idCorso);
				})
				
				row.appendChild(idCorso);
				row.appendChild(nomeCorso);
				row.appendChild(idDocente);
				row.appendChild(appelliButton);
				this.corsiTable.appendChild(row);
				
			})
			this.corsiTable.style.display= "";
			this.corsiDiv.style.display = "";
		}
		
		hide(){
			this.corsiDiv.style.display = "none";
		}
		
	}
	
	 class Appelli{
		constructor(pageOrchestrator){
			this.appelliTable = document.getElementById("appelli-table");
			this.appelliDiv = document.getElementById("appelli-div");
			this.nomeCorsoTitolo = document.getElementById("idCorso-titolo");
			this.pageOrchestrator = pageOrchestrator;
			this.appelliDiv.style.display = "none";
			
		}
		
		show(idCorso){
			let corsoForm = new FormData();
			corsoForm.append("idCorso", idCorso);

			makeCall("POST", "GetAppelli", corsoForm , request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					switch(request.status){
		                case 200: //Okay, save the taxonomy
		                    this.appelli = JSON.parse(request.responseText);
		                    warningDiv.style.display = "none"; // Reset error div
		                    this.update(idCorso);
		                    break;
		                case 400: // bad request  (fallthrough)
		                case 401: // unauthorized       |
		                case 500: // server error       v
		                    warningDiv.textContent = request.responseText;
		                    warningDiv.style.display = "block"; // Show error div
		                    break;
		                default: //Error
		                    warningDiv.textContent = "Request reported status " + request.status;
		                    warningDiv.style.display = "block"; // Show error div
		    		}
				}
			})
		}
		
		update(idCorso){
			document.querySelectorAll(".riga-appello").forEach(riga => riga.remove());
			this.nomeCorsoTitolo.innerText = idCorso;
			this.matricola = localStorage.getItem("matricola");
			this.appelli.forEach(appello => {
				let row = document.createElement("tr");
				row.setAttribute("class", "riga-appello");
				let data = document.createElement("td");
				console.log(appello.dataString);
				data.textContent = appello.dataString;
		
				let votoButton = document.createElement("td");
				votoButton.classList.add("btn","btn-small","btn-colored-solid");
				votoButton.innerText = "voto";
				
				votoButton.addEventListener("click", () => {
					this.pageOrchestrator.goToVoto(appello.idCorso,appello.dataString,this.matricola);
				})
				
				row.appendChild(data);
				row.appendChild(votoButton);
				this.appelliTable.appendChild(row);
				
			})
			this.appelliTable.style.display= "";
			this.appelliDiv.style.display = "";
		
		}
		
		hide(){
			this.appelliDiv.style.display = "none";
		}
	}
	 
	class Voto{
		constructor(pageOrchestrator){
			this.votoTable = document.getElementById("voto-table");
			this.votoDiv = document.getElementById("voto-div");
			this.votoDiv.style.display = "none";
			this.nomeCorsoTitoloVoto = document.getElementById("nomeCorso-voto-titolo");
			this.dataTitoloVoto = document.getElementById("data-voto-titolo");
			this.votoNonDefinito = document.getElementById("voto-non-definito");
			this.rigaIdCorso = document.getElementById("riga-idCorso");
			this.rigaIdDocente = document.getElementById("riga-idDocente");
			this.rigaData = document.getElementById("riga-data");
			this.rigaMatricola = document.getElementById("riga-matricola");
			this.rigaCognome = document.getElementById("riga-cognome");
			this.rigaNome = document.getElementById("riga-nome");
			this.rigaVoto = document.getElementById("riga-voto");
			this.votoRifiutato = document.getElementById("voto-rifiutato");
			this.rifiutaBottone = document.getElementById("rifiuta-button");
			this.pageOrchestrator = pageOrchestrator;
			
			
			this.rifiutaBottone.addEventListener("click", () => {
				let rifiutaVotoForm = new FormData();
				rifiutaVotoForm.append("idCorso", this.esito.studente.idCorso);
				rifiutaVotoForm.append("data", this.esito.dataString);
				rifiutaVotoForm.append("matricola", this.esito.studente.matricola);
				
				makeCall("POST", "RifiutaVoto", rifiutaVotoForm , request => {
					if (request.readyState == XMLHttpRequest.DONE) {
						switch(request.status){
			                case 200: //Okay, save the taxonomy			                    
			                    warningDiv.style.display = "none"; // Reset error div			            		             
			                    this.pageOrchestrator.goToVoto(this.esito.studente.idCorso,this.esito.dataString,this.esito.studente.matricola);
			                    break;
			                case 400: // bad request  (fallthrough)
			                case 401: // unauthorized       |
			                case 500: // server error       v
			                    warningDiv.textContent = request.responseText;
			                    warningDiv.style.display = "block"; // Show error div
			                    break;
			                default: //Error
			                    warningDiv.textContent = "Request reported status " + request.status;
			                    warningDiv.style.display = "block"; // Show error div
			    		}
					}
				})
					
			})
			
			
		}
		
		show(idCorso,data,matricola){
			let votoForm = new FormData();
			votoForm.append("idCorso", idCorso);
			votoForm.append("data", data);
			votoForm.append("matricola", matricola);

			makeCall("POST", "GetVoto", votoForm , request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					switch(request.status){
		                case 200: //Okay, save the taxonomy
		                    this.esito = JSON.parse(request.responseText);
		              		console.log(this.esito);
		                    warningDiv.style.display = "none"; // Reset error div
		                    this.update();
		                    break;
		                case 400: // bad request  (fallthrough)
		                case 401: // unauthorized       |
		                case 500: // server error       v
		                    warningDiv.textContent = request.responseText;
		                    warningDiv.style.display = "block"; // Show error div
		                    break;
		                default: //Error
		                    warningDiv.textContent = "Request reported status " + request.status;
		                    warningDiv.style.display = "block"; // Show error div
		    		}
				}
			})
		}
		
		update(){
			this.rifiutaBottone.style.display = "none";
			this.votoNonDefinito.style.display = "none"
			this.votoRifiutato.style.display = "none";
			this.nomeCorsoTitoloVoto.innerText = this.esito.studente.idCorso;
			this.dataTitoloVoto.innerText = this.esito.studente.data;
			if(this.esito.studente.voto == "non_inserito"){
				this.votoNonDefinito.style.display = "";
			}
			this.rigaIdCorso.textContent = "idCorso: " + this.esito.studente.idCorso;
			this.rigaIdDocente.textContent = "idDocente: " + this.esito.idDocente;	
			this.rigaData.textContent = "data: " + this.esito.studente.data;
			this.rigaMatricola.textContent = "matricola: " + this.esito.studente.matricola;
			this.rigaCognome.textContent = "cognome: " + this.esito.studente.cognome;
			this.rigaNome.textContent = "nome: " + this.esito.studente.nome;
			this.rigaVoto.textContent = "voto: " + this.esito.studente.voto;
			if(this.esito.studente.statoDellaValutazione == "rifiutato"){
				this.votoRifiutato.style.display = "";
			}
			
			if(this.esito.studente.voto != "assente" && this.esito.studente.voto != "rimandato" && this.esito.studente.voto != "riprovato" &&
			this.esito.studente.statoDellaValutazione != "rifiutato" && this.esito.studente.statoDellaValutazione != "verbalizzato"){
				this.rifiutaBottone.style.display = "";
			}
			
			this.votoTable.style.display= "";
			this.votoDiv.style.display = "";
		
		}
		
		hide(){
			this.votoDiv.style.display = "none";
		}
	}
	
         
    
}) ();