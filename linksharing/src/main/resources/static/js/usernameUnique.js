$(document).on('focusout', '#edit-username', function () {
    var uname = this.value;
    console.log(uname);
    var check = $.ajax({
        url: "/checkUsernameAvailabilityToEdit",
        data: {"uname": uname},
        method: "GET"
    });
    check.done(function (data) {
        if (data) {
            document.getElementById('edit-username').value = null;
            $('#uname-msg').text("Username already exists");
            console.log(data);
        }
        else {
            $('#uname-msg').text("Username unique..");
        }
    });
    check.fail(function (jqXHR, textStatus) {
        document.getElementById('reg-username').value = "not valid";
        console.log("Error in fetching usernames");
    })
});