package de.yaio.datatransfer.importer;

import java.util.HashMap;
import java.util.Map;

import de.yaio.BaseTest.TestObj;
import de.yaio.core.node.BaseNode;

/**
 * <h4>FeatureDomain:</h4>
 *     Test
 * <h4>FeatureDescription:</h4>
 *     testnode for importer-logic
 * 
 * @package de.yaio.datatransfer.importer
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category tests
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class TestNode extends BaseNode implements TestObj {
    
    @Override
    public String toString() {
        StringBuffer resBuffer = new StringBuffer();
        resBuffer.append(this.getName()).append("|")
                 .append(this.getState()).append("|")
                 .append(this.getClass().getName()).append("|")
                 ;
        return resBuffer.toString();
        
    }

    // Status-Konstanten
    public static Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();
    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put("TEST", "TEST");
        CONST_MAP_NODETYPE_IDENTIFIER.put("TEST1", "TEST");
    }

    public static void configureNodeTypeIdentifier(NodeFactory nodeFactory) {
        nodeFactory.addNodeTypeIdentifier(TestNode.CONST_MAP_NODETYPE_IDENTIFIER, TestNode.class);
    }

    public static void configureNodeTypes(Importer importer) {
        importer.addNodeTypeIdentifierVariantMapping(TestNode.CONST_MAP_NODETYPE_IDENTIFIER);
    }

    public static void configureWorkflowNodeTypeMapping(Importer importer) {
        // NOP 
    }
}