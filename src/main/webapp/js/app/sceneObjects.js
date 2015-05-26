define(["jquery"], function ($)
{

    return function ()
    {
        function SceneObjects()
        {
            this.objectArray = [];
            this.objectMap = {};
            this.mergedSceneObjects = null;
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

            if (!objectToReturn)
            {
                $.each(this.getObjectArray(), function (idx, object)
                {
                    objectToReturn = object.getObjectById(objectId);

                    if (objectToReturn)
                    {
                        return false; //break
                    }
                });
            }

            return objectToReturn;
        };

        SceneObjects.prototype.addToScene = function addToScene(scene, geometryCollection)
        {
            /*
            Tell all children to merge themselves into the scene.
            This may involve adding components directly to the scene or merging into the geometry collection to be
            rendered as one large mesh (this is done for the FileUnits as they all have the same material)
             */
            $.each(this.getObjectArray(), function (idx, object)
            {
                object.mergeIntoScene(scene, geometryCollection);
            });

            console.log("creating mega mesh");
            var total = new THREE.Mesh(geometryCollection, new THREE.MeshLambertMaterial({ color: 0x0aeedf, wrapAround: true }));
            scene.add(total);

            this.mergedSceneObjects = total;
        };

        SceneObjects.prototype.clear = function clear(scene)
        {
            $.each(this.getObjectArray(), function (idx, object)
            {
                object.removeFromScene(scene);
            });

            if (this.mergedSceneObjects)
            {
                scene.remove(this.mergedSceneObjects);
            }

            this.objectArray = [];
            this.objectMap = {};
        };

        SceneObjects.prototype.raycast = function raycast(raycaster, intersects)
        {
            $.each(this.getObjectArray(), function (idx, object)
            {
                object.raycast(raycaster, intersects);
            });
        };

        return SceneObjects;
    }();
});
