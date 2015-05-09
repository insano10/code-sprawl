define(['detector', 'app', 'container', 'uiBehaviour', 'CityPlanner', 'controls', 'renderer', 'scene', 'camera', 'cameraMover'],
    function (Detector, Application, container, UiBehaviour, CityPlanner, controls, renderer, scene, camera, CameraMover)
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
                    cityPlanner.loadCity(bluePrint);
                    cameraMover.onCityLoaded(cityPlanner.getCodeUnits());
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
            })
        };

        return {"start": start};
    });
