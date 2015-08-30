package com.mmiagency.knime.keyworddensity;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "KeywordDensity" Node.
 * 
 *
 * @author Ed Ng
 */
public class KeywordDensityNodeFactory 
        extends NodeFactory<KeywordDensityNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public KeywordDensityNodeModel createNodeModel() {
        return new KeywordDensityNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<KeywordDensityNodeModel> createNodeView(final int viewIndex,
            final KeywordDensityNodeModel nodeModel) {
        return new KeywordDensityNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new KeywordDensityNodeDialog();
    }

}

