define(['jquery', 'LoadedCityBlueprint', 'controls'], function ($, LoadedCityBlueprint, controls)
{

    return function ()
    {
        var isSearching = false;

        function UiBehaviour(cameraMover)
        {
            this.cameraMover = cameraMover;

            var setDirectoriesBtn = $('#set-directories-btn');
            setDirectoriesBtn.bind({
                click: function (event)
                {
                    var sourceDirectory = $('#source-directory-input').val();
                    var classDirectory = $('#class-directory-input').val();

                    $.ajax({
                        type:     'POST',
                        url:      window.location.href.split("#")[0] + "definition/location",
                        dataType: "json",
                        data:     {
                            sourceLocation: sourceDirectory,
                            classLocation:  classDirectory
                        },
                        success:  function (response)
                        {
                            console.log('set source directory to ' + sourceDirectory);
                            console.log('set class directory to ' + classDirectory);
                        },
                        error:    function (e)
                        {
                            console.log("failed to set source directory to " + sourceDirectory + " and class directory to " + classDirectory + ". " + JSON.stringify(e));
                        }
                    });

                }
            });

            var onKeyDownWrapper = function (event)
            {
                onKeyDown(event, cameraMover);
            };

            document.addEventListener('keydown', onKeyDownWrapper, false);
        }

        var onKeyDown = function (event, cameraMover)
        {
            var searchBar = $("#code-unit-search");

            switch (event.keyCode)
            {
                case 17: // Ctrl

                    if (isSearching)
                    {
                        searchBar.blur();
                        searchBar.val('');
                        controls.setEnabled(true);
                    }
                    else
                    {
                        searchBar.focus();
                        controls.setEnabled(false);
                    }
                    isSearching = !isSearching;

                    break;

                case 13: // Return

                    if (isSearching)
                    {
                        var codeUnitName = searchBar.val();
                        cameraMover.lookAt(codeUnitName);
                    }
            }
        };

        UiBehaviour.prototype.setLoadCityCallback = function setLoadCityCallback(loadCityCallback)
        {
            var sourceInspectBtn = $('#source-inspect-btn');
            sourceInspectBtn.bind({
                click: function (event)
                {

                    var loadingIcon = $('.loading');
                    loadingIcon.show();
                    $.ajax({
                        type:    'GET',
                        url:     window.location.href.split("#")[0] + 'definition',
                        success: function (response)
                        {
                            console.log('finished inspecting. Response is ' + response);

                            var codeUnits = JSON.parse(response);
                            loadCityCallback(new LoadedCityBlueprint(codeUnits));

                            loadingIcon.hide();
                        },
                        error:   function (e)
                        {
                            console.log("failed to inspect. " + JSON.stringify(e));
                            loadingIcon.hide();
                        }
                    });

                }
            });
        };

        return UiBehaviour;
    }();

});
