define( ["jquery", "three", "scene", "sceneObjects"], function ($, THREE, scene, SceneObjects) {

    return function()
    {
        function CodeNeighbourhood(id, name, codeUnitArray, ground, xLength, zLength)
        {
            this.id = "Neighbourhood-" + id;
            this.name = name;
            this.ground = ground;
            this.xLength = xLength;
            this.zLength = zLength;
            this.sceneObjects = new SceneObjects();

            var codeNeighbourhood = this;
            $.each(codeUnitArray, function(idx, codeUnit) {
               codeNeighbourhood.sceneObjects.add(codeUnit);
            });
        }

        CodeNeighbourhood.prototype.addToScene = function addToScene(scene)
        {
            this.sceneObjects.addToScene(scene, true);
            scene.add(this.ground);
        };

        CodeNeighbourhood.prototype.removeFromScene = function removeFromScene(scene)
        {
            this.sceneObjects.clear(scene);
            scene.remove(this.ground);
        };

        CodeNeighbourhood.prototype.raycast = function raycast(raycaster, intersects)
        {
            this.sceneObjects.raycast(raycaster, intersects);
        };

        CodeNeighbourhood.prototype.getName = function getName()
        {
            return this.name;
        };

        CodeNeighbourhood.prototype.getObjectById = function getObjectById(objectId)
        {
            return this.sceneObjects.getObjectById(objectId);
        };

        CodeNeighbourhood.prototype.getId = function getId()
        {
            return this.id;
        };

        CodeNeighbourhood.prototype.getXLength = function getXLength()
        {
            return this.xLength;
        };

        CodeNeighbourhood.prototype.getZLength = function getZLength()
        {
            return this.zLength;
        };

        return CodeNeighbourhood;
    }();
} );
