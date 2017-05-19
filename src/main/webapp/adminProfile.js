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
                var deleteUserButton = row.insertCell(4);
                deleteUserButton.innerHTML = "<button class='ui inverted blue fluid button' onclick='deleteUserAccount("+ data[i].id +", " + i +")'> Delete user account</button>";
            }
        }
    });

    $.ajax ({
        method: 'GET',
        url: '/getAllAnnouncements',
        contentType: false,
        success (data) {
            console.log(data);
            var userEstates = data;
            var currentDiv;
            var favoriteAnnouncement;
            for (var i = 0; i < userEstates.length; i ++){
                if (userEstates[i].estateAttachements.length > 0) {
                    currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='" + userEstates[i].estateAttachements[0].iconUri+"' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + userEstates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><button class='ui basic black button' type='submit' onclick='deleteEstate(" + userEstates[i].id + ")'>Delete</button></div></div><div class='two wide column'><button id='heart"+ userEstates[i].id +"' class='ui inverted blue button' onClick=changeLikeState('heart"+ userEstates[i].id+"')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                    favoriteAnnouncement = $("<div class='item' id='"+ userEstates[i].id +"'><div class='ui small image'> <img src='"+ userEstates[i].estateAttachements[0].iconUri + "'onclick='bounce()'></div><div class='content'><a class='header'>Header</a><div class='meta'><span>Description</span></div><div class='description'><p></p></div><div class='extra'>Additional Details</div></div></div>");
                } else {
                    currentDiv =  $("<div id='"+ userEstates[i].id +"' class='only row'><div class='column'><div class='ui raised card' style='width:91%;margin-top:-1%;margin-left:2%;'><div class='content'><img class='right floated tiny ui image' src='./images/house-logo-md.png' style='width:120px;'><div class='header'>" + userEstates[i].typeOfTransaction + " " + userEstates[i].rooms + " room/s " + userEstates[i].type + "</div><div class='meta'>" + userEstates[i].city + "</div><div class='description'>" + userEstates[i].description + "</div></div><div class='extra content'><div class='ui grid'><div class='thirteen wide column' ><div class='ui two buttons'><input type='hidden' name='estate' value='" + userEstates[i].id + "' /><button class='ui blue button' onclick='updateEstate(" + userEstates[i].id + ")'>Update</button><button class='ui basic black button' type='submit' onclick='deleteEstate(" + userEstates[i].id + ")'>Delete</button></div></div><div class='two wide column'><button id='heart"+ userEstates[i].id +"' class='ui inverted blue button' onClick=changeLikeState('heart"+ userEstates[i].id+"')><i class='heart icon' style='width:8px;'></i></button></div></div></div></div></div></div>");
                }
                $("#estatesProfile").append(currentDiv);

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