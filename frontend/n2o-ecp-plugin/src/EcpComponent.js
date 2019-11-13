import React from 'react'
import compose from 'recompose/compose';
import withState from 'recompose/withState';
import withHandlers from 'recompose/withHandlers';
import Modal from 'reactstrap/lib/Modal';
import ModalHeader from 'reactstrap/lib/ModalHeader';
import ModalBody from 'reactstrap/lib/ModalBody';
import ModalFooter from 'reactstrap/lib/ModalFooter';
import Button from 'reactstrap/lib/Button';
import Alert from 'reactstrap/lib/Alert';

function EcpComponent({ setOpen, isOpen, content = 'content will be here' }) {
  return (
      <div className="n2o-ecp-plugin">
          <Button onClick={setOpen}>Open ECP</Button>
          <Modal isOpen={isOpen}>
              <ModalHeader>N2O ECP plugin</ModalHeader>
              <ModalBody>{content}</ModalBody>
              <ModalFooter className='d-flex justify-content-between'>
                  <Button>Подписать</Button>
                  <Button onClick={setOpen}>Отмена</Button>
              </ModalFooter>
          </Modal>
      </div>
  );
}

export default compose(
    withState('isOpen', 'setOpen', false),
)(EcpComponent)
