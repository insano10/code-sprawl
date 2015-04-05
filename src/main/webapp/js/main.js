define(['detector', 'app', 'container', 'uiBehaviour', 'CityPlanner'],
    function (Detector, Application, container, uiBehaviour, CityPlanner)
    {
        var start = function ()
        {
            $(document).ready(function ()
            {
                var cityPlanner = new CityPlanner();

                uiBehaviour.setBehaviour();

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
