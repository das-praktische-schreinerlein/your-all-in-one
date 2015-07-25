jasmine.getFixtures().fixturesPath = 'base/src/test/static/unit/fixtures/';
jasmine.getStyleFixtures().fixturesPath = 'base/src/test/static/unit/fixtures/';
jasmine.getJSONFixtures().fixturesPath = 'base/src/test/static/unit/fixtures/';

yaioAppBase = YaioAppBase(); 
yaioAppBase.publishDetectorStyles();
