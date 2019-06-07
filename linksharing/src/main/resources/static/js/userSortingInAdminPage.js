$(document).on('change', '#selectUsers', function () {
    var user = this.value;

    var url;

    if(user === "true") {
        url = "/userSorting/active";
    }
    else if(user === "false") {
        url = "/userSorting/deActive";
    }
    else {
        url = "/userList"
    }

    console.log(url);
    $.ajax({
        url:url,
        method:"POST"
    }).done(function(data){
        $("table").load(location.href + " table");
    });
});

/*
$(document).ready(
  $('#sortByIdAc').onclick(
      function () {
          alert("clicked");
          var order = this.value;
          alert(order);
          var url;

          $.ajax({
              url:"/sortById",
              data:order,
              method:"POST"
          }).done(function(data){
              $("table").load(location.href + " table");
          });
      }
  )
);

$(document).on('click', '.sortById', function () {
    alert("clicked");
    var order = this.value;
    alert(order);
    var url;

    $.ajax({
        url:"/sortById",
        data:order,
        method:"POST"
    }).done(function(data){
        $("table").load(location.href + " table");
    });
});*/
