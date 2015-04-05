define( ["jquery"], function ($) {

    return function()
    {
        function SceneObjects()
        {
            this.objectArray = [];
            this.objectMap = {};
        }

        SceneObjects.prototype.add = function add(object)
        {
            this.objectArray.push(object);
            this.objectMap[object.getId()] = object;
        };

        SceneObjects.prototype.getObjectArray = function getObjectArray()
        {
            return this.objectArray;
        };

        SceneObjects.prototype.getObjectById = function getObjectById(objectId)
        {
            var objectToReturn = this.objectMap[objectId];

            if(!objectToReturn)
            {
                $.each(this.getObjectArray(), function(idx, object) {
                    objectToReturn = object.getObjectById(objectId);

                    if(objectToReturn)
                    {
                        return false; //break
                    }
                });
            }

            return objectToReturn;
        };

        SceneObjects.prototype.addToScene = function addToScene(scene)
        {
            $.each(this.getObjectArray(), function(idx, object) {
               object.addToScene(scene);
            });
        };

        SceneObjects.prototype.clear = function clear(scene)
        {
            $.each(this.getObjectArray(), function(idx, object) {
                object.removeFromScene(scene);
            });

            this.objectArray = [];
            this.objectMap = {};
        };

        SceneObjects.prototype.raycast = function raycast(raycaster, intersects)
        {
            $.each(this.getObjectArray(), function(idx, object) {
                object.raycast(raycaster, intersects);
            });
        };

        return SceneObjects;
    }();
} );
