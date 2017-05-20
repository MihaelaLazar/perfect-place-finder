// $('a').click(function(){
//     $('html, body').animate({
//         scrollTop: $( $(this).attr('href') ).offset().top
//     }, 500);
//     return false;
// });

var globalMap11;
var isForRent = 0;
var isForSale = 0;
var isNegociable = 0;
var addressLat = 0;
var addressLng = 0;
var markersArray = [];
var city;

window.onload = function() {
    checkSessionAddProperty();
}

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
                    $('#city-chosen').val(city);
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


var hasParking;
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
    var price = priceInput.value;
    var surfaceInput = document.getElementById('usable-surface');
    var surface = surfaceInput.value;
    var yearInput = document.getElementById('construction-year');
    var year = yearInput.value;
    var furnitureInput = document.getElementById('furniture-utilities-select');
    var furniture = furnitureInput.value;
    var descriptionInput = document.getElementById('description');
    var description = descriptionInput.value;
    var nameInput = document.getElementById('name');
    var levelOfComfortSelect = document.getElementById('comfort-select');
    var levelOfComfort = levelOfComfortSelect.value;
    var phoneNumberInput = document.getElementById('phone-number');
    var phoneNumber = phoneNumberInput.value;
    var divisionInput = document.getElementById('division-select');
    var division = divisionInput.value;
    var bathroomsInput = document.getElementById('bathrooms');
    var bathrooms = bathroomsInput.value;
    var floorInput = document.getElementById('floor');
    var floor = floorInput.value;
    var rent_price;
    var buy_price;

    var cityChosen = document.getElementById('city-chosen').value;
    if (cityChosen === "") {
        $('#error-city').css("display", "block");
    }
    if (division === "" && category !== "space") {
        isValidLogin = false;
        $('#error-division').css("display", "block");
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
    if (levelOfComfort === "" && category !== "space" ) {
        $('#error-level-on-comfort').css("display", "block");
        isValidLogin = false;
    }
    if (bathrooms === "" && category !== "space") {
        $('#error-bathrooms').css("display", "block");
        isValidLogin = false;
    }
    if (category === "") {
        $('#error-category').css("display", "block");
        isValidLogin = false;
    }
    if (rooms === "" && category !== "space") {
        $('#error-rooms').css("display", "block");
        isValidLogin = false;
    }
    if (price === "") {
        $('#error-price').css("display", "block");
        isValidLogin = false;
    }
    if (surface === "") {
        $('#error-surface').css("display", "block");
        isValidLogin = false;
    }
    if (year === "" && category !== "space") {
        $('#error-year').css("display", "block");
        isValidLogin = false;
    }
    if (furniture === "" && category !== "space") {
        $('#error-utilities').css("display", "block");
        isValidLogin = false;
    }
    if (description === "") {
        $('#error-description').css("display", "block");
        isValidLogin = false;
    }
    if (phoneNumber === "") {
        $('#error-phone').css("display", "block");
        isValidLogin = false;
    }
    if (category === "appartment" && floor === ""){
        $('#error-floor').css("display", "block");
        isValidLogin = false;
    }
    if (category === "space"){
        rooms = 0;
        year = 0;
        division = " ";
        furniture = 0;
        levelOfComfort = 0;
        bathrooms = 0;
        hasParking = 0;
        floor = -1;
    }
    if (isValidLogin === true ){
        if(isForRent === 1){
            rent_price = price;
            buy_price = 0;
        }
        if(isForSale===1){
            buy_price = price;
            rent_price = 0;
        }
        console.log("buy price: " + buy_price + ", rent price: " + rent_price);
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
            "contactNumber" : phoneNumber,
            "levelOfComfort" : levelOfComfort,
            "bathrooms" : bathrooms,
            "carDisposal" : hasParking,
            "floor" : floor,
            "announcementImagesArray": announcementImagesArray,
            "announcementImagesIconsURIArray" : announcementImagesIconsURIArray
        };
        $.ajax({
                method: 'POST',
                url: '/estate/add/property',
                contentType: false,
                data: JSON.stringify(postDataProperty),
                contentType: 'application/json',
                success: function(data, textStatus, xhr) {
                    document.getElementById('addPropertyFailedModal').style.display='block';
                    $('#error-city').css("display", "none");
                    $('#error-division').css("display", "none");
                    $('#error-level-on-comfort').css("display", "none");
                    $('#error-bathrooms').css("display", "none");
                    $('#error-category').css("display", "none");
                    $('#error-rooms').css("display", "none");
                    $('#error-price').css("display", "none");
                    $('#error-surface').css("display", "none");
                    $('#error-year').css("display", "none");
                    $('#error-utilities').css("display", "none");
                    $('#error-description').css("display", "none");
                    $('#error-phone').css("display", "none");
                    $('#error-floor').css("display", "none");
                },
                error: function (xhr, ajaxOptions, thrownError,textStatus) {
                    var errors = xhr.responseText;
                    console.log('status:::: ' + xhr.status);
                    if (xhr.status === 403 || xhr.status === 409) {
                        $('#addPropertyFailedModal').css("display", "block");
                    } else {
                        document.getElementById('addPropertySuccessfulModal').style.display='block';
                    }
                }
         });

    }
}

var modalPropertyFailed = document.getElementById('addPropertyFailedModal');
var modalPropertySuccess = document.getElementById('addPropertySuccessfulModal');


function displayOtherFields(){
    var categoryInput = document.getElementById('category-select');
    var category = categoryInput.value;
    if (category === "space") {
        document.getElementById('comfort-select-field').style.visibility = 'hidden';
        document.getElementById('bathrooms-input').style.visibility = 'hidden';
        document.getElementById('rooms-field').style.visibility = 'hidden';
        document.getElementById('floor-input').style.visibility = 'hidden';
        document.getElementById('year-and-car').style.visibility = 'hidden';
        document.getElementById('utilities-and-division').style.visibility = 'hidden';
    }

    if (category === "house") {
        document.getElementById('comfort-select-field').style.visibility = 'visible';
        document.getElementById('bathrooms-input').style.visibility = 'visible';
        document.getElementById('rooms-field').style.visibility = 'visible';
        document.getElementById('floor-input').style.visibility = 'hidden';
        document.getElementById('year-and-car').style.visibility = 'visible';
        document.getElementById('utilities-and-division').style.visibility = 'visible';
    }

    if (category === "appartment") {
        document.getElementById('comfort-select-field').style.visibility = 'visible';
        document.getElementById('bathrooms-input').style.visibility = 'visible';
        document.getElementById('rooms-field').style.visibility = 'visible';
        document.getElementById('floor-input').style.visibility = 'visible';
        document.getElementById('year-and-car').style.visibility = 'visible';
        document.getElementById('utilities-and-division').style.visibility = 'visible';
    }
}

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
    if(document.getElementById(id).checked) {
        if(id ==="transaction-type-sale-input")
            isForSale = 1;
        if(id ==="transaction-type-rent-input")
            isForRent = 1;
        if (id === "parking") {
            hasParking = 1;
        }
        if (id === "garage") {
            hasParking = 2;
        }
        if (id === "none") {
            hasParking = 0;
        }
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

var announcementImagesArray = [];
var announcementImagesIconsURIArray = [];
var announcementImagesArrayCount = 0;
/**
 * This function adds images to current announcement
*/
$('#file1').change(function() {
    if (this.files && this.files[0] && this.files[0].name.match(/\.(jpg|jpeg|png|JPG|JPEG)$/) ) {
        if(this.files[0].size>1048576) {
            console.log('File size is larger than 1MB!');
        } else {
            var reader = new FileReader();
            reader.onload = function (e){
                result = e.target.result;
                $('#image1').attr('src', result);
            }
            reader.readAsDataURL(this.files[0]);
            var frm = new FormData();
            frm.append('image', this.files[0]);
            $.ajax({
                method: 'POST',
                url: '/estate/save/image',
                data: frm,
                contentType: false,
                enctype: 'multipart/form-data',
                processData: false,
                cache: false,
                success : function(data) {
                    $('#image1').attr('src','/images/image.png');
                    console.log("image added: " + data.imageName);
                    var newImage = $("<div class='ui fluid image' id='" + data.imageName + "' style='margin-top:-0.5%;'><a onclick={deleteImage('" + data.imageName + "')} style='position:absolute; right:0; top:0;'> <i class=' large remove icon' ></i></a> <img src='" + data.imageURI +"'></div></div> ");
                    $('#list-of-images').append(newImage);
                    announcementImagesArray[announcementImagesArrayCount] = data.imageName;
                    announcementImagesIconsURIArray[announcementImagesArrayCount] = data.imageURI.toString();
                    announcementImagesArrayCount = announcementImagesArrayCount + 1;
                }
            });
        }
    } else console.log('This is not an image file!');
});

function redirectToSearchCity() {
     $.ajax ({
         method: 'GET',
         url: '/user/logout',
         contentType: false,
         success (data) {
            window.location = "./searchCity.html";
         }
     });
}


function checkSessionAddProperty() {
    var request = new XMLHttpRequest();
    request.open('GET', '/user/check/session', false);  // `false` makes the request synchronous
    request.send(" ");
    var data = request.responseText;
    if (request.status === 200) {
        console.log("session existent");
        console.log();
        document.getElementById('logoutAddProperty').style.display = 'block';
        document.getElementById('loggedIn-addProperty').style.display = 'block';
        $('#loggedInButton-addProperty').text(data);
    } else {
        console.log("not existent session");

        document.getElementById('logoutAddProperty').style.display = 'none';
        document.getElementById('loggedIn-addProperty').style.display = 'none';
    }
}


/* This function redirects tu user's profile page */
function redirectToProfileAddProperty() {
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