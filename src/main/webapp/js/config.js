require(
    {
        shim:  {
            // --- Use shim to mix together all THREE.js subcomponents
            'threeCore':         { exports: 'THREE' },
            'TrackballControls': { deps: ['threeCore'], exports: 'THREE' },
            // --- end THREE sub-components
            'detector':          { exports: 'Detector' },
            'stats':             { exports: 'Stats' }
        },
        // Third party code lives in js/lib
        paths: {
            // --- start THREE sub-components
            three:             'lib/threeWrapper',
            threeCore:         'lib/three',
            TrackballControls: 'lib/controls/TrackballControls',
            // --- end THREE sub-components
            detector:          'lib/Detector',
            stats:             'lib/stats.min',
            // Require.js plugins
            text:              'lib/text',
            shader:            'lib/shader',
            // Where to look for shader files
            shaders:           'shaders',
            //app
            app:               'app/app',
            camera:            'app/camera',
            container:         'app/container',
            controls:          'app/controls',
            geometry:          'app/geometry',
            light:             'app/light',
            material:          'app/material',
            renderer:          'app/renderer',
            scene:             'app/scene',
            texture:           'app/texture',
            data:              'data/cubes'
        }
    },
    ["main"],
    function (main)
    {
        main.start();
    }
);

