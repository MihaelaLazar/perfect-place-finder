 function goToAddProperty(){
         window.location = "/addPropertyRaluca.html";
      }
         var myCurrentCityInPage = getQueryVariable('city');
         function insertParam(key, value) {
           var myURL = document.location.toString();
           var newURL;
           var foundQueryString = false;
           var index;
           for (index = 0; index < myURL.length; index ++){
             if (myURL[index] === "?"){
               foundQueryString = true;
             }
           }
           if (foundQueryString === false) {
             newURL = myURL + "?"+ key + "="+value;
           } else {
             newURL = myURL + "&"+ key + "="+value;
           }
           window.history.pushState({}, null, newURL);
          }

          function removeURLParameter(url, parameter) {
                //prefer to use l.search if you have a location/link object
                var urlparts= url.split('?');
                if (urlparts.length >= 2) {

                    var prefix= encodeURIComponent(parameter)+'=';
                    var pars= urlparts[1].split(/[&;]/g);
                    if (pars.length === 1 ){
                      var copyOfPars = "=" + pars;
                      var firstPartOrPars = copyOfPars.split(/[=;]/g);
                      if (parameter === firstPartOrPars[1]) {
                        var uri = window.location.toString();
                        var clean_uri = uri.substring(0, uri.indexOf("?"));
                        window.history.replaceState({}, document.title, clean_uri);
                        return url.split("?")[0];
                      }
                    } else {
                      //reverse iteration as may be destructive
                      for (var i = pars.length; i-- > 0;) {
                          //idiom for string.startsWith
                          if (pars[i].lastIndexOf(prefix, 0) !== -1) {
                              pars.splice(i, 1);
                          }
                      }

                      url= urlparts[0] + (pars.length > 0 ? '?' + pars.join('&') : "");
                      return url;
                    }

                } else {
                    return url;
                }
          }
         function getQueryVariable(variable) {
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
         var selectCity ;

          selectCity = document.getElementById('city');

        var city_id;

        if(selectCity.value === '0') {
            selectCity.value = getQueryVariable('city');
             city_id =  getQueryVariable('city');
        }
        console.log("SELECTCITY: " + selectCity.value);

        if(city_id != "") {
          console.log("my city value is: " + city_id);
        }
         function getEstatesByFilter() {
           var myNode = document.getElementById("estates");
           while (myNode.firstChild) {
             myNode.removeChild(myNode.firstChild);
           }

            var city_id = selectCity.options[selectCity.selectedIndex].value;
            cityCoordinates = getCityCoordinates(city_id);
            if (map.getCenter() != cityCoordinates) {
              console.log("CHANGING CITY TO: " + city_id);
              map.setCenter(cityCoordinates);

            }
            var selectType = document.getElementById('type');
            var type_id = selectType.options[selectType.selectedIndex].value;
            var selectSquare = document.getElementById('square');
            var square_id = selectSquare.options[selectSquare.selectedIndex].value;
            var selectMinPrice = document.getElementById('minPrice');
            var min_price_id = selectMinPrice.options[selectMinPrice.selectedIndex].value;
            var selectMaxPrice = document.getElementById('maxPrice');
            var max_price_id = selectMaxPrice.options[selectMaxPrice.selectedIndex].value;
            var selectYear = document.getElementById('year');
            var year_id = selectYear.options[selectYear.selectedIndex].value;
            var selectTransType = document.getElementById('transType');
            var trans_type_id = selectTransType.options[selectTransType.selectedIndex].value;
            if (getQueryVariable('city') != null) {
              window.history.pushState({}, null, removeURLParameter(window.location.search,'city'));
            }
            if (getQueryVariable('type') != null) {
              window.history.pushState({}, null, removeURLParameter(window.location.search,'type'));
            }
            if (getQueryVariable('square') != null) {
              window.history.pushState({}, null, removeURLParameter(window.location.search,'square'));
            }
            if (getQueryVariable('minPrice') != null) {
              window.history.pushState({}, null, removeURLParameter(window.location.search,'minPrice'));
            }
            if (getQueryVariable('maxPrice') != null) {
              window.history.pushState({}, null, removeURLParameter(window.location.search,'maxPrice'));
            }
            if (getQueryVariable('year') != null) {
              window.history.pushState({}, null, removeURLParameter(window.location.search,'year'));
            }
            if (getQueryVariable('transType') != null) {
              window.history.pushState({}, null, removeURLParameter(window.location.search,'transType'));
            }
            if (city_id != 0) {
              insertParam('city', city_id);
            }
            if (type_id != 0) {
              insertParam('type', type_id);
            }
            if (square_id != 0){
              insertParam('square', square_id);
            }
            if(min_price_id != 0 ){
                insertParam('minPrice',min_price_id);
            }
            if (max_price_id != 0){
                insertParam('maxPrice', max_price_id);
            }
            if (year_id != 0){
                insertParam('year', year_id);
            }
            if (trans_type_id != 0){
                insertParam('transType',trans_type_id);
            }
            var i;
            var firstChildPut = false;
            for (i = 0; i < estates.length; i ++) {
                  var currentDiv;
                  var respectsFilters = true;
                  if (year_id != 0 && estates[i].year < year_id){
                    respectsFilters = false;
                  }
                  if (trans_type_id != 0 && estates[i].typeOfTransaction != trans_type_id){
                    respectsFilters = false;
                  }
                  if (type_id != 0 && estates[i].typeOfEstate != type_id){
                    respectsFilters = false;
                  }
                  if (min_price_id != 0 && estates[i].price < min_price_id){
                    respectsFilters = false;
                  }
                  if (max_price_id != 0 && estates[i].price > max_price_id && max_price_id < 3100 ){
                    respectsFilters = false;
                  }
                  if (square_id != 0 && estates[i].surface < square_id && square_id >= 30 ){
                    respectsFilters = false;
                  }
                  if (estates[i].city.toLowerCase() === city_id && respectsFilters === true) {
                    if (firstChildPut === false) {
                      currentDiv = $("<div id='"+ estates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'> <img class='right floated tiny ui image' src='" + estates[i].photo+"/profile.jpg' style='width:120px;'><div class='header'>" + estates[i].name + "</div><div class='meta'>" + estates[i].city + "</div><div class='description'>" + estates[i].description + "</div></div><div class='extra content'><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estates[i].id +"'><input type='hidden' name='estate' value='" + estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate=" + estates[i].id  +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div></div></div></div>");
                      firstChildPut = true;
                    } else {
                      currentDiv = $("<div id='"+ estates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-2%;margin-left:2%;'><div class='content'> <img class='right floated tiny ui image' src='" + estates[i].photo+"/profile.jpg' style='width:120px;'><div class='header'>" + estates[i].name + "</div><div class='meta'>" + estates[i].city + "</div><div class='description'>" + estates[i].description + "</div></div><div class='extra content'><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estates[i].id +"'><input type='hidden' name='estate' value='" + estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate= " + estates[i].id +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div></div></div></div>");
                    }
                    $("#estates").append(currentDiv);
                    var icon = {
                        url: "images/Marker Filled-50.png",
                        scaledSize: new google.maps.Size(40, 40),
                        origin: new google.maps.Point(0,0),
                        anchor: new google.maps.Point(0,0)
                    };
                     var latLong = new google.maps.LatLng(estates[i].coordinates[0], estates[i].coordinates[1]);
                     var marker = new google.maps.Marker({
                      position: latLong,
                      animation: google.maps.Animation.DROP,
                      icon:icon,
                      map:map
                    });
                    estatesMarkers.push(estates[i].coordinates);
                    console.log(estatesMarkers);
                    marker.addListener('click', function toggleBounce() {
                        if (marker.getAnimation() !== null) {
                           marker.setAnimation(null);
                       } else {
                         marker.setAnimation(google.maps.Animation.BOUNCE);
                       }
                    });
                  }
            }

        }

         var scrollLoad = true;
         var myScroll = document.getElementById('scrolling');
         window.onload = function() {
                    console.log("LOAD");
                    if (getQueryVariable('type') != null) {
                      document.getElementById('type').value = getQueryVariable('type');
                    }
                    if (getQueryVariable('city') != null) {
                      document.getElementById('city').value = getQueryVariable('city');

                    }else {
                      document.getElementById('city').value = 'iasi';
                    }
                    if (getQueryVariable('maxPrice') != null) {
                      document.getElementById('maxPrice').value = getQueryVariable('maxPrice');
                    }
                    if (getQueryVariable('minPrice') != null) {
                      document.getElementById('minPrice').value = getQueryVariable('minPrice');
                    }
                    if (getQueryVariable('transType') != null) {
                      document.getElementById('transType').value = getQueryVariable('transType');
                    }
                    if (getQueryVariable('year') != null) {
                      document.getElementById('year').value = getQueryVariable('year');
                    }
                    if (getQueryVariable('square') != null) {
                      document.getElementById('square').value = getQueryVariable('square');
                    }

                    getEstatesByFilter();
                    initMap();
                    var i;
                    var firstChildPut = false;
//                    for (i = 0; i < estates.length; i ++) {
//                      var currentDiv;
//                      if (estates[i].city.toLowerCase() === city_id) {
//                        if (firstChildPut === false ) {
//                          currentDiv = $("<div id='"+ estates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'> <img class='right floated tiny ui image' src='" + estates[i].photo+"/profile.jpg' style='width:120px;'><div class='header'>" + estates[i].name + "</div><div class='meta'>" + estates[i].city + "</div><div class='description'>" + estates[i].description + "</div></div><div class='extra content'><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estates[i].id +"'><input type='hidden' name='estate' value='" + estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate=" + estates[i].id +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div></div></div></div>");
//                          firstChildPut = true;
//                        } else {
//                          currentDiv = $("<div id='"+ estates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-2%;margin-left:2%;'><div class='content'> <img class='right floated tiny ui image' src='" + estates[i].photo+"/profile.jpg' style='width:120px;'><div class='header'>" + estates[i].name + "</div><div class='meta'>" + estates[i].city + "</div><div class='description'>" + estates[i].description + "</div></div><div class='extra content'><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estates[i].id +"'><input type='hidden' name='estate' value='" + estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate="+ estates[i].id +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div></div></div></div>");
//                        }
//                        console.log(estates[i].coordinates);
//                        $("#estates").append(currentDiv);
//                        var icon = {
//                              url: "images/Marker Filled-50.png",
//                              scaledSize: new google.maps.Size(40, 40),
//                              origin: new google.maps.Point(0,0),
//                              anchor: new google.maps.Point(0, 0)
//                            };
//                         var marker = new google.maps.Marker({
//                          position: estates[i].coordinates,
//                          icon: icon,
//                          map:map
//                        });
//                        marker.addListener('click', function toggleBounce(){
//                            if (marker.getAnimation() !== null) {
//                               marker.setAnimation(null);
//                           } else {
//                             marker.setAnimation(google.maps.Animation.BOUNCE);
//                           }
//                        });
//                        estatesMarkers.push(estates[i].coordinates);
//                    }
//                  }
               };


                  $("#scrolling").scroll(function () {
                        if (isScrollBottom()) {
                            console.log("Hi I reached bottom. Do Something!");
                            var div = $("<div class='only row'><div class='column'><div class='ui card' style='width:91%;margin-top:-7%;margin-left:2%;'><div class='content'> <img class='right floated tiny ui image' src='images/house-example.png' style='width:120px;'><div class='header'> Morningside Park </div><div class='meta'>New York</div><div class='description'> Villa with 3 rooms, 2 floors situated in a residential area. </div></div><div class='extra content'><div class='ui two buttons'><form method='get' action='estateDetails.html'><button class='ui blue button' type='submit'>See details</button></form><form method='get' action='estateDetails.html' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div></div></div></div>");
                          //  $("#estates").append(div);
                        }
                    });

                    function isScrollBottom() {

                        var elementHeight = $("#scrolling")[0].scrollHeight;
                        var scrollPosition = $("#scrolling").innerHeight() + $("#scrolling").scrollTop() + 0.5;
                      //  console.log("elementHeight: " + elementHeight + ", scrollPosition: " + scrollPosition);
                        return (elementHeight <= scrollPosition);
                    }

                function loadmore() {
                }
              var map  = null;
              var markerGlobal;
              var trafficLayer;
              var transitLayer;
              var pollutionLayerIasi;
              var pollutionLayerBucuresti;
              var pollutionLayerNewYork;
              var pollutionLayerLondon;
              var geocoder;
              var smogMarkersArray = [];
              var noiseMarkersArray = [];
              var schoolsMarkersArray = [];
              var cityCoordinates;
              var heatmap;
              var estatesMarkers = [];

              function initMap() {
                    console.log("initMap");
                    cityCoordinates = getCityCoordinates(getQueryVariable('city'));
                    console.log("cityyyyy: " + getQueryVariable('city'));
                    if (getQueryVariable('city') === null) {
                      cityCoordinates = getCityCoordinates('iasi');
                    }
                    map  = null;
                    geocoder = new google.maps.Geocoder();
                    var localMap = new google.maps.Map(document.getElementById('map'), {
                      zoom: 14,
                      center: cityCoordinates
                    });
                    map = localMap;
                    var image = 'images/pollution-marker.png';
                    var icon = {
                      url: "images/pollution-marker.png",
                      scaledSize: new google.maps.Size(40, 40),
                      origin: new google.maps.Point(0,0),
                      anchor: new google.maps.Point(0, 0)
                    };
                    var marker = new google.maps.Marker({
                      position: cityCoordinates,
                      map: map,
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
                      map.setCenter(marker.getPosition()),
                      document.getElementById('myButton').style.display = 'initial';
                   })
                    marker.addListener('mouseover',function(){
                      map.setCenter(marker.getPosition()),
                      infoWindowOnMouseover.open(map,marker)

                    })
                    google.maps.event.addListener(marker, 'mouseout', function () {
                        infoWindowOnMouseover.close();
                    });
                   trafficLayer = new google.maps.TrafficLayer();
                   transitLayer = new google.maps.TransitLayer();
                   pollutionLayerIasi = new google.maps.Data();
                   pollutionLayerBucuresti = new google.maps.Data();
                   pollutionLayerNewYork = new google.maps.Data();
                   pollutionLayerLondon = new google.maps.Data();

                  var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
                  var icon = {
                      url: "images/Marker Filled-50.png",
                      scaledSize: new google.maps.Size(40, 40),
                      origin: new google.maps.Point(0,0),
                      anchor: new google.maps.Point(0, 0)
                  };
                  var markers = estatesMarkers.map(function(location,i) {
                      return new google.maps.Marker({
                          position: location,
                          label: labels[i % labels.length],
                          icon:  icon
                      });
                  });

                  // Add a marker clusterer to manage the markers.
                  var markerCluster = new MarkerClusterer(map, markers,
                      {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});

              }

              function getCityCoordinates(city_id) {
                var i = 0;
                var currentCityCoordinates = {};
                console.log("hei, city_id: " + city_id);
                for (i = 0; i < citiesCenterCoordinates.length; i++){
                  if (city_id == citiesCenterCoordinates[i].name.toLowerCase()){
                    currentCityCoordinates.lat = citiesCenterCoordinates[i].lat;
                    currentCityCoordinates.lng = citiesCenterCoordinates[i].lng;
                  }
                }
                return currentCityCoordinates;
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
                    } else {
                      if (id == 'noiseLayer'){
                        var marker, i;
                        for (i in noiseMarkersArray) {
                          noiseMarkersArray[i]['marker'].setMap(null);
                          noiseMarkersArray[i]['circle'].setMap(null);
                          delete noiseMarkersArray[i];
                        }
                      } else {
                        if (id=="schools"){
                          for (i in schoolsMarkersArray) {
                            schoolsMarkersArray[i].setMap(null);
                            delete schoolsMarkersArray[i];
                          }
                        } else {
                          if ( id === "pollutionLayer"){
                              pollutionLayerIasi.setMap(null);
                              pollutionLayerBucuresti.setMap(null);
                              pollutionLayerNewYork.setMap(null);
                              pollutionLayerLondon.setMap(null);
                          } else {
                            if (id === "congestionLayer"){
                              heatmap.setMap(null);

                            }
                          }
                        }
                      }
                    }
                  }
                }
             }
             function addOverlay(id) {
               if(id == 'trafficLayer'){
                 trafficLayer.setMap(map);
               }else {
                 if (id == 'transitLayer'){
                   transitLayer.setMap(map);
                 } else {
                   if (id == 'smogLayer'){
                     var icon = {
                       url: "images/pollution-marker.png",
                       scaledSize: new google.maps.Size(40, 40),
                       origin: new google.maps.Point(0,0),
                       anchor: new google.maps.Point(0, 0)
                     };
                     var marker, i;
                     for (i = 0; i < smogLocations.length; i++) {
                       marker = new google.maps.Marker({
                         position: new google.maps.LatLng(smogLocations[i][0], smogLocations[i][1]),
                         map: map,
                         icon: icon
                       });
                       var cityCircle = new google.maps.Circle({
                         strokeColor: '#808080',
                         strokeOpacity: 0.8,
                         strokeWeight: 2,
                         fillColor: '#808080',
                         fillOpacity: 0.35,
                         map: map,
                         center: marker.position,
                         radius: 100
                         });
                         smogMarkersArray[i] = {marker: marker, circle: cityCircle};
                       }
                       console.log(smogMarkersArray);
                   } else {
                     if (id == 'noiseLayer'){
                       var icon = {
                         url: "images/pollution-marker.png",
                         scaledSize: new google.maps.Size(40, 40),
                         origin: new google.maps.Point(0,0),
                         anchor: new google.maps.Point(0, 0)
                       };
                       var marker, i;
                       for (i = 0; i < noiseLocations.length; i++) {
                         marker = new google.maps.Marker({
                           position: new google.maps.LatLng(noiseLocations[i][0], noiseLocations[i][1]),
                          // map: map,
                           icon: icon
                         });
                         var cityCircle = new google.maps.Circle({
                           strokeColor: '#808080',
                           strokeOpacity: 0.8,
                           strokeWeight: 2,
                           fillColor: '#808080',
                           fillOpacity: 0.35,
                           map: map,
                           center: marker.position,
                           radius: 100
                           });
                           noiseMarkersArray[i] = {marker: marker, circle: cityCircle};
                         }
                         console.log(noiseMarkersArray);
                     } else {
                       if (id == "schools"){
                         var service = new google.maps.places.PlacesService(map);
                         service.nearbySearch({
                           location: map.getCenter(),
                           radius: 1000,
                           type: ['school']
                         }, callback);
                       } else {
                         if (id === "pollutionLayer") {
                             pollutionLayerIasi.addGeoJson(iasiPollution);
                             pollutionLayerIasi.setStyle(function(feature) {
                                return ({
                                  fillColor: feature.getProperty('color'),
                                  strokeWeight: 1,
                                  strokeColor: feature.getProperty('strokeColor'),
                                  fillOpacity: feature.getProperty('fillOpacity')
                                });
                              });
                              pollutionLayerIasi.setMap(map);
                              pollutionLayerBucuresti.addGeoJson(bucurestiPollution);
                              pollutionLayerBucuresti.setStyle(function(feature) {
                                  return ({
                                    fillColor: feature.getProperty('color'),
                                    strokeWeight: 1,
                                    strokeColor: feature.getProperty('strokeColor'),
                                    fillOpacity: feature.getProperty('fillOpacity')
                                  });
                              });
                              pollutionLayerBucuresti.setMap(map);
                              pollutionLayerNewYork.addGeoJson(newyorkPollution);
                              pollutionLayerNewYork.setStyle(function(feature) {
                                  return ({
                                    fillColor: feature.getProperty('color'),
                                    strokeWeight: 1,
                                    strokeColor: feature.getProperty('strokeColor'),
                                    fillOpacity: feature.getProperty('fillOpacity')
                                  });
                              });
                              pollutionLayerNewYork.setMap(map);
                              pollutionLayerLondon.addGeoJson(LondonPollution);
                              pollutionLayerLondon.setStyle(function(feature) {
                                  return ({
                                    fillColor: feature.getProperty('color'),
                                    strokeWeight: 1,
                                    strokeColor: feature.getProperty('strokeColor'),
                                    fillOpacity: feature.getProperty('fillOpacity')
                                  });
                              });
                              pollutionLayerLondon.setMap(map);

                                   } else {
                             if (id === "congestionLayer"){
                               heatmap = new google.maps.visualization.HeatmapLayer({
                                   data: getPoints(),
                                   map: map
                               });
                              changeGradient();
                             }
                           }
                         }
                       }
                     }
                   }
                 }
               }
               function callback(results, status) {
                    if (status === google.maps.places.PlacesServiceStatus.OK) {
                      for (var i = 0; i < results.length; i++) {
                          createMarker(results[i]);
                      }
                    }
                }
                function createMarker(place) {
                    var placeLoc = place.geometry.location;
                    var marker = new google.maps.Marker({
                      map: map,
                      position: place.geometry.location
                    });
                    schoolsMarkersArray.push(marker);
                }
                function codeAddress() {
                  var address = document.getElementById('address').value;
                  geocoder.geocode( { 'address': address}, function(results, status) {
                    if (status == 'OK') {
                      map.setCenter(results[0].geometry.location);
                      var marker = new google.maps.Marker({
                        map: map,
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

                function redirectToEstate(estateId){
                  window.location = "./estateDetails.html?estate=" + estateId;
                }

    $( "marker" )
            .mouseup(function() {
              $(this).hide();
            });


            var modalLoginSearchCity = document.getElementById('loginButton');
                  // When the user clicks anywhere outside of the modal, close it
                  var modalSignupSearchCity = document.getElementById('signupButton');
                  var modalSignedId = document.getElementById('signInStatus');
                  window.onclick = function(event) {
                      if(event.target == modalSignupSearchCity ){
                         console.log('CLOSSSE modalSignupSearchCity');
                          modalSignupSearchCity.style.display = "none";
                          $('#first-name-input').removeClass('error');
                          $('#first-name').attr("placeholder","First name");
                          $('#last-name-input').removeClass('error');
                          $('#last-name').attr("placeholder","Last name");
                          $('#email-input').removeClass('error');
                          $('#email').attr("placeholder","Email");
                          $('#password-input').removeClass('error');
                          $('#password-name').attr("placeholder","Password");
                          $('#errorMessageContainer').css("display", "none");
                          $("#email").val("");
                          $("#passwordSignUp").val("");
                          $("#first-name").val("");
                          $("#last-name").val("");
                      } else {
                        if (event.target == modalLoginSearchCity){
                          modalLoginSearchCity.style.display = "none";
                        } else {
                            if (event.target === document.getElementById('signInStatus')) {
                                document.getElementById('signInStatus').style.display='none';
                            } else {
                                if (event.target ===  document.getElementById('signInStatusFailed')){
                                    document.getElementById('signInStatusFailed').style.display='none';
                                }
                            }
                        }
                      }
                  }


                  function logInPOST() {
                         $('#password-input-login').removeClass('error');
                         $('#email-input-login').removeClass('error');
                         console.log('logInPOST');
                         var url = "/loginPerson";
                         var method = "POST";
                         var passInput = document.getElementById('password-login');
                         var password = passInput.value;
                         var emailInput = document.getElementById('email-login');
                         var email = emailInput.value;
                         var isValid = true;
                         var atpos = email.indexOf("@");
                         var dotpos = email.lastIndexOf(".");
                         if (password === "") {
                               $('#password-input-login').addClass('error');
                               $('#password-login').attr("placeholder","Insert password");
                                    $('#errorMessage').text("Insert password");
                                    $('#errorMessageContainer').css("display", "block");
                               isValid = false;
                         }
                         if (email === "" || atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
                               $('#email-input-login').addClass('error');
                               $('#email-login').attr("placeholder","Insert email");
                                   $('#errorMessage').text("Insert email");
                                   $('#errorMessageContainer').css("display", "block");
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
                                   if (data === 'Incorrect') {
                                       console.log ("Incorrect email/password");
                                       document.getElementById('loginButton').style.display='none';
                                       document.getElementById('logInStatusFailed').style.display='block';
                                   } else {
                                     document.getElementById('loginButton').style.display='none';
                                     document.getElementById('logInStatus').style.display='block';
                                 }
                             }
                             request.open(method,url,true);
                             request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                             request.send(JSON.stringify(postData));
                             document.getElementById('loginButton').style.display='none';
                              $("#email-login").val("");
                              $("#password-login").val("");
                         }
                  }

           function signUpPOST() {
                  $('#password-input').removeClass('error');
                  $('#first-name-input').removeClass('error');
                  $('#last-name-input').removeClass('error');
                  $('#email-input').removeClass('error');
                  console.log('signUpPOST');
                  var url = "/create/user";
                  var method = "POST";
                  var firstNameInput = document.getElementById('first-name');
                  var firstName = firstNameInput.value;
                  var lastNameInput = document.getElementById('last-name');
                  var lastName = lastNameInput.value;
                  var passInput = document.getElementById('passwordSignUp');
                  var password = passInput.value;
                  var emailInput = document.getElementById('email');
                  var email = emailInput.value;
                  var isValid = true;
                  var atpos = email.indexOf("@");
                  var dotpos = email.lastIndexOf(".");
                 if (firstName === "") {
                        $('#first-name-input').addClass('error');
                        $('#first-name').attr("placeholder","Insert first name");
                        $('#errorMessage').text("Insert first name");
                        $('#errorMessageContainer').css("display", "block");
                        isValid = false;
                  }
                  if (lastName === "") {
                        $('#last-name-input').addClass('error');
                        $('#last-name').attr("placeholder","Insert last name");
                            $('#errorMessage').text("Insert last name");
                            $('#errorMessageContainer').css("display", "block");

                        isValid = false;
                  }
                  if (password === "") {
                        $('#password-input').addClass('error');
                        $('#passwordSignUp').attr("placeholder","Insert password");
                             $('#errorMessage').text("Insert password");
                             $('#errorMessageContainer').css("display", "block");

                        isValid = false;
                  }
                  if (email === "" || atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
                        $('#email-input').addClass('error');
                        $('#email').attr("placeholder","Insert email");
                            $('#errorMessage').text("Insert email");
                            $('#errorMessageContainer').css("display", "block");

                        isValid = false;
                  }
                  var postData = {
                      "first-name": firstName,
                       "last-name" : lastName,
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
                                document.getElementById('signupButton').style.display='none';
                                document.getElementById('signInStatusFailed').style.display='block';
                            } else {
                              document.getElementById('signupButton').style.display='none';
                              document.getElementById('signInStatus').style.display='block';
                            }
                      }

                      request.open(method, url,true);
                      request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                      request.send(JSON.stringify(postData));
                      document.getElementById('signupButton').style.display='none';
                       $("#email").val("");
                       $("#passwordSignUp").val("");
                       $("#first-name").val("");
                       $("#last-name").val("");

                  }
           }

           $('#loginButtonSubmit').click(function(event){
                event.preventDefault();
                var inpObj = document.getElementById('password');
                if (inpObj.checkValidity() == false) {
                     document.getElementById("passwordContainerLogin").innerHTML = inpObj.validationMessage;
                 }
                 var requestLogin = new XMLHttpRequest();

                document.getElementById('loginButton').style.display='none';

            });
