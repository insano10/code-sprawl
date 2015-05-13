define(["controls"], function (controls)
{
    return function ()
    {
        function CameraMover()
        {
            this.fileUnits = {};
        }

        CameraMover.prototype.lookAt = function lookAt(fileUnitName)
        {
            var chosenUnit = this.fileUnits[fileUnitName];
            controls.lookAt(chosenUnit.getFocusPoint());
        };

        CameraMover.prototype.onCityLoaded = function onCityLoaded(fileUnits)
        {
            this.fileUnits = fileUnits;
        };

        return CameraMover;
    }();

});
