define( ["jquery","three", "CubeData", "codeUnitBar", "scene", "sceneObjects"], function ($, THREE, CubeData, CodeUnitBar, scene, SceneObjects) {

    return function()
    {
        function CodeGroup(id, name, xPositionOffset, zPositionOffset)
        {
            this.id = id;
            this.name = name;
            this.sceneObjects = new SceneObjects();
            this.ground = null;

            var gapBetweenCubes = 10;
            var distanceFromGround = 20;
            var cubeData = new CubeData();
            var cubeOffset = gapBetweenCubes + cubeData.getDataSideLength();

            for (var i = 0; i < cubeData.getDataArrayWidth(); i++)
            {
                for (var j = 0; j < cubeData.getDataArrayHeight(); j++)
                {
                    var cubeGeometry = cubeData.getGeometryAt(i,j);

                    var position = new THREE.Vector3();
                    position.x = xPositionOffset + cubeOffset * i;
                    position.y = distanceFromGround + (cubeGeometry[1] / 2);
                    position.z = zPositionOffset + cubeOffset * j;
                    var unit = new CodeUnitBar(cubeGeometry[0], cubeGeometry[1], cubeGeometry[2], "Bar@ " + JSON.stringify(position));
                    unit.setPosition(position);

                    this.sceneObjects.add(unit);
                }
            }

            var groundBorderWidth = 100;
            var groundSideLengthForData = (cubeData.getDataArrayWidth() * cubeData.getDataSideLength()) + ((cubeData.getDataArrayWidth() - 1) * gapBetweenCubes);
            var groundSideLength = (groundBorderWidth * 2) + groundSideLengthForData;
            this.ground = new THREE.Mesh(
                new THREE.PlaneBufferGeometry(groundSideLength, groundSideLength),
                new THREE.MeshPhongMaterial({ color: 0x7d7d7d }));
            this.ground.position.x = xPositionOffset + (groundSideLengthForData / 2) - (cubeData.getDataSideLength() / 2);
            this.ground.position.z = zPositionOffset + (groundSideLengthForData / 2) - (cubeData.getDataSideLength() / 2);
            this.ground.rotation.x = -Math.PI / 2;
            this.ground.receiveShadow = true;
        }

        CodeGroup.prototype.addToScene = function addToScene(scene)
        {
            this.sceneObjects.addToScene(scene);
            scene.add(this.ground);
        };

        CodeGroup.prototype.raycast = function raycast(raycaster, intersects)
        {
            this.sceneObjects.raycast(raycaster, intersects);
        };

        CodeGroup.prototype.getName = function getName()
        {
            return this.name;
        };

        CodeGroup.prototype.getObjectById = function getObjectById(objectId)
        {
            return this.sceneObjects.getObjectById(objectId);
        };

        CodeGroup.prototype.getId = function getId()
        {
            return this.id;
        };

        return CodeGroup;
    }();
} );
