$(document).on('click','.edit-topic-name',
    function () {
        var topicId=$(this).attr("topic-id-attr1")
        console.log(topicId)
        $(".old-topic-name" + topicId).hide();
     //   $(".new-topic-name").show();
      //  $(".new-topic-name61").show();
        $(".new-topic-name" + topicId).show();

    }
);

$(document).on('click','.save-new-topic-name',
    function (event) {
        var newname=$(this).parent().find('.new-topic-name-text').val();
        console.log(newname)
        var topicId=$(this).attr("topic-id-attr")
        console.log("topicID"+topicId)
        var status = $.ajax({
            url : "/changeTopicname",
            data: {'newname':newname, 'topicId':topicId},
            method: "POST"
        });

        status.done(function (data) {
                console.log("done")
            $(".old-topic" + topicId).text(data);
            $(".old-topic-name" + topicId).show();
            $(".new-topic-name" + topicId).hide();
            }

        );

        status.fail(function () {
                console.log("Failure")
            }

        )
    }
);


$(document).on('click','.close-topic-edit',
    function () {
        $(".old-topic-name").show();
        $(".new-topic-name").hide();
    }
);

$(document).on('focusout', '.new-topic-name-text', function () {
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
            $('.newtname-msg').text("topic already exists");
            console.log(data);
        }
        else {
            $('.newtname-msg').text("topic unique..");
        }
    });
    check.fail(function (jqXHR, textStatus) {
        document.getElementById('reg-username').value = "not valid";
        console.log("Error in fetching usernames");
    })
});