define(["controls"], function (controls)
{
    return function ()
    {
        function CameraMover()
        {
            this.codeUnits = {};
        }

        CameraMover.prototype.lookAt = function lookAt(codeUnitName)
        {
            var chosenUnit = this.codeUnits[codeUnitName];
            controls.lookAt(chosenUnit.getFocusPoint());
        };

        CameraMover.prototype.onCityLoaded = function onCityLoaded(codeUnits)
        {
            this.codeUnits = codeUnits;
        };

        return CameraMover;
    }();

});
