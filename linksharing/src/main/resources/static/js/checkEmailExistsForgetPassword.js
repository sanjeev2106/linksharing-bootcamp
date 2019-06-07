
$(document).on('focusout', '#email', function () {
    var email = this.value;
    console.log(email);
    var check = $.ajax({
        url: "/checkEmailExistsInSystemForgetPwd",
        data: {"email": email},
        method: "GET"
    });
    check.done(function (data) {
        if(data=="pass"){
            $('#email-msg').text("registered email");
            console.log(data);
        }
        else {
            console.log(data);
            document.getElementById('email').value = null;
            $('#email-msg').text("Not a registed email Id..");
        }
    });
    check.fail(function (jqXHR, textStatus) {
        document.getElementById('reg-email').value = "not valid";
        console.log("Error in fetching emails");
    })
})