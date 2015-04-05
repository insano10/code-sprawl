define(['detector', 'app', 'container', 'uiBehaviour'],
    function (Detector, Application, container, uiBehaviour)
    {
        var start = function ()
        {
            $(document).ready(function ()
            {
                uiBehaviour.setBehaviour();

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
