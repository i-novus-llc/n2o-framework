import React from 'react';
import PropTypes from 'prop-types';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import { closeModal, hidePrompt } from '../../actions/modals';
import { modalsSelector } from '../../selectors/modals';


import ModalPage from './ModalPage';

/**
 * Компонент, отображающий все модальные окна
 * @reactProps {object} modals - Массив объктов модалок (из Redux)
 * @example
 *  <ModalPages/>
 */
function ModalPages(props) {
  const { modals } = props;
  const modalPages = modals.map(
    modal =>
      modal.visible && (
        <ModalPage
          key={modal.pageId}
          close={props.close}
          hidePrompt={props.hidePrompt}
          {...modal}
          {...modal.props}
        />
      )
  );
  return <div className="n2o-modal-pages">{modalPages}</div>;
}

ModalPages.propTypes = {
  modals: PropTypes.array,
};

ModalPages.defaultProps = {
  modals: {},
};

const mapStateToProps = createStructuredSelector({
  modals: (state, props) => modalsSelector(state),
});

function mapDispatchToProps(dispatch) {
  return {
    close: (name, prompt) => {
      dispatch(closeModal(name, prompt));
    },
    hidePrompt: name => {
      dispatch(hidePrompt(name));
    },
  };
}

ModalPages.propTypes = {
  modals: PropTypes.array,
  options: PropTypes.object,
  actions: PropTypes.object,
};

export { ModalPages };
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ModalPages);
