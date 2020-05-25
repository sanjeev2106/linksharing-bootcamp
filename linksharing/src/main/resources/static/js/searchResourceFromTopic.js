$(document).on('keyup','#search-resource',function () {
    var search=this.value;
    var topicId = $(this).attr("topic-id");
    console.log(search);
    console.log(topicId);
if(search!=null) {
    var check = $.ajax({
        url: '/searchForResources',
        method: 'GET',
        data: {'search': search, 'topicId': topicId}
    });
    check.done(function (data) {
        $('#myreload').html(data);
    });

    check.fail(function (jqxhr,textStatus) {
        document.getElementById('.search-post').value = "not Valid";
        console.log("Error in fetching username");
    })
}
else
    $('#myreload').load(' #myreload');

});