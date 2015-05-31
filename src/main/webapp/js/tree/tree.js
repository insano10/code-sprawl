/**
 * Created by jenny on 31/05/15.
 */

define(["jquery"], function ($)
{
    return function ()
    {
        function Tree(treeObjectStructure)
        {
            this.head = treeObjectStructure.node;
        }

        Tree.prototype.postOrderTraversal = function postOrderTraversal(visitCallback)
        {
            /*
             postorder(node)
             if node == null then return
             visitAllChildrenLeftToRight
             visit(node)
             */
            postOrder(this.head, visitCallback, []);
        };

        var postOrder = function postOrder(node, visitCallback, visitedArray)
        {
            if (node != null)
            {
                var nextChildToVisit = getNextChildToVisit(node, visitedArray);
                while (nextChildToVisit != null)
                {
                    postOrder(nextChildToVisit, visitCallback, visitedArray);

                    nextChildToVisit = getNextChildToVisit(node, visitedArray);
                }
                visitedArray.push(visit(node, visitCallback).name);
            }
        };

        var getNextChildToVisit = function getNextChildToVisit(node, visitedArray)
        {
            var nextChild = null;
            $.each(node.children, function (idx, child)
            {
                if ($.inArray(child.node.name, visitedArray) == -1)
                {
                    nextChild = child.node;
                    return false;
                }
            });
            return nextChild;
        };

        var visit = function visit(node, visitCallback)
        {
            visitCallback(node);
            return node;
        };

        return Tree;
    }();

});
