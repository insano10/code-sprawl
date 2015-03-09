define( [], function () {

    var DATA_ARRAY_WIDTH = 10;
    var DATA_ARRAY_HEIGHT = 10;
    var DATA_SIDE_LENGTH = 50;

    function createRandomGeometry() {

        var width = DATA_SIDE_LENGTH;
        var height = Math.floor(Math.random() * 150) + 1;
        var depth = DATA_SIDE_LENGTH;

        return [width, height, depth];
    }

    function createGeometryMap() {

        var geometryMap = new Array(DATA_ARRAY_WIDTH);

        for(var i=0 ; i<DATA_ARRAY_WIDTH ; i++) {

            geometryMap[i] = new Array(DATA_ARRAY_HEIGHT);

            for(var j=0 ; j<DATA_ARRAY_HEIGHT ; j++) {
                geometryMap[i][j] = createRandomGeometry();
            }
        }
        return geometryMap;
    }

    return {
        dataArrayWidth: DATA_ARRAY_WIDTH,
        dataArrayHeight: DATA_ARRAY_HEIGHT,
        dataSideLength: DATA_SIDE_LENGTH,
        data: createGeometryMap()
    };
} );
