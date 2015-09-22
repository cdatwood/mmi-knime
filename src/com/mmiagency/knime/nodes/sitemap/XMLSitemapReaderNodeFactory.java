package com.mmiagency.knime.nodes.sitemap;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "XMLSitemapReader" Node.
 * 
 *
 * @author Ed Ng
 */
public class XMLSitemapReaderNodeFactory 
        extends NodeFactory<XMLSitemapReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public XMLSitemapReaderNodeModel createNodeModel() {
        return new XMLSitemapReaderNodeModel();
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
    public NodeView<XMLSitemapReaderNodeModel> createNodeView(final int viewIndex,
            final XMLSitemapReaderNodeModel nodeModel) {
        return new XMLSitemapReaderNodeView(nodeModel);
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
        return new XMLSitemapReaderNodeDialog();
    }

}

