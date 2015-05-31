require(
    {
        shim:  {
            "bootstrap":           { deps: ['jquery'] },
            'threeCore':           { exports: 'THREE' },
            'tween':               { exports: 'TWEEN' },
            'PointerLockControls': { deps: ['threeCore'], exports: 'THREE' },
            'FPSControls':         { deps: ['threeCore'], exports: 'THREE' },
            'octree':              { deps: ['threeCore'], exports: 'THREE' },
            'detector':            { exports: 'Detector' },
            'stats':               { exports: 'Stats' }
        },
        paths: {
            bootstrap: "lib/bootstrap-3.3.0/js/bootstrap.min",
            jquery:    'lib/jquery-2.1.0.min',
            jqueryui:  'lib/jquery-ui-1.11.4.custom/jquery-ui.min',
            three:     'lib/threeWrapper',
            threeCore: 'lib/three',
            octree:    'lib/Octree',
            tween:     'lib/tween.min',
            detector:  'lib/Detector',
            stats:     'lib/stats.min',
            text:      'lib/text',
            shader:    'lib/shader',
            shaders:   'shaders',

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
            cameraMover:         'controls/cameraMover',
            CityPlanner:         'data/CityPlanner',
            TestCityBlueprint:   'data/TestCityBlueprint',
            LoadedCityBlueprint: 'data/LoadedCityBlueprint',
            FileUnit:            'renderedObjects/FileUnit',
            FileNeighbourhood:   'renderedObjects/FileNeighbourhood',
            InformationPanel:    'renderedObjects/InformationPanel',
            uiBehaviour:         'uiBehaviour/uiBehaviour',
            tree:                "cityConstruction/tree",
            neighbourhood:       "cityConstruction/Neighbourhood"
        }
    },
    ["main"],
    function (main)
    {
        main.start();
    }
);

