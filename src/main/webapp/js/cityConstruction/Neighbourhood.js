define(["jquery"], function ($)
{
    return function ()
    {
        function Neighbourhood(name, unitArray, childNeighbourhoods)
        {
            this.unitArray = unitArray;
            this.childNeighbourhoods = childNeighbourhoods;

            var tokens = name.split("/");
            this.name = tokens.pop();
            this.name = (this.name.length == 0) ? "/" : this.name;

            this.parentName = tokens.join("/");
            this.parentName = (this.parentName.length == 0 && this.name != "/") ? "/" : this.parentName;
        }

        Neighbourhood.prototype.getName = function getName()
        {
          return this.name;
        };

        Neighbourhood.prototype.getParentName = function getParentName()
        {
            return this.parentName;
        };

        Neighbourhood.prototype.getFileUnits = function getFileUnits()
        {
            return this.unitArray;
        };

        Neighbourhood.prototype.getChildNeighbourhoods = function getChildNeighbourhoods()
        {
            return this.childNeighbourhoods;
        };

        return Neighbourhood;
    }();

});
