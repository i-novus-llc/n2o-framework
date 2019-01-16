import React from 'react';
import PropTypes from 'prop-types';
import { Helmet } from 'react-helmet';
import { connect } from 'react-redux';
import { getModelSelector } from '../../selectors/models';
import propsResolver from '../../utils/propsResolver';
import { createStructuredSelector } from 'reselect';

function DocumentTitle({ title, modelLink }) {
  let resolveTitle = title;
  if (title && modelLink && modelLink.link) {
    resolveTitle = propsResolver(title, modelLink.link);
  }

  return <Helmet title={resolveTitle} />;
}

DocumentTitle.propTypes = {
  title: PropTypes.string,
  modelLink: PropTypes.string
};

const mapStateToProps = createStructuredSelector({
  model: (state, { modelLink }) => getModelSelector(modelLink)(state)
});

export default connect(mapStateToProps)(DocumentTitle);
