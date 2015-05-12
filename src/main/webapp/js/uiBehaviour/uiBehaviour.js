define(['jquery', 'LoadedCityBlueprint', 'controls', 'jqueryui'], function ($, LoadedCityBlueprint, controls)
{

    return function ()
    {
        var isSearching = false;

        function UiBehaviour(cameraMover)
        {
            this.cameraMover = cameraMover;

            //construct configuration dialog
            var dialog;

            function configureProject()
            {
                var sourceDirectory = $('#source-directory-input').val();

                $.ajax({
                    type:     'POST',
                    url:      window.location.href.split("#")[0] + "definition/location",
                    dataType: "json",
                    data:     {
                        sourceLocation: sourceDirectory
                    },
                    success:  function (response)
                    {
                        console.log('set directory to ' + sourceDirectory);
                        dialog.dialog("close");

                    },
                    error:    function (e)
                    {
                        console.log("failed to set source directory to " + sourceDirectory + ". " + JSON.stringify(e));
                    }
                });
            }

            dialog = $("#configure-dialog-form").dialog({
                autoOpen: false,
                height:   300,
                width:    700,
                modal:    true,
                buttons:  {
                    "Save": configureProject,
                    Cancel: function ()
                    {
                        dialog.dialog("close");
                    }
                }
            });

            $("#configure-btn").button().on("click", function ()
            {
                dialog.dialog("open");
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
            var visualiseBtn = $('#visualise-btn');
            visualiseBtn.bind({
                click: function (event)
                {

                    var loadingIcon = $('.loading');
                    loadingIcon.show();
                    $.ajax({
                        type:    'GET',
                        url:     window.location.href.split("#")[0] + 'definition',
                        success: function (response)
                        {
                            console.log('finished visualising. Response is ' + response);

                            var codeUnits = JSON.parse(response);
                            loadCityCallback(new LoadedCityBlueprint(codeUnits));

                            loadingIcon.hide();
                        },
                        error:   function (e)
                        {
                            console.log("failed to visualise. " + JSON.stringify(e));
                            loadingIcon.hide();
                        }
                    });

                }
            });
        };

        UiBehaviour.prototype.onCityLoaded = function onCityLoaded(codeUnits)
        {
            var fullyQualifiedCodeUnitNames = [];

            $.each(codeUnits, function (idx, codeUnit)
            {
                fullyQualifiedCodeUnitNames.push(codeUnit.getFullyQualifiedName());
            });

            $('#code-unit-search').autocomplete({
                source: fullyQualifiedCodeUnitNames
            });
        };

        return UiBehaviour;
    }();

});
