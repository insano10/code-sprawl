define(["three", "camera", "scene", "renderer"], function (THREE, camera, scene, renderer)
{
    var currentIntersectedObject;

    var showDebugPickingLine = true;
    var debugPickingLine;

    var showPickingLine = function (origin, target)
    {
        var geometry = new THREE.Geometry();

        geometry.vertices.push(origin);
        geometry.vertices.push(target);

        scene.remove(debugPickingLine);
        debugPickingLine = new THREE.Line(geometry, new THREE.LineBasicMaterial({color: 0x990000}));
        scene.add(debugPickingLine);
    };

    var getTarget = function (controls, objects)
    {
        var vectorDirection = new THREE.Vector3();
        controls.getDirection(vectorDirection);

        var rayCaster = new THREE.Raycaster(controls.getPosition(), vectorDirection);
        var intersections = rayCaster.intersectObjects(objects);

        return (intersections.length > 0) ? intersections[0] : null;
    };

    return {

        update: function (controls, objectArray, objectMap)
        {
            var target = getTarget(controls, objectArray);

            if (target != null)
            {
                if (currentIntersectedObject != target.object)
                {

                    if (currentIntersectedObject)
                    {
                        currentIntersectedObject.material.emissive.setHex(currentIntersectedObject.currentHex);
                    }

                    currentIntersectedObject = target.object;
                    currentIntersectedObject.currentHex = currentIntersectedObject.material.emissive.getHex();
                    currentIntersectedObject.material.emissive.setHex(0xff0000);

                    var codeBarUnit = objectMap[currentIntersectedObject.id];
                    console.log("Pointing at unit " + codeBarUnit.getName());
                }

                if (showDebugPickingLine)
                {
                    showPickingLine(controls.getPosition(), target.point);
                }
            }
            else
            {
                if (currentIntersectedObject) currentIntersectedObject.material.emissive.setHex(currentIntersectedObject.currentHex);

                currentIntersectedObject = null;
            }

        }
    };
});
