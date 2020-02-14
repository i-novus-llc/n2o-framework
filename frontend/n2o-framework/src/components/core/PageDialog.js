import React from 'react';

import get from 'lodash/get';

import Modal from 'reactstrap/lib/Modal';
import ModalHeader from 'reactstrap/lib/ModalHeader';
import ModalBody from 'reactstrap/lib/ModalBody';
import ModalFooter from 'reactstrap/lib/ModalFooter';

import withOverlayMethods from './withOverlayMethods';
import Toolbar from '../buttons/Toolbar';

function PageDialog({ visible, props }) {
  return (
    <div className="modal-page-overlay">
      <Modal isOpen={visible} size="sm">
        <ModalHeader>{props.title}</ModalHeader>
        <ModalBody className="white-space-pre-line">
          {props.description}
        </ModalBody>
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

export default withOverlayMethods(PageDialog);
