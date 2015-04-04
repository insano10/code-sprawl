define(["three", "camera", "controls", "light", "renderer", "scene", "sceneObjects", "data", "crossHair", "mousePointerLock", "codeUnitBar", "informationPanel"],
    function (THREE, camera, controls, light, renderer, scene, SceneObjects, cubeData, crossHair, mousePointerLock, CodeUnitBar, informationPanel)
    {
        return function ()
        {
            function Application()
            {
                this.sceneObjects = new SceneObjects();
                this.isAnimating = false;
                this.currentAnimationFrame = null;
            }

            Application.prototype.initialise = function initialise()
            {
                grabPointer(this);

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

                        this.sceneObjects.add(unit);
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
                this.isAnimating = true;
            };

            Application.prototype.animate = function animate()
            {
                if (this.isAnimating)
                {
                    this.currentAnimationFrame = window.requestAnimationFrame(this.animate.bind(this));
                    controls.update();
                    crossHair.update(controls, this.sceneObjects);
                    renderer.render(scene, camera);
                    informationPanel.draw();
                }
            };

            Application.prototype.startAnimation = function startAnimation()
            {
                if (!this.currentAnimationFrame)
                {
                    this.isAnimating = true;
                    this.animate();
                }
            };

            Application.prototype.stopAnimation = function stopAnimation()
            {
                this.isAnimating = false;
                if (this.currentAnimationFrame)
                {
                    console.log("Cancelling animation");
                    window.cancelRequestAnimationFrame(this.currentAnimationFrame);
                    this.currentAnimationFrame = null;
                }
            };

            var grabPointer = function grabPointer(app)
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
                        app.stopAnimation();
                    });
            };

            return Application;

        }();
    });
