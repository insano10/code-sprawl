define(["jquery", "CityBuilder"], function ($, CityBuilder)
{
    describe("CityBuilder", function ()
    {

        var treeObject;

        beforeEach(function ()
        {

            /*

             /
             |
             -- README.md
             -- app
                 |
                 -- code
                      |
                      -- cat.java
                      -- dog.java
             |
             -- resources
                      |
                      -- project.properties
                      -- log4j.xml
                      -- database
                              |
                              -- connection.properties
                              -- jdbc.properties
                              -- config.properties
                              -- logging.properties
                              -- settings
                                      |
                                      -- settings.xml
                      -- docs
                           |
                           -- userGuide.html
             |
             -- tests
                   |
                   -- uberTest.js
             |
             */
            treeObject = {

                node: { name: "/",
                    files:         [
                        {name: "README", lineCount: 10, fileExtension: "md"}
                    ],
                    children:      [
                        {node: {name: "/app",
                            files:         [],
                            children:      [
                                {node: {name: "/app/code",
                                    files:         [
                                        {name: "cat", lineCount: 230, fileExtension: "java"},
                                        {name: "dog", lineCount: 453, fileExtension: "java"}
                                    ], children:   []}}
                            ]}},
                        {node: {name: "/resources",
                            files:         [
                                {name: "project", lineCount: 3, fileExtension: "properties"},
                                {name: "log4j", lineCount: 38, fileExtension: "xml"}
                            ],
                            children:      [
                                {node: {name: "/resources/database",
                                    files:         [
                                        {name: "connection", lineCount: 5, fileExtension: "properties"},
                                        {name: "jdbc", lineCount: 6, fileExtension: "properties"},
                                        {name: "config", lineCount: 7, fileExtension: "properties"},
                                        {name: "logging", lineCount: 8, fileExtension: "properties"}
                                    ],
                                    children:      [
                                        {node: {name: "/resources/database/settings",
                                            files:         [
                                                {name: "settings", lineCount: 999, fileExtension: "xml"}
                                            ], children:   []}}
                                    ]}},
                                {node: {name: "/resources/docs",
                                    files:         [
                                        {name: "userGuide", lineCount: 57, fileExtension: "html"}
                                    ], children:   []}}
                            ]}},
                        {node: {name: "/tests",
                            files:         [
                                {name: "uberTest", lineCount: 8362, fileExtension: "js"}
                            ], children:   []}}
                    ]
                }
            };

        });

        it("should build city from tree object", function ()
        {
            var cityBuilder = new CityBuilder(treeObject);
            var rootNeighbourhood = cityBuilder.build();

            expect(rootNeighbourhood.getName()).toEqual("/");
            expect(rootNeighbourhood.getChildNeighbourhoods().length).toEqual(3);
            expect(rootNeighbourhood.getChildNeighbourhoods()[0].getName()).toEqual("app");
            expect(rootNeighbourhood.getChildNeighbourhoods()[1].getName()).toEqual("resources");
            expect(rootNeighbourhood.getChildNeighbourhoods()[2].getName()).toEqual("tests");

            expect(rootNeighbourhood.getChildNeighbourhoods()[0].getChildNeighbourhoods().length).toEqual(1);
            expect(rootNeighbourhood.getChildNeighbourhoods()[0].getChildNeighbourhoods()[0].getName()).toEqual("code");

            expect(rootNeighbourhood.getChildNeighbourhoods()[1].getChildNeighbourhoods().length).toEqual(2);
            expect(rootNeighbourhood.getChildNeighbourhoods()[1].getChildNeighbourhoods()[0].getName()).toEqual("database");
            expect(rootNeighbourhood.getChildNeighbourhoods()[1].getChildNeighbourhoods()[1].getName()).toEqual("docs");

            expect(rootNeighbourhood.getChildNeighbourhoods()[1].getChildNeighbourhoods()[0].getChildNeighbourhoods().length).toEqual(1);
            expect(rootNeighbourhood.getChildNeighbourhoods()[1].getChildNeighbourhoods()[0].getChildNeighbourhoods()[0].getName()).toEqual("settings");
        });

    });
});
