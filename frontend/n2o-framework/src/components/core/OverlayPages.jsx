import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers } from 'recompose';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import map from 'lodash/map';
import has from 'lodash/has';
import defaultTo from 'lodash/defaultTo';
import { closeOverlay, hidePrompt } from '../../actions/overlays';
import { overlaysSelector } from '../../selectors/overlays';

import ModalPage from './ModalPage';
import DrawerPage from './DrawerPage';
import PageDialog from './PageDialog';

const ModalMode = {
  MODAL: 'modal',
  DRAWER: 'drawer',
  DIALOG: 'dialog',
};

const PageComponent = {
  [ModalMode.MODAL]: ModalPage,
  [ModalMode.DRAWER]: DrawerPage,
  [ModalMode.DIALOG]: PageDialog,
};

/**
 * Компонент, отображающий все оверлейные окна
 * @reactProps {object} overlays - Массив объектов (из Redux)
 * @example
 *  <OverlayPages/>
 */
function OverlayPages({ renderOverlays, overlays }) {
  return <div className="n2o-overlay-pages">{renderOverlays(overlays)}</div>;
}

const mapStateToProps = createStructuredSelector({
  overlays: state => overlaysSelector(state),
});

const mapDispatchToProps = dispatch => ({
  close: (name, prompt) => {
    dispatch(closeOverlay(name, prompt));
  },
  hidePrompt: name => {
    dispatch(hidePrompt(name));
  },
});

OverlayPages.propTypes = {
  overlays: PropTypes.array,
};

OverlayPages.defaultProps = {
  overlays: {},
};

export { OverlayPages };
export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withHandlers({
    prepareProps: props => overlay => ({
      ...props,
      ...overlay,
      ...defaultTo(overlay.props, {})
    }),
  }),
  withHandlers({
    renderOverlays: ({ prepareProps }) => overlays =>
      map(
        overlays,
        ({ mode, ...rest }) =>
          has(PageComponent, mode) &&
          React.createElement(PageComponent[mode], prepareProps(rest))
      ),
  }),
)(OverlayPages);
