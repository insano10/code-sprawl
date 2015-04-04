define(['detector', 'app', 'container'],
    function (Detector, Application, container)
    {
        var start = function ()
        {
            $(document).ready(function ()
            {
                console.log("Initializing!");

                if (!Detector.webgl)
                {
                    Detector.addGetWebGLMessage();
                    container.innerHTML = "";
                }

                var app = new Application();
                app.initialise();
            })
        };

        return {"start": start};
    });
