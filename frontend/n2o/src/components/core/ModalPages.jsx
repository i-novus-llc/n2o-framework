import React from 'react';
import PropTypes from 'prop-types';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import { closeOverlay, hidePrompt } from '../../actions/overlays';
import { overlaysSelector } from '../../selectors/overlays';
import compileUrl from '../../utils/compileUrl';

import ModalPage from './ModalPage';

/**
 * Компонент, отображающий все модальные окна
 * @reactProps {object} overlays - Массив объектов (из Redux)
 * @example
 *  <ModalPages/>
 */
class ModalPages extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { overlays } = this.props;
    const modalPages = overlays.map(
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
    );
    return <div>{modalPages}</div>;
  }
}

ModalPages.propTypes = {
  overlays: PropTypes.array,
};

ModalPages.defaultProps = {
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

ModalPages.propTypes = {
  overlays: PropTypes.array,
  options: PropTypes.object,
  actions: PropTypes.object,
};

ModalPages = connect(
  mapStateToProps,
  mapDispatchToProps
)(ModalPages);
export default ModalPages;
