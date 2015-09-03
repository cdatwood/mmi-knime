package com.mmiagency.knime.randomdata;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;


/**
 *
 * @author Tobias Koetter, University of Konstanz
 */
public class RandomDataNodeFactory extends NodeFactory<RandomDataNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected NodeDialogPane createNodeDialogPane() {
        return new RandomDataNodeDialog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomDataNodeModel createNodeModel() {
        return new RandomDataNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<RandomDataNodeModel> createNodeView(final int viewIndex, final RandomDataNodeModel nodeModel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasDialog() {
        return true;
    }

}
