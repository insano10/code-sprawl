define(["three", "camera", "scene", "cameraMover"], function (THREE, camera, scene, cameraMover)
{
    var controls = new THREE.FPSControls(camera, {x: 1144, y: 1000, z: 1110}, -0.75, 0.82, cameraMover);
    controls.addToScene(scene);

    return controls;
});
