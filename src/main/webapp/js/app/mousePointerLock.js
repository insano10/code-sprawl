define([], function() {

    var grabPointer = function (controlsEnabledCallback)
    {
        var blocker = document.getElementById('blocker');
        var instructions = document.getElementById( 'instructions' );
        var havePointerLock = 'pointerLockElement' in document || 'mozPointerLockElement' in document || 'webkitPointerLockElement' in document;

        if (havePointerLock)
        {
            var element = document.body;

            var pointerLockChangeCallback = function (event)
            {
                if (document.pointerLockElement === element || document.mozPointerLockElement === element || document.webkitPointerLockElement === element)
                {
                    controlsEnabledCallback();
                    blocker.style.display = 'none';

                }
                else
                {
                    blocker.style.display = '-webkit-box';
                    blocker.style.display = '-moz-box';
                    blocker.style.display = 'box';
                    instructions.style.display = '';
                }
            };

            var pointerLockError = function (event)
            {
                instructions.style.display = '';
            };

            // Hook pointer lock state change events
            document.addEventListener('pointerlockchange', pointerLockChangeCallback, false);
            document.addEventListener('mozpointerlockchange', pointerLockChangeCallback, false);
            document.addEventListener('webkitpointerlockchange', pointerLockChangeCallback, false);

            document.addEventListener('pointerlockerror', pointerLockError, false);
            document.addEventListener('mozpointerlockerror', pointerLockError, false);
            document.addEventListener('webkitpointerlockerror', pointerLockError, false);

            instructions.addEventListener('click', function (event)
            {
                instructions.style.display = 'none';

                // Ask the browser to lock the pointer
                element.requestPointerLock = element.requestPointerLock || element.mozRequestPointerLock || element.webkitRequestPointerLock;

                if (/Firefox/i.test(navigator.userAgent))
                {
                    var fullScreenChange = function (event)
                    {
                        if (document.fullscreenElement === element || document.mozFullscreenElement === element || document.mozFullScreenElement === element)
                        {
                            document.removeEventListener('fullscreenchange', fullScreenChange);
                            document.removeEventListener('mozfullscreenchange', fullScreenChange);

                            element.requestPointerLock();
                        }
                    };

                    document.addEventListener('fullscreenchange', fullScreenChange, false);
                    document.addEventListener('mozfullscreenchange', fullScreenChange, false);

                    element.requestFullscreen = element.requestFullscreen || element.mozRequestFullscreen || element.mozRequestFullScreen || element.webkitRequestFullscreen;
                    element.requestFullscreen();
                }
                else
                {
                    element.requestPointerLock();
                }

            }, false);

        }
        else
        {
            instructions.innerHTML = 'Your browser doesn\'t seem to support Pointer Lock API';
        }
    };

    return { grabPointer: grabPointer };
});
