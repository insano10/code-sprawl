define(["jquery"], function ($)
{
    return function ()
    {
        function LoadedCityBluePrint(fileUnitArray, visualisationSourceDir)
        {
            this.data = fileUnitArray;
            this.visualisationSourceDir = visualisationSourceDir;
        }

        LoadedCityBluePrint.prototype.getVisualisationSourceDir = function getVisualisationSourceDir()
        {
            return this.visualisationSourceDir;
        };

        LoadedCityBluePrint.prototype.getInhabitants = function getInhabitants()
        {
            return this.data;
        };

        return LoadedCityBluePrint;
    }();
});
