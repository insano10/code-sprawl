define(['detector', 'app', 'container', 'uiBehaviour', 'CityPlanner', 'controls', 'renderer', 'scene', 'camera', 'cameraMover', 'crossHair'],
    function (Detector, Application, container, UiBehaviour, CityPlanner, controls, renderer, scene, camera, CameraMover, crossHair)
    {
        var start = function ()
        {
            $(document).ready(function ()
            {
                var cameraMover = new CameraMover();
                var uiBehaviour = new UiBehaviour(cameraMover);
                var cityPlanner = new CityPlanner();

                var loadCityCallback = function (bluePrint)
                {
                    crossHair.clear();
                    cityPlanner.loadCity(bluePrint);
                    cameraMover.onCityLoaded(cityPlanner.getCodeUnits());
                    uiBehaviour.onCityLoaded(cityPlanner.getCodeUnits());
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

                var app = new Application(cityPlanner, cameraMover);
                app.initialise();

                //load test data
                cityPlanner.loadTestCity();
                cameraMover.onCityLoaded(cityPlanner.getCodeUnits());
                uiBehaviour.onCityLoaded(cityPlanner.getCodeUnits());
            })
        };

        return {"start": start};
    });
