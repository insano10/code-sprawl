define(["three", "camera", "scene", "InformationPanel"], function (THREE, camera, scene, InformationPanel)
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

    var deselectObject = function (object)
    {
        if (object)
        {
            scene.remove(object);
        }
    };

    var createSelectedObject = function (object)
    {
        var parameters = object.geometry.parameters;
        var newWidth = parameters.width + 10;
        var newHeight = parameters.height + 2;
        var newDepth = parameters.depth + 10;

        var selectedMesh = new THREE.Mesh(
            new THREE.BoxGeometry(newWidth, newHeight, newDepth),
            new THREE.MeshLambertMaterial({ color: 0x0aeedf, emissive: 0xff0000, wrapAround: true }));

        selectedMesh.position.x = object.position.x;
        selectedMesh.position.y = object.position.y;
        selectedMesh.position.z = object.position.z;

        return selectedMesh;
    };

    return {

        update: function (controls, sceneObjects)
        {
            var target = getTarget(controls, sceneObjects);

            if (target != null)
            {
                if (currentIntersectedObject != target.object)
                {
                    deselectObject(currentIntersectedObject);

                    //select new object
                    currentIntersectedObject = createSelectedObject(target.object);
                    scene.add(currentIntersectedObject);

                    var fileUnitBar = sceneObjects.getObjectById(target.object.id);
                    InformationPanel.updateTarget(fileUnitBar);
                }

                if (showDebugPickingLine)
                {
                    showPickingLine(controls.getPosition(), target.point);
                }
            }
            else
            {
                deselectObject(currentIntersectedObject);

                currentIntersectedObject = null;
                InformationPanel.clearTarget();
            }

        },
        clear: function() {

            deselectObject(currentIntersectedObject);
        }
    };
});
