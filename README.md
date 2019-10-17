<h1 align="center">Progetto di Luca Marini e Edoardo Focante</h1>

Il progetto consiste in un'applicazione REST la quale, prelevato un dataset in formato tsv, ne resituisce dati e statistiche in formato JSON dopo aver applicato opportuni filtri. Il tutto avviene rispondendo a richieste http inviate dall'utente.

<h2 align="center">DATASET</h2> 

Il dataset viene prelevato dal [JSON](http://data.europa.eu/euodp/data/api/3/action/package_show?id=ojAmzVahjBnws2njEN0qhQ) assegnatoci. Si tratta di una serie di record costituiti da un campo indic_ur contenente la sigla di un indice della qualità della vita,
un campo city con il codice della città europea a cui sono riferiti i dati del record, e il valore dell' indicatore nella determinata città per ogni anno dal 1990 al 2018.

<h2 align="center">MODELLO</h2> 

Abbiamo deciso di modellare il singolo record del file .tsv attraverso la classe City, dotata di 3 attributi: indic_ur, citycode, e un array di float contente i valori dell'indicatore per ogni anno.
Viene poi creata nell'application un'arraylist di oggetti city che contiene tutto il dataset e che verrà passata attraverso uno static import al controller che eseguirà le operazioni richieste dall'utente.
Il parsing viene effettuato dalla classe TSVParse, convertendo i dati alfanumerici in numeri di tipo float e i dati mancanti -1, così da poterli poi escludere dalle statistiche.

La classe Statiscs modella le statistische relative ad un singolo anno di una determinita ArrayList di oggetti City. Possiede l'attributo year per specificare a quale anno le statistiche fanno riferimento e tutti gli attributi associati alle statistiche richieste dal progetto(min, max, sum, count, avg, std_dev).

 <h2 align="center">PATH</h2> 

### /data
Si può accedere a questo path attraverso richieste GET o POST, ma nel caso POST il body è superfluo. Restituisce l'intera lista di record in formato JSON senza applicare filtri.
### /metadata
Si può accedere a questo path attraverso richieste GET o POST, ma nel caso POST il body è superfluo. Restituisce la struttura della classe City in formato JSON.
### /filter
Si può accedere a questo path attraverso una richiesta di tipo POST. Restituisce un arraylist in formato JSON ottenuta applicando un filtro in formato JSON passato dall'utente tramite il body della richiesta POST. Non è possibile non inserire un filtro, ma è possibile inserire un filtro vuoto.
### /stats
Si può accedere a questo path attraverso una richiesta di tipo POST. Restituisce le statistiche su un dataset filtrato per ogni anno e per un singolo anno che è possibile specificare tramite il parametro year da inserire nella richiesta http.

<h2 align="center">FILTRI</h2> 

### Filtro generico and + or

```
{
    "and/or": [
        {
            "fieldname1": {
                "operatore1": "value1"
            }
        },
        {
            "fieldname2": {
                "operatore2": "value2"
            }
        }
        ],
    

     "and/or": [
        {
            "fieldname3": {
                "operatore3": "value3"
            }
        },
	{
            "fieldname4": {
                "operatore4": "value4"
            }
        }
        }
    ]
}
```

Dopo aver ricevuto un filtro, l'applicazione esegue l'or tra le istruzioni nella parte *$or* e  al risultato applica tutte le condizioni *$and*, operando, implicitamente, una and tra il JSONArray *$or* e il JSONArray *$and*. In presenza di almeno uno di questi due JSONArray l'applicazione ignorerà ciò che si trova all'esterno.

I due JSONArray posso essere inseriti in qualsiasi ordine all'interno del JSON e possono contenere un numero qualsiasi di condizioni. Non è possibile, in quanto non conforme con lo standard JSON, avere multipli JSONArray *$and* o *$or*.

**And/or** può assumere i valori $and o $or.

**fare una and tra le due sezioni**

**fieldname1-4** sono i nomi dei campi su cui verificare le condizioni del filtro.
È possibile inserire i valori:
* "indic_ur", 
* "citycode" 
* una stringa contenente l'anno su cui si vuole valutare la condizione. 

**operatore1-4** sono i diversi operatori per effettuare la verifica del confronto. 
I possibili sono
 * "$eq" : Uguale ad un parametro
 * "$not" : Diverso da un parametro
 * "$in" : Contenuto in un array di elementi
 * "$nin" : Non Contenuto in un array di elementi
 * "$gt" : Maggiore di un numero
 * "$gte" : Maggiore o uguale a un numero
 * "$lt" : Minore di un numero
 * "$lte" : Maggiore o uguale a un numero
 * "$bt" : Compreso tra due numeri
 
**value1-4** sono i parametri con cui comparare i valori del dataset, possono essere numeri, stringhe, array di numeri o array di stringhe a seconda del tipo di operatore



### Filtro con una singola and o una singola or
```
{
    "and/or": [
        {
            "fieldname1": {
                "operatore": "value1"
            }
        },
        {
            "fieldname2": {
                "operatore": "value2"
            }
        }
    ]
}
```

L'applicazione eseguirà una *$and* o una *$or* tra tutte le condizioni presenti nel singolo JSONArray. È possibile inserire un numero arbitrario di conzdizione nel singolo JSONArray.
L'applicazione ignorerà tutto ciò che si trova al di fuori dei JSONArray *$and* o *$or*. 

### Filtro con una singola condizione
```
{
    "fieldname1": {
        "operatore": "value1"
    }
}
```

È possibile inserire una sola condizione nel filtro, ma non è possibile inserirne un'altra senza racchiudere entrambe in un JSONArray *$and* o *$or*.

### Filtro nullo
```
{}
```
È possibile inserire un filtro nullo. In questo caso le operazioni verranno eseguite sull'intero dataset non filtrato. Se ne sconsiglia l'uso ai fini del calcolo delle statistiche
in quanto i diversi record rappresentanto grandezze diverse e ha poco senso calcolare le statistiche su tipi eterogenei di dato.

<h2 align="center">ESEMPI DI FILTRI</h2> 

Di seguito alcuni filtri utilizzati per testare l'applicazione. Dovranno costituire il body di richieste di tipo POST atraverso i PATH /filter o /stats. Per il PATH /stats sarà possibile specificare o meno il parametro year.

### 2 and + 2 or
```
{
    "$and": [
        {
            "indic_ur": {
                "$eq": "CR1015V"
            }
        },
        {
            "citycode": {
                "$eq": "BE001L2"
            }
        }
    ],
    "$or": [
        {
            "2011": {
                "$gt": 10
            }
        },
        {
            "2011": {
                "$lt": 7
            }
        }
    ]
}
```
### 2 or :
```
{
    "$or": [
        {
            "2011": {
                "$gt": 10
            }
        },
        {
            "2013": {
                "$lte": 20
            }
        }
    ]
}
```

### Between:

```
{
    "2011": {
        "$bt": [
            10,
            421
        ]
    }
}
```

### In:

```
{
    "citycode": {
        "$in": [
            "BE002L2",
            "Antonio",
            "Cina",
            "BG005L1",
            "Fiesta"
        ]
    }
}
```

### Errore parametri:

```
{
    "$and": [
        {
            "indic_ur": {
                "$eq": "CR1015V"
            }
        },
        {
            "citycode": {
                "$eq": "BE001L2"
            }
        }
    ],
    "$or": [
        {
            "2011": {
                "$gte": [10,20]
            }
        }
    ]
}
```

### 1 and 
```
{
    "$and": [
        {
            "indic_ur": {
                "$not" : "CR1015V"
            }
        }
    ]
}	
```



 


<h2><div align="center">UML</div>
</21>
<p>Sono riportati di seguito i diagrammi UML dell'applicazione
</p>
<h3>UseCase Diagram
</h3>
<p align="center"><img src="https://github.com/EdoardoFocante/FocanteMarini/blob/master/UML%20FocanteMarini/UseCaseDiagram.png" alt style="max-width:100%;">
</p>
<h3>Activity Diagram
</h3>
<p align="center"><img src="https://github.com/EdoardoFocante/FocanteMarini/blob/master/UML%20FocanteMarini/ClassDiagram.png" alt style="max-width:100%;">
</p>

## Software utilizzato
[Eclipse](https://www.eclipse.org) Per scrivere il codice

[SpringInitializr](https://start.spring.io/) Per creare il progetto spring

[GitHub](https://github.com) Per il versioning del codice

[Postman](https://www.getpostman.com/) Per testare le diverse richieste

[draw.io](https://www.draw.io) Per disegnare i diagrammi UML

