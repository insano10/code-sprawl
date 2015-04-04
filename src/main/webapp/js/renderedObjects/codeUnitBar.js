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

            this.mesh = new THREE.Mesh(
                new THREE.BoxGeometry(this.width, this.height, this.depth),
                new THREE.MeshLambertMaterial({ color: DEFAULT_COLOUR }));
            this.mesh.castShadow = true;
        }

        CodeUnitBar.prototype.setPosition = function setPosition(position3D)
        {
            this.mesh.position.x = position3D.x;
            this.mesh.position.y = position3D.y;
            this.mesh.position.z = position3D.z;
        };

        CodeUnitBar.prototype.addToScene = function addToScene(scene)
        {
            scene.add(this.mesh);
        };

        CodeUnitBar.prototype.raycast = function raycast(raycaster, intersects)
        {
            this.mesh.raycast(raycaster, intersects);
        };

        CodeUnitBar.prototype.getId = function getId()
        {
            return this.mesh.id;
        };

        CodeUnitBar.prototype.getName = function getName()
        {
            return this.name;
        };

        CodeUnitBar.prototype.getObjectById = function getObjectById(objectId)
        {
            return undefined;
        };

        return CodeUnitBar;
    }();
} );
