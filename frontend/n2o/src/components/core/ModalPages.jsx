import React from 'react';
import PropTypes from 'prop-types';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import { closeModal, hidePrompt } from '../../actions/modals';
import { modalsSelector } from '../../selectors/modals';
import compileUrl from '../../utils/compileUrl';

import ModalPage from './ModalPage';

/**
 * Компонент, отображающий все модальные окна
 * @reactProps {object} modals - Массив объктов модалок (из Redux)
 * @example
 *  <ModalPages/>
 */
class ModalPages extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { modals } = this.props;
    const modalPages = modals.map(
      modal =>
        modal.visible && (
          <ModalPage
            key={modal.pageId}
            close={this.props.close}
            hidePrompt={this.props.hidePrompt}
            {...modal}
            {...modal.props}
          />
        )
    );
    return <div>{modalPages}</div>;
  }
}

ModalPages.propTypes = {
  modals: PropTypes.array
};

ModalPages.defaultProps = {
  modals: {}
};

const mapStateToProps = createStructuredSelector({
  modals: (state, props) => modalsSelector(state)
});

function mapDispatchToProps(dispatch) {
  return {
    close: (name, prompt) => {
      dispatch(closeModal(name, prompt));
    },
    hidePrompt: name => {
      dispatch(hidePrompt(name));
    }
  };
}

ModalPages.propTypes = {
  modals: PropTypes.array,
  options: PropTypes.object,
  actions: PropTypes.object
};

ModalPages = connect(
  mapStateToProps,
  mapDispatchToProps
)(ModalPages);
export default ModalPages;
