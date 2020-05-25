$(document).on('click', '.star', function () {
    var num = parseInt($(this).attr('value'));
    var grandParentId = ($(this).parent().parent().attr('id'));
    //console.log(grandParentId);
    $('.star' + grandParentId).removeClass("fa fa-heart").addClass("fa fa-heart-o");
    for (i = 1; i <= num; i++) {
        //console.log(i);
        $('#' + grandParentId).find('#' + i).removeClass("fa fa-heart-o").addClass("fa fa-heart");
    }

    var status = $.ajax({
        url: "/resourceRating",
        data: {'rating': num, 'resourceId': grandParentId},
        method: "POST"
    });

    status.done(function () {
            console.log("done")
        }
    );
});

/*
$(document).on('load', '.star', function () {
    var status = $.ajax({
        url: "/previousRating",
        method: "POSt"
    });

})*/
