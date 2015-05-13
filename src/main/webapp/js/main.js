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
                };

                uiBehaviour.setLoadCityCallback(loadCityCallback);

                console.log("Initializing!");

                if (!Detector.webgl)
                {
                    Detector.addGetWebGLMessage();
                    container.innerHTML = "";
                }

                app.initialise();

                //load test data
                cityPlanner.loadTestCity();
                cameraMover.onCityLoaded(cityPlanner.getFileUnits());
                uiBehaviour.onCityLoaded(cityPlanner.getFileUnits());
            })
        };

        return {"start": start};
    });
