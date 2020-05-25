$(document).on('click', '.newseriousness', function () {
    var newseriousness = this.value;
    // var topicId = document.querySelector('.subscriptions-card').id;
    var topicId = $(this).attr("topic-id");
    var status = $.ajax({
        url : "/changeSeriousness",
        data: {'newseriousness':newseriousness, 'topicId':topicId},
        method: "POST"
    });

    status.done(function (data) {
        console.log("done")
        $('.newseriousness').val(newseriousness);
        }

    );

    status.fail(function () {
        console.log("Failure")
        }

    )
});