define( ["jquery", "three", "scene", "sceneObjects"], function ($, THREE, scene, SceneObjects) {

    return function()
    {
        function FileNeighbourhood(id, name, fileUnitArray, ground, xLength, zLength)
        {
            this.id = "Neighbourhood-" + id;
            this.name = name;
            this.ground = ground;
            this.xLength = xLength;
            this.zLength = zLength;
            this.sceneObjects = new SceneObjects();

            var fileNeighbourhood = this;
            $.each(fileUnitArray, function(idx, fileUnit) {
               fileNeighbourhood.sceneObjects.add(fileUnit);
            });
        }

        FileNeighbourhood.prototype.addToScene = function addToScene(scene)
        {
            this.sceneObjects.addToScene(scene, true);
            scene.add(this.ground);
        };

        FileNeighbourhood.prototype.removeFromScene = function removeFromScene(scene)
        {
            this.sceneObjects.clear(scene);
            scene.remove(this.ground);
        };

        FileNeighbourhood.prototype.raycast = function raycast(raycaster, intersects)
        {
            this.sceneObjects.raycast(raycaster, intersects);
        };

        FileNeighbourhood.prototype.getName = function getName()
        {
            return this.name;
        };

        FileNeighbourhood.prototype.getObjectById = function getObjectById(objectId)
        {
            return this.sceneObjects.getObjectById(objectId);
        };

        FileNeighbourhood.prototype.getId = function getId()
        {
            return this.id;
        };

        FileNeighbourhood.prototype.getXLength = function getXLength()
        {
            return this.xLength;
        };

        FileNeighbourhood.prototype.getZLength = function getZLength()
        {
            return this.zLength;
        };

        return FileNeighbourhood;
    }();
} );
