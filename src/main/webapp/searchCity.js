/* This function redirects to addProperty.html page when clicking "Add property" button. */
function goToAddProperty(){
    window.location = "/addProperty.html";
}

var loggedInUser;

/* This function checks if session exists */
function checkSession() {
    var request = new XMLHttpRequest();
    request.open('GET', '/user/check/session', false);  // `false` makes the request synchronous
    request.send(" ");
    var data = request.responseText;
    if (request.status === 200) {
          console.log("session existent");
          console.log(data);
          $( "#searchCity-container" ).empty();
          var $addProperty = $("<button class='ui inverted blue button' style='margin-top:5px;' onclick=redirectToAddPropertySearchCity() >Add property</button>");
          $addProperty.appendTo($("#searchCity-container"));
          var $currentDiv = $("<button id='loggedIn' style='margin-top:5px;' class='ui inverted blue button' onclick=redirectToProfileSearchCity() >" + data+"</button>");
          $currentDiv.appendTo($("#searchCity-container"));
          var $logout = $("<button id='logoutButton-estateDetails' style='margin-top:5px;' class='ui inverted blue button' onclick=invalidateSessionSearchCity()>Log out</button>");
          $logout.appendTo($("#searchCity-container"));
          loggedInUser = data;
//          getUserFavAnnouncements();
    } else {
        console.log("not existent session");
        $( "#searchCity-container" ).empty();
        var $signup = $("<button class='ui inverted blue button' onclick=document.getElementById('signupButton').style.display='block' style='margin-top:5px;'>Sign up</button>");
        $signup.appendTo($("#searchCity-container"));
        var $login = $("<button class='ui inverted blue button' onclick=document.getElementById('loginButton').style.display='block' style='margin-top:5px;'>Log in</button>");
        $login.appendTo($("#searchCity-container"));
        loggedInUser = data;
    }
}

function redirectToAddPropertySearchCity() {
    $.ajax ({
        method: 'GET',
        url: '/user/addProperty',
        contentType: false,
        success: function (data) {
            window.location = data;
        },
        error: function (xhr, ajaxOptions, thrownError,textStatus) {
            console.log('error Status code ' + xhr.status);
            console.log('Not logged in');
        }
    });
}

/* This function redirects tu user's profile page */
function redirectToProfileSearchCity() {
    $.ajax ({
        method: 'GET',
        url: '/user/getProfile',
        contentType: false,
        success: function (data) {
            window.location = data;
        },
        error: function (xhr, ajaxOptions, thrownError,textStatus) {
            console.log('error Status code ' + xhr.status);
            console.log('Not logged in');
        }
    });
}


/* This function invalidates session */
function invalidateSessionSearchCity() {
     $.ajax ({
         method: 'GET',
         url: '/user/logout',
         contentType: false,
         success (data) {
             console.log('session invalidated');
             $( "#searchCity-container" ).empty();
              var $signup = $("<button class='ui inverted blue button' style='margin-top:5px;' onclick=document.getElementById('signupButton').style.display='block'>Sign up</button>");
              $signup.appendTo($("#searchCity-container"));
              var $login = $("<button class='ui inverted blue button' style='margin-top:5px;' onclick='document.getElementById('loginButton').style.display='block''>Log in</button>");
              $login.appendTo($("#searchCity-container"));
             checkSession();
             getEstatesByFilter();
         }
     });
 }


function checkIfEstateIsFavorite(idEstate, favEstates) {
    for (var i = 0; i < favEstates.length; i ++) {
        if (idEstate === favEstates[i]) {
            return 1;
        }
    }

    return 0;
}

var myCurrentCityInPage = getQueryVariable('city');


/* This function inserts the given parameter(key,value) in the query string of URL. */
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

/* This function removes given parameter from URL. */
function removeURLParameter(url, parameter) {
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

/* This function return the value of the given variable from query string of current URL. */
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
var transTypeVisible = 0;
var offset = 0;
var currentCountOfProperties = 0;
var numberOfPropertiesReturned = 0;
var userFavoritesAnnouncementsSearchCity = [];
var totalCountOfProperties;

/* This function filters announcements
 - checks if every filter is chosen
 - if chosen, insert de filter in query string
 - reloads the page
 */
function getEstatesByFilter() {

    var myNode = document.getElementById("estates");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
    var city_id = selectCity.options[selectCity.selectedIndex].value;
    cityCoordinates = getCityCoordinates(city_id);
    if (map.getCenter() != cityCoordinates) {
        map.setCenter(cityCoordinates);
    }
    estatesMarkersCoordinates = [];
    estatesMarkers = [];
    var selectType = document.getElementById('type');
    var type_id = selectType.options[selectType.selectedIndex].value;
    var selectSquare = document.getElementById('square');
    var square_id = selectSquare.options[selectSquare.selectedIndex].value;
    var selectMinPriceSale = document.getElementById('minPriceSale');
    var min_price_sale_id = selectMinPriceSale.options[selectMinPriceSale.selectedIndex].value;
    var selectMaxPriceSale = document.getElementById('maxPriceSale');
    var max_price_sale_id = selectMaxPriceSale.options[selectMaxPriceSale.selectedIndex].value;
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
    if (getQueryVariable('minPriceSale') != null) {
        window.history.pushState({}, null, removeURLParameter(window.location.search,'minPriceSale'));
    }
    if (getQueryVariable('maxPriceSale') != null) {
        window.history.pushState({}, null, removeURLParameter(window.location.search,'maxPriceSale'));
    }
    if (getQueryVariable('year') != null) {
        window.history.pushState({}, null, removeURLParameter(window.location.search,'year'));
    }
    if (getQueryVariable('transType') != null) {
        window.history.pushState({}, null, removeURLParameter(window.location.search,'transType'));
    }
    if (getQueryVariable('offset') != null) {
        window.history.pushState({}, null, removeURLParameter(window.location.search,'offset'));
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
    if(min_price_sale_id != 0 ){
        insertParam('minPriceSale',min_price_sale_id);
    }
    if (max_price_sale_id != 0){
        insertParam('maxPriceSale', max_price_sale_id);
    }
    if (year_id != 0) {
        insertParam('year', year_id);
    }
    if (trans_type_id != 0){
        insertParam('transType',trans_type_id);
        if (document.getElementById('transType').value === 'sale' && transTypeVisible === 1){
            var i;
            var selectboxMin = document.getElementById("minPriceSale");
            for(i = selectboxMin.options.length - 1 ; i >= 0 ; i--) {
                selectboxMin.remove(i);
            }
            var selectboxMax = document.getElementById("maxPriceSale");
            for(i = selectboxMax.options.length - 1 ; i >= 0 ; i--) {
                selectboxMax.remove(i);
            }
            addOption('sale', 'min');
            addOption('sale', 'max');
            transTypeVisible = 0;
            if (getQueryVariable('minPriceSale') != null) {
                window.history.pushState({}, null, removeURLParameter(window.location.search,'minPriceSale'));
            }
            if (getQueryVariable('maxPriceSale') != null) {
                window.history.pushState({}, null, removeURLParameter(window.location.search,'maxPriceSale'));
            }
        } else {
            if (document.getElementById('transType').value === 'rent' && transTypeVisible === 0){
                console.log('TRANS TYPE IS RENT');
                var i;
                var selectboxMin = document.getElementById("minPriceSale");
                for(i = selectboxMin.options.length - 1 ; i >= 0 ; i--) {
                    selectboxMin.remove(i);
                }
                var selectboxMax = document.getElementById("maxPriceSale");
                for(i = selectboxMax.options.length - 1 ; i >= 0 ; i--) {
                    selectboxMax.remove(i);
                }
                addOption('rent', 'min');
                addOption('rent', 'max');
                transTypeVisible = 1;
                if (getQueryVariable('minPriceSale') != null) {
                    window.history.pushState({}, null, removeURLParameter(window.location.search,'minPriceSale'));
                }
                if (getQueryVariable('maxPriceSale') != null) {
                    window.history.pushState({}, null, removeURLParameter(window.location.search,'maxPriceSale'));
                }
            }
        }
    }
    offset = 0;
    insertParam('offset', offset);
    var i;
    var firstChildPut = false;
    var myURL = document.location.toString().split("?");
    var queryString =  myURL[1];
    var url = "/estate/getByFilters?" + queryString;
    var method = "GET";
    var async = true;
    var request = new XMLHttpRequest();
    var status;
    var data;

    request.onload = function () {

        status = request.status; // HTTP response status, e.g., 200 for "200 OK"
        data = request.responseText; // Returned data, e.g., an HTML document.
        var estatesByFilters = JSON.parse(data);
        for(var i = 0; i < estatesByFilters.estates.length; i ++ ) {
            var currentDiv;
            var shortDescription = estatesByFilters.estates[i].description.substr(0, 65) + "...";
               var price;
            if (estatesByFilters.estates[i].buyPrice != 0) {
                price = estatesByFilters.estates[i].buyPrice;
            } else {
                price = estatesByFilters.estates[i].rentPrice;
            }
            if (loggedInUser != "not existent") {
                var isFavorite = checkIfEstateIsFavorite(estatesByFilters.estates[i].id, estatesByFilters.favEstates);
                if (isFavorite === 1) {
                    if (estatesByFilters.estates[i].estateAttachements.length > 0) {
                        currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + estatesByFilters.estates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div style='font-size: 18px;font-weight: 700;color: #FB6B00;'> € " + price + " </div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form></div></div><div class='two wide column'><button id='heart" + estatesByFilters.estates[i].id + "' class='ui inverted orange button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    } else {
                        currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div style='font-size: 18px;font-weight: 700;color: #FB6B00;'> € " + price + " </div><div class='description'>" + shortDescription+ "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form></div></div><div class='two wide column'><button id='heart"+ estatesByFilters.estates[i].id + "' class='ui inverted orange button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    }
                } else {
                    if (estatesByFilters.estates[i].estateAttachements. length > 0) {
                        currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + estatesByFilters.estates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div style='font-size: 18px;font-weight: 700;color: #FB6B00;'> € " + price + " </div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form></div></div><div class='two wide column'><button id='heart" + estatesByFilters.estates[i].id + "' class='ui inverted blue button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    } else {
                        currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div style='font-size: 18px;font-weight: 700;color: #FB6B00;'> € " + price + " </div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form></div></div><div class='two wide column'><button id='heart"+ estatesByFilters.estates[i].id + "' class='ui inverted blue button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    }
                }

            } else {
                if (estatesByFilters.estates[i].estateAttachements. length > 0) {
                    currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + estatesByFilters.estates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div style='font-size: 18px;font-weight: 700;color: #FB6B00;'> € " + price + " </div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form></div></div><div class='two wide column'><button id='heart" + estatesByFilters.estates[i].id + "' class='ui inverted blue button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "') style='display:none;'><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                } else {
                    currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div style='font-size: 18px;font-weight: 700;color: #FB6B00;'> € " + price + " </div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form></div></div><div class='two wide column'><button id='heart"+ estatesByFilters.estates[i].id + "' class='ui inverted blue button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "') style='display:none;'><i class='heart icon' style='width:8px; ></i></button></div></div></div></div></div></div>");
                }
            }
            $("#estates").append(currentDiv);
            var address = estatesByFilters.estates[i].address.split(" ");
            var latLong = new google.maps.LatLng(address[0], address[1]);
            var icon = {
                url: "images/Marker Filled-50.png",
                scaledSize: new google.maps.Size(40, 40),
                origin: new google.maps.Point(0,0),
                anchor: new google.maps.Point(0,0)
            };
            estatesMarkersCoordinates.push(latLong);
            var marker = new google.maps.Marker({
                position: latLong,
               // draggable: true,
                animation: google.maps.Animation.DROP,
                icon:icon,
                map:map
            });
            marker.addListener('click', function toggleBounce() {
                if (marker.getAnimation() !== null) {
                    marker.setAnimation(null);
                } else {
                    marker.setAnimation(google.maps.Animation.BOUNCE);
                }
            });
            estatesMarkers[i] = marker;
        }
        totalCountOfProperties = estatesByFilters.totalCount;
        currentCountOfProperties =   estatesByFilters.estates.length;
        numberOfPropertiesReturned = estatesByFilters.estates.length;
        var numberOfProperties = $("<h3 class='ui left aligned header'>Number of properties: "+ currentCountOfProperties + "/"+ totalCountOfProperties + "</h3>");
        $( "#number-of-properties" ).empty();
        $('#number-of-properties').append(numberOfProperties);
        console.log('before initMap in getEstatesByFilter');
        initMap();
    }
    request.open(method, url,true);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send();

    // request.send(JSON.stringify(getData));

}

/* This function changes the state of announcement: if the user liked it(set it to favorites) or not.
 - orange if favorite
 - blue otherwise (default)
 */
function changeLikeState (id) {
    if ( $('#' + id).hasClass('blue') ){
        $.ajax({
                method: 'POST',
                url: '/user/add/favoriteAnnouncement?idAnnouncement=' +  id.substring(5),
                contentType: false,
                success(data) {
                    $('#' + id).removeClass('blue');
                    $('#' + id).addClass('orange');
                }
        });
    } else {
        $.ajax({
                method: 'POST',
                url: '/user/delete/favoriteAnnouncement?idAnnouncement=' +  id.substring(5),
                contentType: false,
                success(data) {
                    $('#' + id).removeClass('orange');
                    $('#' + id).addClass('blue');
                }
        });

    }
}

function raiseMarker(estateId) {
    estatesMarkers[estateId].setAnimation(google.maps.Animation.BOUNCE);
}


function unraiseMarker(estateId){
    estatesMarkers[estateId].setAnimation(null);
}

/* This function is called when type of transaction is changed.
 - when transaction is 'sale', adds different prices on dropdown,
 erasing the prices from 'rent' type transaction
 - for 'rent' : the same
 */
function addOption(transType, typeOfPrice) {
    if (transType === 'sale' && typeOfPrice === 'min') {
        var opt = document.createElement("option");
        opt.value = "0";
        opt.text = "Min price";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "20";
        opt.text = "€20 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "25";
        opt.text = "€25 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "35";
        opt.text = "€35 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "40";
        opt.text = "€40 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "50";
        opt.text = "€50 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "65";
        opt.text = "€65 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "80";
        opt.text = "€80 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "100";
        opt.text = "€100 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "150";
        opt.text = "€150 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "200";
        opt.text = "€200 000";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "300";
        opt.text = "€300 000";
        document.getElementById('minPriceSale').add(opt);
        document.getElementById("minPriceSale").options[0].disabled = true;
    }

    if (transType === 'sale' && typeOfPrice === 'max') {
        var opt = document.createElement("option");
        opt.value = "0";
        opt.text = "Max price";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "50";
        opt.text = "€50 000";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "60";
        opt.text = "€60 000";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "75";
        opt.text = "€75 000";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "100";
        opt.text = "€100 000";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "150";
        opt.text = "€150 000";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "200";
        opt.text = "€200 000";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "300";
        opt.text = "€300 000";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "310";
        opt.text = "€300 000 +";
        document.getElementById('maxPriceSale').add(opt);
        document.getElementById("maxPriceSale").options[0].disabled = true;
    }
    if (transType === 'rent' && typeOfPrice === 'min') {
        var opt = document.createElement("option");
        opt.value = "0";
        opt.text = "Min price";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "100";
        opt.text = "€100";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "200";
        opt.text = "€200";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "300";
        opt.text = "€300";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "400";
        opt.text = "€400";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "500";
        opt.text = "€500";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "600";
        opt.text = "€600";
        opt = document.createElement("option");
        document.getElementById('minPriceSale').add(opt);
        opt.value = "700";
        opt.text = "€700";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "800";
        opt.text = "€800";
        opt = document.createElement("option");
        opt.value = "900";
        opt.text = "€900";
        document.getElementById('minPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "1000";
        opt.text = "€1000";
        document.getElementById('minPriceSale').add(opt);
        document.getElementById("minPriceSale").options[0].disabled = true;
    }

    if (transType === 'rent' && typeOfPrice === 'max') {
        var opt = document.createElement("option");
        opt.value = "0";
        opt.text = "Max price";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "300";
        opt.text = "€300";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "400";
        opt.text = "€400";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "500";
        opt.text = "€500";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "600";
        opt.text = "€600";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "700";
        opt.text = "€700";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "800";
        opt.text = "€800";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "900";
        opt.text = "€900";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "1000";
        opt.text = "€1000";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "1100";
        opt.text = "€1100";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "1200";
        opt.text = "€1200";
        document.getElementById('maxPriceSale').add(opt);

        opt = document.createElement("option");
        opt.value = "1300";
        opt.text = "€1300";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "1400";
        opt.text = "€1400";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "1500";
        opt.text = "€1500";
        document.getElementById('maxPriceSale').add(opt);
        opt = document.createElement("option");
        opt.value = "1500";
        opt.text = "€1500 +";
        document.getElementById('maxPriceSale').add(opt);;
        document.getElementById("maxPriceSale").options[0].disabled = true;
    }
}

var scrollLoad = true;
var myScroll = document.getElementById('scrolling');

/* Refreshes the query string when page is loaded
 - checks for chosen filters
 */
//window.onload = function() {
window.onload = function () {
    checkSession();
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
    console.log("ON LOAD");
    console.log('before initMap in window.onload');
    initMap();
    var i;
    var firstChildPut = false;
}



/* This function is for lazy loading the announcements from  database*/
$("#scrolling").scroll(function () {
    if (isScrollBottom()) {
        console.log("reached the bottom");
        console.log("numberOfPropertiesReturned: " + numberOfPropertiesReturned);
        console.log("currentCountOfProperties : " + currentCountOfProperties);
        console.log("totalCountOfProperties : " + totalCountOfProperties);
        if (currentCountOfProperties < totalCountOfProperties ) {
            loadMore();
        }
        // if (numberOfPropertiesReturned > 0 ) {
        //     loadMore();
        // }
    }
});

/* This function checks if scroll has reach the bottom of element (for loading more data). */
function isScrollBottom() {
    var elementHeight = $("#scrolling")[0].scrollHeight;
    var scrollPosition = $("#scrolling").innerHeight() + $("#scrolling").scrollTop() + 0.5;
    return (elementHeight <= scrollPosition);
}


/* This function (will) load more data from database.*/
function loadMore() {
    offset = offset + 10;
 //   console.log('offset increased by 10');
    if (getQueryVariable('offset') != null) {
        window.history.pushState({}, null, removeURLParameter(window.location.search,'offset'));
    }
    insertParam('offset',offset);
    var myURL = document.location.toString().split("?");
    var queryString =  myURL[1];
    var url = "/estate/getByFilters?" + queryString;
    var request = new XMLHttpRequest();
    var status;
    var data;
    var method = "GET";
    request.onload = function () {
        status = request.status; // HTTP response status, e.g., 200 for "200 OK"
        data = request.responseText; // Returned data, e.g., an HTML document.
        var estatesByFilters = JSON.parse(data);
        console.log('estates returned: ' + estatesByFilters.estates.length);
        for(var i = 0; i < estatesByFilters.estates.length; i ++ ) {
            var currentDiv;
            if (loggedInUser != "not existent") {
                var isFavorite = checkIfEstateIsFavorite(estatesByFilters.estates[i].id, estatesByFilters.favEstates);
                if (isFavorite === 1) {
                    if (estatesByFilters.estates[i].estateAttachements.length > 0) {
                        currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + estatesByFilters.estates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div class='description'>" + estatesByFilters.estates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate=" + estatesByFilters.estates[i].id  +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div><div class='two wide column'><button id='heart" + estatesByFilters.estates[i].id + "' class='ui inverted orange button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    } else {
                        currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div class='description'>" + estatesByFilters.estates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate=" + estatesByFilters.estates[i].id  +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div><div class='two wide column'><button id='heart"+ estatesByFilters.estates[i].id + "' class='ui inverted orange button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    }
                } else {
                    if (estatesByFilters.estates[i].estateAttachements. length > 0) {
                        currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + estatesByFilters.estates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div class='description'>" + estatesByFilters.estates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate=" + estatesByFilters.estates[i].id  +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div><div class='two wide column'><button id='heart" + estatesByFilters.estates[i].id + "' class='ui inverted blue button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    } else {
                        currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div class='description'>" + estatesByFilters.estates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate=" + estatesByFilters.estates[i].id  +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div><div class='two wide column'><button id='heart"+ estatesByFilters.estates[i].id + "' class='ui inverted blue button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    }
                }

            } else {
                if (estatesByFilters.estates[i].estateAttachements. length > 0) {
                    currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + estatesByFilters.estates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div class='description'>" + estatesByFilters.estates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate=" + estatesByFilters.estates[i].id  +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div><div class='two wide column'><button id='heart" + estatesByFilters.estates[i].id + "' class='ui inverted blue button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "') style='display:none;'><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                } else {
                    currentDiv =  $("<div id='"+ estatesByFilters.estates[i].id +"' class='only row' onmouseover='raiseMarker(" + i + ")' onmouseout='unraiseMarker("+ i +")'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + estatesByFilters.estates[i].typeOfTransaction + " " + estatesByFilters.estates[i].rooms + " room/s " + estatesByFilters.estates[i].type + "</div><div class='meta'>" + estatesByFilters.estates[i].city + "</div><div class='description'>" + estatesByFilters.estates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><form method='get' action='estateDetails.html?estate="+ estatesByFilters.estates[i].id +"'><input type='hidden' name='estate' value='" + estatesByFilters.estates[i].id + "' /><button class='ui blue button' type='submit' onclick=redirectToEstate("+ estatesByFilters.estates[i].id +")>See details</button></form><form method='get' action='estateDetails.html?estate=" + estatesByFilters.estates[i].id  +"' ><button class='ui basic black button' type='submit'>Check availability</button></form></div></div><div class='two wide column'><button id='heart"+ estatesByFilters.estates[i].id + "' class='ui inverted blue button' onClick=changeLikeState('heart" + estatesByFilters.estates[i].id + "') style='display:none;'><i class='heart icon' style='width:8px; ></i></button></div></div></div></div></div></div>");
                }
            }
            $("#estates").append(currentDiv);
            var address = estatesByFilters.estates[i].address.split(" ");
            var latLong = new google.maps.LatLng(address[0], address[1]);
            var icon = {
                url: "images/Marker Filled-50.png",
                scaledSize: new google.maps.Size(40, 40),
                origin: new google.maps.Point(0,0),
                anchor: new google.maps.Point(0,0)
            };
            estatesMarkersCoordinates.push(latLong);
            var marker = new google.maps.Marker({
                position: latLong,
                // draggable: true,
                animation: google.maps.Animation.DROP,
                icon:icon,
                map:map
            });
            marker.addListener('click', function toggleBounce() {
                if (marker.getAnimation() !== null) {
                    marker.setAnimation(null);
                } else {
                    marker.setAnimation(google.maps.Animation.BOUNCE);
                }
            });
            estatesMarkers[i] = marker;
        }
        c = estatesByFilters.totalCount;
        console.log("IN GET :totalCountOfProperties: " + totalCountOfProperties);
        currentCountOfProperties = currentCountOfProperties + estatesByFilters.estates.length;
        numberOfPropertiesReturned = estatesByFilters.estates.length;
        var numberOfProperties = $("<h3 class='ui left aligned header'>Number of properties: "+ currentCountOfProperties + "/"+ totalCountOfProperties + "</h3>");
        $( "#number-of-properties" ).empty();
        $('#number-of-properties').append(numberOfProperties);
        console.log('before initMap in loadMore');
        initMap();
    }
    request.open(method, url,true);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send();

    // request.send(JSON.stringify(getData));
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
var estatesMarkersCoordinates = [];
var estatesMarkers= [];
/* This function initializes the map on the page and adds cluster to markers(of announcements). */
function initMap() {
    console.log("initMap");
    cityCoordinates = getCityCoordinates(getQueryVariable('city'));
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
        title: "Click here to zoom",
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
    var markers = estatesMarkersCoordinates.map(function(location,i) {
        return new google.maps.Marker({
            position: location,
            label: labels[i % labels.length],
            icon:  icon,
            //draggable: true,
            animation: google.maps.Animation.DROP
        });
    });

    var markerCluster = new MarkerClusterer(map, estatesMarkers,
        {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});
    for(var i = 0; i < markers.length; i ++) {
        estatesMarkers[i] = markers[i];
    }
    checkSelectedOverlays();
}

/* This function gets chosen city coordinates to place the center of map on the center of the city. */
function getCityCoordinates(city_id) {
    var i = 0;
    var currentCityCoordinates = {};
    for (i = 0; i < citiesCenterCoordinates.length; i++){
        if (city_id == citiesCenterCoordinates[i].name.toLowerCase()){
            currentCityCoordinates.lat = citiesCenterCoordinates[i].lat;
            currentCityCoordinates.lng = citiesCenterCoordinates[i].lng;
        }
    }
    return currentCityCoordinates;
}

function checkSelectedOverlays() {
    if(document.getElementById("trafficLayer").checked) {
        addOverlay("trafficLayer");
    }

    if(document.getElementById("noiseLayer").checked) {
        addOverlay("noiseLayer");
    }

    if(document.getElementById("pollutionLayer").checked) {
        addOverlay("pollutionLayer");
    }
    if(document.getElementById("congestionLayer").checked) {
        addOverlay("congestionLayer");
    }

    if(document.getElementById("transitLayer").checked) {
        addOverlay("transitLayer");
    }
}

/* This function changes the state of overlays on checking/unchecking the checkbox: add/ remove overlay. */
function changeOverlay(id){
    if(document.getElementById(id).checked) {
        addOverlay(id);
    } else {
        removeOverlay(id);
    }
}

var  waqiMapOverlay;

/* This function removes the given overlay from the map */
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
                    if ( id === "pollutionLayer"){
                        map.overlayMapTypes.removeAt(0,waqiMapOverlay);
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

/* This function adds an overlay on the map
 - id: the overlay identified from its corresponding html element id
 */
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
                //console.log(smogMarkersArray);
            } else {
                if (id == 'noiseLayer'){
                    var icon = {
                        url: "images/pollution-marker.png",
                        scaledSize: new google.maps.Size(40, 40),
                        origin: new google.maps.Point(0,0),
                        anchor: new google.maps.Point(0, 0)
                    };
                    var marker, i;
                    $.ajax({
                        method: 'GET',
                        url: '/get/noise/tweets',
                        contentType: false,
                        success: function (data) {
                            console.log(data);
                            for (var i = 0; i < data.length; i ++) {
                                var latCoordinate = data[i].split(',')[0];
                                var longCoordinate = data[i].split(',')[1];
                                marker = new google.maps.Marker({
                                    position: new google.maps.LatLng(latCoordinate, longCoordinate),
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
                                    radius: 500
                                });
                                noiseMarkersArray[i] = {marker: marker, circle: cityCircle};
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError,textStatus) {
                            console.log('error Status code ' + xhr.status);
                            console.log('Not logged in');
                        }
                    })
//
//                    for (i = 0; i < noiseLocations.length; i++) {
//                        marker = new google.maps.Marker({
//                            position: new google.maps.LatLng(noiseLocations[i][0], noiseLocations[i][1]),
//                            // map: map,
//                            icon: icon
//                        });
//                        var cityCircle = new google.maps.Circle({
//                            strokeColor: '#808080',
//                            strokeOpacity: 0.8,
//                            strokeWeight: 2,
//                            fillColor: '#808080',
//                            fillOpacity: 0.35,
//                            map: map,
//                            center: marker.position,
//                            radius: 100
//                        });
//                        noiseMarkersArray[i] = {marker: marker, circle: cityCircle};
//                    }
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
                              waqiMapOverlay  =  new  google.maps.ImageMapType({
                              getTileUrl:  function(coord,  zoom)  {
                                        return  'https://tiles.waqi.info/tiles/usepa-aqi/'  +  zoom  +  "/"  +  coord.x  +  "/"  +  coord.y  +  ".png?token=_TOKEN_ID_";
                              },
                              name:  "Air  Quality",
                            });

                            map.overlayMapTypes.insertAt(0,waqiMapOverlay);
//                            pollutionLayerIasi.addGeoJson(iasiPollution);
//                            pollutionLayerIasi.setStyle(function(feature) {
//                                return ({
//                                    fillColor: feature.getProperty('color'),
//                                    strokeWeight: 1,
//                                    strokeColor: feature.getProperty('strokeColor'),
//                                    fillOpacity: feature.getProperty('fillOpacity')
//                                });
//                            });
//                            pollutionLayerIasi.setMap(map);
//                            pollutionLayerBucuresti.addGeoJson(bucurestiPollution);
//                            pollutionLayerBucuresti.setStyle(function(feature) {
//                                return ({
//                                    fillColor: feature.getProperty('color'),
//                                    strokeWeight: 1,
//                                    strokeColor: feature.getProperty('strokeColor'),
//                                    fillOpacity: feature.getProperty('fillOpacity')
//                                });
//                            });
//                            pollutionLayerBucuresti.setMap(map);
//                            pollutionLayerNewYork.addGeoJson(newyorkPollution);
//                            pollutionLayerNewYork.setStyle(function(feature) {
//                                return ({
//                                    fillColor: feature.getProperty('color'),
//                                    strokeWeight: 1,
//                                    strokeColor: feature.getProperty('strokeColor'),
//                                    fillOpacity: feature.getProperty('fillOpacity')
//                                });
//                            });
//                            pollutionLayerNewYork.setMap(map);
//                            pollutionLayerLondon.addGeoJson(LondonPollution);
//                            pollutionLayerLondon.setStyle(function(feature) {
//                                return ({
//                                    fillColor: feature.getProperty('color'),
//                                    strokeWeight: 0.5,
//                                    strokeColor: feature.getProperty('strokeColor'),
//                                    fillOpacity: feature.getProperty('fillOpacity')
//                                });
//                            });
//                            pollutionLayerLondon.setMap(map);

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

/* This function is a callback and creates an array of markers from the result given. */
function callback(results, status) {
    if (status === google.maps.places.PlacesServiceStatus.OK) {
        for (var i = 0; i < results.length; i++) {
            createMarker(results[i]);
        }
    }
}

/* This function creates a marker
 - place: the location on the map to place the marker
 */
function createMarker(place) {
    var placeLoc = place.geometry.location;
    var marker = new google.maps.Marker({
        map: map,
        position: place.geometry.location
    });
    schoolsMarkersArray.push(marker);
}

/* This function checks which layer is checked. */
function addLayerOnPadding() {
    var selectLayer = document.getElementById('layer');
    var layer_id = selectLayer.options[selectLayer.selectedIndex].value;
}

/* This function redirects to estateDetails.html page when clicking "See details" button of announcement. */
function redirectToEstate(estateId){
    window.location = "./estateDetails.html?estate=" + estateId;
}


$( "marker" ).mouseup(function() {
    $(this).hide();
});


var modalLoginSearchCity = document.getElementById('loginButton');
var modalSignupSearchCity = document.getElementById('signupButton');
var modalSignedId = document.getElementById('signInStatus');

/* An event listener when clicking outide modals
 - modalSignUp
 - modalLogin
 - signInStatus
 */
window.onclick = function(event) {
    if(event.target == modalSignupSearchCity ){
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
                    $("#email").val("");
                    $("#passwordSignUp").val("");
                    $("#first-name").val("");
                    $("#last-name").val("");

                } else {
                    if (event.target ===  document.getElementById('logInStatus')){
                        document.getElementById('logInStatus').style.display='none';
                    } else {
                        if (event.target === document.getElementById('logInStatusFailed')) {
                             document.getElementById('logInStatusFailed').style.display='none';
                        }
                    }
                }
            }
        }
    }
}

function verifyLoginDataSearchCity(loginData) {
    console.log("In verifyData() " + JSON.stringify(loginData) );
    $.ajax({
        method: 'POST',
        url: '/verify/user',
        contentType: false,
        data: JSON.stringify(loginData),
        contentType: 'application/json',
        success: function(data, textStatus, xhr) {
                console.log('Status code ' + xhr.status + "data: " + data);
                document.getElementById('loginButton').style.display='none';
                document.getElementById('logInStatus').style.display='block';
                // document.getElementById('login-searchCity').style.display = 'none';
                // document.getElementById('signup-searchCity').style.display = 'none';
                // document.getElementById('logout-searchCity').style.display = 'block';
                checkSession();
                // $('#loggedInButton-searchCity').text(data);
                // document.getElementById('loggedIn-searchCity').style.display='block';
                checkSession();
                getEstatesByFilter();
        },
        error: function (xhr, ajaxOptions, thrownError,textStatus) {
            console.log('error Status code ' + xhr.status + ' ' + xhr.responseText);
            document.getElementById('logInStatus').style.display='none';
            document.getElementById('logInStatusFailed').style.display='block';
            var errors = xhr.responseText.split(';');
            for (var i = 0; i < errors.length; i ++) {
                if (errors[i] === "Invalid email") {
                    $('#email-input-login').addClass('error');
                    $('#email-login').attr("placeholder","Insert email");
                    $('#errorMessage').text("Insert email");
                    $('#errorMessageContainer').css("display", "block");
                }

                if (errors[i] === "Invalid password") {
                    $('#password-input-login').addClass('error');
                    $('#password-login').attr("placeholder","Insert password");
                    $('#errorMessage').text("Insert password");
                    $('#errorMessageContainer').css("display", "block");
                }

                if (errors[i] === "EMAIL NOT FOUND") {
                    $('#email-input-login').addClass('error');
                    $('#email-login').attr("placeholder","Insert existent email");
                    $('#errorMessage').text("Insert existent email");
                    $('#errorMessageContainer').css("display", "block");
                }
            }
        }
    });
}

/* POST request to server for user login.  */
function logInPOST() {
    $('#password-input-login').removeClass('error');
    $('#email-input-login').removeClass('error');
    console.log('logInPOST');
    var url = "/verify/user";
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
        verifyLoginDataSearchCity(postData);
        document.getElementById('loginButton').style.display='none';
        $("#email-login").val("");
        $("#password-login").val("");
    }
}

/* POST request to server for user sign up.  */
function signUpInSearchCity () {
    $('#password-input').removeClass('error');
    $('#first-name-input').removeClass('error');
    $('#last-name-input').removeClass('error');
    $('#email-input').removeClass('error');
    var firstNameInput = document.getElementById('first-name');
    var firstName = firstNameInput.value;
    var lastNameInput = document.getElementById('last-name');
    var lastName = lastNameInput.value;
    var passInput = document.getElementById('passwordSignUp');
    var password = passInput.value;
    var emailInput = document.getElementById('email');
    var email = emailInput.value;
    var postData = {
        "firstName": firstName,
        "lastName" : lastName,
        "password" : password,
        "email" : email
    };
    $.ajax ({
        method: 'POST',
        url: '/create/user',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(postData),
        success(data){
            document.getElementById('signupButton').style.display='none';
            document.getElementById('signInStatus').style.display='block';
            document.getElementById('signupButton').style.display='none';
            $("#email").val("");
            $("#passwordSignUp").val("");
            $("#first-name").val("");
            $("#last-name").val("");
        },
        error: function (xhr, ajaxOptions, thrownError, textStatus) {
            console.log('error Status code ' + xhr.status);
            console.log('Text status: ' + xhr.responseText);
            if (xhr.responseText === "DUPLICATE") {
                document.getElementById('signupButton').style.display='none';
                document.getElementById('signInStatusFailed').style.display='block';

            }
            if (xhr.responseText === "Added in database") {
                document.getElementById('signUpEstateDetails').style.display='none';
                document.getElementById('signInStatus-EstateDetails').style.display='block';
                document.getElementById('signUpEstateDetails').style.display='none';
                $("#email-EstateDetails").val("");
                $("#passwordSignUp-EstateDetails").val("");
                $("#first-name-EstateDetails").val("");
                $("#last-name-EstateDetails").val("");
            }

            var errors = xhr.responseText.split(';');
            for (var i = 0; i < errors.length; i ++) {
                if (errors[i] === "Invalid email format") {
                    $('#email-input').addClass('error');
                    $('#email').attr("placeholder","Insert email");
                    $('#errorMessage').text("Insert email");
                    $('#errorMessageContainer').css("display", "block");
                }

                if (errors[i] === "Invalid lastName") {
                    $('#last-name-input').addClass('error');
                    $('#last-name').attr("placeholder","Insert last name");
                    $('#errorMessage').text("Insert last name");
                    $('#errorMessageContainer').css("display", "block");
                }

                if (errors[i] === "Invalid firstName") {
                    $('#first-name-input').addClass('error');
                    $('#first-name').attr("placeholder","Insert first name");
                    $('#errorMessage').text("Insert first name");
                    $('#errorMessageContainer').css("display", "block");
                }

                if (errors[i] === "Invalid password length") {
                    $('#password-input').addClass('error');
                    $('#passwordSignUp').attr("placeholder","Insert valid password");
                    $('#errorMessage').text("Insert password");
                    $('#errorMessageContainer').css("display", "block");
                }
            }
        }
    });
}

/* An event trigger/listener for button "Submit" from login. */
$('#loginButtonSubmit').click(function(event){
    event.preventDefault();
    var inpObj = document.getElementById('password');
    if (inpObj.checkValidity() == false) {
        document.getElementById("passwordContainerLogin").innerHTML = inpObj.validationMessage;
    }
    var requestLogin = new XMLHttpRequest();
    document.getElementById('loginButton').style.display='none';

});
