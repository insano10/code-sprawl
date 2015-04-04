define( [], function () {

    return function()
    {
        var DATA_ARRAY_WIDTH = 10;
        var DATA_ARRAY_HEIGHT = 10;
        var DATA_SIDE_LENGTH = 50;

        function CubeData()
        {
            this.data = createGeometryMap();
        }

        var createRandomGeometry = function() {

            var width = DATA_SIDE_LENGTH;
            var height = Math.floor(Math.random() * 150) + 1;
            var depth = DATA_SIDE_LENGTH;

            return [width, height, depth];
        };

        var createGeometryMap = function() {

            var geometryMap = new Array(DATA_ARRAY_WIDTH);

            for(var i=0 ; i<DATA_ARRAY_WIDTH ; i++) {

                geometryMap[i] = new Array(DATA_ARRAY_HEIGHT);

                for(var j=0 ; j<DATA_ARRAY_HEIGHT ; j++) {
                    geometryMap[i][j] = createRandomGeometry();
                }
            }
            return geometryMap;
        };

        CubeData.prototype.getGeometryAt = function getGeometryAt(x, y)
        {
            return this.data[x][y];
        };

        CubeData.prototype.getDataArrayWidth = function getDataArrayWidth()
        {
            return DATA_ARRAY_WIDTH;
        };

        CubeData.prototype.getDataArrayHeight = function getDataArrayHeight()
        {
            return DATA_ARRAY_HEIGHT
        };

        CubeData.prototype.getDataSideLength = function getDataSideLength()
        {
            return DATA_SIDE_LENGTH;
        };

        return CubeData;
    }();
} );
