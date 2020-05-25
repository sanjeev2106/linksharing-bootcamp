$(document).on('focusout', '#topic-name', function () {
    var topicname = this.value;
    console.log(topicname);
    var check = $.ajax({
        url: "/checkTopicNameUnique",
        data: {"topicname": topicname},
        method: "GET"
    });
    check.done(function (data) {
        if (!data) {
            document.getElementById('topic-name').value = null;
            $('#tname-msg').text("topic already exists");
            console.log(data);
        }
        else {
            $('#tname-msg').text("topic unique..");
        }
    });
    check.fail(function (jqXHR, textStatus) {
        document.getElementById('reg-username').value = "not valid";
        console.log("Error in fetching usernames");
    })
});