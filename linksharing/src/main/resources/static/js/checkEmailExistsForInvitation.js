
$(document).on('focusout', '#inviteEmail', function () {
    var email = this.value;
    var check = $.ajax({
        url: "/checkEmailExistsInSystem",
        data: {"email": email},
        method: "GET"
    });
    check.done(function (data) {
        if (data=="yourself") {
            document.getElementById('inviteEmail').value = null;
            $('#email-msg').text("Cannot send invitation to yourself");
            console.log(data);
        }
        else if(data=="pass"){
            $('#email-msg').text("registered email");
            console.log(data);
        }
        else {
            console.log(data);
            document.getElementById('inviteEmail').value = null;
            $('#email-msg').text("Not a registed email Id..");
        }
    });
    check.fail(function (jqXHR, textStatus) {
        document.getElementById('reg-email').value = "not valid";
        console.log("Error in fetching emails");
    })
})


/*$(document).read(function () {
    $("#SendInvitation")[0].click(

    var topicId = this.value;
    alert(topicId.toString());
    $('#hiddenTopicId').val(topicId)

    );
})*/


/*function SetTopicId() {
    var id = document.getElementById("SendInvitation");
    alert(id);
    document.getElementById("hiddenTopicId").value = parseInt(id);

}*/

/*$(document).ready(function(){
    $('#SendInvitation')[0].click(function() {
        var topicId = this.value;
        console.log(topicId.toString());
        $('#hiddenTopicId').val(topicId)    });

});*/

/*
on('click', '#SendInvitation', function () {

    var topicId = this.value;
    alert(topicId.toString());
    $('#hiddenTopicId').val(topicId);
})*/
