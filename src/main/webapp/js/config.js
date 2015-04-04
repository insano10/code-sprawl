require(
    {
        shim:  {
            'threeCore':           { exports: 'THREE' },
            'PointerLockControls': { deps: ['threeCore'], exports: 'THREE' },
            'FPSControls':         { deps: ['threeCore'], exports: 'THREE' },
            'detector':            { exports: 'Detector' },
            'stats':               { exports: 'Stats' }
        },
        paths: {
            three:               'lib/threeWrapper',
            threeCore:           'lib/three',
            detector:            'lib/Detector',
            stats:               'lib/stats.min',
            text:                'lib/text',
            shader:              'lib/shader',
            shaders:             'shaders',

            app:                 'app/app',
            sceneObjects:        'app/sceneObjects',
            camera:              'app/components/camera',
            container:           'app/components/container',
            controls:            'app/components/controls',
            light:               'app/components/light',
            renderer:            'app/components/renderer',
            scene:               'app/components/scene',
            texture:             'app/components/texture',
            crossHair:           'controls/crossHair',
            FPSControls:         'controls/fpsControls',
            mousePointerLock:    'controls/mousePointerLock',
            PointerLockControls: 'controls/pointerLockControls',
            data:                'data/cubes',
            codeUnitBar:         'renderedObjects/codeUnitBar',
            informationPanel:    'renderedObjects/informationPanel'
        }
    },
    ["main"],
    function (main)
    {
        main.start();
    }
);

