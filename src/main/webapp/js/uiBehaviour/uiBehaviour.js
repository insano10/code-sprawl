define(['jquery', 'LoadedCityBlueprint', 'controls', 'jqueryui'], function ($, LoadedCityBlueprint, controls)
{

    return function ()
    {
        var searchEnabled = true;
        var isSearching = false;

        function UiBehaviour(cameraMover)
        {
            this.cameraMover = cameraMover;

            //construct configuration dialog
            var dialog;

            function configureProject()
            {
                var vcsRoot = $('#vcs-root-input').val();
                var sourceDirectory = $('#source-directory-input').val();
                var fileExtensions = $('#file-type-input').val().split(/[\s,]+/);
                var vcsOption = $("input:radio[name ='vcs-radio']:checked").val();

                $.ajax({
                    type:     'POST',
                    url:      window.location.href.split("#")[0] + "definition/configuration",
                    dataType: "json",
                    data:     {
                        configuration: JSON.stringify({
                            vcsRoot:         vcsRoot,
                            sourceDirectory: sourceDirectory,
                            fileExtensions:  fileExtensions,
                            vcsOption:       vcsOption
                        })
                    },
                    success:  function (response)
                    {
                        closeConfigurationDialog();

                    },
                    error:    function (e)
                    {
                        console.log("failed to set configuration: " +
                            "  vcsRoot: " + vcsRoot +
                            ", sourceDirectory: " + sourceDirectory +
                            ", fileExtensions: " + fileExtensions +
                            ", vcsOption: " + vcsOption +
                            ". " + JSON.stringify(e));
                    }
                });
            }

            function openConfigurationDialog()
            {
                dialog.dialog("open");
                searchEnabled = false;
            }

            function closeConfigurationDialog()
            {
                dialog.dialog("close");
                searchEnabled = true;
            }

            dialog = $("#configure-dialog-form").dialog({
                autoOpen: false,
                height:   500,
                width:    700,
                modal:    true,
                buttons:  {
                    "Save": configureProject,
                    Cancel: function ()
                    {
                        closeConfigurationDialog();
                    }
                }
            });

            $("#configure-btn").button().on("click", function ()
            {
                openConfigurationDialog();
            });


            var onKeyDownWrapper = function (event)
            {
                onKeyDown(event, cameraMover);
            };

            document.addEventListener('keydown', onKeyDownWrapper, false);
        }

        var onKeyDown = function (event, cameraMover)
        {
            if(searchEnabled)
            {
                var searchBar = $("#file-unit-search");

                switch (event.keyCode)
                {
                    case 32: // space

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
                        event.preventDefault();

                        break;

                    case 13: // Return

                        if (isSearching)
                        {
                            var fileUnitName = searchBar.val();
                            cameraMover.lookAt(fileUnitName);
                        }
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

                            var fileUnits = JSON.parse(response);
                            loadCityCallback(new LoadedCityBlueprint(fileUnits));

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

        UiBehaviour.prototype.displayConfiguration = function displayConfiguration(config)
        {
            $('#vcs-root-input').val(config.vcsRoot);
            $('#source-directory-input').val(config.sourceDirectory);
            $('#file-type-input').val(config.fileExtensions);
            $("input[name=vcs-radio][value=" + config.vcsOption + "]").prop('checked', true);
        };

        UiBehaviour.prototype.onCityLoaded = function onCityLoaded(fileUnits)
        {
            var fullyQualifiedFileUnitNames = [];

            $.each(fileUnits, function (idx, fileUnit)
            {
                fullyQualifiedFileUnitNames.push(fileUnit.getFullyQualifiedName());
            });

            $('#file-unit-search').autocomplete({
                source: fullyQualifiedFileUnitNames
            });
        };

        return UiBehaviour;
    }();

});
