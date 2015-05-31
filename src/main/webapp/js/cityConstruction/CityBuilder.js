define(["jquery", "tree", "Neighbourhood"], function ($, Tree, Neighbourhood)
{
    return function ()
    {
        function CityBuilder(neighbourhoodTreeObj)
        {
            this.tree = new Tree(neighbourhoodTreeObj);
            this.neighbourhoodMap = {};
            this.rootNeighbourhood = null;
        }

        var buildNeighbourhood = function buildNeighbourhood(name, files, neighbourhoodMap)
        {
            //create the neighbourhood
            var childNeighbourhoods = neighbourhoodMap[name];

            console.log("found child neighbourhoods " + JSON.stringify(childNeighbourhoods) + " for " + name);
            var neighbourhood = new Neighbourhood(name, files, childNeighbourhoods);

            //store it for use by parent neighbourhoods as we encounter them
            var parent = neighbourhoodMap[neighbourhood.getParentName()];
            if (parent == null)
            {
                console.log("storing " + neighbourhood.getName() + " under " + neighbourhood.getParentName() + " first!");
                neighbourhoodMap[neighbourhood.getParentName()] = [neighbourhood];
            }
            else
            {
                console.log("storing " + name + " under " + neighbourhood.getParentName());
                parent.push(neighbourhood);
            }

            return neighbourhood;
        };

        var addNeighbourhoodToCity = function addNeighbourhoodToCity(neighbourhoodTreeNode, neighbourhoodMap)
        {
            console.log("adding neighbourhood: " + neighbourhoodTreeNode.name);
            var name = neighbourhoodTreeNode.name;
            var files = neighbourhoodTreeNode.files;

            return buildNeighbourhood(name, files, neighbourhoodMap);
        };

        CityBuilder.prototype.build = function build()
        {
            var callback = function (node)
            {
                this.rootNeighbourhood = addNeighbourhoodToCity(node, this.neighbourhoodMap);
            };
            this.tree.postOrderTraversal(callback.bind(this));

            return this.rootNeighbourhood;
        };

        return CityBuilder;
    }();

});
