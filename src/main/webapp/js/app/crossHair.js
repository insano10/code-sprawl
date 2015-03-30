define(["three", "camera", "scene", "renderer"], function (THREE, camera, scene, renderer)
{
    var currentIntersectedObject;
    var debugPickingLine;

    return {

        update: function (controls, objects)
        {
            var vectorDirection = new THREE.Vector3();
            controls.getDirection(vectorDirection);

            var rayCaster = new THREE.Raycaster(controls.getPosition(), vectorDirection );

            var intersections = rayCaster.intersectObjects(objects);

            if ( intersections.length > 0 ) {

                if ( currentIntersectedObject != intersections[ 0 ].object ) {

                    if ( currentIntersectedObject ) currentIntersectedObject.material.emissive.setHex( currentIntersectedObject.currentHex );

                    currentIntersectedObject = intersections[ 0 ].object;
                    currentIntersectedObject.currentHex = currentIntersectedObject.material.emissive.getHex();
                    currentIntersectedObject.material.emissive.setHex( 0xff0000 );
                }

                var geometry = new THREE.Geometry();

                //vertex at ray origin
                geometry.vertices.push( controls.getPosition() );
                //vertext at object intersection
                geometry.vertices.push( intersections[0].point );

                scene.remove(debugPickingLine);
                debugPickingLine = new THREE.Line(geometry, new THREE.LineBasicMaterial({color: 0x990000}));
                scene.add(debugPickingLine);

            } else {

                if ( currentIntersectedObject ) currentIntersectedObject.material.emissive.setHex( currentIntersectedObject.currentHex );

                currentIntersectedObject = null;

            }
        }
    };
});
