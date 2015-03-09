define(["three", "container"], function (THREE, container)
{
    camera = new THREE.PerspectiveCamera(30, window.innerWidth / window.innerHeight, 1, 100000);
    camera.position.x = 1200;
    camera.position.y = 1000;
    camera.position.z = 1000;
    camera.lookAt({ x: 0, y: 0, z: 0 });

    var updateSize = function ()
    {
        camera.aspect = container.offsetWidth / container.offsetHeight;
        camera.updateProjectionMatrix();
    };
    window.addEventListener('resize', updateSize, false);
    updateSize();

    return camera;
});
