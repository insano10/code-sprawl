define(["jquery"], function ($)
{
    return function ()
    {
        function LoadedCityBluePrint(fileUnitArray)
        {
            this.data = fileUnitArray;
        }

        LoadedCityBluePrint.prototype.getInhabitants = function getInhabitants()
        {
            return this.data;
        };

        return LoadedCityBluePrint;
    }();
});
