import React from 'react';
import PropTypes from 'prop-types';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import { get, omit } from 'lodash';
import { closeOverlay, hidePrompt } from '../../actions/overlays';
import { overlaysSelector } from '../../selectors/overlays';

import ModalPage from './ModalPage';
import DrawerPage from './DrawerPage';

/**
 * Компонент, отображающий все оверлейные окна
 * @reactProps {object} overlays - Массив объектов (из Redux)
 * @example
 *  <OverlayPages/>
 */
function OverlayPages(props) {
  const renderModalPage = overlay => (
    <ModalPage
      key={get(overlay, 'props.pageId', '')}
      {...props}
      {...overlay}
      {...overlay.props}
    />
  );
  const renderDrawerPage = overlay => (
    <DrawerPage
      key={get(overlay, 'props.pageId', '')}
      {...props}
      {...overlay}
      {...overlay.props}
    />
  );

  const { overlays } = props;
  const overlayPages = overlays.map(
    overlay =>
      (overlay.visible &&
        overlay.mode === 'modal' &&
        renderModalPage(omit(overlay, ['mode']))) ||
      (overlay.visible &&
        overlay.mode === 'drawer' &&
        renderDrawerPage(omit(overlay, ['mode'])))
  );
  return <div className="n2o-overlay-pages">{overlayPages}</div>;
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

OverlayPages.propTypes = {
  overlays: PropTypes.array,
};

OverlayPages.defaultProps = {
  overlays: {},
};

export { OverlayPages };
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OverlayPages);
