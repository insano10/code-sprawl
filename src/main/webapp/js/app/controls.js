define(["three", "camera", "scene"], function (THREE, camera, scene)
{
    var controls = new THREE.PointerLockControls(camera);
    scene.add( controls.getObject() );

    return controls;
});
