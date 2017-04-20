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
    if (id === 'accountSettings') {
        $('#accountSettings').css("display", "block");
        $('#messages').css("display", "none");
        $('#announcements').css("display", "none");
    } else {
        $('#accountSettings').css("display", "none");
        if (id === 'announcements') {
            $('#announcements').css("display", "block");
            $('#messages').css("display", "none");

        } else {
            $('#announcements').css("display", "none");
            if (id === 'messages') {
                $('#messages').css("display", "block");
            } else {
                $('#messages').css("display", "none");
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