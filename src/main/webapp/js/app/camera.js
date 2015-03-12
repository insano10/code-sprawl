define(["three", "container", "scene"], function (THREE, container, scene)
{
    var camera = new THREE.PerspectiveCamera(30, window.innerWidth / window.innerHeight, 1, 100000);

    camera.position.x = 0;
    camera.position.y = 50;
    camera.position.z = 1000;

    var updateSize = function ()
    {
        camera.aspect = container.offsetWidth / container.offsetHeight;
        camera.updateProjectionMatrix();
    };
    window.addEventListener('resize', updateSize, false);
    updateSize();

    return camera;
});
