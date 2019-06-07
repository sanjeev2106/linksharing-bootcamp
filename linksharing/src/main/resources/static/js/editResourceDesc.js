$(document).on(
    'click','.edit-topic-desc',function () {

        var resourceId = $(this).attr("resource-id");
        var resourceType = $(this).attr("resource-type");
        var resourceDesc = $(this).attr("resource-desc");
        var resourceTopic = $(this).attr("resource-topic");
        var resourcePath = $(this).attr("resource-path");
        console.log("1"+resourceId);
        console.log("2"+resourceType);
        console.log("3"+resourceDesc);
        console.log("4"+resourceTopic);
        console.log("5"+resourcePath);

        $('#edit-path').text(resourcePath);
        $('#edit-desc').text(resourceDesc);
        $('#edit-topic').text(resourceTopic);
        $('#resourceId').text(resourceId);
        //$('#resourceId').value=resourceId;//$(this).attr("resource-id");
    }
);