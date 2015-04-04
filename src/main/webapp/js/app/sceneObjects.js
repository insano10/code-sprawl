define( [], function () {

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
            return this.objectMap[objectId];
        };

        return SceneObjects;
    }();
} );
