define(["jquery"], function ($)
{
    return function ()
    {
        function LoadedCityBluePrint(fileUnitArray, visualisationSourceDir, vcsHistoryMap)
        {
            this.inhabitants = fileUnitArray;
            this.visualisationSourceDir = visualisationSourceDir;
            this.vcsHistoryMap = vcsHistoryMap;
        }

        LoadedCityBluePrint.prototype.getVisualisationSourceDir = function getVisualisationSourceDir()
        {
            return this.visualisationSourceDir;
        };

        LoadedCityBluePrint.prototype.getInhabitants = function getInhabitants()
        {
            return this.inhabitants;
        };

        LoadedCityBluePrint.prototype.getVcsHistoryMap = function getVcsHistoryMap()
        {
            return this.vcsHistoryMap;
        };

        return LoadedCityBluePrint;
    }();
});
