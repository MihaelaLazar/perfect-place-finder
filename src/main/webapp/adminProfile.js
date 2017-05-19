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

window.onload = function() {
    $.ajax({
        method: 'GET',
        url: '/user/getAll',
        contentType: false,
        success(data) {
            console.log(data);
        }
    });

}