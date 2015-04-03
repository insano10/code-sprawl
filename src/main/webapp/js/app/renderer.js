define(["three", "container"], function (THREE, container)
{
    container.innerHTML = "";
    var renderer = new THREE.WebGLRenderer({ alpha: true });
    renderer.setClearColor( 0xffffff, 1);
    renderer.sortObjects = false;
    renderer.autoClear = false;
    renderer.shadowMapEnabled = true;
    renderer.shadowMapSoft = true;
    renderer.domElement.classList.add("main-canvas");
    container.appendChild(renderer.domElement);

    var updateSize = function ()
    {
        renderer.setSize(container.offsetWidth, container.offsetHeight);
    };
    window.addEventListener('resize', updateSize, false);
    updateSize();

    return renderer;
});
