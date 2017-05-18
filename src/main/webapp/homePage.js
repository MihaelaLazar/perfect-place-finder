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
                     $("#email").val("");
                     $("#passwordSignUp").val("");
                     $("#first-name").val("");
                     $("#last-name").val("");
                } else {
                    if (event.target ===  document.getElementById('signInStatusFailed-homePage')){
                        document.getElementById('signInStatusFailed-homePage').style.display='none';
                        $("#email").val("");
                        $("#passwordSignUp").val("");
                        $("#first-name").val("");
                        $("#last-name").val("");
                    } else {
                        if (event.target === modalLoginStatusOk)  {
                            document.getElementById('logInStatus-homePage').style.display='none';
                            $("#email-login-homePage").val("");
                            $("#password-login-homePage").val("");
                        } else {
                          if (event.target === modalLoginStatusFailed) {
                             document.getElementById('logInStatusFailed-homePage').style.display='none';
                             $("#email-login-homePage").val("");
                             $("#password-login-homePage").val("");
                             $('#password-login-homePage').style.display='none';
                          }
                        }
                    }
                }
          }
        }
        $('#errorMessageContainer-login-homePage').css("display", "none");

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
                    document.getElementById('logInhomePage').style.display='none';
                     $("#email-login-homePage").val("");
                     $("#password-login-homePage").val("");
                     document.getElementById('errorMessage-login-homePage').style.display = 'none';
            },
            error: function (xhr, ajaxOptions, thrownError,textStatus) {
                console.log('error Status code ' + xhr.status);
                document.getElementById('logInhomePage').style.display='none';
                document.getElementById('logInStatusFailed-homePage').style.display='block';
                var errors = xhr.responseText.split(';');
                for (var i = 0; i < errors.length; i ++) {
                    if (errors[i] === "Invalid email") {
                        $('#email-input-login-homePage').addClass('error');
                        $('#email-login-homePage').attr("placeholder","Insert email");
                        $('#errorMessage-login-homePage').text("Incorrect email type");
                        $('#errorMessageContainer-login-homePage').css("display", "block");
                    }

                    if (errors[i] === "Invalid password") {
                        $('#password-input-login-homePage').addClass('error');
                        $('#errorMessageContainer-login-homePage').text("Insert correct password");
                        $('#errorMessageContainer-login-homePage').css("display", "block");
                    }
                }
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

/* POST request to server for user login.  */
function loginInHomePage() {
     $('#password-input-login-homePage').removeClass('error');
     $('#email-input-login-homePage').removeClass('error');
     var passInput = document.getElementById('password-login-homePage');
     var password = passInput.value;
     var emailInput = document.getElementById('email-login-homePage');
     var email = emailInput.value;
     var postData = {
        "password" : password,
        "email" : email
     };
     verifyLoginData(postData);
}

/* POST request to server for user sign up.  */
function signUpInHomePage() {
    $('#password-input-homePage').removeClass('error');
    $('#first-name-input-homePage').removeClass('error');
    $('#last-name-input-homePage').removeClass('error');
    $('#email-input-homePage').removeClass('error');
    var firstNameInput = document.getElementById('first-name-homePage');
    var firstName = firstNameInput.value;
    var lastNameInput = document.getElementById('last-name-homePage');
    var lastName = lastNameInput.value;
    var passInput = document.getElementById('passwordSignUp-homePage');
    var password = passInput.value;
    var emailInput = document.getElementById('email-homePage');
    var email = emailInput.value;
    var postData = {
        "firstName": firstName,
        "lastName" : lastName,
        "password" : password,
        "email" : email
    };
    $.ajax ({
        method: 'POST',
        url: '/create/user',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(postData),
        success(data){
            document.getElementById('signupButton').style.display='none';
            document.getElementById('signInStatus').style.display='block';
            document.getElementById('signupButton').style.display='none';
            $("#email").val("");
            $("#passwordSignUp").val("");
            $("#first-name").val("");
            $("#last-name").val("");
        },
        error: function (xhr, ajaxOptions, thrownError, textStatus) {
            console.log('error Status code ' + xhr.status);
            console.log('Text status: ' + xhr.responseText);
            if (xhr.responseText === "DUPLICATE") {
                document.getElementById('signUphomePage').style.display='none';
                document.getElementById('signInStatus-homePage').style.display='block';
                document.getElementById('signUphomePage').style.display='none';
            }
            if (xhr.responseText === "Added in database") {
                document.getElementById('signUphomePage').style.display='none';
                document.getElementById('signInStatus-homePage').style.display='block';
                document.getElementById('signUphomePage').style.display='none';
                $("#email-homePage").val("");
               $("#passwordSignUp-homePage").val("");
               $("#first-name-homePage").val("");
               $("#last-name-homePage").val("");
            }

            var errors = xhr.responseText.split(';');
            for (var i = 0; i < errors.length; i ++) {
                if (errors[i] === "Invalid email format") {
                    $('#email-input-homePage').addClass('error');
                    $('#email-homePage').attr("placeholder","Insert email");
                    $('#errorMessage-homePage').text("Insert email");
                    $('#errorMessageContainer-homePage').css("display", "block");
                }

                if (errors[i] === "Invalid lastName") {
                    $('#last-name-input-homePage').addClass('error');
                    $('#last-name-homePage').attr("placeholder","Insert last name");
                    $('#errorMessage-homePage').text("Insert last name");
                    $('#errorMessageContainer-homePage').css("display", "block");
                }

                if (errors[i] === "Invalid firstName") {
                    $('#first-name-input-homePage').addClass('error');
                    $('#first-name-homePage').attr("placeholder","Insert first name");
                    $('#errorMessage-homePage').text("Insert first name");
                    $('#errorMessageContainer-homePage').css("display", "block");
                }

                if (errors[i] === "Invalid password length") {
                    $('#password-input-homePage').addClass('error');
                    $('#passwordSignUp-homePage').attr("placeholder","Insert password");
                    $('#errorMessage-homePage').text("Insert password");
                    $('#errorMessageContainer-homePage').css("display", "block");
                }
            }
        }
    });
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