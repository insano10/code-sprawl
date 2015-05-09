define(["controls"], function (controls)
{
    return function ()
    {
        var shouldMove = false;

        function CameraMover()
        {
            this.codeUnits = {};

            document.addEventListener('keydown', onKeyDown, false);
        }

        var onKeyDown = function (event)
        {
            switch (event.keyCode)
            {
                case 76: // l
                    shouldMove = true;
                    break;

            }
        };

        CameraMover.prototype.update = function update()
        {
            if (shouldMove)
            {
                var sideOffset = 25;
                var chosenUnit = this.codeUnits['unit: 10'];

                controls.lookAt(chosenUnit.getFocusPoint());
                shouldMove = false;
            }
        };


        CameraMover.prototype.onCityLoaded = function onCityLoaded(codeUnits)
        {
            this.codeUnits = codeUnits;
        };

        return CameraMover;
    }();

});
