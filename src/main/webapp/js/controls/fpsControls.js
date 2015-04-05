/**
 * @author insano10
 */

THREE.FPSControls = function (camera, initialPosition, initialPitchXRotationRad, initialYawYRotationRad)
{
    var pointerLockControls = new THREE.PointerLockControls(camera);
    var clock = new THREE.Clock();
    var velocity = new THREE.Vector3();
    var moveForward = false;
    var moveBackward = false;
    var moveLeft = false;
    var moveRight = false;
    var moveUp = false;
    var moveDown = false;
    var enabled = false;
    var initialPosition = initialPosition;
    var initialPitchXRotationRad = initialPitchXRotationRad;
    var initialYawYRotationRad = initialYawYRotationRad;

    var speed = 5000.0;

    var onKeyDown = function (event)
    {
        switch (event.keyCode)
        {
            case 38: // up
            case 87: // w
                moveForward = true;
                break;

            case 37: // left
            case 65: // a
                moveLeft = true;
                break;

            case 40: // down
            case 83: // s
                moveBackward = true;
                break;

            case 39: // right
            case 68: // d
                moveRight = true;
                break;

            case 82: // r
                moveUp = true;
                break;

            case 70: // f
                moveDown = true;
                break;
        }
    };

    var onKeyUp = function (event)
    {
        switch (event.keyCode)
        {
            case 38: // up
            case 87: // w
                moveForward = false;
                break;

            case 37: // left
            case 65: // a
                moveLeft = false;
                break;

            case 40: // down
            case 83: // s
                moveBackward = false;
                break;

            case 39: // right
            case 68: // d
                moveRight = false;
                break;

            case 82: // r
                moveUp = false;
                break;

            case 70: // f
                moveDown = false;
                break;
        }
    };

    this.reset = function()
    {
        pointerLockControls.getObject().position.x = initialPosition.x;
        pointerLockControls.getObject().position.y = initialPosition.y;
        pointerLockControls.getObject().position.z = initialPosition.z;
        pointerLockControls.setRotation(initialPitchXRotationRad, initialYawYRotationRad);
    };

    this.addToScene = function (scene)
    {
        document.addEventListener('keydown', onKeyDown, false);
        document.addEventListener('keyup', onKeyUp, false);

        scene.add(pointerLockControls.getObject());
    };

    this.setEnabled = function (isEnabled)
    {
        enabled = isEnabled;
        pointerLockControls.enabled = isEnabled;
    };

    this.getPosition = function()
    {
        return pointerLockControls.getObject().position;
    };

    this.getDirection = function(vector)
    {
        return pointerLockControls.getDirection(vector);
    };

    this.update = function ()
    {
        if (!enabled)
        {
            return;
        }

        var delta = clock.getDelta();

        velocity.x -= velocity.x * 10.0 * delta;
        velocity.y -= velocity.y * 10.0 * delta;
        velocity.z -= velocity.z * 10.0 * delta;

        if (moveLeft) velocity.x -= speed * delta;
        if (moveRight) velocity.x += speed * delta;

        if (moveDown) velocity.y -= speed * delta;
        if (moveUp) velocity.y += speed * delta;

        if (moveForward) velocity.z -= speed * delta;
        if (moveBackward) velocity.z += speed * delta;

        pointerLockControls.getObject().translateX(velocity.x * delta);
        pointerLockControls.getObject().translateY(velocity.y * delta);
        pointerLockControls.getObject().translateZ(velocity.z * delta);

    }

};
