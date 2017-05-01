 $('a').click(function(){
      $('html, body').animate({
          scrollTop: $( $(this).attr('href') ).offset().top
      }, 500);
      return false;
      });

 var globalMap11;
 var isForRent = 0;
 var isForSale = 0;
 var isNegociable = 0;
 var addressLat = 0;
 var addressLng = 0;
 var markersArray = [];
 var city;

// Initialize the map

 function initMap11() {
     var myLatLng = {lat:47, lng:27};
       var map11 = new google.maps.Map(document.getElementById('map11'), {
         zoom: 14,
         center: {lat:47,lng:27}
       });
       globalMap11 = map11;
       var marker = new google.maps.Marker({
         position: myLatLng,
         map: globalMap11
       });
       addressLat=47;
       addressLng=27;
       markersArray.push(marker);
       google.maps.event.addListener(globalMap11, 'click', function(event) {
             placeMarker(event.latLng);
             addressLat = event.latLng.lat();
             addressLng = event.latLng.lng();
             var geocoder = new google.maps.Geocoder;
             var latlng = {lat: addressLat, lng: addressLng};
             geocoder.geocode({'location': latlng}, function(results, status) {
                if (status === 'OK') {
                   console.log(results[1].formatted_address);
                   var cityAddress = results[1].formatted_address.split(", ")
                   var cityWithDiacritics = cityAddress[cityAddress.length - 2];
                   city = cityWithDiacritics.replace(/ș/i, "s");
                   console.log(city);
                 }
               }
             );
       });
 }

 /* Places the marker at the given location on map */
 function placeMarker(location) {
     deleteMarker();
     var marker = new google.maps.Marker({
         position: location,
         map: globalMap11
     });
     markersArray.push(marker);
 }

 /* Deletes markers when user chooses other location on map */
 function deleteMarker() {
     if (markersArray) {
         for (i in markersArray) {
             markersArray[i].setMap(null);
         }
     markersArray.length = 0;
     }
 }

 function addPropertyPOST(event) {
         event.preventDefault();
        console.log('addPropertyPOST');
        var isValidLogin = true;
        var methodLogin = "POST";
        var urlLogin = "/estate/add/property";
        var categoryInput = document.getElementById('category-select');
        var category = categoryInput.value;
        var roomsInput = document.getElementById('rooms-select');
        var rooms = roomsInput.value;
        var priceInput = document.getElementById('price');
        var price= priceInput.value;
        var surfaceInput = document.getElementById('usable-surface');
        var surface = surfaceInput.value;
        var yearInput = document.getElementById('construction-year');
        var year = yearInput.value;
        var furnitureInput = document.getElementById('furniture-utilities-select');
        var furniture = furnitureInput.value;
        var descriptionInput = document.getElementById('description');
        var description = descriptionInput.value;
        var nameInput = document.getElementById('name');
//        var name = nameInput.value;
//        var emailInput = document.getElementById('email');
//        var email = emailInput.value;
        var phoneNumberInput = document.getElementById('phone-number');
        var phoneNumber = phoneNumberInput.value;
        var divisionInput = document.getElementById('division-select');
        var division = divisionInput.value;
        var rent_price;
        var buy_price;
         if (division === "") {
             isValidLogin = false;
         } else {
             if (division === 1) {
                 division = "Semi-detached";
             } else {
                 if (division === 2 ) {
                     division = "Detached";
                 } else {
                     division = "Non-detached";
                 }
             }
         }
        if (category === "") {
                console.log("category null");
               isValidLogin = false;
        }
        if (rooms === "") {
               console.log("rooms null");
               isValidLogin = false;
         }
        if (price === "") {
             console.log("price null");
             isValidLogin = false;
         }
        if (surface === "") {
             console.log("surface null");
               isValidLogin = false;
         }
        if (year === "") {
             console.log("year null");
               isValidLogin = false;
         }
        if (furniture === "") {
             console.log("furniture null");
               isValidLogin = false;
         }
        if (description === "") {
             console.log("description null");
               isValidLogin = false;
         }
        if (phoneNumber === "") {
              console.log("phoneNumber null");
                isValidLogin = false;
         }
//        if (email === "") {
//             console.log("email null");
//                 isValidLogin = false;
//         }
        if (phoneNumber === "") {
               console.log("phoneNumber null");
               isValidLogin = false;
        }
        if (furniture === "") {
            isValidLogin = false;
        }

        if (isValidLogin === true ){
              if(isForRent===1){
                 rent_price = price;
                  buy_price = "0";
              }
              if(isForSale===1){
                 buy_price = price;
                 rent_price = "0";
              }
              var addressLatString =  addressLat.toString();
              var addressLngString = addressLng.toString();
              var postDataProperty = {
                  "realEstateType": category,
                   "surface" : surface,
                   "roomsNumber" : rooms,
                   "rentPrice" : rent_price,
                   "buyPrice" : buy_price,
                   "constructionYear" : year,
                   "description" : description,
                   "addressLat" : addressLatString,
                   "addressLng" : addressLngString,
                   "division" : division,
                   "city" : city,
                   "utilities" : furniture,
                   "contactNumber" : phoneNumber
             };
            var async = true;
            var requestAddProperty = new XMLHttpRequest();
            var data;
            var status;
            requestAddProperty.onload = function () {
                  status = requestAddProperty.status; // HTTP response status, e.g., 200 for "200 OK"
                  data = requestAddProperty.responseText; // Returned data, e.g., an HTML document.
                  console.log(status);
                  if (data === 'Could not add property') {
                         document.getElementById('addPropertyFailedModal').style.display='block';
                     } else {
                       document.getElementById('addPropertySuccessfulModal').style.display='block';
                     }
            }


            requestAddProperty.open(methodLogin, urlLogin,true);
            requestAddProperty.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            requestAddProperty.send(JSON.stringify(postDataProperty));
        }
 }

var modalPropertyFailed = document.getElementById('addPropertyFailedModal');
var modalPropertySuccess = document.getElementById('addPropertySuccessfulModal');


/* Event listener for clicking outside modals */
window.onclick = function(event) {
       if(event.target == modalPropertyFailed ){
             modalPropertyFailed.style.display = "none";
       } else {
             if (event.target == modalPropertySuccess) {
                 modalPropertySuccess.style.display = "none";
             }
       }
 }

/* This function saves the value checked on the transaction type checkbox */
function setCheckbox(id) {
    isForRent=0;
    isForSale=0;
      if(document.getElementById(id).checked) {
        if(id==="transaction-type-sale-input")
          isForSale=1;
        if(id==="transaction-type-rent-input")
            isForRent=1;
      }
}
/* This function checks if the price of announcement is negociable or not */
function setNegociable(id) {
    isNegociable = 0;
          if(document.getElementById(id).checked) {
                if(id === "is-negociable-input")
                    isNegociable = 1;
      }
}

/* This function makes the top menu bar stay fixed when scrolling */
$(function(){
    var stickyHeaderTopAddProperty = $('#topAddProperty').offset().top;

    $(window).scroll(function(){
            if( $(window).scrollTop() > stickyHeaderTopAddProperty ) {
                    $('#topAddProperty').css({position: 'fixed', top: '0px'});
                    $('#topAddProperty').css('width','100%');
                    $('#topAddProperty').css('display', 'block');
                    $('#topAddProperty').css('z-index','999999');
            } else {
                    $('#topAddProperty').css({position: 'static', top: '0px'});
            }
    });
});