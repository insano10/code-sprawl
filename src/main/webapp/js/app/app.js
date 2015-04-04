define(["three", "camera", "controls", "light", "renderer", "scene", "sceneObjects", "data", "crossHair", "mousePointerLock", "codeGroup", "informationPanel"],
    function (THREE, camera, controls, light, renderer, scene, SceneObjects, cubeData, crossHair, mousePointerLock, CodeGroup, informationPanel)
    {
        return function ()
        {
            function Application()
            {
                this.topLevelObjects = new SceneObjects();
                this.isAnimating = false;
                this.currentAnimationFrame = null;
            }

            Application.prototype.initialise = function initialise()
            {
                grabPointer(this);

                var codeGroup = new CodeGroup(0, "Hello Code Group");
                codeGroup.addToScene(scene);

                this.topLevelObjects.add(codeGroup);
                this.isAnimating = true;
            };

            Application.prototype.animate = function animate()
            {
                if (this.isAnimating)
                {
                    this.currentAnimationFrame = window.requestAnimationFrame(this.animate.bind(this));
                    controls.update();
                    crossHair.update(controls, this.topLevelObjects);
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
