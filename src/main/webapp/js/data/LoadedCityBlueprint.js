define(["jquery"], function ($)
{
    return function ()
    {
        function LoadedCityBluePrint(codeUnitArray)
        {
            this.data = codeUnitArray;
        }

        LoadedCityBluePrint.prototype.getInhabitants = function getInhabitants()
        {
            return this.data;
        };

        return LoadedCityBluePrint;
    }();
});
