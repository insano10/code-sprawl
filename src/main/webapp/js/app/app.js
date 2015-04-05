define(["jquery", "three", "camera", "controls", "light", "renderer", "scene", "sceneObjects", "crossHair", "mousePointerLock", "InformationPanel"],
    function ($, THREE, camera, controls, light, renderer, scene, SceneObjects, crossHair, mousePointerLock, InformationPanel)
    {
        return function ()
        {
            function Application(cityPlanner)
            {
                this.isAnimating = false;
                this.currentAnimationFrame = null;
                this.cityPlanner = cityPlanner;
            }

            Application.prototype.initialise = function initialise()
            {
                grabPointer(this);

                var app = this;
                this.cityPlanner.loadTestCity();

                this.isAnimating = true;
            };

            Application.prototype.animate = function animate()
            {
                if (this.isAnimating)
                {
                    this.currentAnimationFrame = window.requestAnimationFrame(this.animate.bind(this));
                    controls.update();
                    crossHair.update(controls, this.cityPlanner.getSceneObjects());
                    renderer.render(scene, camera);
                    InformationPanel.draw();
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
                        controls.reset();
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
