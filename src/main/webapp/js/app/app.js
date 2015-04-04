define(["three", "camera", "controls", "light", "renderer", "scene", "sceneObjects", "crossHair", "mousePointerLock", "codeGroup", "informationPanel"],
    function (THREE, camera, controls, light, renderer, scene, SceneObjects, crossHair, mousePointerLock, CodeGroup, informationPanel)
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

                createCodeGroup(0, this.topLevelObjects, 0, 0);
                createCodeGroup(1, this.topLevelObjects, 800, 0);
                createCodeGroup(2, this.topLevelObjects, 0, 800);
                createCodeGroup(3, this.topLevelObjects, 800, 800);

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

            var createCodeGroup = function createCodeGroup(id, sceneObjects, xPositionOffset, zPositionOffset)
            {
                var codeGroup = new CodeGroup(id, "Hello Code Group " + id, xPositionOffset, zPositionOffset);
                codeGroup.addToScene(scene);
                sceneObjects.add(codeGroup);
            };

            return Application;

        }();
    });
