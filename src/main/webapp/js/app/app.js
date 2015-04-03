define(["three", "camera", "controls", "light", "renderer", "scene", "data", "crossHair", "mousePointerLock", "codeUnitBar", "informationPanel"],
    function (THREE, camera, controls, light, renderer, scene, cubeData, crossHair, mousePointerLock, CodeUnitBar, informationPanel)
    {
        var app = {
            objectArray:      [],
            objectMap:        {},
            isAnimating:      false,
            animationJob:     null,
            init:             function ()
            {
                //grab the mouse to navigate around the 3D model
                mousePointerLock.grabPointer(
                    function ()
                    {
                        controls.setEnabled(true);
                        controls.initialise({x: 1144, y: 1000, z: 1110}, -0.75, 0.82);
                        app.startAnimation();
                    },
                    function ()
                    {
                        app.cancelAnimation();
                    });

                var groundBorderWidth = 100;
                var gapBetweenCubes = 10;
                var distanceFromGround = 20;
                var cubeOffset = gapBetweenCubes + cubeData.dataSideLength;

                for (var i = 0; i < cubeData.dataArrayWidth; i++)
                {

                    for (var j = 0; j < cubeData.dataArrayHeight; j++)
                    {

                        var cubeGeometry = cubeData.data[i][j];

                        var position = new THREE.Vector3();
                        position.x = cubeOffset * i;
                        position.y = distanceFromGround + (cubeGeometry[1] / 2);
                        position.z = cubeOffset * j;
                        var unit = new CodeUnitBar(cubeGeometry[0], cubeGeometry[1], cubeGeometry[2], "Bar@ " + JSON.stringify(position));
                        unit.addToScene(scene, position);

                        app.objectArray.push(unit);
                        app.objectMap[unit.getId()] = unit;
                    }
                }

                //ground
                var groundSideLengthForData = (cubeData.dataArrayWidth * cubeData.dataSideLength) + ((cubeData.dataArrayWidth - 1) * gapBetweenCubes);
                var groundSideLength = (groundBorderWidth * 2) + groundSideLengthForData;
                var plane = new THREE.Mesh(new THREE.PlaneBufferGeometry(groundSideLength, groundSideLength),
                    new THREE.MeshPhongMaterial({ color: 0x7d7d7d }));
                plane.position.x = (groundSideLengthForData / 2) - (cubeData.dataSideLength / 2);
                plane.position.z = (groundSideLengthForData / 2) - (cubeData.dataSideLength / 2);
                plane.rotation.x = -Math.PI / 2;
                plane.receiveShadow = true;

                scene.add(plane);
                app.isAnimating = true;
            },
            animate:          function ()
            {
                if (app.isAnimating)
                {
                    app.animationJob = window.requestAnimationFrame(app.animate);
                    controls.update();
                    crossHair.update(controls, app.objectArray, app.objectMap);
                    renderer.render(scene, camera);
                    informationPanel.draw();
                }
            },
            startAnimation:   function ()
            {
                if (!app.animationJob)
                {
                    app.isAnimating = true;
                    app.animate();
                }
            },
            cancelAnimation:  function ()
            {
                app.isAnimating = false;
                if (app.animationJob)
                {
                    console.log("Cancelling animation");
                    app.animationJob = null;
                }
            }
        };
        return app;
    });
