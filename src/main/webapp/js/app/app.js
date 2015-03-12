define(["three", "camera", "controls", "geometry", "light", "material", "renderer", "scene", "data", "mousePointerLock"],
    function (THREE, camera, controls, geometry, light, material, renderer, scene, cubeData, mousePointerLock)
    {
        var controlsEnabled = false;
        var clock = new THREE.Clock();
        var raycaster = new THREE.Raycaster( new THREE.Vector3(), new THREE.Vector3( 0, - 1, 0 ), 0, 10 );
        var velocity = new THREE.Vector3();

        var app = {
            meshes:  [],
            init:    function ()
            {
                //grab the mouse to navigate around the 3D model
                mousePointerLock.grabPointer(function() {
                    controlsEnabled = true;
                    controls.enabled = true;
                });

                var groundBorderWidth = 100;
                var gapBetweenCubes = 10;
                var distanceFromGround = 20;
                var cubeOffset = gapBetweenCubes + cubeData.dataSideLength;

                for(var i=0 ; i<cubeData.dataArrayWidth ; i++) {

                    for(var j=0 ; j<cubeData.dataArrayHeight ; j++) {

                        var color = 0x0aeedf;

                        if(i==0 && j==0) {
                            color = 0xff0000;
                        }

                        var cubeGeometry = cubeData.data[i][j];
                        var cube = new THREE.Mesh(new THREE.BoxGeometry(cubeGeometry[0], cubeGeometry[1], cubeGeometry[2]),
                                                  new THREE.MeshLambertMaterial({ color: color }));
                        cube.castShadow = true;
                        cube.position.x = cubeOffset*i;
                        cube.position.y = distanceFromGround + (cubeGeometry[1]/2);
                        cube.position.z = cubeOffset*j;
                        scene.add(cube);
                    }
                }

                //ground
                var groundSideLengthForData = (cubeData.dataArrayWidth * cubeData.dataSideLength) + ((cubeData.dataArrayWidth-1) * gapBetweenCubes);
                var groundSideLength = (groundBorderWidth * 2) + groundSideLengthForData;
                var plane = new THREE.Mesh(new THREE.PlaneBufferGeometry(groundSideLength, groundSideLength),
                                           new THREE.MeshPhongMaterial({ color: 0x7d7d7d }));
                plane.position.x =(groundSideLengthForData/2) - (cubeData.dataSideLength/2);
                plane.position.z =(groundSideLengthForData/2) - (cubeData.dataSideLength/2);
                plane.rotation.x = -Math.PI / 2;
                plane.receiveShadow = true;

                scene.add(plane);

            },
            animate: function ()
            {
                window.requestAnimationFrame(app.animate);

                if ( controlsEnabled ) {
                    console.log("controls enabled");
                    raycaster.ray.origin.copy( controls.getObject().position );
                    raycaster.ray.origin.y -= 10;

                    var objects = [];
                    var intersections = raycaster.intersectObjects( objects );

                    var isOnObject = intersections.length > 0;

                    var delta = clock.getDelta();

                    velocity.x -= velocity.x * 10.0 * delta;
                    velocity.z -= velocity.z * 10.0 * delta;

                    velocity.y -= 9.8 * 100.0 * delta; // 100.0 = mass

//                    if ( moveForward ) velocity.z -= 400.0 * delta;
//                    if ( moveBackward ) velocity.z += 400.0 * delta;

//                    if ( moveLeft ) velocity.x -= 400.0 * delta;
//                    if ( moveRight ) velocity.x += 400.0 * delta;

                    if ( isOnObject === true ) {
                        velocity.y = Math.max( 0, velocity.y );

                    }

                    controls.getObject().translateX( velocity.x * delta );
                    controls.getObject().translateY( velocity.y * delta );
                    controls.getObject().translateZ( velocity.z * delta );

                    if ( controls.getObject().position.y < 10 ) {

                        velocity.y = 0;
                        controls.getObject().position.y = 10;

                    }
                }

                renderer.render(scene, camera);
            }
        };
        return app;
    });
