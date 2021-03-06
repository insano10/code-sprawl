define(["three"], function (THREE)
{

    var DEFAULT_COLOUR = 0x0aeedf;
    var MATERIAL = new THREE.MeshLambertMaterial({ color: DEFAULT_COLOUR, wrapAround: true });

    return function ()
    {
        function FileUnit(width, height, depth, name, groupName, lineCount, fileExtension)
        {
            this.width = width;
            this.height = height;
            this.depth = depth;
            this.name = name;
            this.groupName = groupName;
            this.lineCount = lineCount;
            this.fileExtension = fileExtension;

            var boxGeometry = new THREE.BoxGeometry(this.width, this.height, this.depth);

            //remove the bottom face as it is never seen
            boxGeometry.faces.splice(6, 2);

            this.mesh = new THREE.Mesh(boxGeometry, MATERIAL);
            this.mesh.name = this.getFullyQualifiedName();
            this.mesh.castShadow = true;
            this.vcsInfo = null;
        }

        FileUnit.prototype.setVcsInfo = function setVcsInfo(vcsInfo)
        {
            this.vcsInfo = vcsInfo;
        };

        FileUnit.prototype.setPosition = function setPosition(position3D)
        {
            this.mesh.position.x = position3D.x;
            this.mesh.position.y = position3D.y;
            this.mesh.position.z = position3D.z;
        };

        FileUnit.prototype.update = function update()
        {
            this.mesh.updateMatrixWorld(true);
        };

        FileUnit.prototype.mergeIntoScene = function mergeIntoScene(scene, geometry)
        {
            this.mesh.updateMatrix();
            geometry.merge(this.mesh.geometry, this.mesh.matrix);
        };

        FileUnit.prototype.addToOctree = function addToOctree(octree)
        {
            octree.add(this.mesh);
        };

        FileUnit.prototype.removeFromScene = function removeFromScene(scene)
        {
            scene.remove(this.mesh);
        };

        FileUnit.prototype.raycast = function raycast(raycaster, intersects)
        {
            this.mesh.raycast(raycaster, intersects);
        };

        FileUnit.prototype.getId = function getId()
        {
            return this.mesh.id;
        };

        FileUnit.prototype.getProperties = function getProperties()
        {
            return {
                groupName:     this.groupName,
                name:          this.name,
                fileExtension: this.fileExtension,
                lineCount:     this.lineCount
            };
        };

        FileUnit.prototype.getFullyQualifiedName = function getFullyQualifiedName()
        {
            return this.groupName + "/" + this.name;
        };

        FileUnit.prototype.getVcsHistoryId = function getVcsHistoryId()
        {
            return this.groupName + ":" + this.name + "." + this.fileExtension;
        };

        FileUnit.prototype.getVcsInfo = function getVcsInfo()
        {
            return this.vcsInfo;
        };

        FileUnit.prototype.getObjectById = function getObjectById(objectId)
        {
            return undefined;
        };

        FileUnit.prototype.getFocusPoint = function getFocusPoint()
        {
            return {x: this.mesh.position.x + (this.width / 2), y: this.height + 1200, z: this.mesh.position.z + (this.depth / 2)};
        };

        return FileUnit;
    }();
});
