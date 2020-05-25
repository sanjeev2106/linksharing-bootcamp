$(document).on('focusout', '#reg-email', function () {
    var email = this.value;
    var check = $.ajax({
        url: "/checkEmailAvailability",
        data: {"email": email},
        method: "GET"
    });
    check.done(function (data) {
        if (data) {
            document.getElementById('reg-email').value = null;
            $('#email-msg').text("this email is already registered");
            console.log(data);
        }
    });
    check.fail(function (jqXHR, textStatus) {
        document.getElementById('reg-email').value = "not valid";
        console.log("Error in fetching emails");
    })
});


$(document).on('focusout', '#reg-username', function () {
    var uname = this.value;
    var check = $.ajax({
        url: "/checkUsernameAvailability",
        data: {"uname": uname},
        method: "GET"
    });
    check.done(function (data) {
        if (data) {
            document.getElementById('reg-username').value = null;
            $('#uname-msg').text("Username already exists");
            console.log(data);
        }
    });
    check.fail(function (jqXHR, textStatus) {
        document.getElementById('reg-username').value = "not valid";
        console.log("Error in fetching usernames");
    })
});

$(document).on('focusout', '#confirm-password', function () {
    var pwd = document.getElementById('password').value;
    var cpwd = document.getElementById('confirm-password').value;
    console.log(pwd + " " + cpwd);
    if (pwd !== cpwd) {
        document.getElementById('confirm-password').value = null;
        $('#pwd-error-msg').text("password dont match");
    }
    /*else {
        $('#pwd-error-msg').text('matched');
    }*/
});





