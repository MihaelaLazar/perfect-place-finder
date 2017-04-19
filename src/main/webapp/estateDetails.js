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