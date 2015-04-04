define(["three", "camera", "scene", "informationPanel"], function (THREE, camera, scene, informationPanel)
{
    var currentIntersectedObject;

    var showDebugPickingLine = false;
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

    var getTarget = function (controls, sceneObjects)
    {
        var vectorDirection = new THREE.Vector3();
        controls.getDirection(vectorDirection);

        var rayCaster = new THREE.Raycaster(controls.getPosition(), vectorDirection);
        var intersections = rayCaster.intersectObjects([sceneObjects]);

        return (intersections.length > 0) ? intersections[0] : null;
    };

    return {

        update: function (controls, sceneObjects)
        {
            var target = getTarget(controls, sceneObjects);

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

                    var codeBarUnit = sceneObjects.getObjectById(currentIntersectedObject.id);
                    informationPanel.updateTarget(codeBarUnit);
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
                informationPanel.clearTarget();
            }

        }
    };
});
