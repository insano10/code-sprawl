define(["three", "container"], function (THREE, container)
{
    var camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera.position.z = 5;

    var updateSize = function ()
    {
        camera.aspect = container.offsetWidth / container.offsetHeight;
        camera.updateProjectionMatrix();
    };
    window.addEventListener('resize', updateSize, false);
    updateSize();

    return camera;
});
