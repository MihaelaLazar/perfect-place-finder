window.onload = function () {
    $.ajax ({
         method: 'GET',
         url: '/user/getAnnouncements',
         contentType: false,
         success (data) {
            console.log(data);
            var userEstates = data;
            var currentDiv;
            var favoriteAnnouncement;
            for (var i = 0; i < userEstates.length; i ++){
                var shortDescription = userEstates[i].description;
                if (userEstates[i].description.length > 70) {
                        shortDescription = userEstates[i].description.substr(0, 70);
                }
                 if (userEstates[i].estateAttachements.length > 0) {
                    currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + userEstates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><button class='ui basic black button' type='submit' onclick='deleteEstate(" + userEstates[i].id + ")'>Delete</button></div></div><div class='two wide column'> </div></div></div></div></div></div>");
                    favoriteAnnouncement = $("<div class='item' id='"+ userEstates[i].id +"'><div class='ui small image'> <img src='"+ userEstates[i].estateAttachements[0].iconUri + "'onclick='bounce()'></div><div class='content'><a class='header'>Header</a><div class='meta'><span>Description</span></div><div class='description'><p></p></div><div class='extra'>Additional Details</div></div></div>");
                 } else {
                     currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><button class='ui basic black button' type='submit' onclick='deleteEstate(" + userEstates[i].id + ")'>Delete</button></div></div><div class='two wide column'></div></div></div></div></div></div>");
                 }
                 $("#estatesProfile").append(currentDiv);

            }
         }
    });

    $.ajax({
        method: 'GET',
        url: '/user/get/messages',
        contentType: false,
        success(data) {
            console.log(data);
            var messages = data;
            for (var index = 0; index < messages.length; index ++) {
                var messagesPart = messages[index].text.split(".");
                var messageLastPart;
                var currentMessage = $("<div id='" + messages[index].id + "' class='ui card' style='width: 100%; margin-top:15px; margin-left: 15px;'> <div class='content'><div onclick='removeMessage(" + messages[index].id + "," + messages[index].idAnnouncement  + ")'><i class='right floated remove icon'></i></div> <div class='header' ><a href='/estateDetails.html?estate=" + messages[index].idAnnouncement  +"'>" + "ID " + messages[index].idAnnouncement + "</a></div> <div class='meta'>" + messages[index].createdAtToString + "</div><div class='description'><p>"+ messages[index].text +".</p> <p>" + messages[index].secondPartText + ".</p> </div></div></div> ");
                $('#userMessages').append(currentMessage);
            }
        }
    });

    $.ajax({
        method: 'GET',
        url: '/user/get/favoriteAnnouncements',
        contentType: false,
        success(data) {
            var favoriteAnnouncements = data;
            for (var index = 0; index < favoriteAnnouncements.length; index ++) {
                var currentAnnouncement;
                var price;
                if (favoriteAnnouncements[index].buyPrice > 0) {
                    price = favoriteAnnouncements[index].buyPrice;
                } else {
                    price = favoriteAnnouncements[index].rentPrice + "/month";
                }
                if (favoriteAnnouncements[index].estateAttachements.length > 0) {
                      currentAnnouncement = $("<div class='item' id='"+ favoriteAnnouncements[index].id +"'><div onclick='removeFavoriteAnnouncement(" + favoriteAnnouncements[index].id + ")'><i class='right floated remove icon'></i></div><div class='ui small image'> <img src='" + favoriteAnnouncements[index].estateAttachements[0].iconUri + "'></div><div class='content'><a class='header' href='/estateDetails.html?estate=" + favoriteAnnouncements[index].id  +"'>" + favoriteAnnouncements[index].typeOfTransaction + " " + favoriteAnnouncements[index].rooms + " room/s " + favoriteAnnouncements[index].type +  "</a> <div class='meta'><br><span>" + favoriteAnnouncements[index].description + "</span></div><div class='description'><p></p></div><div class='extra'><br><strong>$ " + price + "</strong></div></div></div>");
                } else {
                      currentAnnouncement = $("<div class='item' id='"+ favoriteAnnouncements[index].id +"'><div onclick='removeFavoriteAnnouncement(" + favoriteAnnouncements[index].id + ")'><i class='right floated remove icon'></div></i><div class='ui small image'> <img src='./images/house-logo-md.png'></div><div class='content'><a class='header' href='/estateDetails.html?estate=" + favoriteAnnouncements[index].id  +"'>" + favoriteAnnouncements[index].typeOfTransaction + " " + favoriteAnnouncements[index].rooms + " room/s " + favoriteAnnouncements[index].type + "</a><div class='meta'><br><span>" + favoriteAnnouncements[index].description + "</span></div><div class='description'><p></p></div><div class='extra'><br><strong>$ " + price + "</strong></div></div></div>");
                }
                $("#favouritesEstatesList").append(currentAnnouncement);
            }
        },
        error: function (xhr, ajaxOptions, thrownError,textStatus) {
            console.log('error Status code ' + xhr.status);
            console.log('Text status: ' + textStatus);
        }
    });

    $.ajax({
        method: 'GET',
        url: '/user/get/profileAccount',
        contentType: false,
        success (data){
            console.log(data);
            $('#first-name-homePage').val( data.firstname);
            $('#last-name-homePage').val(data.lastname);
            $('#email-homePage').val(data.email);
        },
        error: function (xhr, ajaxOptions, thrownError,textStatus) {
            console.log('error Status code ' + xhr.status);
            console.log('Text status: ' + textStatus);
        }
    });

}

window.onclick = function(event) {
    if(event.target === document.getElementById('updated-profile-modal-success') ) {
       document.getElementById('updated-profile-modal-success').style.display = "none";
    } else {
        if(event.target === document.getElementById('updated-estate-modal-success') ) {
                    document.getElementById('updated-estate-modal-success').style.display = "none";
            } else {
                if(event.target === document.getElementById('updated-profile-modal-error') ) {
                            document.getElementById('updated-profile-modal-error').style.display = "none";
                    } else {
                        if(event.target === document.getElementById('updated-password-modal-success') ) {
                                    document.getElementById('updated-password-modal-success').style.display = "none";
                            } else {
                                if(event.target === document.getElementById('updated-password-modal-error') ) {
                                            document.getElementById('updated-password-modal-error').style.display = "none";
                                    } else {
                                        if(event.target === document.getElementById('updated-password-modal-error-not-equal') ) {
                                                    document.getElementById('updated-password-modal-error-not-equal').style.display = "none";
                                            } else {
                                                if(event.target === document.getElementById('updated-estate-modal-error') ) {
                                                        document.getElementById('updated-estate-modal-error').style.display = "none";
                                                } else {
                                                    if(event.target === document.getElementById('image-size') ) {
                                                            document.getElementById('image-size').style.display = "none";
                                                    }
                                                }
                                            }
                                    }
                            }
                    }
            }
    }
}

function updateUserProfile() {
    var userEmail = document.getElementById('newEmailSignUp-homePage').value;
    console.log("USER EMAIL " + userEmail);
    var updateUserData = {
      "email" : userEmail,
      "firstName": document.getElementById('first-name-homePage').value,
      "lastName": document.getElementById('last-name-homePage').value,
      "idUser": "0"
    };

    $.ajax({
         method: 'POST',
         url : '/user/update/profile',
         contentType: 'application/json;charset=UTF-8',
         data: JSON.stringify(updateUserData),
         success (data) {
            $('#email-homePage').val(document.getElementById('newEmailSignUp-homePage').value);
            $('#newEmailSignUp-homePage').val("");
         },
         error : function (xhr, ajaxOptions, thrownError,textStatus) {
             console.log('error Status code ' + xhr.status);
             console.log('Text status: ' + xhr.responseText);
             if (xhr.responseText === "email_null" || xhr.responseText === "email_invalid") {
                     $('#newEmailSignUp-homePage').val("Insert valid email");
             }
             if (xhr.responseText === "Update could not be completed.") {
                    document.getElementById('updated-profile-modal-error').style.display = "block";
             }
             if (xhr.status === 202) {
                    document.getElementById('updated-profile-modal-success').style.display = "block";
             }
         }
    });
}

function updatePassword() {
    var newPassword = document.getElementById("new-password").value;
    var userPasswordConfirmed = document.getElementById("new-password-confirmed").value;

    var updateUserData = {
        "newPassword" : newPassword,
        "userPasswordConfirmed": userPasswordConfirmed
    };
    $.ajax({
         method: 'POST',
         url : '/user/update/password',
         contentType: 'application/json;charset=UTF-8',
         data: JSON.stringify(updateUserData),
         success (data) {
            $('#new-password').val("");
            $('#new-password-confirmed').val("");
            document.getElementById("updated-password-modal-success").style.display = "block";

         },
         error : function (xhr, ajaxOptions, thrownError,textStatus) {
             console.log('error Status code ' + xhr.status);
             console.log('Text status: ' + xhr.responseText);
             if (xhr.responseText === "password_null") {
                   $('#new-password').val("Insert password");
             }
             if (xhr.responseText === "password_confirmed_null") {
                   $('#new-password-confirmed').val("Insert password");
             }
             if (xhr.responseText === "password_confirmed_not_equal") {
                    document.getElementById("updated-password-modal-error-not-equal").style.display = "block";
             }
             if (xhr.responseText === "Update password could not be completed.") {
                 document.getElementById("updated-password-modal-error-not-equal").style.display = "block";
             }
         }
    });
}

/* This function removes the message with the given id. */
function removeMessage(id, idAnnouncement) {
    var messageToDelete = {
        "idMessage" : id,
        "idAnnouncement": idAnnouncement
    };
    $.ajax({
            method: 'POST',
            url: '/estate/delete/message',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(messageToDelete),
            success(data) {
                $('#userMessages').remove("#" + id);
                var elementToDelete = document.getElementById(id);
                elementToDelete.parentNode.removeChild(elementToDelete);
            }
    });
}

/* This function removes the saved announcement with the given id. */
function removeFavoriteAnnouncement(id) {
    $.ajax({
            method: 'POST',
            url: '/user/delete/favoriteAnnouncement?idAnnouncement=' + id,
            contentType: false,
            success(data) {
                $('#favouritesEstatesList').remove("#" + id);
                var elementToDelete = document.getElementById(id);
                elementToDelete.parentNode.removeChild(elementToDelete);
                $("#" + id).remove();
            }
    });
}

/* This function deletes the announcement with given id. */
function deleteEstate(id) {
    console.log('ID estate to delete: ' + id)
    $.ajax ({
        method: 'POST',
        url: '/estate/delete/property',
        contentType: 'application/json;charset=UTF-8',
        data: id.toString(),
        success (data) {
            console.log(data);
        },
        error: function (xhr, ajaxOptions, thrownError,textStatus) {
            console.log('error Status code ' + xhr.status);
            console.log('Text status: ' + textStatus);
        }
    });
    $("#estatesProfile").remove("#" + id);
    var elementToDelete = document.getElementById(id);
    elementToDelete.parentNode.removeChild(elementToDelete);
}

/* This function changes the tab in the section of user's account settings (from change password to change username and viceversa).*/
function changeTab() {
    if($('#firstItem').hasClass('active') === false ){
        $('#firstItem').addClass('active');
        $('#secondItem').removeClass('active');
        $('#secondTabSegment').removeClass('active');
        $('#firstTabSegment').addClass('active');
    } else {
        if($('#secondItem').hasClass('active') === false ){
            $('#secondItem').addClass('active');
            $('#firstItem').removeClass('active');
            $('#firstTabSegment').removeClass('active');
            $('#secondTabSegment').addClass('active')
        }
    }
}

/* This function changes the appearance of every section when clicking button:
    - update user's announcements section
    - user's account settings
    - user's announcements
    - user's messages
*/
function getField(id) {
    $('#updateAnnouncement').css("display", "none");
    if (id === 'accountSettings') {
        $('#accountSettings').css("display", "block");
        $('#messages').css("display", "none");
        $('#announcements').css("display", "none");
        $('#favouritesEstates').css("display", "none");
    } else {
        $('#accountSettings').css("display", "none");
        if (id === 'announcements') {
            $("#estatesProfile").empty();
            $.ajax ({
                     method: 'GET',
                     url: '/user/getAnnouncements',
                     contentType: false,
                     success (data) {
                        console.log(data);
                        var userEstates = data;
                        var currentDiv;
                        var favoriteAnnouncement;
                        for (var i = 0; i < userEstates.length; i ++){
                            var shortDescription = userEstates[i].description;
                            if (userEstates[i].description.length > 70) {
                                    shortDescription = userEstates[i].description.substr(1, 70);
                            }
                             if (userEstates[i].estateAttachements.length > 0) {
                                currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + userEstates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><button class='ui basic black button' type='submit' onclick='deleteEstate(" + userEstates[i].id + ")'>Delete</button></div></div><div class='two wide column'> </div></div></div></div></div></div>");
                                favoriteAnnouncement = $("<div class='item' id='"+ userEstates[i].id +"'><div class='ui small image'> <img src='"+ userEstates[i].estateAttachements[0].iconUri + "'onclick='bounce()'></div><div class='content'><a class='header'>Header</a><div class='meta'><span>Description</span></div><div class='description'><p></p></div><div class='extra'>Additional Details</div></div></div>");
                             } else {
                                 currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><button class='ui basic black button' type='submit' onclick='deleteEstate(" + userEstates[i].id + ")'>Delete</button></div></div><div class='two wide column'></div></div></div></div></div></div>");
                             }
                             $("#estatesProfile").append(currentDiv);

                        }
                     }
                });
            $('#announcements').css("display", "block");
            $('#messages').css("display", "none");
            $('#favouritesEstates').css("display", "none");

        } else {
            $('#announcements').css("display", "none");
            if (id === 'messages') {
                $('#messages').css("display", "block");
                $('#favouritesEstates').css("display", "none");
            } else {
                $('#messages').css("display", "none");
                if (id === 'favouritesEstates') {
                      $('#favouritesEstates').css("display", "block");
                }
            }
        }
    }

}

var categoryProfile;
var estateIdProfile;
var idUser;
var isForSaleProfile = 0;
var isForRentProfile = 0;
var hasParkingProfile = -1;
var announcementImagesArrayProfile = [];
var announcementImagesArrayProfilePathToFile = [];
var announcementImagesArrayCountProfile = 0;

/* This function display the section of updating the chosen announcement(triggered when clicking "Update" button). */
function updateEstate(id) {
    $('#updateAnnouncement').css("display", "block");
    $('#announcements').css("display", "none");
    var url = '/estate/getDetails?id=' + id;
    $.ajax ({
        method: 'GET',
        url: url,
        processData: false,
        cache: false,
        enctype: 'multipart/form-data',
        success : function(data) {
            var estateDetails = data;
            console.log(estateDetails);
            if (estateDetails.buyPrice !== 0){
                $('#price-input-profile').val(estateDetails.buyPrice);
                $('#transaction-type-sale-input-profile').prop("checked", true);
                isForSaleProfile = 1;
            } else {
                $('#price-input-profile').val(estateDetails.rentPrice);
                $('#transaction-type-rent-input-profile').prop("checked", true);
                isForRentProfile = 1;
            }
            $('#level-of-comfort-input-profile').val(estateDetails.levelOfComfort);
            $('#usable-surface-input-profile').val(estateDetails.surface);
            $('#bathrooms-input-profile').val(estateDetails.bathrooms);
            $('#rooms-input-profile').val(estateDetails.rooms);
            $('#floor-input-profile').val(estateDetails.floor);
            if (estateDetails.car === 1) {
                $('#parking-profile').prop("checked", true);
                hasParkingProfile = 1;
            } else {
                if (estateDetails.car === 2) {
                    $('#garage-profile').prop("checked", true);
                    hasParkingProfile = 2;
                } else {
                    $('#none-profile').prop("checked", true);
                }
            }
            $('#description-profile').text(estateDetails.description);
//            $('#list-of-images-profile').empty();
//            var firstImagetoAdd = $("<label for='file-profile' class='ui large image'> <img id='image-profile' src='/images/image.png'></label><input accept=''.jpg, .jpeg, .png'' multiple type='file' id='file-profile' style='display:none' >");
//            $('#list-of-images-profile').append(firstImagetoAdd);
            if (estateDetails.estateAttachements.length > 0) {
                for (var index = 0; index < estateDetails.estateAttachements.length; index ++) {
                    var imageName;
                    var indexImageName = estateDetails.estateAttachements[index].pathToFile.size -1;
                    var newImage = $("<div class='ui fluid image' id='" + estateDetails.estateAttachements[index].imageName + "' style='margin-top:-0.5%;'><a onclick={deleteImageProfile('" + estateDetails.estateAttachements[index].imageName + "')} style='position:absolute; right:0; top:0;'> <i class=' large remove icon' ></i></a> <img src='" + estateDetails.estateAttachements[index].iconUri +"'></div></div> ");
                    $('#list-of-images-profile').append(newImage);
                    announcementImagesArrayProfile[index] = estateDetails.estateAttachements[index].imageName;
                    announcementImagesArrayProfilePathToFile[index] = estateDetails.estateAttachements[index].pathToFile;
//                    announcementImagesIconsURIArrayProfile[index] = estateDetails.estateAttachements[index].iconURI;
                    console.log('DATA URI: ' + estateDetails.estateAttachements[index].iconURI);
                }
            }
            if (estateDetails.utilities !== 0) {
                if (estateDetails.utilities === 1) {
                    $('#furniture-profile').filter(function() {
                        return $(this).text() == '1';
                    }).attr('selected', true);
                } else {
                     if (estateDetails.utilities === 3) {
                        $('#furniture-profile').filter(function() {
                            return $(this).text() == '3';
                        }).attr('selected', true);
                     } else {
                           $('#furniture-profile').filter(function() {
                                return $(this).text() == '2';
                            }).attr('selected', true);
                     }

                }
            }
            announcementImagesArrayCountProfile = estateDetails.estateAttachements.length;
            categoryProfile = estateDetails.type;
            estateIdProfile = estateDetails.id;
            idUser = estateDetails.idUser;
        }
    });
}

$('#file-profile').change(function() {
    if (this.files && this.files[0] && this.files[0].name.match(/\.(jpg|jpeg|png|JPG|JPEG)$/) ) {
        if(this.files[0].size>1048576) {
            console.log('File size is larger than 1MB!');
            $('#image-size').css("display", "block");
        } else {
            var reader = new FileReader();
            reader.onload = function (e){
                result = e.target.result;
                $('#image-profile').attr('src', result);
            }
            reader.readAsDataURL(this.files[0]);
            var frm = new FormData();
            frm.append('image', this.files[0]);
            $.ajax({
                method: 'POST',
                url: '/estate/save/image',
                data: frm,
                contentType: false,
                processData: false,
                cache: false,
                success : function(data) {
                    $('#image-profile').attr('src','/images/image.png');
                    console.log("image added: " + data.iconPathname);
                    var newImage = $("<div class='ui fluid image' id='" + data.imageName + "' style='margin-top:-0.5%;'><a onclick={deleteImageProfile('" + data.imageName + "')} style='position:absolute; right:0; top:0;'> <i class=' large remove icon' ></i></a> <img src='" + data.imageURI +"'></div></div> ");
                    $('#list-of-images-profile').append(newImage);
                    announcementImagesArrayProfile[announcementImagesArrayCountProfile] = data.imageName;
                    announcementImagesArrayProfilePathToFile[announcementImagesArrayCountProfile] = data.iconPathname;
//                    announcementImagesIconsURIArrayProfile[announcementImagesArrayCountProfile] = data.imageURI.toString();
                    announcementImagesArrayCountProfile = announcementImagesArrayCountProfile + 1;
                }
            });
        }
    } else console.log('This is not an image file!');
});


function deleteImageProfile(id) {
    console.log("IMAGE TO DELETE: " + id);
    console.log("all images: " + announcementImagesArrayProfile);
    for(var i = announcementImagesArrayProfile.length - 1; i >= 0; i--) {
        if(announcementImagesArrayProfile[i] === id) {
            announcementImagesArrayProfile.splice(i, 1);
            announcementImagesArrayProfilePathToFile.splice(i, 1);
            console.log("FOUND IMAGE TO DELETE");
//            announcementImagesArrayProfilePathToFile = announcementImagesArrayProfilePathToFile - 1;
            announcementImagesArrayCountProfile = announcementImagesArrayCountProfile - 1;
            $('#list-of-images-profile').remove("#" + id);
            var imageToDelete = document.getElementById(id);
            imageToDelete.parentNode.removeChild(imageToDelete);
        }
    }
}

/* This function saves the value checked on the transaction type checkbox */
function setCheckboxProfile(id) {
    if(document.getElementById(id).checked) {
        if(id ==="transaction-type-sale-input-profile")
            isForSaleProfile = 1;
        if(id ==="transaction-type-rent-input-profile")
            isForRentProfile = 1;
        if (id === "parking-profile") {
            hasParkingProfile = 1;
        }
        if (id === "garage-profile") {
            hasParkingProfile = 2;
        }
        if (id === "none-profile") {
            hasParkingProfile = 0;
        }
    }
}

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

/* This function makes the top menu bar stay fixed when scrolling */
$(window).scroll(function(){
    if( $(window).scrollTop() > 0 ) {
        $('#topProfilePage').css({position: 'fixed', top: '0px'});
        $('#topProfilePage').css('width','100%');
        $('#topProfilePage').css('display', 'block');
        $('#topProfilePage').css('z-index','999999');
    } else {
        $('#topProfilePage').removeClass('fixed');
        $('#topProfilePage').css({position: 'static', top: '0px'});
        $('#topProfilePage').css('display', 'block');
    }
});

function updatePropertyPOST(event) {
    event.preventDefault();
    console.log('addPropertyPOST');
    var isValidUpdate = true;
    var methodUpdate = "POST";
    var urlUpdate= "/estate/update/property";
    var priceInputProfile = document.getElementById('price-input-profile');
    var priceProfile = priceInputProfile.value;
    var levelOfComfortSelect = document.getElementById('level-of-comfort-input-profile');
    var levelOfComfortProfile = levelOfComfortSelect.value;
    var rent_price_profile = 0;
    var buy_price_profile = 0;
    if(isForRentProfile === 1){
        rent_price_profile = priceProfile;
        buy_price_profile = 0;
    }
    if(isForSaleProfile === 1){
        buy_price_profile = priceProfile;
        rent_price_profile = 0;
    }
    var surfaceInputProfile = document.getElementById('usable-surface-input-profile');
    var surfaceProfile = surfaceInputProfile.value;
    var bathroomsInputProfile = document.getElementById('bathrooms-input-profile');
    var bathroomsProfile = bathroomsInputProfile.value;
    var roomsInputProfile = document.getElementById('rooms-input-profile');
    var roomsProfile = roomsInputProfile.value;
    var floorInputProfile = document.getElementById('floor-input-profile');
    var floorProfile = floorInputProfile.value;
    var descriptionInputProfile = document.getElementById('description-profile');
    var descriptionProfile = descriptionInputProfile.value;
    var furnitureInputProfile = document.getElementById('furniture-profile');
    var furnitureProfile = furnitureInputProfile.value;
    if (priceProfile === "") {
        isValidUpdate = false;
    }
    if (levelOfComfortProfile === "" && categoryProfile !== "space" ) {
        isValidUpdate = false;
    }
    if (bathroomsProfile === "" && categoryProfile !== "space") {
        console.log("bathrooms null");
        isValidUpdate = false;
    }
    if (roomsProfile === "" && categoryProfile !== "space") {
        console.log("rooms null");
        isValidUpdate = false;
    }
    if (surfaceProfile === "") {
        console.log("surface null");
        isValidUpdate = false;
    }
    if (descriptionProfile === "") {
        console.log("description null");
        categoryProfile = false;
    }
    if (categoryProfile === "appartment" && floorProfile === ""){
        isValidUpdate = false;
    }
    if (furnitureProfile === "" && categoryProfile !== "space") {
        console.log("furniture null");
        furnitureProfile = 0;
        isValidUpdate = false;
    }



    if (isValidUpdate === true) {
        var postDataPropertyUpdate = {
            "idEstate" : estateIdProfile,
            "surface" : surfaceProfile,
            "roomsNumber" : roomsProfile,
            "rentPrice" : rent_price_profile,
            "buyPrice" : buy_price_profile,
            "description" : descriptionProfile,
            "utilities" : furnitureProfile,
            "levelOfComfort" : levelOfComfortProfile,
            "bathrooms" : bathroomsProfile,
            "carDisposal" : hasParkingProfile,
            "floor" : floorProfile,
            "announcementImagesArray": announcementImagesArrayProfilePathToFile,
//            "announcementImagesIconsURIArray" : announcementImagesIconsURIArrayProfile,
            "idUser" : idUser
        };
        $.ajax ({
            method: methodUpdate,
            url: urlUpdate,
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(postDataPropertyUpdate),
            success (data) {
                console.log(data);
                document.getElementById('f').style.display='block';
                location.reload();
            },
            error: function (xhr, ajaxOptions, thrownError,textStatus) {
                console.log('error Status code ' + xhr.status);
                console.log('Text status: ' + xhr.responseText);
                if (xhr.status === 200){
                    document.getElementById('updated-estate-modal-success').style.display='block';
                    location.reload();
                }
                if (xhr.status === 403) {
                    document.getElementById('updated-estate-modal-error').style.display='block';
                }
            }
        });
    }



}
