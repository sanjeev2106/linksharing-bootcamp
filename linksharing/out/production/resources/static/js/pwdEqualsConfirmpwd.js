$(document).on('focusout', '#confirm-password', function () {
    //alert("password confirmation")
    var pwd = document.getElementById('password').value;
    var cpwd = document.getElementById('confirm-password').value;
    console.log(pwd + " " + cpwd);
    if (pwd !== cpwd) {
        document.getElementById('confirm-password').value = null;
        $('#pwd-error-msg').text("password don't match");
    }
    else {
        $('#pwd-error-msg').text('matched');
    }
});