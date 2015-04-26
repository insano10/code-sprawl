define( ["three"], function (THREE) {

    return function()
    {
        var DEFAULT_COLOUR = 0x0aeedf;

        function CodeUnit(width, height, depth, name, groupName, lineCount, methodCount)
        {
            this.width = width;
            this.height = height;
            this.depth = depth;
            this.name = name;
            this.groupName = groupName;
            this.lineCount = lineCount;
            this.methodCount = methodCount;

            this.mesh = new THREE.Mesh(
                new THREE.BoxGeometry(this.width, this.height, this.depth),
                new THREE.MeshLambertMaterial({ color: DEFAULT_COLOUR }));
            this.mesh.castShadow = true;
        }

        CodeUnit.prototype.setPosition = function setPosition(position3D)
        {
            this.mesh.position.x = position3D.x;
            this.mesh.position.y = position3D.y;
            this.mesh.position.z = position3D.z;
        };

        CodeUnit.prototype.addToScene = function addToScene(scene)
        {
            scene.add(this.mesh);
        };

        CodeUnit.prototype.removeFromScene = function removeFromScene(scene)
        {
            scene.remove(this.mesh);
        };

        CodeUnit.prototype.raycast = function raycast(raycaster, intersects)
        {
            this.mesh.raycast(raycaster, intersects);
        };

        CodeUnit.prototype.getId = function getId()
        {
            return this.mesh.id;
        };

        CodeUnit.prototype.getName = function getName()
        {
            return this.name;
        };

        CodeUnit.prototype.getGroupName = function getGroupName()
        {
            return this.groupName;
        };

        CodeUnit.prototype.getLineCount = function getLineCount()
        {
            return this.lineCount;
        };

        CodeUnit.prototype.getMethodCount = function getMethodCount()
        {
            return this.methodCount;
        };

        CodeUnit.prototype.getObjectById = function getObjectById(objectId)
        {
            return undefined;
        };

        return CodeUnit;
    }();
} );
