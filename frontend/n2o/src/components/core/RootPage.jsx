import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from 'lodash';
import { connect } from 'react-redux';
import { compose, defaultProps, getContext, withProps } from 'recompose';
import { createStructuredSelector } from 'reselect';

import Page from './Page';
import ModalPages from './ModalPages';
import GlobalAlerts from './GlobalAlerts';

import { SimpleTemplate } from './templates';

import { rootPageSelector } from '../../selectors/global';

function RootPage({ rootPageId, defaultTemplate, match: { params } }) {
  const Template = defaultTemplate;
  const rootPageUrl = params.pageUrl || '';

  return (
    <Template>
      <GlobalAlerts />
      <Page rootPage pageId={rootPageId} pageUrl={rootPageUrl} />
      <ModalPages />
    </Template>
  );
}

RootPage.propTypes = {
  defaultTemplate: PropTypes.element,
  rootPageId: PropTypes.string
};

const mapStateToProps = createStructuredSelector({
  rootPageId: rootPageSelector
});

export default compose(
  defaultProps({
    defaultTemplate: SimpleTemplate
  }),
  getContext({
    defaultTemplate: PropTypes.element
  }),
  connect(mapStateToProps)
)(RootPage);
