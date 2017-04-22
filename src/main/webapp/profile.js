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

$(function(){
      var stickyHeaderTop = $('#topProfilePage').offset().top;
      $(window).scroll(function(){
                  if( $(window).scrollTop() > stickyHeaderTop ) {
                          $('#topProfilePage').css({position: 'fixed', top: '0px'});
                          $('#topProfilePage').css('width','100%');
                          $('#topProfilePage').css('display', 'block');
                          $('#topProfilePage').css('z-index','999999');
                  } else {
                          $('#topProfilePage').css({position: 'static', top: '0px'});
                  }
          });
});

var currentDiv;
for (i = 0; i < estates.length; i ++) {
    currentDiv = $("<div id='"+ estates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:1%;margin-left:2%;'><div class='content'> <img class='right floated tiny ui image' src='" + estates[i].photo+"/profile.jpg' style='width:120px;'><div class='header'>" + estates[i].name + "</div><div class='meta'>" + estates[i].city + "</div><div class='description'>" + estates[i].description + "</div></div><div class='extra content'><div class='ui two buttons'><input type='hidden' name='estate' value='" + estates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + estates[i].id + ")'>Update</button><form method='get' action='estateDetails.html?estate=" + estates[i].id  +"' ><button class='ui basic black button' type='submit'>Delete</button></form></div></div></div></div></div>");
    $("#estatesProfile").append(currentDiv);
}

function updateEstate(id) {
    $('#updateAnnouncement').css("display", "block");
    $('#announcements').css("display", "none");

}