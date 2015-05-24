define(["jquery"], function ($)
{
    return function ()
    {
        function TestCityBluePrint()
        {
            this.inhabitants = createTestInhabitants();
            this.vcsHistoryMap = createTestVcsHistory(this.inhabitants);
        }

        TestCityBluePrint.prototype.getVisualisationSourceDir = function getVisualisationSourceDir()
        {
            return "";
        };

        TestCityBluePrint.prototype.getInhabitants = function getInhabitants()
        {
            return this.inhabitants;
        };

        TestCityBluePrint.prototype.getVcsHistoryMap = function getVcsHistoryMap()
        {
            return this.vcsHistoryMap;
        };

        var createTestInhabitants = function createTestInhabitants()
        {
            var cubeData = [];

            for (var groupId = 0; groupId < 9; groupId++)
            {
                for (var unitId = 0; unitId < Math.floor(Math.random() * 150) + 1; unitId++)
                {
                    cubeData.push(createTestFileUnit(groupId, unitId));
                }
            }
            return cubeData;
        };

        var createTestVcsHistory = function createTestVcsHistory(fileUnits)
        {
            var history = {};
            $.each(fileUnits, function(idx, fileUnit) {

                history[fileUnit.groupName + ":" + fileUnit.name + "." + fileUnit.fileExtension] = {
                    groupName: fileUnit.groupName,
                    fileName: fileUnit.name,
                    fileExtension: fileUnit.fileExtension,
                    lastModifiedByUser: "Bob",
                    lastModifiedTimeMillis: 1432494925000
                };
            });

            return history;
        };

        var createTestFileUnit = function createTestFileUnit(groupId, unitId)
        {
            return {
                groupName: "group: " + groupId,
                name:      "unit: " + unitId,
                lineCount: Math.floor(Math.random() * 150) + 1,
                fileExtension: "java"
            };
        };

        return TestCityBluePrint;
    }();
});
