import React from 'react';
import PropTypes from 'prop-types';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import { closeOverlay, hidePrompt } from '../../actions/overlays';
import { overlaysSelector } from '../../selectors/overlays';

import ModalPage from './ModalPage';
import DrawerPage from './DrawerPage';

/**
 * Компонент, отображающий все модальные окна
 * @reactProps {object} overlays - Массив объектов (из Redux)
 * @example
 *  <OverlayPages/>
 */
function OverlayPages(props) {
  const renderModalPage = overlay => (
    <ModalPage
      key={overlay.pageId}
      close={this.props.close}
      hidePrompt={this.props.hidePrompt}
      {...overlay}
      {...overlay.props}
    />
  );
  const renderDrawerPage = overlay => (
    <DrawerPage
      key={overlay.pageId}
      close={this.props.close}
      hidePrompt={this.props.hidePrompt}
      {...overlay}
      {...overlay.props}
    />
  );

  const { overlays } = props;
  const overlayPages = overlays.map(
    overlay =>
      (overlay.visible &&
        overlay.mod === 'modal' &&
        renderModalPage(overlay)) ||
      (overlay.visible &&
        overlay.mod === 'drawer' &&
          renderDrawerPage(overlay))
  );
  return <div>{overlayPages}</div>;
}

const mapStateToProps = createStructuredSelector({
  overlays: (state, props) => overlaysSelector(state),
});

function mapDispatchToProps(dispatch) {
  return {
    close: (name, prompt) => {
      dispatch(closeOverlay(name, prompt));
    },
    hidePrompt: name => {
      dispatch(hidePrompt(name));
    },
  };
}

OverlayPages.defaultProps = {
  overlays: {},
};

OverlayPages.propTypes = {
  overlays: PropTypes.array,
  options: PropTypes.object,
  actions: PropTypes.object,
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OverlayPages);
