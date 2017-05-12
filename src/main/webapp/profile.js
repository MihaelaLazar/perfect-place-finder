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
                 if (userEstates[i].estateAttachements. length > 0) {
                    currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + userEstates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + userEstates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><form method='get' action='estateDetails.html?estate=" + userEstates[i].id  +"' ><button class='ui basic black button' type='submit'>Delete</button></form></div></div><div class='two wide column'><button id='heart"+ userEstates[i].id +"' class='ui inverted blue button' onClick=changeLikeState('heart"+ userEstates[i].id+"')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    favoriteAnnouncement = $("<div class='item' id='"+ userEstates[i].id +"'><div class='ui small image'> <img src='"+ userEstates[i].estateAttachements[0].iconUri + "'onclick='bounce()'></div><div class='content'><a class='header'>Header</a><div class='meta'><span>Description</span></div><div class='description'><p></p></div><div class='extra'>Additional Details</div></div></div>");
                 } else {
                     currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + userEstates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><form method='get' action='estateDetails.html?estate=" + userEstates[i].id  +"' ><button class='ui basic black button' type='submit'>Delete</button></form></div></div><div class='two wide column'><button id='heart"+ userEstates[i].id +"' class='ui inverted blue button' onClick=changeLikeState('heart"+ userEstates[i].id+"')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                      favoriteAnnouncement = $("<div class='item' id='"+ userEstates[i].id +"'><div class='ui small image'> <img src='./images/house-logo-md.png'></div><div class='content'><a class='header'>Header</a><div class='meta'><span>Description</span></div><div class='description'><p></p></div><div class='extra'>Details</div></div></div>");
                 }
                 $("#estatesProfile").append(currentDiv);
                 $("#favouritesEstatesList").append(favoriteAnnouncement);
            }
         }
    });
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


/* This function display the section of updating the chosen announcement(triggered when clicking "Update" button). */
function updateEstate(id) {
    $('#updateAnnouncement').css("display", "block");
    $('#announcements').css("display", "none");


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
    var stickyHeaderProfilePage = $('#topProfilePage').offset().top;
    if( $(window).scrollTop() > stickyHeaderProfilePage ) {
        $('#topProfilePage').css({position: 'fixed', top: '0px'});
        $('#topProfilePage').css('width','100%');
        $('#topProfilePage').css('display', 'block');
        $('#topProfilePage').css('z-index','999999');
    } else {
        $('#topProfilePage').css({position: 'static', top: '0px'});
//        $('#backToTopProfilePage').css({margin-top: '10%'});
    }
    });