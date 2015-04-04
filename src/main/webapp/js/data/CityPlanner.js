define(["jquery", "CityBlueprint", "CodeNeighbourhood", "CodeUnit"], function ($, CityBlueprint, CodeNeighbourhood, CodeUnit)
{
    return function ()
    {
        var UNIT_SIDE_LENGTH = 50;
        var GAP_BETWEEN_UNITS = 10;
        var DISTANCE_FROM_GROUND = 20;
        var NEIGHBOURHOOD_BORDER_WIDTH = 100;

        function CityPlanner()
        {
            this.neighbourhoodToUnitArrayMap = {};
            this.constructedNeighbourhoods = [];
        }

        CityPlanner.prototype.loadCity = function loadCity()
        {
            console.log("Loading city inhabitants");
            loadInhabitants(this);

            console.log("Building neighbourhoods");
            buildNeighbourhoods(this);
        };

        CityPlanner.prototype.getNeighbourhoods = function getNeighbourhoods()
        {
            return this.constructedNeighbourhoods;
        };

        var loadInhabitants = function loadInhabitants(cityPlanner)
        {
            var data = new CityBlueprint().getInhabitants();

            $.each(data, function (idx, unit)
            {
                var group = unit.groupName;

                if (cityPlanner.neighbourhoodToUnitArrayMap[group] === undefined)
                {
                    cityPlanner.neighbourhoodToUnitArrayMap[group] = [];
                }

                cityPlanner.neighbourhoodToUnitArrayMap[group].push(unit);
            });
        };

        var buildNeighbourhoods = function buildNeighbourhoods(cityPlanner)
        {
            var neighbourhoodCount = Object.keys(cityPlanner.neighbourhoodToUnitArrayMap).length;
            var cityWidth = Math.ceil(Math.sqrt(neighbourhoodCount));
            var groupId = 0;
            var xBoundary = 0;
            var zBoundary = 0;
            var maxZLengthOfRow = -1;
            var column = 0;

            console.log("The city is " + cityWidth + " neighbourhoods wide");

            // Arrange the neighbourhoods as close to a square as possible
            $.each(cityPlanner.neighbourhoodToUnitArrayMap, function (name, unitArray)
            {
                var neighbourhood = buildNeighbourhood(groupId++, name, unitArray, xBoundary, zBoundary);
                cityPlanner.constructedNeighbourhoods.push(neighbourhood);

                column++;
                xBoundary += neighbourhood.getXLength();
                maxZLengthOfRow = Math.max(maxZLengthOfRow, neighbourhood.getZLength());

                if(column == cityWidth)
                {
                    column = 0;
                    xBoundary = 0;
                    zBoundary += maxZLengthOfRow;
                    maxZLengthOfRow = -1;
                }
            });
        };

        var buildNeighbourhood = function buildNeighbourhood(id, name, unitArray, xBoundary, zBoundary)
        {
            var width = Math.ceil(Math.sqrt(unitArray.length));
            var column = 0;
            var row = 0;

            console.log("Neighbourhood '" + name + "' has " + unitArray.length + " inhabitants and is " + width + " units wide");

            var cubeOffset = GAP_BETWEEN_UNITS + UNIT_SIDE_LENGTH;
            var codeUnits = [];

            // Arrange the code units as close to a square as possible
            $.each(unitArray, function (idx, unit)
            {
                var position = new THREE.Vector3();
                position.x = xBoundary + cubeOffset * column;
                position.y = DISTANCE_FROM_GROUND + (unit.lineCount / 2);
                position.z = zBoundary + cubeOffset * row;

                var codeUnit = new CodeUnit(UNIT_SIDE_LENGTH, unit.lineCount, UNIT_SIDE_LENGTH, unit.name, unit.groupName);
                codeUnit.setPosition(position);
                codeUnits.push(codeUnit);

                column++;

                if(column == width)
                {
                    column = 0;
                    row++;
                }
            });

            var groundSideXLengthForData = (width * UNIT_SIDE_LENGTH) + ((width - 1) * GAP_BETWEEN_UNITS);
            var groundSideZLengthForData = (row * UNIT_SIDE_LENGTH) + ((row - 1) * GAP_BETWEEN_UNITS);
            var groundSideXLength = (NEIGHBOURHOOD_BORDER_WIDTH * 2) + groundSideXLengthForData;
            var groundSideZLength = (NEIGHBOURHOOD_BORDER_WIDTH * 2) + groundSideZLengthForData;
            var ground = new THREE.Mesh(
                new THREE.PlaneBufferGeometry(groundSideXLength, groundSideZLength),
                new THREE.MeshPhongMaterial({ color: 0x7d7d7d }));
            ground.position.x = xBoundary + (groundSideXLengthForData / 2) - (UNIT_SIDE_LENGTH / 2);
            ground.position.z = zBoundary + (groundSideZLengthForData / 2) - (UNIT_SIDE_LENGTH / 2);
            ground.rotation.x = -Math.PI / 2;
            ground.receiveShadow = true;

            return new CodeNeighbourhood(id, name, codeUnits, ground, groundSideXLength, groundSideZLength);
        };

        return CityPlanner;
    }();
});
