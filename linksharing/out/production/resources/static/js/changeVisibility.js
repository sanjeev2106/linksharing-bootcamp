$(document).on('click', '.newvisibility', function () {

    var newvisibility = this.value;
    console.log("newvisibility : "+newvisibility);
    var topicId = $(this).attr("topic-id");
    console.log("topicId :"+topicId);
    var status = $.ajax({
        url : "/changeVisibility",
        data: {'newvisibility':newvisibility, 'topicId':topicId},
        method: "POST"
    });

    status.done(function (data) {
            console.log("data");
            $('body').load('/dashboard');
        }

    );

    status.fail(function () {
            console.log("Failure")
        }

    )
});