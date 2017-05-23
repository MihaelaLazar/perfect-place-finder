/* This function changes the appearance of every section when clicking button:
    - update users section
    - admin's account settings
    - announcements
    - messages
*/
function getFieldAdminProfile(id) {
    $('#updateAnnouncement').css("display", "none");
    if (id === 'users') {
        $('#users').css("display", "block");
        $('#accountSettings').css("display", "none");
        $('#announcements').css("display", "none");
        $('#messages').css("display", "none");
    }

    if (id === 'announcements') {
        $('#users').css("display", "none");
        $('#accountSettings').css("display", "none");
        $('#announcements').css("display", "block");
        $('#messages').css("display", "none");
    }

    if (id === 'settings') {
        $('#users').css("display", "none");
        $('#accountSettings').css("display", "block");
        $('#announcements').css("display", "none");
        $('#messages').css("display", "none");
    }

    if (id === 'messages') {
        $('#users').css("display", "none");
        $('#accountSettings').css("display", "none");
        $('#announcements').css("display", "none");
        $('#messages').css("display", "block");
    }
}


function redirectToSearchCity () {
    $.ajax ({
         method: 'GET',
         url: '/user/logout',
         contentType: false,
         success (data) {
            window.location = "./searchCity.html";
         }
     });
}

window.onload = function() {
    $.ajax({
        method: 'GET',
        url: '/user/getAll',
        contentType: false,
        success(data) {
            console.log(data);
            for (var i = 0; i < data.length; i ++) {
                var table  = document.getElementById("users-table");
                var row = table.insertRow(table.rows.length);
                var idCell = row.insertCell(0);
                idCell.innerHTML = data[i].id;
                var emailCell = row.insertCell(1);
                emailCell.innerHTML = data[i].email;
                var firstNameCell = row.insertCell(2);
                firstNameCell.innerHTML = data[i].firstname;
                var lastNameCell = row.insertCell(3);
                lastNameCell.innerHTML = data[i].lastname;
                var deleteUserButton = row.insertCell(4);
                deleteUserButton.innerHTML = "<button class='ui inverted blue fluid button' onclick='deleteUserAccount("+ data[i].id +", " + i +")'>  <i class='remove user icon'></i> Delete user account</button>";
            }
        }
    });

    $.ajax ({
        method: 'GET',
        url: '/estate/getAllAnnouncements',
        contentType: false,
        success (data) {
            console.log(data);
            var userEstates = data;
            var currentDiv;
            var favoriteAnnouncement;
            for (var i = 0; i < userEstates.length; i ++){
                if (userEstates[i].estateAttachements.length > 0) {
                    var shortDescription = userEstates[i].description.substr(1, 70);
                    currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + userEstates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><button class='ui basic black button' type='submit' onclick='deleteEstate(" + userEstates[i].id + ")'>Delete</button></div></div><div class='two wide column'></div></div></div></div></div></div>");
                    favoriteAnnouncement = $("<div class='item' id='"+ userEstates[i].id +"'><div class='ui small image'> <img src='"+ userEstates[i].estateAttachements[0].iconUri + "'onclick='bounce()'></div><div class='content'><a class='header'>Header</a><div class='meta'><span>Description</span></div><div class='description'><p></p></div><div class='extra'>Additional Details</div></div></div>");
                } else {
                    currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + shortDescription + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><button class='ui basic black button' type='submit' onclick='deleteEstate(" + userEstates[i].id + ")'>Delete</button></div></div><div class='two wide column'></div></div></div></div></div></div>");
                }
                $("#announcements-list").append(currentDiv);

            }
        }
    });

    $.ajax({
            method: 'GET',
            url: '/estate/getAllMessages',
            contentType: false,
            success(data) {
                console.log(data);
                var messages = data;
                for (var index = 0; index < messages.length; index ++) {
                    var messagesPart = messages[index].text.split(".");
                    var messageLastPart;
                    var currentMessage = $("<div id='" + messages[index].id + "' class='ui card' style='width: 100%; margin-top:15px; margin-left: 15px;'> <div class='content'><div onclick='removeMessage(" + messages[index].id + "," + messages[index].idAnnouncement  + ")'><i class='right floated remove icon'></i></div> <div class='header' ><a href='/estateDetails.html?estate=" + messages[index].idAnnouncement  +"'>" + "ID " + messages[index].idAnnouncement + "</a></div> <div class='meta'>" + messages[index].createdAtToString + "</div><div class='description'><p>"+ messages[index].text +".</p> <p>" + messages[index].secondPartText + ".</p> </div></div></div> ");
                    $('#messagesList').append(currentMessage);
                }
            }
        });

}

function deleteUserAccount(id, indexRow) {

    $.ajax({
        method: 'POST',
        url: '/user/deleteAccount?id=' + id,
        contentType: false,
        success(data) {
            document.getElementById("users-table").deleteRow(indexRow + 1);
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
    $("#announcements-list").remove("#" + id);
    var elementToDelete = document.getElementById(id);
    elementToDelete.parentNode.removeChild(elementToDelete);
}

var announcementImagesArrayProfile = [];
var announcementImagesArrayCountProfile = 0;
var announcementImagesArrayProfilePathToFile = [];
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
                    var newImage = $("<div class='ui fluid image' id='" + data.imageName + "' style='margin-top:-0.5%;'><a onclick={deleteImage('" + data.imageName + "')} style='position:absolute; right:0; top:0;'> <i class=' large remove icon' ></i></a> <img src='" + data.imageURI +"'></div></div> ");
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
    for(var i = announcementImagesArrayProfile.length - 1; i >= 0; i--) {
        if(announcementImagesArrayProfile[i] === id) {
            announcementImagesArrayProfile.splice(i, 1);
            announcementImagesArrayProfilePathToFile.splice(i, 1);
//            announcementImagesArrayProfilePathToFile = announcementImagesArrayProfilePathToFile - 1;
            announcementImagesArrayProfile = announcementImagesArrayCountProfile - 1;
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