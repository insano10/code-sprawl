define( ["three"], function (THREE) {

    return function()
    {
        var DEFAULT_COLOUR = 0x0aeedf;

        function CodeUnitBar(width, height, depth, name)
        {
            this.width = width;
            this.height = height;
            this.depth = depth;
            this.name = name;
            this.mesh = null;
        }

        CodeUnitBar.prototype.addToScene = function addToScene(scene, position3D)
        {
            this.mesh = new THREE.Mesh(
                new THREE.BoxGeometry(this.width, this.height, this.depth),
                new THREE.MeshLambertMaterial({ color: DEFAULT_COLOUR }));
            this.mesh.castShadow = true;
            this.mesh.position.x = position3D.x;
            this.mesh.position.y = position3D.y;
            this.mesh.position.z = position3D.z;

            scene.add(this.mesh);
        };

        CodeUnitBar.prototype.raycast = function raycast(raycaster, intersects)
        {
            this.mesh.raycast(raycaster, intersects);
        };

        CodeUnitBar.prototype.getName = function getName()
        {
            return this.name;
        };

        return CodeUnitBar;
    }();
} );
