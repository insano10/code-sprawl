define(["jquery", "tree"], function ($, Tree)
{
    describe("Tree", function ()
    {

        var treeObject;

        beforeEach(function ()
        {

            /*
                         [1]
                  [0]    [2]     [1]
                  [2]  [4] [1]
                       [1]
             */
            treeObject = {

                node: { name: "root", count: 1,
                    children: [
                        {node: {name: "left1", count: 0,
                            children: [
                                {node: {name: "left1left2", count: 2, children: []}}
                            ]}},
                        {node: {name: "centre1", count: 2,
                            children: [
                                {node: {name: "centre1left2", count: 4,
                                    children: [
                                        {node: {name: "centre1left2left3", count: 1, children: []}}
                                    ]}},
                                {node: {name: "centre1right2", count: 1, children: []}}
                            ]}},
                        {node: {name: "right1", count: 1, children: []}}
                    ]
                }
            };

        });

        it("should visit tree in post order", function ()
        {

            var tree = new Tree(treeObject);
            var visitedNodes = [];

            tree.postOrderTraversal(function(node) {
                visitedNodes.push(node);
            });

            expect(visitedNodes[0].name).toEqual("left1left2");
            expect(visitedNodes[1].name).toEqual("left1");
            expect(visitedNodes[2].name).toEqual("centre1left2left3");
            expect(visitedNodes[3].name).toEqual("centre1left2");
            expect(visitedNodes[4].name).toEqual("centre1right2");
            expect(visitedNodes[5].name).toEqual("centre1");
            expect(visitedNodes[6].name).toEqual("right1");
            expect(visitedNodes[7].name).toEqual("root");
        });

    });
});
