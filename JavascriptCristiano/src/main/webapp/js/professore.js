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
            this.iscritti = new Iscritti(this);
            this.modifica = new Modifica(this);
            this.verbale = new Verbale(this);
            this.inserimentoMultiplo = new InserimentoMultiplo(this);
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
		
		goToIscritti(idCorso,data){
			this.current.hide();
			this.iscritti.show(idCorso,data);
			this.current = this.iscritti;
		}
		goToModifica(iscritto,data){
			this.current.hide();
			this.modifica.show(iscritto,data);
			this.current = this.modifica;
		}
		goToVerbale(verbale,idCorso,data){
			this.current.hide();
			this.verbale.show(verbale,idCorso,data);
			this.current = this.verbale;
		}
		showInserimentoMultiplo(iscritti,idCorso,data){
			this.inserimentoMultiplo.show(iscritti,idCorso,data);
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
			this.appelli.forEach(appello => {
				let row = document.createElement("tr");
				row.setAttribute("class", "riga-appello");
				let data = document.createElement("td");
				console.log(appello.dataString);
				data.textContent = appello.dataString;
		
				let iscrittiButton = document.createElement("td");
				iscrittiButton.classList.add("btn","btn-small","btn-colored-solid");
				iscrittiButton.innerText = "iscritti";
				
				iscrittiButton.addEventListener("click", () => {
					this.pageOrchestrator.goToIscritti(appello.idCorso,appello.dataString);
				})
				
				row.appendChild(data);
				row.appendChild(iscrittiButton);
				this.appelliTable.appendChild(row);
				
			})
			this.appelliTable.style.display= "";
			this.appelliDiv.style.display = "";
		
		}
		
		hide(){
			this.appelliDiv.style.display = "none";
		}
	}
	
	class Iscritti{
		constructor(pageOrchestrator){
			this.iscrittiDiv = document.getElementById("iscritti-div");
			this.iscrittiDiv.style.display = "none";
			this.idCorsoIscrittiTitolo = document.getElementById("idCorso-iscritti-titolo");
			this.dataIscrittiTitolo = document.getElementById("data-iscritti-titolo");
			this.iscrittiTable = document.getElementById("iscritti-table");
			this.pubblicaButton = document.getElementById("pubblica-button");
			this.verbalizzaButton = document.getElementById("verbalizza-button");
			this.inserimentoMultiploButton = document.getElementById("inserimentoMultiplo-button");
			this.pageOrchestrator = pageOrchestrator;
			this.ord = "MATRICOLA_ASC";
			
			document.getElementById("matricola-button").addEventListener("click", () => {
				if(this.ord == "MATRICOLA_ASC"){
					this.iscritti=this.iscritti.sort((a,b) => {
						return b.matricola-a.matricola;									
					})
					this.ord="MATRICOLA_DESC";
				}else{
					this.iscritti=this.iscritti.sort((a,b) => {
						return a.matricola-b.matricola;
					})
					this.ord="MATRICOLA_ASC";
				}
				this.update();
			})
			
			document.getElementById("nome-button").addEventListener("click", () => {
				if(this.ord == "NOME_ASC"){
					this.iscritti=this.iscritti.sort((a,b) => {						
						if (b.nome < a.nome){
							return -1;
						}else if ( b.nome > a.nome){
							return 1;
						}
						return 0;								
					})
					this.ord="NOME_DESC";
				}else{
					this.iscritti=this.iscritti.sort((a,b) => {
						if (a.nome < b.nome){
							return -1;
						}else if ( a.nome > b.nome){
							return 1;
						}
						return 0;
					})
					this.ord="NOME_ASC";
				}
				this.update();
			})
			
			document.getElementById("cognome-button").addEventListener("click", () => {
				if(this.ord == "COGNOME_ASC"){
					this.iscritti=this.iscritti.sort((a,b) => {						
						if (b.cognome < a.cognome){
							return -1;
						}else if ( b.cognome > a.cognome){
							return 1;
						}
						return 0;								
					})
					this.ord="COGNOME_DESC";
				}else{
					this.iscritti=this.iscritti.sort((a,b) => {
						if (a.cognome < b.cognome){
							return -1;
						}else if ( a.cognome > b.cognome){
							return 1;
						}
						return 0;
					})
					this.ord="COGNOME_ASC";
				}
				this.update();
			})
			
			document.getElementById("email-button").addEventListener("click", () => {
				if(this.ord == "EMAIL_ASC"){
					this.iscritti=this.iscritti.sort((a,b) => {						
						if (b.email < a.email){
							return -1;
						}else if ( b.email > a.email){
							return 1;
						}
						return 0;								
					})
					this.ord="EMAIL_DESC";
				}else{
					this.iscritti=this.iscritti.sort((a,b) => {
						if (a.email < b.email){
							return -1;
						}else if ( a.email > b.email){
							return 1;
						}
						return 0;
					})
					this.ord="EMAIL_ASC";
				}
				this.update();
			})
			
			document.getElementById("corsoLaurea-button").addEventListener("click", () => {
				if(this.ord == "CORSODILAUREA_ASC"){
					this.iscritti=this.iscritti.sort((a,b) => {						
						if (b.corsoDiLaurea < a.corsoDiLaurea){
							return -1;
						}else if ( b.corsoDiLaurea > a.corsoDiLaurea){
							return 1;
						}
						return 0;								
					})
					this.ord="CORSODILAUREA_DESC";
				}else{
					this.iscritti=this.iscritti.sort((a,b) => {
						if (a.corsoDiLaurea < b.corsoDiLaurea){
							return -1;
						}else if ( a.corsoDiLaurea > b.corsoDiLaurea){
							return 1;
						}
						return 0;
					})
					this.ord="CORSODILAUREA_ASC";
				}
				this.update();
			})
			
			document.getElementById("voto-button").addEventListener("click", () => {
				if(this.ord == "VOTO_ASC"){
					this.iscritti=this.iscritti.sort((a,b) => {						
						if (b.voto < a.voto){
							return -1;
						}else if ( b.voto > a.voto){
							return 1;
						}
						return 0;								
					})
					this.ord="VOTO_DESC";
				}else{
					this.iscritti=this.iscritti.sort((a,b) => {
						if (a.voto < b.voto){
							return -1;
						}else if ( a.voto > b.voto){
							return 1;
						}
						return 0;
					})
					this.ord="VOTO_ASC";
				}
				this.update();
			})
			
			document.getElementById("statoValutazione-button").addEventListener("click", () => {
				if(this.ord == "STATOVALUTAZIONE_ASC"){
					this.iscritti=this.iscritti.sort((a,b) => {						
						if (b.statoDellaValutazione < a.statoDellaValutazione){
							return -1;
						}else if ( b.statoDellaValutazione > a.statoDellaValutazione){
							return 1;
						}
						return 0;								
					})
					this.ord="STATOVALUTAZIONE_DESC";
				}else{
					this.iscritti=this.iscritti.sort((a,b) => {
						if (a.statoDellaValutazione < b.statoDellaValutazione){
							return -1;
						}else if ( a.statoDellaValutazione > b.statoDellaValutazione){
							return 1;
						}
						return 0;
					})
					this.ord="STATOVALUTAZIONE_ASC";
				}
				this.update();
			})
			
			this.pubblicaButton.addEventListener("click", () => {
				let pubblicaForm = new FormData();
				pubblicaForm.append("idCorso", this.idCorso);
				pubblicaForm.append("data", this.data);
				
				makeCall("POST", "Pubblica", pubblicaForm , request => {
					if (request.readyState == XMLHttpRequest.DONE) {
						switch(request.status){
			                case 200: //Okay, save the taxonomy			                    
			                    warningDiv.style.display = "none"; // Reset error div		             
			                    this.show(this.idCorso,this.data);
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
			
			this.inserimentoMultiploButton.addEventListener("click", () => {
				let inserimentoMultiploIscritti = this.iscritti.filter(iscritto => iscritto.statoDellaValutazione == "non_inserito");
				console.log(inserimentoMultiploIscritti);
				pageOrchestrator.showInserimentoMultiplo(inserimentoMultiploIscritti,this.idCorso,this.data);
			})
				
			this.verbalizzaButton.addEventListener("click", () => {
				let verbaleForm = new FormData();
				verbaleForm.append("idCorso", this.idCorso);
				verbaleForm.append("data", this.data);
				
				makeCall("POST", "Verbalizza", verbaleForm , request => {
					if (request.readyState == XMLHttpRequest.DONE) {
						switch(request.status){
			                case 200: //Okay, save the taxonomy			                    
			                    warningDiv.style.display = "none"; // Reset error div
			                    let verbale = JSON.parse(request.responseText);		             
			                    this.pageOrchestrator.goToVerbale(verbale,this.idCorso,this.data);
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
		
		show(idCorso,data){
			let appelloForm = new FormData();
			appelloForm.append("idCorso", idCorso);
			appelloForm.append("data", data);
			this.idCorso = idCorso;
			this.data= data;
		

			makeCall("POST", "GetIscritti", appelloForm , request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					switch(request.status){
		                case 200: //Okay, save the taxonomy
		                    this.iscritti = JSON.parse(request.responseText);
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
			document.querySelectorAll(".riga-iscritto").forEach(riga => riga.remove());
			this.idCorsoIscrittiTitolo.innerText = this.idCorso;
			this.dataIscrittiTitolo.innerText = this.data;
			this.inserimentoMultiploButton.style.display = "none";
			this.pubblicaButton.style.display = "none";
			this.verbalizzaButton.style.display = "none";
		
			this.iscritti.forEach(iscritto => {
				let row = document.createElement("tr");
				row.setAttribute("class", "riga-iscritto");
				let matricola = document.createElement("td");
				matricola.textContent = iscritto.matricola;
				let nome = document.createElement("td");
				nome.textContent = iscritto.nome;
				let cognome = document.createElement("td");
				cognome.textContent = iscritto.cognome;
				let email = document.createElement("td");
				email.textContent = iscritto.email;
				let corsoLaurea = document.createElement("td");
				corsoLaurea.textContent = iscritto.corsoDiLaurea;
				let voto = document.createElement("td");
				voto.textContent = iscritto.voto;
				let statoValutazione = document.createElement("td");
				statoValutazione.textContent = iscritto.statoDellaValutazione;
				
				if (iscritto.statoDellaValutazione == "inserito"){
					this.pubblicaButton.style.display = "";
				}else if (iscritto.statoDellaValutazione == "pubblicato" || iscritto.statoDellaValutazione == "rifiutato"){
					this.verbalizzaButton.style.display="";
				}if (iscritto.statoDellaValutazione == "non_inserito"){
					this.inserimentoMultiploButton.style.display = "";
				}
				
			
				
				row.appendChild(matricola);
				row.appendChild(nome);
				row.appendChild(cognome);
				row.appendChild(email);
				row.appendChild(corsoLaurea);
				row.appendChild(voto);
				row.appendChild(statoValutazione);
				
				if(iscritto.statoDellaValutazione == "inserito" || iscritto.statoDellaValutazione == "non_inserito" ){
					let modificaButton = document.createElement("td");
					modificaButton.classList.add("btn","btn-small","btn-colored-solid");
					modificaButton.innerText = "Modifica";
					
					modificaButton.addEventListener("click", () => {
						this.pageOrchestrator.goToModifica(iscritto,this.data);
					})
					row.appendChild(modificaButton);
				}
				
				this.iscrittiTable.appendChild(row);
				
			})
			
		
			
			this.iscrittiTable.style.display= "";
			this.iscrittiDiv.style.display = "";
		
		}
		
		hide(){
			this.iscrittiDiv.style.display = "none";
		}
	}				
	
	 class Modifica{
		constructor(pageOrchestrator){
			let modificaButton = document.getElementById("modifica-button");
			this.modificaDiv = document.getElementById("modifica-div");
			this.modificaTable = document.getElementById("modifica-table");
			this.modificaIdCorso = document.getElementById("modifica-idCorso");
			this.modificaData = document.getElementById("modifica-data");
			this.modificaMatricola = document.getElementById("modifica-matricola");
			this.pageOrchestrator = pageOrchestrator;
			this.modificaDiv.style.display = "none";
			
			attachForm(modificaButton, warningDiv, "Modifica", request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					switch(request.status){
				        case 200: //Okay, parse and save data to local storage and go to home
				            this.pageOrchestrator.goToIscritti(this.idCorso,this.data);                 
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
			}});
				
		}
		
		show(iscritto,data){
			this.idCorso=iscritto.idCorso;
			this.data=data;
			console.log(data);
			this.modificaIdCorso.value = iscritto.idCorso;
			this.modificaData.value = data;
			this.modificaMatricola.value = iscritto.matricola;
			
			let oldRiga = document.getElementById("riga-modifica");
			if(oldRiga != null){
				oldRiga.remove();
			}
			let row = document.createElement("tr");
			row.setAttribute("class", "riga-modifica");
			let matricola = document.createElement("td");
			matricola.textContent = iscritto.matricola;
			let nome = document.createElement("td");
			nome.textContent = iscritto.nome;
			let cognome = document.createElement("td");
			cognome.textContent = iscritto.cognome;
			let email = document.createElement("td");
			email.textContent = iscritto.email;
			let corsoLaurea = document.createElement("td");
			corsoLaurea.textContent = iscritto.corsoDiLaurea;
			
			row.appendChild(matricola);
			row.appendChild(nome);
			row.appendChild(cognome);
			row.appendChild(email);
			row.appendChild(corsoLaurea);
			this.modificaTable.appendChild(row);
		
			
			this.modificaTable.style.display= "";
			this.modificaDiv.style.display = "";

		}
				
		hide(){
			this.modificaDiv.style.display = "none";
		}
	}
	
	class Verbale{
		constructor(pageOrchestrator){
			this.nomeCorsoTitoloVerbale = document.getElementById("nomeCorso-titolo-verbale");
			this.dataAppelloTitoloVerbale = document.getElementById("dataAppello-titolo-verbale");
			this.dataVerbaleTitoloVerbale = document.getElementById("dataVerbale-titolo-verbale");
			this.verbaleDiv = document.getElementById("verbale-div");
			this.verbaleTable = document.getElementById("verbale-table");
			this.pageOrchestrator = pageOrchestrator;
			this.verbaleDiv.style.display = "none";
				
		}
		
		show(verbale,idCorso,data){
			document.querySelectorAll(".riga-verbale").forEach(riga => riga.remove());
			this.nomeCorsoTitoloVerbale.innerText = idCorso;
			this.dataAppelloTitoloVerbale.innerText = data;
			this.dataVerbaleTitoloVerbale.innerText = verbale.timestamp;
			verbale.iscritti.forEach(iscritto => {
				let row = document.createElement("tr");
				row.setAttribute("class", "riga-verbale");
				let matricola = document.createElement("td");
				matricola.textContent = iscritto.matricola;
				let nome = document.createElement("td");
				nome.textContent = iscritto.nome;
				let cognome = document.createElement("td");
				cognome.textContent = iscritto.cognome;
				let voto = document.createElement("td");
				voto.textContent = iscritto.voto;
				
				row.appendChild(matricola);
				row.appendChild(nome);
				row.appendChild(cognome);
				row.appendChild(voto);
				this.verbaleTable.appendChild(row);
			})
		
		
			
			this.verbaleTable.style.display= "";
			this.verbaleDiv.style.display = "";

		}
				
		hide(){
			this.verbaleDiv.style.display = "none";
		}
	}
	
	class InserimentoMultiplo{
		constructor(pageOrchestrator){
			this.inserimentoMultiploIdCorso = document.getElementById("inserimento-multiplo-idCorso");
			this.inserimentoMultiploData = document.getElementById("inserimento-multiplo-data");
			this.modalForm = document.getElementById("modal-form");
			this.inviaButton = document.getElementById("invia-button");
			this.modalTitle = document.getElementById("modal-title");
			this.modalBox = document.getElementById("modal-box");
			this.esciButton = document.getElementById("esci-button");
			this.pageOrchestrator = pageOrchestrator;
			this.modalBox.style.display = "none";
			
			this.esciButton.addEventListener("click", () => {
				this.modalBox.style.display = "none";
			})
			
			attachForm(this.inviaButton, warningDiv, "InserimentoMultiplo", request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					switch(request.status){
				        case 200: //Okay, parse and save data to local storage and go to home
				        	this.modalBox.style.display = "none";
				            this.pageOrchestrator.goToIscritti(this.idCorso,this.data);                 
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
			}});
				
		}
		
		
		show(iscritti,idCorso,data){
			document.querySelectorAll(".insMult").forEach(div => div.remove());
			this.idCorso = idCorso;
			this.data = data;
			this.inserimentoMultiploIdCorso.value = idCorso;
			this.inserimentoMultiploData.value = data;
			iscritti.forEach(iscritto => {
				let div = document.createElement("div");
				div.setAttribute("class", "form-entry insMult");
				let label = document.createElement("label");
				label.textContent = iscritto.matricola + " " +  iscritto.nome + " " + iscritto.cognome;
				label.setAttribute("for", "checkBox");
				let checkBox = document.createElement("input");
				checkBox.setAttribute("name","checkBox");
				checkBox.setAttribute("type","checkbox");
				checkBox.setAttribute("value", iscritto.matricola);
				
				div.appendChild(label);
				div.appendChild(checkBox);
				this.modalForm.prepend(div);
			})
			this.modalBox.style.display="";
		}
	}
	
				
}) ();
