define(["three", "camera", "controls", "geometry", "light", "material", "renderer", "scene"],
    function (THREE, camera, controls, geometry, light, material, renderer, scene)
    {
        var app = {
            meshes:  [],
            init:    function ()
            {
                var boxgeometry = new THREE.BoxGeometry(50, 100, 50);
                var boxmaterial = new THREE.MeshLambertMaterial({ color: 0x0aeedf });
                var cube = new THREE.Mesh(boxgeometry, boxmaterial);
                cube.castShadow = true;
                cube.position.x = 0;
                cube.position.y = 60;
                cube.position.z = 0;
                scene.add(cube);

                var boxgeometry2 = new THREE.BoxGeometry(50, 50, 50);
                var cube2 = new THREE.Mesh(boxgeometry2, boxmaterial);
                cube2.castShadow = true;
                cube2.position.x = 0;
                cube2.position.y = 35;
                cube2.position.z = 60;
                scene.add(cube2);


                //ground
                var groundMaterial = new THREE.MeshPhongMaterial({ color: 0x7d7d7d });
                plane = new THREE.Mesh(new THREE.PlaneBufferGeometry(500, 500), groundMaterial);
                plane.rotation.x = -Math.PI / 2;
                plane.receiveShadow = true;

                scene.add(plane);

            },
            animate: function ()
            {
                window.requestAnimationFrame(app.animate);
                controls.update();
                renderer.render(scene, camera);
            }
        };
        return app;
    });
