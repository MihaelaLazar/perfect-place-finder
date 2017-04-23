function getQueryVariableEstateDetails(variable) {
     var query = window.location.search.substring(1);
     var vars = query.split("&");
     for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable) {
           return pair[1].replace('%20',' ');
        }
     }
     return null;
 }

var globalMap2;
var markerGlobal;
var parkingLayer;
var transitLayer;
var geocoder;
var estatePosition;
var parkingLayerArray = [];
var schoolsNearbyArray = [];
var storesNearbyArray = [];
function initMap2() {
      console.log("initMap2");
      var idEstate = getQueryVariableEstateDetails('estate');
      console.log('estate id(query): ' + idEstate );
      for (var i = 0; i < estates.length; i ++) {
            if (estates[i].id == idEstate) {
                estatePosition = new google.maps.LatLng(estates[i].coordinates.lat, estates[i].coordinates.lng);
                console.log('found ' + estates[i].coordinates.lat);
            }
      }
      console.log('estate id: ' + estatePosition.lat() );
      geocoder = new google.maps.Geocoder();
      globalMap2 = new google.maps.Map(document.getElementById('map2'), {
        zoom: 14,
        center: estatePosition
      });
      var image = 'images/Marker Filled-50.png';
      var icon = {
        url: "images/Marker Filled-50.png",
        scaledSize: new google.maps.Size(40, 40),
        origin: new google.maps.Point(0,0),
        anchor: new google.maps.Point(0, 0)
      };
      var marker = new google.maps.Marker({
        position: estatePosition,
        map: globalMap2,
        title: "Click here to zoom",
        icon: icon
      });
}

function changeOverlayEstate(id){
    console.log('in changeOverlayEstate');
  if(document.getElementById(id).checked) {
    addOverlay(id);
  } else {
    removeOverlay(id);
  }
}
function removeOverlay(id) {
  if(id == 'parkingLayer'){
    for (i in parkingLayerArray) {
        parkingLayerArray[i].setMap(null);
        delete parkingLayerArray[i];
     }
  } else {
        if (id === 'schoolsLayerEstate'){
            for (i in schoolsNearbyArray) {
                schoolsNearbyArray[i].setMap(null);
                delete schoolsNearbyArray[i];
             }
        } else {
            if (id === 'storesLayer'){
                for (i in storesNearbyArray) {
                    storesNearbyArray[i].setMap(null);
                    delete storesNearbyArray[i];
                }
            }
        }
  }
}

function callbackParking(results, status) {
   if (status === google.maps.places.PlacesServiceStatus.OK) {
     for (var i = 0; i < results.length; i++) {
         createMarker(results[i], 'parking');
     }
   }
}

function callbackStores(results, status) {
   if (status === google.maps.places.PlacesServiceStatus.OK) {
     for (var i = 0; i < results.length; i++) {
         createMarker(results[i], 'store');
     }
   }
}

function callbackSchools(results, status) {
   if (status === google.maps.places.PlacesServiceStatus.OK) {
     for (var i = 0; i < results.length; i++) {
         createMarker(results[i],'schools');
     }
   }
}
function createMarker(place, type) {
   var placeLoc = place.geometry.location;
   if (type === 'parking') {
        var icon = {
           url: "images/parking.png",
           scaledSize: new google.maps.Size(40, 40),
           origin: new google.maps.Point(0,0),
           anchor: new google.maps.Point(0, 0)
        }
        var marker = new google.maps.Marker({
             map: globalMap2,
             position: place.geometry.location,
             icon: icon
           });
        parkingLayerArray.push(marker);
   } else {
        if (type === 'schools'){
            var icon = {
               url: "images/school.png",
               scaledSize: new google.maps.Size(50, 50),
               origin: new google.maps.Point(0,0),
               anchor: new google.maps.Point(0, 0)
            }
            var marker = new google.maps.Marker({
                 map: globalMap2,
                 position: place.geometry.location,
                 icon : icon
               });
            schoolsNearbyArray.push(marker);
        } else {
            if (type === 'store'){
                var icon = {
                       url: "images/store.png",
                       scaledSize: new google.maps.Size(40, 40),
                       origin: new google.maps.Point(0,0),
                       anchor: new google.maps.Point(0, 0)
                    };
                   var markerStore = new google.maps.Marker({
                        position: place.geometry.location,
                        map: globalMap2,
                        icon: icon
                   });
                storesNearbyArray.push(markerStore);
            }
        }
   }

}

function addOverlay(id) {
 if(id == 'parkingLayer'){
   var parkings = new google.maps.places.PlacesService(globalMap2);
    parkings.nearbySearch({
      location: globalMap2.getCenter(),
      radius: 1000,
      type: ['parking']
    }, callbackParking);
 }else {
   if (id == 'schoolsLayerEstate'){
     var schools = new google.maps.places.PlacesService(globalMap2);
         schools.nearbySearch({
           location: globalMap2.getCenter(),
           radius: 1000,
           type: ['school']
         }, callbackSchools);
   } else {
        if (id == 'storesLayer'){
         var schools = new google.maps.places.PlacesService(globalMap2);
             schools.nearbySearch({
               location: globalMap2.getCenter(),
               radius: 1000,
               type: ['store']
             }, callbackStores);
       }
   }
 }
}

function codeAddress() {
  var address = document.getElementById('address').value;
  geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == 'OK') {
      globalMap.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
        map: globalMap,
        position: results[0].geometry.location
      });
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });
}
function addLayerOnPadding() {
  var selectLayer = document.getElementById('layer');
  var layer_id = selectLayer.options[selectLayer.selectedIndex].value;
}


$('#propertiesA').click(function(){
    $('html, body').animate({
        scrollTop: $( $(this).attr('href') ).offset().top
    }, 500);
    return false;
});
$('#neighbourhoodA').click(function(){
    $('html, body').animate({
        scrollTop: $( $(this).attr('href') ).offset().top
    }, 500);
    return false;
});
$('#utilitiesA').click(function(){
    $('html, body').animate({
        scrollTop: $( $(this).attr('href') ).offset().top
    }, 500);
    return false;
});
$('#backToTheTop').click(function(){
    $('html, body').animate({
        scrollTop: $( $(this).attr('href') ).offset().top
    }, 500);
    return false;
});

var modalSignUpEstateDetails = document.getElementById('signUpEstateDetails');
// When the user clicks anywhere outside of the modal, close it
var modalLoginEstateDetails = document.getElementById('logInEstateDetails');
var modalLoginStatusEstateDetails = document.getElementById('logInStatus-EstateDetails');
var modalLoginStatusFailedEstateDetails = document.getElementById('logInStatusFailed-EstateDetails');
window.onclick = function(event) {
    if(event.target === modalSignUpEstateDetails )
    {
        modalSignUpEstateDetails.style.display = "none";
        $('#first-name-input-EstateDetails').removeClass('error');
        $('#first-name-EstateDetails').attr("placeholder","First name");
        $('#last-name-input-EstateDetails').removeClass('error');
        $('#last-name-EstateDetails').attr("placeholder","Last name");
        $('#email-input-EstateDetails').removeClass('error');
        $('#email-EstateDetails').attr("placeholder","Email");
        $('#password-input-EstateDetails').removeClass('error');
        $('#password-name-EstateDetails').attr("placeholder","Password");
        $('#errorMessageContainer-EstateDetails').css("display", "none");
        $("#email-EstateDetails").val("");
        $("#passwordSignUp-EstateDetails").val("");
        $("#first-name-EstateDetails").val("");
        $("#last-name-EstateDetails").val("");
    }
    else {
      if (event.target == modalLoginEstateDetails)
      {
        modalLoginEstateDetails.style.display = "none";
        $('#email-input-login-EstateDetails').removeClass('error');
        $('#email-login-EstateDetails').attr("placeholder","Email");
        $('#password-input-login-EstateDetails').removeClass('error');
        $('#errorMessageContainer-EstateDetails').css("display", "none");
        $("#email-login-EstateDetails").val("");
        $("#password-login-EstateDetails").val("");
        $('#errorMessageContainer-login-EstateDetails').css("display", "none");
      }
      else
      {
            if (event.target === document.getElementById('signInStatus-EstateDetails')) {
                 document.getElementById('signInStatus-EstateDetails').style.display='none';
            } else {
                if (event.target ===  document.getElementById('signInStatusFailed-EstateDetails')){
                    document.getElementById('signInStatusFailed-EstateDetails').style.display='none';
                } else {
                    if (event.target === modalLoginStatusEstateDetails)  {
                        document.getElementById('logInStatus-EstateDetails').style.display='none';
                    } else {
                      if (event.target === modalLoginStatusFailedEstateDetails) {
                         document.getElementById('logInStatusFailed-EstateDetails').style.display='none';
                      }
                    }
                }
            }
      }
    }
}

function logInPOSTEstateDetails() {
      $('#password-input-login-EstateDetails').removeClass('error');
      $('#email-input-login-EstateDetails').removeClass('error');
      console.log('logInPOST');
      var url = "/verify/user";
      var method = "POST";
      var passInput = document.getElementById('password-login-EstateDetails');
      var password = passInput.value;
      var emailInput = document.getElementById('email-login-EstateDetails');
      var email = emailInput.value;
      var isValid = true;
      var atpos = email.indexOf("@");
      var dotpos = email.lastIndexOf(".");
      isValid = true;
      if (password === "") {
            $('#password-input-login-EstateDetails').addClass('error');
            $('#password-login-EstateDetails').attr("placeholder","Insert password");
                 $('#errorMessageContainer-login-EstateDetails').text("Insert password");
                 $('#errorMessageContainer-login-EstateDetails').css("display", "block");
            isValid = false;
      }
      if (email === "" || atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
            $('#email-input-login-EstateDetails').addClass('error');
            $('#email-login-EstateDetails').attr("placeholder","Insert email");
                $('#errorMessage-login-EstateDetails').text("Incorrect email type");
                $('#errorMessageContainer-login-EstateDetails').css("display", "block");
            isValid = false;
      }
      var postData = {
           "password" : password,
           "email" : email
          };
      if (isValid === true ){
          var async = true;
          var request = new XMLHttpRequest();
          var status;
          var data;
          request.onload = function () {
                status = request.status; // HTTP response status, e.g., 200 for "200 OK"
                data = request.responseText; // Returned data, e.g., an HTML document.
                if (data === 'INVALID USER/PASSWORD') {
                    console.log ("incorrect EMAIL");
                    document.getElementById('logInEstateDetails').style.display='none';
                    document.getElementById('logInStatusFailed-EstateDetails').style.display='block';
                } else {
                  document.getElementById('logInEstateDetails').style.display='none';
                  document.getElementById('logInStatus-EstateDetails').style.display='block';
              }
          }
          request.open(method, url,true);
          request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
          request.send(JSON.stringify(postData));
          document.getElementById('logInEstateDetails').style.display='none';
          $("#email-login-EstateDetails").val("");
          $("#password-login-EstateDetails").val("");
          document.getElementById('errorMessage-login-EstateDetails').style.display = 'none';
      }

}

function signUpPOSTEstateDetails() {
          $('#password-input-EstateDetails').removeClass('error');
          $('#first-name-input-EstateDetails').removeClass('error');
          $('#last-name-input-EstateDetails').removeClass('error');
          $('#email-input-EstateDetails').removeClass('error');
          console.log('signUpPOST');
          var url = "/create/user";
          var method = "POST";
          var firstNameInput = document.getElementById('first-name-EstateDetails');
          var firstName = firstNameInput.value;
          var lastNameInput = document.getElementById('last-name-EstateDetails');
          var lastName = lastNameInput.value;
          var passInput = document.getElementById('passwordSignUp-EstateDetails');
          var password = passInput.value;
          var emailInput = document.getElementById('email-EstateDetails');
          var email = emailInput.value;
          var isValid = true;
          var atpos = email.indexOf("@");
          var dotpos = email.lastIndexOf(".");
         if (firstName === "") {
                $('#first-name-input-EstateDetails').addClass('error');
                $('#first-name-EstateDetails').attr("placeholder","Insert first name");
                $('#errorMessage-EstateDetails').text("Insert first name");
                $('#errorMessageContainer-EstateDetails').css("display", "block");
                isValid = false;
          }
          if (lastName === "") {
                $('#last-name-input-EstateDetails').addClass('error');
                $('#last-name-EstateDetails').attr("placeholder","Insert last name");
                    $('#errorMessage-EstateDetails').text("Insert last name");
                    $('#errorMessageContainer-EstateDetails').css("display", "block");
                isValid = false;
          }
          if (password === "") {
                $('#password-input-EstateDetails').addClass('error');
                $('#passwordSignUp-EstateDetails').attr("placeholder","Insert password");
                     $('#errorMessage-EstateDetails').text("Insert password");
                     $('#errorMessageContainer-EstateDetails').css("display", "block");
                isValid = false;
          }
          if (email === "" || atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
                $('#email-input-EstateDetails').addClass('error');
                $('#email-EstateDetails').attr("placeholder","Insert email");
                    $('#errorMessage-EstateDetails').text("Insert email");
                    $('#errorMessageContainer-EstateDetails').css("display", "block");
                isValid = false;
          }
          var postData = {
              "firstName": firstName,
               "lastName" : lastName,
               "password" : password,
               "email" : email
              };
          if (isValid === true ){
              var async = true;
              var request = new XMLHttpRequest();
              var status;
              var data;
              request.onload = function () {
                    status = request.status; // HTTP response status, e.g., 200 for "200 OK"
                    data = request.responseText; // Returned data, e.g., an HTML document.
                    if (data === 'DUPLICATE') {
                        console.log ("DUPLICATE EMAIL");
                        document.getElementById('signUpEstateDetails').style.display='none';
                        document.getElementById('signInStatusFailed-EstateDetails').style.display='block';
                    } else {
                      document.getElementById('signUpEstateDetails').style.display='none';
                      document.getElementById('signInStatus-EstateDetails').style.display='block';
                  }
              }
              request.open(method, url,true);
              request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
              request.send(JSON.stringify(postData));
              document.getElementById('signUpEstateDetails').style.display='none';
               $("#email-EstateDetails").val("");
               $("#passwordSignUp-EstateDetails").val("");
               $("#first-name-EstateDetails").val("");
               $("#last-name-EstateDetails").val("");
          }
}
