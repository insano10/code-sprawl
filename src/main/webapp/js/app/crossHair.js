define(["three", "camera", "scene", "renderer"], function (THREE, camera, scene, renderer)
{
    var rayCaster = new THREE.Raycaster();
    var rayVector = new THREE.Vector3(0, 0, 0.5);
    var INTERSECTED;
    var line;
    var x,y;

    function onMouseMove( event ) {

        event.preventDefault();

        var elem = renderer.domElement,
            boundingRect = elem.getBoundingClientRect();
        x = (event.clientX - boundingRect.left) * (elem.width / boundingRect.width),
        y = (event.clientY - boundingRect.top) * (elem.height / boundingRect.height);
    }

    document.addEventListener( 'mousemove', onMouseMove, false );


    return {

        update: function (controls, objects)
        {
            rayVector.set(((x/window.innerWidth)*2-1), (1-(y/window.innerHeight)*2), 1).unproject(camera);
            rayCaster.set(controls.getPosition(), rayVector.sub(controls.getPosition()).normalize());


            var intersections = rayCaster.intersectObjects(objects);
            console.log(intersections.length);

            if ( intersections.length > 0 ) {

                if ( INTERSECTED != intersections[ 0 ].object ) {

                    if ( INTERSECTED ) INTERSECTED.material.emissive.setHex( INTERSECTED.currentHex );

                    INTERSECTED = intersections[ 0 ].object;
                    INTERSECTED.currentHex = INTERSECTED.material.emissive.getHex();
                    INTERSECTED.material.emissive.setHex( 0xff0000 );
                }

                var geometry = new THREE.Geometry();

                //vertex at ray origin
                geometry.vertices.push( controls.getPosition() );
                //vertext at object intersection
                geometry.vertices.push( intersections[0].point );

                scene.remove(line);
                line = new THREE.Line(geometry, new THREE.LineBasicMaterial({color: 0x990000}));
                scene.add(line);

            } else {

                if ( INTERSECTED ) INTERSECTED.material.emissive.setHex( INTERSECTED.currentHex );

                INTERSECTED = null;

            }
        }
    };
});
