define(["three", "camera", "scene"], function (THREE, camera, scene)
{
    var controls = new THREE.FPSControls(camera);
    controls.addToScene(scene);

    return controls;
});
