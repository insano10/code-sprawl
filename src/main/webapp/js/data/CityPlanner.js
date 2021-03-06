define(["jquery", "TestCityBlueprint", "FileNeighbourhood", "FileUnit", "sceneObjects", "scene"],
    function ($, TestCityBlueprint, FileNeighbourhood, FileUnit, SceneObjects, scene)
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
                this.sceneObjects = new SceneObjects();
                this.fileUnits = {};
                this.visualisationSourceDir = "";
                this.vcsHistory = {};
                this.octree = new THREE.Octree( {
//                    scene: scene,
                    undeferred: false,
                    depthMax: Infinity,
                    objectsThreshold: 8,
                    overlapPct: 0.15
                } );
            }

            CityPlanner.prototype.loadTestCity = function loadTestCity()
            {
                this.loadCity(new TestCityBlueprint());
            };

            CityPlanner.prototype.loadCity = function loadCity(bluePrint)
            {
                clearCity(this);

                this.visualisationSourceDir = bluePrint.getVisualisationSourceDir();
                this.vcsHistory = bluePrint.getVcsHistoryMap();

                console.log("Loading city inhabitants");
                loadInhabitants(this, bluePrint);

                console.log("Building neighbourhoods");
                buildNeighbourhoods(this);

                //we don't need this any more
                this.vcsHistory = {};
            };

            CityPlanner.prototype.getSceneObjects = function getSceneObjects()
            {
                return this.sceneObjects;
            };

            CityPlanner.prototype.getFileUnits = function getFileUnits()
            {
                return this.fileUnits;
            };

            CityPlanner.prototype.getOctree = function getOctree()
            {
                return this.octree;
            };

            CityPlanner.prototype.indexFileUnitsIntoScene = function indexFileUnitsIntoScene()
            {
                $.each(this.fileUnits, function(key, unit) {
                    unit.update();
                });
                this.octree.update();
            };

            var loadInhabitants = function loadInhabitants(cityPlanner, bluePrint)
            {
                var data = bluePrint.getInhabitants();

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
                    var neighbourhood = buildNeighbourhood(groupId++, name, unitArray, xBoundary, zBoundary, cityPlanner);
                    cityPlanner.sceneObjects.add(neighbourhood);

                    column++;
                    xBoundary += neighbourhood.getXLength();
                    maxZLengthOfRow = Math.max(maxZLengthOfRow, neighbourhood.getZLength());

                    if (column == cityWidth)
                    {
                        column = 0;
                        xBoundary = 0;
                        zBoundary += maxZLengthOfRow;
                        maxZLengthOfRow = -1;
                    }
                });

                var cityGeometry = new THREE.Geometry();
                cityPlanner.sceneObjects.addToScene(scene, cityGeometry);
            };

            var buildNeighbourhood = function buildNeighbourhood(id, name, unitArray, xBoundary, zBoundary, cityPlanner)
            {
                var width = Math.ceil(Math.sqrt(unitArray.length));
                var column = 0;
                var row = 0;

                console.log("Neighbourhood '" + name + "' has " + unitArray.length + " inhabitants and is " + width + " units wide");

                var cubeOffset = GAP_BETWEEN_UNITS + UNIT_SIDE_LENGTH;
                var neighbourhoodUnits = [];

                // Arrange the file units as close to a square as possible
                $.each(unitArray, function (idx, unit)
                {
                    var position = new THREE.Vector3();
                    position.x = xBoundary + cubeOffset * column;
                    position.y = DISTANCE_FROM_GROUND + (unit.lineCount / 2);
                    position.z = zBoundary + cubeOffset * row;

                    var fileUnit = new FileUnit(UNIT_SIDE_LENGTH, unit.lineCount, UNIT_SIDE_LENGTH, unit.name, unit.groupName, unit.lineCount, unit.fileExtension);
                    fileUnit.setPosition(position);
                    neighbourhoodUnits.push(fileUnit);
                    indexFileUnit(cityPlanner, fileUnit);

                    column++;

                    if (column == width)
                    {
                        column = 0;
                        row++;
                    }

                    var historyKey = getVcsHistoryKey(cityPlanner, fileUnit);

                    fileUnit.setVcsInfo(cityPlanner.vcsHistory[historyKey]);
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

                return new FileNeighbourhood(id, name, neighbourhoodUnits, ground, groundSideXLength, groundSideZLength);
            };

            var clearCity = function clearCity(cityPlanner)
            {
                cityPlanner.sceneObjects.clear(scene);
                cityPlanner.neighbourhoodToUnitArrayMap = {};
                cityPlanner.fileUnits = {};
            };

            var getVcsHistoryKey = function(cityPlanner, fileUnit)
            {
                if(cityPlanner.visualisationSourceDir.length > 0)
                {
                    var delimiter = "";
                    if(!/\/$/.test(cityPlanner.visualisationSourceDir))
                    {
                        delimiter = "/";
                    }
                    return cityPlanner.visualisationSourceDir + delimiter + fileUnit.getVcsHistoryId();
                }
                else
                {
                    return fileUnit.getVcsHistoryId();
                }
            };

            var indexFileUnit = function indexFileUnit(cityPlanner, fileUnit)
            {
                cityPlanner.fileUnits[fileUnit.getFullyQualifiedName()] = fileUnit;
                fileUnit.addToOctree(cityPlanner.octree);
            };

            return CityPlanner;
        }();
    });
