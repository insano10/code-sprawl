define(["three", "camera", "scene"], function (THREE, camera, scene)
{
    var controls = new THREE.FPSControls(camera, {x: 1144, y: 1000, z: 1110}, -0.75, 0.82);
    controls.addToScene(scene);

    return controls;
});
