var estates = [
    {
      id :"e1",
      name: "Monarc at Met3",
      city: "New York",
      year: 2010,
      typeOfTransaction: "rent",
      surface: 100,
      address: "201 SE 2nd Avenue, Miami, FL 33131",
      description: "Monarc at Met3 is setting the bar for luxury high-rise apartments in Miami.",
      photo: "images/Iasi-estates/e1",
      typeOfEstate: "house",
      rooms: 3,
      price: 1000,
      coordinates: {lat:47.159842, lng: 27.5890153}
    },
    {
      id :"e2",
      name: "5 Rooms House, Cug",
      city: "Iasi",
      year: 2016,
      typeOfTransaction: "rent",
      surface: 120,
      address: "",
      description: "Rivera Real Estate – Vanzare casa, 5 camere, 120 mp utili, bucatarie inchisa, 500 mp teren, regim inaltime P+M, zona Cug cu acces la mijloacele de transport comun.",
      photo: "images/Iasi-estates/e2",
      typeOfEstate: "house",
      rooms: 5,
      price: 100000,
      coordinates: {lat:47.15511 , lng: 27.5863357 }
    },
    {
      id :"e3",
      name: "Casa Demisol+Parter+Etaj+Mansarda, Copou - Aleea Sadoveanu",
      city: "Iasi",
      year: 1995,
      typeOfTransaction: "rent",
      surface: 343,
      address: "",
      description: "Agentia Imobiliara Casa Alba va prezinta spre vanzare proprietate situata in zona Copou - Aleea Sadoveanu, la strada.Casa este amplasata pe o suprafata de teren de 365mp, este constructie 1995, pe cadre cu zidarie de caramida (la subsol avand fundatie din beton armat), fiind dispusa pe Demisol, Parter, Etaj si Mansarda.",
      photo: "images/Iasi-estates/e3",
      typeOfEstate: "house",
      rooms: 5,
      price: 220000,
      coordinates: {lat: 47.1502871, lng: 27.5866785}
    },
    {
      id :"e4",
      name: "Vanzare casa/vila, C.U.G, Iasi",
      city: "Iasi",
      year: 2016,
      typeOfTransaction: "rent",
      surface: 79,
      address: "",
      description: "Stil imobil va prezinta spre vanzare o casa superba, model deosebit in stilul boem.",
      photo: "images/Iasi-estates/e4",
      typeOfEstate: "house",
      rooms: 3,
      price: 65000,
      coordinates: {lat: 47.136373, lng: 27.5960554}
    },
    {
      id :"e5",
      name: "Vanzare casa/vila, Galata, Iasi",
      city: "Iasi",
      year: 2016,
      typeOfTransaction: "rent",
      surface: 90,
      address: "",
      description: "Vila duplex de vanzare in zona Galata - Iasi, reper Platoul Insorit, avand 4 camere, 3 dormitoare, suprafata utila 90 mp si suprafata teren 250 mp. ",
      photo: "images/Iasi-estates/e5",
      typeOfEstate: "house",
      rooms: 4,
      price: 67000,
      coordinates: {lat: 47.1369953, lng:  27.6040804}
    },
    {
      id :"e6",
      name: "Vanzare casa/vila, C.U.G, Iasi",
      city: "Iasi",
      year: 2016,
      typeOfTransaction: "rent",
      surface: 79,
      address: "",
      description: "Stil imobil va prezinta spre vanzare o casa superba, model deosebit in stilul boem.",
      photo: "images/Iasi-estates/e4",
      typeOfEstate: "house",
      rooms: 3,
      price: 65000,
      coordinates: {lat: 47.1433629, lng:  27.5973609}
    },
    {
      id :"e7",
      name: "25 Columbus Circle",
      city: "Iasi",
      year: "2002",
      typeOfTransaction: "rent",
      surface: 100,
      address: "",
      description: "",
      photo: "images/Iasi-estates/e5",
      typeOfEstate: "house",
      rooms: "3",
      price: 200,
      coordinates: {lat: 47.1503973, lng: 27.5886367 }
    },
    {
      id :"e8",
      name: "25 Columbus Circle",
      city: "Iasi",
      year: "2002",
      typeOfTransaction: "rent",
      surface: 100,
      address: "",
      description: "",
      photo: "images/Iasi-estates/e2",
      typeOfEstate: "house",
      rooms: "3",
      price: 200,
      coordinates: {lat: 47.15023, lng: 27.5878581}
    },
    {
      id :"e9",
      name: "25 Columbus Circle",
      city: "Iasi",
      year: "2002",
      typeOfTransaction: "rent",
      surface: 100,
      address: "",
      description: "",
      photo: "images/Iasi-estates/e1",
      typeOfEstate: "house",
      rooms: "3",
      price: 200,
      coordinates: {lat: 47.15028, lng: 27.5878581 }
    },
    {
      id :"e10",
      name: "25 Columbus Circle",
      city: "Iasi",
      year: "2002",
      typeOfTransaction: "rent",
      surface: 100,
      address: "",
      description: "",
      photo: "images/Iasi-estates/e3",
      typeOfEstate: "house",
      rooms: "3",
      price: 200,
      coordinates: {lat: 47.1653452, lng: 27.5715171 }
    },
    {
      id :"e11",
      name: "25 Columbus Circle",
      city: "New York",
      year: "2002",
      typeOfTransaction: "rent",
      surface: 100,
      address: "",
      description: "",
      photo: "images/Iasi-estates/e4",
      typeOfEstate: "house",
      rooms: "3",
      price: 200,
      coordinates: {lat: 47.166027, lng: 27.5714267}
    },
    {
      id :"e12",
      name: "25 Columbus Circle",
      city: "Iasi",
      year: "2002",
      typeOfTransaction: "rent",
      surface: 150,
      address: "",
      description: "",
      photo: "images/Iasi-estates/e2",
      typeOfEstate: "house",
      rooms: "3",
      price: 200,
      coordinates: {lat: 47.1567927, lng: 27.5843999 }
    },
    {
      id :"e13",
      name: "25 Columbus Circle",
      city: "Bucuresti",
      year: "2002",
      typeOfTransaction: "rent",
      surface: 100,
      address: "",
      description: "",
      photo: "images/Iasi-estates/e5",
      typeOfEstate: "house",
      rooms: "3",
      price: 200,
      coordinates:{lat: 47.1567927, lng: 27.5843999 }
    }
];
