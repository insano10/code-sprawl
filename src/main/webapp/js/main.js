define(['detector', 'app', 'container'],
    function (Detector, app, container)
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

                // Initialize our app and start the animation loop (animate is expected to call itself)
                app.init();
            })
        };

        return {"start": start};
    });
