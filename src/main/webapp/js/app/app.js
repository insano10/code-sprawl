define(["three", "camera", "controls", "geometry", "light", "material", "renderer", "scene", "data", "crossHair", "mousePointerLock"],
    function (THREE, camera, controls, geometry, light, material, renderer, scene, cubeData, crossHair, mousePointerLock)
    {
        var app = {
            objects:  [],
            init:    function ()
            {
                //grab the mouse to navigate around the 3D model
                mousePointerLock.grabPointer(function() {
                    controls.setEnabled(true);
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
                        app.objects.push(cube);
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
                controls.update();
                crossHair.update(controls, app.objects);
                renderer.render(scene, camera);
            }
        };
        return app;
    });
