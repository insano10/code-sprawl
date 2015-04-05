define(['jquery'], function($) {

    var setBehaviour = function ()
    {
        var sourceDirectoryBtn = $('#source-directory-btn');
        sourceDirectoryBtn.bind({
            click: function(event) {
                var sourceDirectory = $('#source-directory-input').val();

                $.ajax({
                    type:     'POST',
                    url:      window.location.href.split("#")[0] + 'source',
                    dataType: "json",
                    data:     {location: sourceDirectory},
                    success:  function (response)
                    {
                        console.log('set source directory to ' + sourceDirectory);
                    },
                    error:    function (e)
                    {
                        console.log("failed to set source directory to " + sourceDirectory + ". " + e);
                    }
                });

            }
        });
    };

    return { setBehaviour: setBehaviour };
});
