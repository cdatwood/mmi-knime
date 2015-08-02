package com.mmiagency.knime.google.pagespeed;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "GooglePageSpeed" Node.
 * 
 *
 * @author Ed Ng
 */
public class GooglePageSpeedNodeFactory 
        extends NodeFactory<GooglePageSpeedNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GooglePageSpeedNodeModel createNodeModel() {
        return new GooglePageSpeedNodeModel();
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
    public NodeView<GooglePageSpeedNodeModel> createNodeView(final int viewIndex,
            final GooglePageSpeedNodeModel nodeModel) {
        return new GooglePageSpeedNodeView(nodeModel);
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
        return new GooglePageSpeedNodeDialog();
    }

}

