define(["three", "scene"], function (THREE, scene)
{
    scene.add(new THREE.AmbientLight(0x666666));

    var light;

    light = new THREE.DirectionalLight(0xdfebff, 1.0);
    light.position.set(300, 400, 50);
    light.position.multiplyScalar(1.3);

    light.castShadow = true;
//    light.shadowCameraVisible = true;

    light.shadowMapWidth = 512;
    light.shadowMapHeight = 512;

    var d = 200;

    light.shadowCameraLeft = -d;
    light.shadowCameraRight = d;
    light.shadowCameraTop = d;
    light.shadowCameraBottom = -d;

    light.shadowCameraFar = 1000;
    light.shadowDarkness = 0.2;

    scene.add(light);

});
