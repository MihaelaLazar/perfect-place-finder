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
var trafficLayer;
var transitLayer;
var geocoder;
var estatePosition;
var smogMarkersArray = [];
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
          var map2 = new google.maps.Map(document.getElementById('map2'), {
            zoom: 14,
            center: estatePosition
          });
          globalMap2 = map2;
          var image = 'images/pollution-marker.png';
          var icon = {
            url: "images/Marker Filled-50.png", // url
            scaledSize: new google.maps.Size(40, 40), // scaled size
            origin: new google.maps.Point(0,0), // origin
            anchor: new google.maps.Point(0, 0) // anchor
          };
          var marker = new google.maps.Marker({
            position: estatePosition,
            map: map2,
            title: "Click here to zoom",
            icon: icon
          });
          markerGlobal = marker;
          var infoWindow = new google.maps.InfoWindow({
            content: '<select><option value="volvo">Volvo</option><option value="saab">Saab</option><option value="audi">Audi</option></select>'
          })
          var infoWindowOnMouseover = new google.maps.InfoWindow({
            content: '<div popup  data-tooltip="Add users to your feed" data-position="top center"> My fav house </div>'
          })
          marker.addListener('click',function(){
            console.log('click'),
            map2.setCenter(marker.getPosition()),
            document.getElementById('myButton').style.display = 'initial';
         })
          marker.addListener('mouseover',function(){
            map2.setCenter(marker.getPosition()),
            infoWindowOnMouseover.open(globalMap2,marker)
          })
          google.maps.event.addListener(marker, 'mouseout', function () {
              infoWindowOnMouseover.close();
          });
         trafficLayer = new google.maps.TrafficLayer();
         transitLayer = new google.maps.TransitLayer();
        }
        function changeOverlay(id){
          if(document.getElementById(id).checked) {
            addOverlay(id);
          } else {
            removeOverlay(id);
          }
        }
        function removeOverlay(id) {
          if(id == 'trafficLayer'){
            trafficLayer.setMap(null);
          } else {
            if (id == 'transitLayer'){
              transitLayer.setMap(null);
            } else {
              if (id == 'smogLayer'){
                var marker, i;
                for (i in smogMarkersArray) {
                  smogMarkersArray[i]['marker'].setMap(null);
                  smogMarkersArray[i]['circle'].setMap(null);
                  delete smogMarkersArray[i];
                  //smogMarkersArray.splice(i, 1);
                }
              }
            }
          }
       }
       function addOverlay(id) {
         if(id == 'trafficLayer'){
           trafficLayer.setMap(globalMap2);
         }else {
           if (id == 'transitLayer'){
             transitLayer.setMap(globalMap);
           } else {
             if (id == 'smogLayer'){
               var icon = {
                 url: "images/pollution-marker.png", // url
                 scaledSize: new google.maps.Size(40, 40), // scaled size
                 origin: new google.maps.Point(0,0), // origin
                 anchor: new google.maps.Point(0, 0) // anchor
               };
               var marker, i;
               for (i = 0; i < smogLocations.length; i++) {
                 marker = new google.maps.Marker({
                   position: new google.maps.LatLng(smogLocations[i][0], smogLocations[i][1]),
                   map: globalMap,
                   icon: icon
                 });
                 var cityCircle = new google.maps.Circle({
                   strokeColor: '#808080',
                   strokeOpacity: 0.8,
                   strokeWeight: 2,
                   fillColor: '#808080',
                   fillOpacity: 0.35,
                   map: globalMap,
                   center: marker.position,
                   radius: 100
                   });
                   smogMarkersArray[i] = {marker: marker, circle: cityCircle};
                 }
                 console.log(smogMarkersArray);
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
