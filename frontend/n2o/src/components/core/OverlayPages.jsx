import React from 'react';
import PropTypes from 'prop-types';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import { closeOverlay, hidePrompt } from '../../actions/overlays';
import { overlaysSelector } from '../../selectors/overlays';
import compileUrl from '../../utils/compileUrl';

import ModalPage from './ModalPage';
import Drawer from '../snippets/Drawer/Drawer';

/**
 * Компонент, отображающий все модальные окна
 * @reactProps {object} overlays - Массив объектов (из Redux)
 * @example
 *  <OverlayPages/>
 */
class OverlayPages extends React.Component {
  constructor(props) {
    super(props);
    console.log(props);
  }

  render() {
    const { overlays } = this.props;
    const overlayPages = overlays.map(
      overlay =>
        overlay.visible && (
          <ModalPage
            key={overlay.pageId}
            close={this.props.close}
            hidePrompt={this.props.hidePrompt}
            {...overlay}
            {...overlay.props}
          />
        )
      // <Drawer key={overlay.pageId} {...overlay} {...overlay.props} />
    );
    return <div>{overlayPages}</div>;
  }
}

OverlayPages.propTypes = {
  overlays: PropTypes.array,
};

OverlayPages.defaultProps = {
  overlays: {},
};

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
  options: PropTypes.object,
  actions: PropTypes.object,
};

OverlayPages = connect(
  mapStateToProps,
  mapDispatchToProps
)(OverlayPages);
export default OverlayPages;
