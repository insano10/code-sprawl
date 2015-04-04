define(["jquery"], function ($)
{
    return function ()
    {
        function TestCityBluePrint()
        {
            this.data = createTestData();
        }

        TestCityBluePrint.prototype.getInhabitants = function getInhabitants()
        {
            return this.data;
        };

        var createTestData = function createTestData()
        {
            var cubeData = [];

            for (var groupId = 0; groupId < 4; groupId++)
            {
                for (var unitId = 0; unitId < 100; unitId++)
                {
                    cubeData.push(createTestUnit(groupId, unitId));
                }
            }
            return cubeData;
        };

        var createTestUnit = function createTestUnit(groupId, unitId)
        {
            return {
                groupName: "group: " + groupId,
                name:      "unit: " + unitId,
                lineCount: Math.floor(Math.random() * 150) + 1
            };
        };

        return TestCityBluePrint;
    }();
});
