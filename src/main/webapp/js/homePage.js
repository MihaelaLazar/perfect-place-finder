window.onload = function() {
var modal = document.getElementById('id01');
    // When the user clicks anywhere outside of the modal, close it
    var modal2 = document.getElementById('id02');
    window.onclick = function(event) {
        if(event.target == modal ){
            modal.style.display = "none";
        } else {
          if (event.target == modal2){
            modal2.style.display = "none";
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
    if (cityName == "Iasi" || cityName == "Bucuresti" || cityName == "New York" || cityName == "London") {
        window.location = "./searchCity.html?city=" + cityName;
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

//     $(document).ready(function(){
//         $.ajax({
//          url: "/user/register",
//          type: "GET",
//          data : {json: "hello" }, //in servlet use request.getParameters("json")
//          dataType : 'json',
//          success: function(data) {}, //data holds {success:true} - see below
//          error: errorFunction
//         });
//      })

var array = [ 1, 2, 3, 4 ];

	var json = JSON.stringify(array);


	$.ajax({ url:"/create/user",
          type:"POST",
          data: json,
          dataType:'json',
         success:function(data){
				$("#output").append( data );
			//	window.location = 'testArrFour.jsp';
			},
			error: function() {
				 $("#output").html("fail");
			}
	});

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
}