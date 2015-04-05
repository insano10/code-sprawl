define(['detector', 'app', 'container', 'uiBehaviour', 'CityPlanner', 'controls', 'renderer', 'scene', 'camera'],
    function (Detector, Application, container, uiBehaviour, CityPlanner, controls, renderer, scene, camera)
    {
        var start = function ()
        {
            $(document).ready(function ()
            {
                var cityPlanner = new CityPlanner();

                var loadCityCallback = function (bluePrint)
                {
                    cityPlanner.loadCity(bluePrint);
                    controls.reset();
                    renderer.render(scene, camera);
                };

                uiBehaviour.setBehaviour(loadCityCallback);

                console.log("Initializing!");

                if (!Detector.webgl)
                {
                    Detector.addGetWebGLMessage();
                    container.innerHTML = "";
                }

                var app = new Application(cityPlanner);
                app.initialise();
            })
        };

        return {"start": start};
    });
