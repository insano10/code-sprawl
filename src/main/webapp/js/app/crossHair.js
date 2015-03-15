define(["three", "camera"], function (THREE, camera)
{
    var rayCaster = new THREE.Raycaster();
    var mouse = new THREE.Vector3();
    var INTERSECTED;

    function onDocumentMouseMove( event ) {

        event.preventDefault();

        mouse.set(
                ( event.clientX / window.innerWidth ) * 2 - 1,
                - ( event.clientY / window.innerHeight ) * 2 + 1,
            0.5 );
    }

    document.addEventListener( 'mousemove', onDocumentMouseMove, false );


    return {

        update: function (controls, objects)
        {
            rayCaster.ray.origin.copy(controls.getPosition());
            rayCaster.ray.direction.copy(mouse);

            var intersections = rayCaster.intersectObjects(objects);
            console.log("origin: " + JSON.stringify(controls.getPosition()));
            console.log("direction: " + JSON.stringify(camera.position));
            console.log(intersections.length);

            if ( intersections.length > 0 ) {

                if ( INTERSECTED != intersections[ 0 ].object ) {

                    if ( INTERSECTED ) INTERSECTED.material.emissive.setHex( INTERSECTED.currentHex );

                    INTERSECTED = intersections[ 0 ].object;
                    INTERSECTED.currentHex = INTERSECTED.material.emissive.getHex();
                    INTERSECTED.material.emissive.setHex( 0xff0000 );
                }

            } else {

                if ( INTERSECTED ) INTERSECTED.material.emissive.setHex( INTERSECTED.currentHex );

                INTERSECTED = null;

            }
        }
    };
});
