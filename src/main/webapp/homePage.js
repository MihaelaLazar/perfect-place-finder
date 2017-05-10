window.onload = function() {
    checkSessionHomePage();
    $.ajax({
        url: '/user/check/session',
        type: 'GET',
        success: function(data) {
            if (data === "not existent") {
                console.log("not existent session");
            } else {
                console.log("session existent");
                console.log(data);
                document.getElementById('logInhomePage').style.display='none';
//                document.getElementById('logInStatus-homePage').style.display='block';
                document.getElementById('login').style.display = 'none';
                document.getElementById('signup').style.display = 'none';
                document.getElementById('logout').style.display = 'block';
                document.getElementById('loggedIn').style.display = 'block';
                $('#loggedInButton').text(data);
            }
        }
    });
}

function checkSessionHomePage() {

}

$( function() {
    var availableTags = [
      "Iasi",
      "Bucuresti",
      "London",
      "New York"
    ];
    $( "#chosenCity" ).autocomplete({
      source: availableTags
    });
} );

    var modalSignUpHomePage= document.getElementById('signUphomePage');
    // When the user clicks anywhere outside of the modal, close it
    var modalLoginHomePage = document.getElementById('logInhomePage');
    var modalLoginStatusOk = document.getElementById('logInStatus-homePage');
    var modalLoginStatusFailed = document.getElementById('logInStatusFailed-homePage');
    window.onclick = function(event) {
        if(event.target === modalSignUpHomePage )
        {
            modalSignUpHomePage.style.display = "none";
            $('#first-name-input-homePage').removeClass('error');
            $('#first-name-homePage').attr("placeholder","First name");
            $('#last-name-input-homePage').removeClass('error');
            $('#last-name-homePage').attr("placeholder","Last name");
            $('#email-input-homePage').removeClass('error');
            $('#email-homePage').attr("placeholder","Email");
            $('#password-input-homePage').removeClass('error');
            $('#password-name-homePage').attr("placeholder","Password");
            $('#errorMessageContainer-homePage').css("display", "none");
            $("#email-homePage").val("");
            $("#passwordSignUp-homePage").val("");
            $("#first-name-homePage").val("");
            $("#last-name-homePage").val("");
        }
        else {
          if (event.target == modalLoginHomePage)
          {
            modalLoginHomePage.style.display = "none";
            $('#email-input-login-homePage').removeClass('error');
            $('#email-login-homePage').attr("placeholder","Email");
            $('#password-input-login-homePage').removeClass('error');
            $('#errorMessageContainer-homePage').css("display", "none");
            $("#email-login-homePage").val("");
            $("#password-login-homePage").val("");
            $('#errorMessageContainer-login-homePage').css("display", "none");
          }
          else
          {
                if (event.target === document.getElementById('signInStatus-homePage')) {
                     document.getElementById('signInStatus-homePage').style.display='none';
                } else {
                    if (event.target ===  document.getElementById('signInStatusFailed-homePage')){
                        document.getElementById('signInStatusFailed-homePage').style.display='none';
                    } else {
                        if (event.target === modalLoginStatusOk)  {
                            document.getElementById('logInStatus-homePage').style.display='none';
                        } else {
                          if (event.target === modalLoginStatusFailed) {
                             document.getElementById('logInStatusFailed-homePage').style.display='none';
                          }
                        }
                    }
                }
          }
        }

    }


    var txt = document.getElementById('chosenCity');
    txt.addEventListener("keypress", function(event) {
      if (event.keyCode == 13)
          checkCity();
   });


    function checkCity(){
      var input = document.getElementById('chosenCity'),
        cityName = input.value;
        if (cityName.toUpperCase() == "IASI" || cityName.toUpperCase() == "BUCURESTI" || cityName.toUpperCase() == "NEW YORK" || cityName.toUpperCase() == "LONDON") {
            var cityNameCopy  = cityName.toLowerCase();
            window.location = "./searchCity.html?city=" + cityNameCopy;
            var cityInputValue = document.getElementById('chosenCity').value;

            document.getElementById('city').value = cityInputValue;
    } else {
        document.getElementsByName('chosenCity')[0].placeholder='Pleace specify a valid city';
        document.getElementById('chosenCity').value = null;
          console.log("I'll shake");
          $('#myInput').click(function(){
          $('#myInput').addClass('animated shake')
          .one('webkitAnimationEnd oAnimationEnd', function(){
              $('#myInput').removeClass('animated shake');
         });
        });
        document.getElementById('myInput').classList.remove("animated");
        document.getElementById('myInput').classList.remove("shake");
        console.log("removed");
      }
    }

var array = [ 1, 2, 3, 4 ];

var json = JSON.stringify(array);
var data = new FormData();
data.append('name', 'Bob');

    function sendData() {
    console.log("In sendData()");
        $.ajax({
            url: '/create/user',
            type: 'POST',
            contentType: false,
            data: data,
            dataType: 'json'
        });
    }

    function verifyLoginData(loginData) {
        console.log("In verifyData() " + JSON.stringify(loginData) );
        $.ajax({
            method: 'POST',
            url: '/verify/user',
            contentType: false,
            data: JSON.stringify(loginData),
            contentType: 'application/json',
            success: function(data, textStatus, xhr) {
                    console.log('Status code ' + xhr.status + "data: " + data);
                    document.getElementById('logInhomePage').style.display='none';
                    document.getElementById('logInStatus-homePage').style.display='block';
                    document.getElementById('login').style.display = 'none';
                    document.getElementById('signup').style.display = 'none';
                    document.getElementById('logout').style.display = 'block';
                    $('#loggedInButton').text(data);
                    document.getElementById('loggedIn').style.display='block';
            },
            error: function (xhr, ajaxOptions, thrownError,textStatus) {
                console.log('error Status code ' + xhr.status);
                document.getElementById('logInhomePage').style.display='none';
                document.getElementById('logInStatusFailed-homePage').style.display='block';
            }
        });
    }

    function logInPOSThomePage() {
                  $('#password-input-login-homePage').removeClass('error');
                  $('#email-input-login-homePage').removeClass('error');
                  console.log('logInPOST');
                  var url = "/verify/user";
                  var method = "POST";
                  var passInput = document.getElementById('password-login-homePage');
                  var password = passInput.value;
                  var emailInput = document.getElementById('email-login-homePage');
                  var email = emailInput.value;
                  var isValid = true;
                  var atpos = email.indexOf("@");
                  var dotpos = email.lastIndexOf(".");
                  isValid = true;
                  if (password === "") {
                        $('#password-input-login-homePage').addClass('error');
                        $('#password-login-homePage').attr("placeholder","Insert password");
                             $('#errorMessageContainer-login-homePage').text("Insert password");
                             $('#errorMessageContainer-login-homePage').css("display", "block");
                        isValid = false;
                  }
                  if (email === "" || atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
                        $('#email-input-login-homePage').addClass('error');
                        $('#email-login-homePage').attr("placeholder","Insert email");
                            $('#errorMessage-login-homePage').text("Incorrect email type");
                            $('#errorMessageContainer-login-homePage').css("display", "block");
                        isValid = false;
                  }
                  var postData = {
                       "password" : password,
                       "email" : email
                      };
                  if (isValid === true ){
                      var async = true;
                      var request = new XMLHttpRequest();
                      var status;
                      var data;
                      verifyLoginData(postData);
                      document.getElementById('logInhomePage').style.display='none';
                      $("#email-login-homePage").val("");
                      $("#password-login-homePage").val("");
                      document.getElementById('errorMessage-login-homePage').style.display = 'none';
                  }

     }

    function signUpPOSThomePage() {
                  $('#password-input-homePage').removeClass('error');
                  $('#first-name-input-homePage').removeClass('error');
                  $('#last-name-input-homePage').removeClass('error');
                  $('#email-input-homePage').removeClass('error');
                  console.log('signUpPOST');
                  var url = "/create/user";
                  var method = "POST";
                  var firstNameInput = document.getElementById('first-name-homePage');
                  var firstName = firstNameInput.value;
                  var lastNameInput = document.getElementById('last-name-homePage');
                  var lastName = lastNameInput.value;
                  var passInput = document.getElementById('passwordSignUp-homePage');
                  var password = passInput.value;
                  var emailInput = document.getElementById('email-homePage');
                  var email = emailInput.value;
                  var isValid = true;
                  var atpos = email.indexOf("@");
                  var dotpos = email.lastIndexOf(".");
                 if (firstName === "") {
                        $('#first-name-input-homePage').addClass('error');
                        $('#first-name-homePage').attr("placeholder","Insert first name");
                        $('#errorMessage-homePage').text("Insert first name");
                        $('#errorMessageContainer-homePage').css("display", "block");
                        isValid = false;
                  }
                  if (lastName === "") {
                        $('#last-name-input-homePage').addClass('error');
                        $('#last-name-homePage').attr("placeholder","Insert last name");
                            $('#errorMessage-homePage').text("Insert last name");
                            $('#errorMessageContainer-homePage').css("display", "block");

                        isValid = false;
                  }
                  if (password === "") {
                        $('#password-input-homePage').addClass('error');
                        $('#passwordSignUp-homePage').attr("placeholder","Insert password");
                             $('#errorMessage-homePage').text("Insert password");
                             $('#errorMessageContainer-homePage').css("display", "block");

                        isValid = false;
                  }
                  if (email === "" || atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
                        $('#email-input-homePage').addClass('error');
                        $('#email-homePage').attr("placeholder","Insert email");
                            $('#errorMessage-homePage').text("Insert email");
                            $('#errorMessageContainer-homePage').css("display", "block");

                        isValid = false;
                  }
                  var postData = {
                      "firstName": firstName,
                       "lastName" : lastName,
                       "password" : password,
                       "email" : email

                      };
                  if (isValid === true ){

                      var async = true;
                      var request = new XMLHttpRequest();
                      var status;
                      var data;
                      request.onload = function () {
                            status = request.status; // HTTP response status, e.g., 200 for "200 OK"
                            data = request.responseText; // Returned data, e.g., an HTML document.
                            if (data === 'DUPLICATE') {
                                console.log (data);
                                document.getElementById('signUphomePage').style.display='none';
                                document.getElementById('signInStatusFailed-homePage').style.display='block';
                            } else {
                                console.log (data);
                                document.getElementById('signUphomePage').style.display='none';
                                document.getElementById('signInStatus-homePage').style.display='block';
                          }
                      }
                      request.open(method, url,true);
                      request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                      request.send(JSON.stringify(postData));
                      document.getElementById('signUphomePage').style.display='none';
                       $("#email-homePage").val("");
                       $("#passwordSignUp-homePage").val("");
                       $("#first-name-homePage").val("");
                       $("#last-name-homePage").val("");

                  }
           }

function redirectToProfile() {
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

function invalidateSession() {
    $.ajax ({
        method: 'GET',
        url: '/user/logout',
        contentType: false,
        success (data) {
            console.log('session invalidated');
            document.getElementById('logInhomePage').style.display='none';
            document.getElementById('logInStatus-homePage').style.display='none';
            document.getElementById('login').style.display = 'inline';
            document.getElementById('signup').style.display = 'inline';
            document.getElementById('logout').style.display = 'none';
            document.getElementById('loggedIn').style.display='none';
        }
    });
}