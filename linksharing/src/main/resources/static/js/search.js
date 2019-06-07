/*

$("#search-topic-resource").autocomplete({
    // source:function (request,response) {
    //
    //     $.ajax({
    //         method:"GET",
    //         url:"/autocomplete",
    //         data: {"pattern": $("#search-topic-resource").val()},
    //         success:function (data) {
    //             console.log("entered js 2");
    //             var resourcesAvailable=[];
    //             data.forEach(function (e) {
    //                 resourcesAvailable.push(e.resourceId)
    //                 console.log(e.resourceId)
    //
    //             });
    //             response(resourcesAvailable);
    //         }
    //     })
    // }

    serviceUrl: '/autocomplete',
    onSelect: function (suggestion) {
        console.log("sbjkbkjdfnklnv");
        alert('You selected: ' + suggestion.resourceId + ', ' + suggestion.description);
    }
});*/
