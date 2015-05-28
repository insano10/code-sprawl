define(['detector', 'app', 'container', 'uiBehaviour', 'CityPlanner', 'controls', 'renderer', 'scene', 'camera', 'cameraMover', 'crossHair'],
    function (Detector, Application, container, UiBehaviour, CityPlanner, controls, renderer, scene, camera, CameraMover, crossHair)
    {
        var start = function ()
        {
            $(document).ready(function ()
            {
                var cameraMover = new CameraMover();
                var cityPlanner = new CityPlanner();
                var app = new Application(cityPlanner, cameraMover);
                var uiBehaviour = new UiBehaviour(cameraMover, app);

                var loadCityCallback = function (bluePrint)
                {
                    crossHair.clear();
                    cityPlanner.loadCity(bluePrint);
                    cameraMover.onCityLoaded(cityPlanner.getFileUnits());
                    uiBehaviour.onCityLoaded(cityPlanner.getFileUnits());
                    controls.reset();
                    renderer.render(scene, camera);

                    // This needs to be called after the scene has been rendered and the world matrix has been created
                    // As the file units are static, this method only needs to be called once
                    cityPlanner.indexFileUnitsIntoScene();
                };

                uiBehaviour.setLoadCityCallback(loadCityCallback);

                console.log("Initializing!");

                if (!Detector.webgl)
                {
                    Detector.addGetWebGLMessage();
                    container.innerHTML = "";
                }

                getConfiguration(uiBehaviour.displayConfiguration);
                app.initialise();

                //load test data
                cityPlanner.loadTestCity();
                cameraMover.onCityLoaded(cityPlanner.getFileUnits());
                uiBehaviour.onCityLoaded(cityPlanner.getFileUnits());
            })
        };


        var getConfiguration = function getConfiguration(onConfigurationCallback)
        {
            $.ajax({
                type:    'GET',
                url:     window.location.href.split("#")[0] + 'definition/configuration',
                success: function (response)
                {
                    onConfigurationCallback(JSON.parse(response));
                },
                error:   function (e)
                {
                    console.log("failed to retrieve project config " + JSON.stringify(e));
                }
            });
        };

        return {"start": start};
    });
