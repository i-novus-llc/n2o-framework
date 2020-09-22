import React from 'react';
import PropTypes from 'prop-types';

import get from 'lodash/get';

import Modal from 'reactstrap/lib/Modal';
import ModalHeader from 'reactstrap/lib/ModalHeader';
import ModalBody from 'reactstrap/lib/ModalBody';
import ModalFooter from 'reactstrap/lib/ModalFooter';

import withOverlayMethods from './withOverlayMethods';
import Toolbar from '../buttons/Toolbar';

function PageDialog({ visible, props }) {
  const { title, description, size = 'sm', scrollable = false } = props;

  return (
    <div className="modal-page-overlay">
      <Modal isOpen={visible} size={size} scrollable={scrollable}>
        <ModalHeader>{title}</ModalHeader>
        <ModalBody className="white-space-pre-line">{description}</ModalBody>
        <ModalFooter>
          <Toolbar
            className="mr-auto"
            entityKey="dialog"
            toolbar={get(props, 'toolbar.bottomLeft')}
          />
          <Toolbar
            className="ml-auto"
            entityKey="dialog"
            toolbar={get(props, 'toolbar.bottomRight')}
          />
        </ModalFooter>
      </Modal>
    </div>
  );
}

PageDialog.propTypes = {
  visible: PropTypes.bool,
  props: PropTypes.object,
};

PageDialog.defaultProps = {
  props: {},
};

export default withOverlayMethods(PageDialog);
