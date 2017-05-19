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
            }
        }
    });

}